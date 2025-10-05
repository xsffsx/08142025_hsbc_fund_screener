#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import json
import logging
import psycopg2
import psycopg2.extras
from dataclasses import dataclass
from typing import Dict, Any, List, Optional, Tuple
from datetime import datetime, date

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


@dataclass
class DatabaseConfig:
    host: str = os.environ.get("DB_HOST", "localhost")
    port: int = int(os.environ.get("DB_PORT", "5433"))
    database: str = os.environ.get("DB_NAME", "wdv")
    username: str = os.environ.get("DB_USER", "hsbc_user")
    password: str = os.environ.get("DB_PASS", "hsbc_pass")
    schema: str = os.environ.get("DB_SCHEMA", "public")


TABLE_NAME = "tt_fund_detail_job_record"


DDL_SQL = f"""
CREATE TABLE IF NOT EXISTS {TABLE_NAME} (
  id BIGSERIAL PRIMARY KEY,
  batch_name VARCHAR(64) NOT NULL,
  biz_date DATE,
  key1 VARCHAR(64) NOT NULL,
  endpoint VARCHAR(64) NOT NULL,
  status VARCHAR(32),
  attempts INTEGER,
  input_msg JSONB,
  output_msg JSONB,
  err_msg_json_arr JSONB,
  crt_dt_tm TIMESTAMPTZ DEFAULT now(),
  updt_dt_tm TIMESTAMPTZ DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_tt_fund_detail_job_record_key1 ON {TABLE_NAME}(key1);
CREATE INDEX IF NOT EXISTS idx_tt_fund_detail_job_record_endpoint ON {TABLE_NAME}(endpoint);
"""


def connect_database(cfg: DatabaseConfig):
    try:
        conn = psycopg2.connect(
            host=cfg.host,
            port=cfg.port,
            database=cfg.database,
            user=cfg.username,
            password=cfg.password,
        )
        conn.autocommit = True
        with conn.cursor() as cur:
            cur.execute(f"SET search_path TO {cfg.schema}")
        logger.info("数据库连接成功: %s/%s", cfg.database, cfg.schema)
        return conn
    except Exception as e:
        logger.error("数据库连接失败: %s", e)
        raise


def ensure_table(conn):
    with conn.cursor() as cur:
        cur.execute(DDL_SQL)
    logger.info("已确保表结构存在: %s", TABLE_NAME)


def _safe_read_json(path: str) -> Optional[Dict[str, Any]]:
    try:
        with open(path, 'r', encoding='utf-8') as f:
            return json.load(f)
    except Exception as e:
        logger.warning("读取JSON失败: %s, 错误: %s", path, e)
        return None


def _derive_endpoint_from_filename(filename: str) -> str:
    base = os.path.basename(filename)
    name = base.replace('.response.json', '')
    if name == 'amh_ut_product':
        return 'product'
    if name.startswith('wmds_'):
        return name[len('wmds_'):]
    return name


def _parse_biz_date(status_json: Optional[Dict[str, Any]], summary_json: Optional[Dict[str, Any]]) -> Optional[date]:
    # 优先使用 summary.timestamp 的日期部分，其次使用 status.created_at
    ts = None
    if summary_json and 'timestamp' in summary_json:
        ts = summary_json.get('timestamp')
    elif status_json and 'created_at' in status_json:
        ts = status_json.get('created_at')
    if not ts:
        return None
    try:
        # 支持两种格式: ISO 或者 'YYYY-MM-DD HH:MM:SS'
        dt = datetime.fromisoformat(str(ts).replace('Z', '+00:00'))
        return dt.date()
    except Exception:
        try:
            dt = datetime.strptime(str(ts), '%Y-%m-%d %H:%M:%S')
            return dt.date()
        except Exception:
            return None


def build_record_from_response(
    response_file: str,
    status_json: Optional[Dict[str, Any]],
    summary_json: Optional[Dict[str, Any]],
) -> Tuple[Dict[str, Any], List[str]]:
    errs: List[str] = []

    endpoint = _derive_endpoint_from_filename(response_file)
    product_code = None
    attempts = None
    status = None

    if status_json:
        product_code = status_json.get('product_code')
        attempts = status_json.get('retry_count')
        status = status_json.get('status')

    # 读取响应文件
    resp_obj = _safe_read_json(response_file)
    output_msg: Optional[Dict[str, Any]] = None
    input_msg: Optional[Dict[str, Any]] = None

    if resp_obj and isinstance(resp_obj, dict) and 'responses' in resp_obj and resp_obj['responses']:
        first = resp_obj['responses'][0]
        body_str = first.get('response_body', '{}')
        try:
            output_msg = json.loads(body_str) if isinstance(body_str, str) else body_str
        except Exception as e:
            errs.append(f"解析response_body失败: {e}")
            output_msg = None
        # 输入信息（可选）
        input_msg = {
            'url': first.get('url'),
            'method': first.get('method'),
            'request_headers': first.get('request_headers'),
        }
    else:
        errs.append('响应文件格式不匹配或缺少responses数组')

    record = {
        'batch_name': 'fund_detail',
        'biz_date': _parse_biz_date(status_json, summary_json),
        'key1': product_code or '',
        'endpoint': endpoint,
        'status': status or ('ERROR' if errs else None),
        'attempts': attempts if isinstance(attempts, int) else None,
        'input_msg': input_msg,
        'output_msg': output_msg,
        'err_msg_json_arr': errs if errs else None,
    }
    return record, errs


def insert_record(conn, record: Dict[str, Any]) -> None:
    cols = [
        'batch_name', 'biz_date', 'key1', 'endpoint', 'status', 'attempts',
        'input_msg', 'output_msg', 'err_msg_json_arr'
    ]
    placeholders = ', '.join(['%s'] * len(cols))
    sql = f"INSERT INTO {TABLE_NAME} ({', '.join(cols)}) VALUES ({placeholders})"

    values = [
        record.get('batch_name'),
        record.get('biz_date'),
        record.get('key1'),
        record.get('endpoint'),
        record.get('status'),
        record.get('attempts'),
        psycopg2.extras.Json(record.get('input_msg')) if record.get('input_msg') is not None else None,
        psycopg2.extras.Json(record.get('output_msg')) if record.get('output_msg') is not None else None,
        psycopg2.extras.Json(record.get('err_msg_json_arr')) if record.get('err_msg_json_arr') is not None else None,
    ]

    with conn.cursor() as cur:
        cur.execute(sql, values)


def scan_session_dir(session_dir: str) -> List[str]:
    files: List[str] = []
    for root, _, filenames in os.walk(session_dir):
        for fn in filenames:
            if fn.endswith('.response.json'):
                files.append(os.path.join(root, fn))
    return files


def verify_import(conn) -> Dict[str, Any]:
    report: Dict[str, Any] = {}
    with conn.cursor() as cur:
        cur.execute(f"SELECT COUNT(*) FROM {TABLE_NAME}")
        total = cur.fetchone()[0]
        report['total_records'] = total

        cur.execute(f"SELECT endpoint, COUNT(*) FROM {TABLE_NAME} GROUP BY endpoint ORDER BY 2 DESC")
        by_endpoint = cur.fetchall()
        report['by_endpoint'] = [{'endpoint': r[0], 'count': r[1]} for r in by_endpoint]

        cur.execute(f"SELECT COUNT(*) FROM {TABLE_NAME} WHERE output_msg IS NOT NULL")
        with_output = cur.fetchone()[0]
        report['records_with_output'] = with_output

        cur.execute(f"SELECT COUNT(*) FROM {TABLE_NAME} WHERE err_msg_json_arr IS NOT NULL")
        with_errors = cur.fetchone()[0]
        report['records_with_errors'] = with_errors

    return report


def main():
    base_dir = os.path.join(os.path.dirname(__file__), 'data_20250813_212612')
    run_report_path = os.path.join(base_dir, 'run_report.json')

    cfg = DatabaseConfig()
    conn = connect_database(cfg)
    ensure_table(conn)

    items: List[Dict[str, Any]] = []
    run_report = _safe_read_json(run_report_path)
    if run_report and isinstance(run_report, dict) and 'items' in run_report:
        # 过滤不可用路径，必要时尝试映射到当前项目路径
        raw_items = run_report['items']
        items = []
        for it in raw_items:
            sd = it.get('session_dir')
            if sd and os.path.isdir(sd):
                items.append(it)
            else:
                # 尝试路径修正（IdeaProjects -> PycharmProjects）
                if sd and '/IdeaProjects/' in sd:
                    repaired = sd.replace('/IdeaProjects/20250707_MCP/', '/PycharmProjects/20250809_MCP/')
                    if os.path.isdir(repaired):
                        it['session_dir'] = repaired
                        items.append(it)
        if not items:
            logger.warning('run_report.items 中路径无效，改为扫描所有 *.done 目录')
    else:
        logger.warning('未发现 run_report.json 或其不包含 items，改为扫描所有 *.done 目录')
        # fallback: 扫描 *.done 目录
        for name in os.listdir(base_dir):
            if name.endswith('.done'):
                items.append({'session_dir': os.path.join(base_dir, name), 'product_code': None})

    imported_count = 0
    error_count = 0
    per_session_stats: List[Dict[str, Any]] = []

    for item in items:
        session_dir = item.get('session_dir')
        if not session_dir:
            continue

        status_json = _safe_read_json(os.path.join(session_dir, 'status.json'))
        summary_json = _safe_read_json(os.path.join(session_dir, 'summary.json'))

        response_files = scan_session_dir(session_dir)
        if not response_files:
            logger.info("目录未找到 response.json 文件: %s", session_dir)
            # 记录一条错误型记录以表明该会话没有任何响应文件
            today = datetime.now().date()
            empty_record = {
                'batch_name': 'fund_detail',
                'biz_date': _parse_biz_date(status_json, summary_json) or today,
                'key1': (status_json.get('product_code') if status_json and status_json.get('product_code') else 'UNKNOWN'),
                'endpoint': 'none',
                'status': (status_json.get('status') if status_json else 'ERROR'),
                'attempts': (status_json.get('retry_count') if status_json else None),
                'input_msg': None,
                'output_msg': None,
                'err_msg_json_arr': ['未找到任何 *.response.json 文件']
            }
            insert_record(conn, empty_record)
            imported_count += 1
            error_count += 1
            per_session_stats.append({'session_dir': session_dir, 'responses': 0, 'errors': 1})
            continue

        session_errors = 0
        for rf in response_files:
            record, errs = build_record_from_response(rf, status_json, summary_json)
            try:
                insert_record(conn, record)
                imported_count += 1
                if errs:
                    session_errors += 1
                    error_count += 1
            except Exception as e:
                logger.error("插入记录失败: %s", e)
                error_count += 1
                session_errors += 1
        per_session_stats.append({'session_dir': session_dir, 'responses': len(response_files), 'errors': session_errors})

    final_report = verify_import(conn)
    final_report['imported_records'] = imported_count
    final_report['error_records'] = error_count
    final_report['per_session'] = per_session_stats

    # 保存报告到项目根目录
    report_path = os.path.join(os.path.dirname(__file__), 'tt_fund_detail_job_record_import_report.json')
    try:
        with open(report_path, 'w', encoding='utf-8') as f:
            json.dump(final_report, f, ensure_ascii=False, indent=2)
        logger.info("已生成导入与验证报告: %s", report_path)
    except Exception as e:
        logger.warning("写入报告文件失败: %s", e)

    # 亦打印到控制台
    print(json.dumps(final_report, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    main()