#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
WikiSQL完整数据导入MySQL解决方案
支持SQLite数据库和JSONL元数据的完整导入
作者: Augment Agent
日期: 2025-02-04
"""

import sqlite3
import json
import mysql.connector
from mysql.connector import Error
from pathlib import Path
from tqdm import tqdm
import logging
import re
from typing import Dict, List, Tuple, Optional
import sys

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

class WikiSQLMySQLImporter:
    def __init__(self, mysql_config: Dict, wikisql_data_path: str):
        self.mysql_config = mysql_config
        self.wikisql_path = Path(wikisql_data_path)
        self.mysql_conn = None
        
        # 数据类型映射
        self.sqlite_to_mysql_types = {
            'TEXT': 'VARCHAR(500)',
            'INTEGER': 'INT',
            'REAL': 'DECIMAL(15,4)',
            'BLOB': 'LONGBLOB',
            'NUMERIC': 'DECIMAL(15,4)'
        }
        
        # 语义信息缓存
        self.semantic_cache = {}
        
    def connect_mysql(self) -> bool:
        """连接MySQL数据库"""
        try:
            self.mysql_conn = mysql.connector.connect(**self.mysql_config)
            logger.info("✅ MySQL连接成功")
            return True
        except Error as e:
            logger.error(f"❌ MySQL连接失败: {e}")
            return False
    
    def create_metadata_tables(self) -> bool:
        """创建元数据表"""
        if not self.mysql_conn:
            return False
            
        try:
            cursor = self.mysql_conn.cursor()
            
            # 1. 表元数据表
            create_tables_meta = """
            CREATE TABLE IF NOT EXISTS wikisql_tables_meta (
                id INT AUTO_INCREMENT PRIMARY KEY,
                original_table_id VARCHAR(50) NOT NULL UNIQUE,
                sqlite_table_name VARCHAR(100) NOT NULL,
                mysql_table_name VARCHAR(100) NOT NULL,
                page_title VARCHAR(500),
                section_title VARCHAR(200),
                business_domain VARCHAR(100),
                description TEXT,
                column_count INT DEFAULT 0,
                row_count INT DEFAULT 0,
                data_source ENUM('dev', 'test', 'train') NOT NULL,
                created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                UNIQUE KEY uk_original_table_id (original_table_id),
                INDEX idx_page_title (page_title(100)),
                INDEX idx_data_source (data_source)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WikiSQL表元数据'
            """
            
            cursor.execute(create_tables_meta)
            logger.info("✅ 创建wikisql_tables_meta表成功")
            
            # 2. 列元数据表
            create_columns_meta = """
            CREATE TABLE IF NOT EXISTS wikisql_columns_meta (
                id INT AUTO_INCREMENT PRIMARY KEY,
                table_meta_id INT NOT NULL,
                column_index INT NOT NULL,
                original_column_name VARCHAR(200) NOT NULL,
                mysql_column_name VARCHAR(100) NOT NULL,
                sqlite_column_name VARCHAR(100) NOT NULL,
                data_type VARCHAR(50) NOT NULL,
                mysql_data_type VARCHAR(50) NOT NULL,
                description TEXT,
                sample_values TEXT,
                created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (table_meta_id) REFERENCES wikisql_tables_meta(id) ON DELETE CASCADE,
                INDEX idx_table_meta_id (table_meta_id),
                INDEX idx_column_index (column_index)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WikiSQL列元数据'
            """
            
            cursor.execute(create_columns_meta)
            logger.info("✅ 创建wikisql_columns_meta表成功")
            
            # 3. 问题查询表
            create_questions = """
            CREATE TABLE IF NOT EXISTS wikisql_questions (
                id INT AUTO_INCREMENT PRIMARY KEY,
                table_meta_id INT NOT NULL,
                question TEXT NOT NULL,
                sql_select_col INT,
                sql_agg_func INT,
                sql_conditions JSON,
                data_source ENUM('dev', 'test', 'train') NOT NULL,
                created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (table_meta_id) REFERENCES wikisql_tables_meta(id) ON DELETE CASCADE,
                INDEX idx_table_meta_id (table_meta_id),
                INDEX idx_data_source (data_source),
                FULLTEXT KEY ft_question (question)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='WikiSQL问题查询'
            """
            
            cursor.execute(create_questions)
            logger.info("✅ 创建wikisql_questions表成功")
            
            self.mysql_conn.commit()
            cursor.close()
            return True
            
        except Error as e:
            logger.error(f"❌ 创建元数据表失败: {e}")
            return False
    
    def load_semantic_info(self, data_source: str) -> Dict:
        """加载语义信息"""
        semantic_info = {}
        
        tables_jsonl = self.wikisql_path / f"{data_source}.tables.jsonl"
        if not tables_jsonl.exists():
            logger.warning(f"表结构文件不存在: {tables_jsonl}")
            return semantic_info
        
        logger.info(f"📖 加载 {data_source} 语义信息...")
        
        with open(tables_jsonl, 'r', encoding='utf-8') as f:
            for line_num, line in enumerate(f):
                try:
                    table_data = json.loads(line.strip())
                    table_id = table_data['id']
                    
                    # 提取语义信息
                    semantic_info[table_id] = {
                        'page_title': table_data.get('page_title', ''),
                        'section_title': table_data.get('section_title', ''),
                        'headers': table_data.get('header', []),
                        'types': table_data.get('types', []),
                        'caption': table_data.get('caption', ''),
                        'name': table_data.get('name', '')
                    }
                    
                except json.JSONDecodeError as e:
                    logger.warning(f"跳过无效JSON行 {line_num}: {e}")
                    continue
                except Exception as e:
                    logger.error(f"处理行 {line_num} 时发生错误: {e}")
                    continue
        
        logger.info(f"✅ 加载了 {len(semantic_info)} 个表的语义信息")
        return semantic_info
    
    def get_sqlite_table_info(self, sqlite_db_path: Path, table_name: str) -> Optional[Dict]:
        """获取SQLite表信息"""
        try:
            conn = sqlite3.connect(sqlite_db_path)
            cursor = conn.cursor()
            
            # 获取表结构
            cursor.execute(f"PRAGMA table_info({table_name})")
            columns_info = cursor.fetchall()
            
            # 获取行数
            cursor.execute(f"SELECT COUNT(*) FROM {table_name}")
            row_count = cursor.fetchone()[0]
            
            # 获取示例数据
            cursor.execute(f"SELECT * FROM {table_name} LIMIT 3")
            sample_rows = cursor.fetchall()
            
            conn.close()
            
            return {
                'columns': columns_info,
                'row_count': row_count,
                'sample_rows': sample_rows
            }
            
        except Exception as e:
            logger.error(f"获取SQLite表信息失败 {table_name}: {e}")
            return None
    
    def generate_mysql_table_name(self, table_id: str) -> str:
        """生成MySQL表名"""
        # 将特殊字符替换为下划线
        clean_id = re.sub(r'[^a-zA-Z0-9]', '_', table_id)
        return f"wikisql_data_{clean_id}"
    
    def generate_mysql_column_name(self, column_name: str, index: int) -> str:
        """生成MySQL列名"""
        if not column_name or column_name.strip() == '':
            return f"col_{index}"
        
        # 清理列名
        clean_name = re.sub(r'[^a-zA-Z0-9]', '_', column_name.lower())
        clean_name = re.sub(r'_+', '_', clean_name).strip('_')
        
        # 避免MySQL保留字
        mysql_reserved = ['order', 'group', 'select', 'from', 'where', 'limit', 'index']
        if clean_name in mysql_reserved:
            clean_name = f"col_{clean_name}"
        
        return clean_name or f"col_{index}"
    
    def create_mysql_table_from_sqlite(self, table_id: str, sqlite_table_name: str, 
                                     sqlite_db_path: Path, semantic_info: Dict) -> Optional[Tuple]:
        """从SQLite表创建MySQL表"""
        
        # 获取SQLite表信息
        sqlite_info = self.get_sqlite_table_info(sqlite_db_path, sqlite_table_name)
        if not sqlite_info:
            return None
        
        # 生成MySQL表名
        mysql_table_name = self.generate_mysql_table_name(table_id)
        
        # 获取语义信息
        table_semantic = semantic_info.get(table_id, {})
        headers = table_semantic.get('headers', [])
        types = table_semantic.get('types', [])
        
        try:
            cursor = self.mysql_conn.cursor()
            
            # 构建列定义
            column_definitions = []
            column_definitions.append("id INT AUTO_INCREMENT PRIMARY KEY")
            
            columns_meta = []
            
            for i, (cid, name, type_name, notnull, dflt_value, pk) in enumerate(sqlite_info['columns']):
                # 获取原始列名
                original_name = headers[i] if i < len(headers) else name
                
                # 生成MySQL列名
                mysql_col_name = self.generate_mysql_column_name(original_name, i)
                
                # 转换数据类型
                mysql_type = self.sqlite_to_mysql_types.get(type_name.upper(), 'VARCHAR(500)')
                
                column_definitions.append(f"{mysql_col_name} {mysql_type}")
                
                # 记录列元数据
                columns_meta.append({
                    'index': i,
                    'original_name': original_name,
                    'mysql_name': mysql_col_name,
                    'sqlite_name': name,
                    'data_type': type_name,
                    'mysql_type': mysql_type
                })
            
            # 添加时间戳列
            column_definitions.append("created_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
            
            # 创建表
            create_table_sql = f"""
            CREATE TABLE IF NOT EXISTS {mysql_table_name} (
                {', '.join(column_definitions)}
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 
            COMMENT='{table_semantic.get("page_title", "")[:100]}'
            """
            
            cursor.execute(create_table_sql)
            logger.info(f"✅ 创建MySQL表: {mysql_table_name}")
            
            cursor.close()
            return mysql_table_name, columns_meta
            
        except Error as e:
            logger.error(f"❌ 创建MySQL表失败 {mysql_table_name}: {e}")
            return None
    
    def import_table_data(self, sqlite_db_path: Path, sqlite_table_name: str, 
                         mysql_table_name: str, columns_meta: List[Dict]) -> bool:
        """导入表数据"""
        try:
            # 连接SQLite
            sqlite_conn = sqlite3.connect(sqlite_db_path)
            sqlite_cursor = sqlite_conn.cursor()
            
            # 获取所有数据
            sqlite_cursor.execute(f"SELECT * FROM {sqlite_table_name}")
            rows = sqlite_cursor.fetchall()
            
            if not rows:
                logger.info(f"表 {sqlite_table_name} 无数据")
                sqlite_conn.close()
                return True
            
            # 准备MySQL插入
            mysql_cursor = self.mysql_conn.cursor()
            
            # 构建插入SQL
            mysql_columns = [col['mysql_name'] for col in columns_meta]
            placeholders = ', '.join(['%s'] * len(mysql_columns))
            
            insert_sql = f"""
            INSERT INTO {mysql_table_name} ({', '.join(mysql_columns)})
            VALUES ({placeholders})
            """
            
            # 批量插入数据
            batch_data = []
            for row in rows:
                # 确保数据长度匹配
                processed_row = []
                for i, value in enumerate(row):
                    if i < len(columns_meta):
                        # 数据清理和类型转换
                        if value is None:
                            processed_row.append(None)
                        else:
                            # 限制字符串长度
                            str_value = str(value)[:500] if value else None
                            processed_row.append(str_value)
                    
                batch_data.append(tuple(processed_row))
            
            # 执行批量插入
            mysql_cursor.executemany(insert_sql, batch_data)
            self.mysql_conn.commit()
            
            logger.info(f"✅ 导入 {len(batch_data)} 行数据到 {mysql_table_name}")
            
            sqlite_conn.close()
            mysql_cursor.close()
            return True
            
        except Exception as e:
            logger.error(f"❌ 导入表数据失败 {mysql_table_name}: {e}")
            return False
    
    def record_table_metadata(self, table_id: str, sqlite_table_name: str, 
                            mysql_table_name: str, semantic_info: Dict, 
                            columns_meta: List[Dict], data_source: str) -> bool:
        """记录表元数据"""
        try:
            cursor = self.mysql_conn.cursor()
            
            # 插入表元数据
            insert_table_meta = """
            INSERT INTO wikisql_tables_meta 
            (original_table_id, sqlite_table_name, mysql_table_name, page_title, 
             section_title, description, column_count, data_source)
            VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
            """
            
            page_title = semantic_info.get('page_title', '')
            section_title = semantic_info.get('section_title', '')
            description = f"{page_title} - {section_title}" if section_title else page_title
            
            cursor.execute(insert_table_meta, (
                table_id, sqlite_table_name, mysql_table_name,
                page_title, section_title, description,
                len(columns_meta), data_source
            ))
            
            table_meta_id = cursor.lastrowid
            
            # 插入列元数据
            for col_meta in columns_meta:
                insert_column_meta = """
                INSERT INTO wikisql_columns_meta 
                (table_meta_id, column_index, original_column_name, mysql_column_name,
                 sqlite_column_name, data_type, mysql_data_type, description)
                VALUES (%s, %s, %s, %s, %s, %s, %s, %s)
                """
                
                cursor.execute(insert_column_meta, (
                    table_meta_id, col_meta['index'], col_meta['original_name'],
                    col_meta['mysql_name'], col_meta['sqlite_name'],
                    col_meta['data_type'], col_meta['mysql_type'],
                    f"{col_meta['original_name']}列数据"
                ))
            
            self.mysql_conn.commit()
            cursor.close()
            return True
            
        except Error as e:
            logger.error(f"❌ 记录表元数据失败: {e}")
            return False
    
    def import_data_source(self, data_source: str, limit: Optional[int] = None) -> bool:
        """导入指定数据源"""
        logger.info(f"🚀 开始导入 {data_source} 数据集...")

        # 文件路径
        sqlite_db = self.wikisql_path / f"{data_source}.db"

        if not sqlite_db.exists():
            logger.error(f"SQLite数据库不存在: {sqlite_db}")
            return False

        # 加载语义信息
        semantic_info = self.load_semantic_info(data_source)

        # 获取SQLite表列表
        sqlite_conn = sqlite3.connect(sqlite_db)
        cursor = sqlite_conn.cursor()
        cursor.execute("SELECT name FROM sqlite_master WHERE type='table'")
        sqlite_tables = [row[0] for row in cursor.fetchall()]
        sqlite_conn.close()

        logger.info(f"发现 {len(sqlite_tables)} 个SQLite表")

        # 限制处理数量
        if limit:
            sqlite_tables = sqlite_tables[:limit]
            logger.info(f"限制处理 {limit} 个表")

        success_count = 0
        error_count = 0

        # 处理每个表
        for table_name in tqdm(sqlite_tables, desc=f"导入{data_source}表"):
            try:
                # 从表名提取table_id
                # table_1_10015132_11 -> 1-10015132-11
                parts = table_name.split('_')
                if len(parts) >= 4:
                    table_id = f"{parts[1]}-{parts[2]}-{parts[3]}"
                else:
                    logger.warning(f"无法解析表ID: {table_name}")
                    continue

                # 创建MySQL表
                result = self.create_mysql_table_from_sqlite(
                    table_id, table_name, sqlite_db, semantic_info
                )

                if not result:
                    error_count += 1
                    continue

                mysql_table_name, columns_meta = result

                # 导入数据
                if self.import_table_data(sqlite_db, table_name, mysql_table_name, columns_meta):
                    # 记录元数据
                    self.record_table_metadata(table_id, table_name, mysql_table_name,
                                             semantic_info.get(table_id, {}),
                                             columns_meta, data_source)
                    success_count += 1
                else:
                    error_count += 1

            except Exception as e:
                logger.error(f"处理表 {table_name} 时发生错误: {e}")
                error_count += 1
                continue

        logger.info(f"🎉 {data_source} 数据集导入完成！成功: {success_count}, 失败: {error_count}")
        return success_count > 0

    def get_import_statistics(self) -> Dict:
        """获取导入统计信息"""
        if not self.mysql_conn:
            return {}

        try:
            cursor = self.mysql_conn.cursor(dictionary=True)

            stats = {}

            # 表统计
            cursor.execute("""
                SELECT data_source, COUNT(*) as table_count
                FROM wikisql_tables_meta
                GROUP BY data_source
            """)
            stats['tables_by_source'] = {row['data_source']: row['table_count']
                                       for row in cursor.fetchall()}

            # 总体统计
            cursor.execute("SELECT COUNT(*) as total_tables FROM wikisql_tables_meta")
            stats['total_tables'] = cursor.fetchone()['total_tables']

            # 数据库大小
            cursor.execute("""
                SELECT
                    ROUND(SUM(data_length + index_length) / 1024 / 1024, 2) AS size_mb
                FROM information_schema.tables
                WHERE table_schema = DATABASE()
                AND table_name LIKE 'wikisql_%'
            """)
            result = cursor.fetchone()
            stats['database_size_mb'] = result['size_mb'] if result['size_mb'] else 0

            cursor.close()
            return stats

        except Exception as e:
            logger.error(f"❌ 获取统计信息失败: {e}")
            return {}

    def close_connection(self):
        """关闭数据库连接"""
        if self.mysql_conn:
            self.mysql_conn.close()
            logger.info("🔌 数据库连接已关闭")


def main():
    """主函数"""
    # MySQL配置 (根据外部服务脚本配置)
    mysql_config = {
        'host': 'localhost',
        'port': 3306,
        'database': 'nl2sql',
        'user': 'nl2sql_user',
        'password': 'nl2sql_pass',
        'charset': 'utf8mb4',
        'autocommit': False
    }

    # WikiSQL数据路径 - 使用相对路径
    import os
    script_dir = os.path.dirname(os.path.abspath(__file__))
    project_root = os.path.dirname(script_dir)
    wikisql_data_path = os.path.join(project_root, "spring-ai-alibaba/spring-ai-alibaba-nl2sql/doc/pager/WikiSQL/data 2")

    # 创建导入器
    importer = WikiSQLMySQLImporter(mysql_config, wikisql_data_path)

    try:
        # 1. 连接MySQL
        if not importer.connect_mysql():
            logger.error("❌ 无法连接MySQL，请检查服务状态")
            return 1

        # 2. 创建元数据表
        if not importer.create_metadata_tables():
            logger.error("❌ 创建元数据表失败")
            return 1

        # 3. 导入开发集数据 (完整导入)
        logger.info("🚀 开始导入开发集数据...")
        if importer.import_data_source('dev', limit=None):  # 完整导入所有表
            logger.info("✅ 开发集数据导入成功")

            # 4. 显示统计信息
            stats = importer.get_import_statistics()
            logger.info("📊 导入统计信息:")
            for key, value in stats.items():
                logger.info(f"   {key}: {value}")

        else:
            logger.error("❌ 开发集数据导入失败")
            return 1

        logger.info("🎉 WikiSQL数据导入完成！")
        return 0

    except Exception as e:
        logger.error(f"❌ 导入过程发生异常: {e}")
        return 1
    finally:
        importer.close_connection()


if __name__ == "__main__":
    sys.exit(main())
