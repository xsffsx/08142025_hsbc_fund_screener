#!/usr/bin/env python3
# -*- coding: utf-8 -*-

import os
import json
import logging
from typing import Dict, Any, List, Tuple

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


def load_excel_sheets(excel_path: str) -> Dict[str, Any]:
    """加载Excel的所有工作表为DataFrame字典。
    需要 pandas>=1.0 与 openpyxl。
    """
    try:
        import pandas as pd
    except Exception as e:
        logger.error("缺少 pandas 依赖，请先安装: pip install pandas openpyxl。错误: %s", e)
        raise

    if not os.path.isfile(excel_path):
        raise FileNotFoundError(f"Excel文件不存在: {excel_path}")

    xls = pd.ExcelFile(excel_path)
    sheets: Dict[str, Any] = {}
    for sheet_name in xls.sheet_names:
        try:
            df = xls.parse(sheet_name)
            sheets[sheet_name] = df
        except Exception as e:
            logger.warning("读取工作表失败: %s, 错误: %s", sheet_name, e)
    logger.info("已加载工作表: %s", list(sheets.keys()))
    return sheets


def analyze_dataframe(df) -> Dict[str, Any]:
    import pandas as pd
    import numpy as np

    analysis: Dict[str, Any] = {}
    analysis['shape'] = {'rows': int(df.shape[0]), 'cols': int(df.shape[1])}
    analysis['columns'] = list(df.columns.astype(str))
    analysis['dtypes'] = {str(c): str(dt) for c, dt in df.dtypes.items()}

    # 缺失值
    missing = df.isna().sum().to_dict()
    missing_pct = {str(k): (float(v) / max(int(df.shape[0]), 1)) for k, v in missing.items()}
    analysis['missing'] = {
        'count': missing,
        'percent': missing_pct
    }

    # 数值型统计
    num_df = df.select_dtypes(include=['number'])
    if not num_df.empty:
        desc = num_df.describe(include='all').to_dict()
        analysis['numeric_summary'] = desc
        # 相关性矩阵
        corr = num_df.corr(numeric_only=True)
        analysis['correlation'] = corr.to_dict()
    else:
        analysis['numeric_summary'] = {}
        analysis['correlation'] = {}

    # 分类型统计
    cat_df = df.select_dtypes(include=['object'])
    categorical: Dict[str, Any] = {}
    for col in cat_df.columns:
        vc = cat_df[col].fillna('NULL').astype(str).value_counts()
        categorical[str(col)] = {
            'unique_count': int(vc.shape[0]),
            'top_values': [{'value': str(idx), 'count': int(cnt)} for idx, cnt in vc.head(15).items()]
        }
    analysis['categorical_summary'] = categorical

    # 低基数维度分布（<=50 唯一值）
    low_cardinality: Dict[str, Any] = {}
    for col in df.columns:
        uniq = df[col].nunique(dropna=True)
        if uniq <= 50:
            vc = df[col].fillna('NULL').astype(str).value_counts()
            low_cardinality[str(col)] = [{
                'value': str(idx), 'count': int(cnt)
            } for idx, cnt in vc.head(20).items()]
    analysis['low_cardinality_distributions'] = low_cardinality

    # 重复行统计
    analysis['duplicate_rows'] = int(df.duplicated().sum())

    # 关键标识列（启发式）
    key_cols = [c for c in df.columns if str(c).lower() in (
        'symbol', 'prod_alt_num', 'product_code', 'performance_id'
    )]
    key_dupes: Dict[str, Any] = {}
    for kc in key_cols:
        vc = df[kc].fillna('NULL').astype(str).value_counts()
        # 出现次数>1的潜在重复键
        dups = [{'key': str(idx), 'count': int(cnt)} for idx, cnt in vc.items() if cnt > 1][:20]
        key_dupes[str(kc)] = dups
    analysis['key_duplicates'] = key_dupes

    return analysis


def generate_markdown_report(excel_path: str, analyses: Dict[str, Dict[str, Any]], output_md: str) -> None:
    lines: List[str] = []
    lines.append(f"文件: `{excel_path}`")
    for sheet, a in analyses.items():
        lines.append(f"\n## 工作表: {sheet}")
        lines.append(f"- 维度: 行 {a['shape']['rows']}, 列 {a['shape']['cols']}")
        # 缺失值前10
        miss_sorted = sorted(a['missing']['count'].items(), key=lambda x: x[1], reverse=True)
        lines.append("- 缺失值（前10列）:")
        for k, v in miss_sorted[:10]:
            pct = a['missing']['percent'].get(k, 0.0)
            lines.append(f"  - `{k}`: {v} ({pct:.2%})")

        # 数值型统计存在时，列出部分指标
        if a['numeric_summary']:
            lines.append("- 数值型摘要（样例）:")
            for col, stats in list(a['numeric_summary'].items())[:5]:
                # stats 是各统计量的字典，如 count, mean, std, min, 25%, 50%, 75%, max
                parts = []
                for k, v in stats.items():
                    try:
                        parts.append(f"{k}:{float(v):.4f}")
                    except Exception:
                        parts.append(f"{k}:{v}")
                lines.append(f"  - `{col}`: " + ", ".join(parts))

        # 低基数分布
        if a['low_cardinality_distributions']:
            lines.append("- 低基数分布（样例）:")
            count = 0
            for col, dist in a['low_cardinality_distributions'].items():
                lines.append(f"  - `{col}`: " + ", ".join([f"{d['value']}({d['count']})" for d in dist[:10]]))
                count += 1
                if count >= 6:
                    break

        # 关键键的重复
        if a['key_duplicates']:
            lines.append("- 关键标识重复（前20）:")
            for kc, dups in a['key_duplicates'].items():
                if dups:
                    lines.append(f"  - `{kc}` 重复键样例: " + ", ".join([f"{d['key']}({d['count']})" for d in dups[:10]]))

    with open(output_md, 'w', encoding='utf-8') as f:
        f.write("\n".join(lines))
    logger.info("已生成Markdown报告: %s", output_md)


def main():
    excel_path = os.path.join(
        os.path.dirname(__file__),
        '20251005_fund_view_sql',
        'Fund attributes analysis.xlsx'
    )

    try:
        sheets = load_excel_sheets(excel_path)
    except Exception as e:
        logger.error("加载Excel失败: %s", e)
        raise

    analyses: Dict[str, Dict[str, Any]] = {}
    for sheet_name, df in sheets.items():
        try:
            analyses[sheet_name] = analyze_dataframe(df)
        except Exception as e:
            logger.warning("分析工作表失败: %s, 错误: %s", sheet_name, e)

    # 输出目录
    out_dir = os.path.join(os.path.dirname(__file__), 'output')
    os.makedirs(out_dir, exist_ok=True)

    # 保存JSON与Markdown报告
    json_path = os.path.join(out_dir, 'fund_attributes_analysis_summary.json')
    md_path = os.path.join(out_dir, 'fund_attributes_analysis_report.md')

    with open(json_path, 'w', encoding='utf-8') as f:
        json.dump({'excel_path': excel_path, 'analyses': analyses}, f, ensure_ascii=False, indent=2)
    logger.info("已生成JSON报告: %s", json_path)

    generate_markdown_report(excel_path, analyses, md_path)

    # 控制台输出简要摘要
    print(json.dumps({'excel_path': excel_path, 'sheets': list(sheets.keys())}, ensure_ascii=False, indent=2))


if __name__ == '__main__':
    main()