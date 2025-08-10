#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
WikiSQL问题数据导入脚本
专门导入dev.jsonl中的问题和SQL查询数据
作者: Augment Agent
日期: 2025-02-04
"""

import json
import mysql.connector
from mysql.connector import Error
from pathlib import Path
from tqdm import tqdm
import logging
import sys
from typing import Dict, Optional

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

class WikiSQLQuestionsImporter:
    def __init__(self, mysql_config: Dict, wikisql_data_path: str):
        self.mysql_config = mysql_config
        self.wikisql_path = Path(wikisql_data_path)
        self.mysql_conn = None
        
    def connect_mysql(self) -> bool:
        """连接MySQL数据库"""
        try:
            self.mysql_conn = mysql.connector.connect(**self.mysql_config)
            logger.info("✅ MySQL连接成功")
            return True
        except Error as e:
            logger.error(f"❌ MySQL连接失败: {e}")
            return False
    
    def get_table_meta_id(self, table_id: str) -> Optional[int]:
        """获取表的meta_id"""
        if not self.mysql_conn:
            return None
        
        try:
            cursor = self.mysql_conn.cursor()
            cursor.execute(
                "SELECT id FROM wikisql_tables_meta WHERE original_table_id = %s",
                (table_id,)
            )
            result = cursor.fetchone()
            cursor.close()
            
            return result[0] if result else None
            
        except Error as e:
            logger.error(f"❌ 获取表meta_id失败: {e}")
            return None
    
    def import_questions(self, data_source: str) -> bool:
        """导入问题数据"""
        questions_file = self.wikisql_path / f"{data_source}.jsonl"
        
        if not questions_file.exists():
            logger.error(f"问题文件不存在: {questions_file}")
            return False
        
        logger.info(f"📥 开始导入 {data_source} 问题数据...")
        
        try:
            cursor = self.mysql_conn.cursor()
            
            # 统计信息
            total_questions = 0
            success_count = 0
            error_count = 0
            missing_table_count = 0
            
            with open(questions_file, 'r', encoding='utf-8') as f:
                # 先计算总行数
                lines = f.readlines()
                total_lines = len(lines)
                
                logger.info(f"发现 {total_lines} 个问题")
                
                # 重新读取文件进行处理
                f.seek(0)
                
                for line_num, line in enumerate(tqdm(lines, desc=f"导入{data_source}问题"), 1):
                    try:
                        question_data = json.loads(line.strip())
                        total_questions += 1
                        
                        table_id = question_data['table_id']
                        
                        # 查找对应的表元数据ID
                        table_meta_id = self.get_table_meta_id(table_id)
                        
                        if not table_meta_id:
                            missing_table_count += 1
                            if missing_table_count <= 10:  # 只显示前10个缺失的表
                                logger.warning(f"未找到表 {table_id} 的元数据")
                            continue
                        
                        # 插入问题
                        insert_question = """
                        INSERT INTO wikisql_questions 
                        (table_meta_id, question, sql_select_col, sql_agg_func, 
                         sql_conditions, data_source)
                        VALUES (%s, %s, %s, %s, %s, %s)
                        """
                        
                        sql_info = question_data.get('sql', {})
                        
                        cursor.execute(insert_question, (
                            table_meta_id,
                            question_data['question'],
                            sql_info.get('sel'),
                            sql_info.get('agg'),
                            json.dumps(sql_info.get('conds', [])),
                            data_source
                        ))
                        
                        success_count += 1
                        
                        # 每1000条提交一次
                        if success_count % 1000 == 0:
                            self.mysql_conn.commit()
                            logger.info(f"已导入 {success_count} 个问题...")
                        
                    except json.JSONDecodeError as e:
                        logger.warning(f"跳过无效JSON行 {line_num}: {e}")
                        error_count += 1
                        continue
                    except Exception as e:
                        logger.error(f"处理问题 {line_num} 时发生错误: {e}")
                        error_count += 1
                        continue
            
            # 最终提交
            self.mysql_conn.commit()
            cursor.close()
            
            logger.info(f"🎉 {data_source} 问题数据导入完成!")
            logger.info(f"   总问题数: {total_questions}")
            logger.info(f"   成功导入: {success_count}")
            logger.info(f"   错误数量: {error_count}")
            logger.info(f"   缺失表数: {missing_table_count}")
            
            return True
            
        except Exception as e:
            logger.error(f"❌ 导入问题数据失败: {e}")
            return False
    
    def get_import_statistics(self) -> Dict:
        """获取导入统计信息"""
        if not self.mysql_conn:
            return {}
        
        try:
            cursor = self.mysql_conn.cursor(dictionary=True)
            
            stats = {}
            
            # 问题统计
            cursor.execute("""
                SELECT data_source, COUNT(*) as question_count 
                FROM wikisql_questions 
                GROUP BY data_source
            """)
            stats['questions_by_source'] = {row['data_source']: row['question_count'] 
                                          for row in cursor.fetchall()}
            
            # 总体统计
            cursor.execute("SELECT COUNT(*) as total_questions FROM wikisql_questions")
            stats['total_questions'] = cursor.fetchone()['total_questions']
            
            # 表覆盖率
            cursor.execute("""
                SELECT 
                    COUNT(DISTINCT wq.table_meta_id) as tables_with_questions,
                    (SELECT COUNT(*) FROM wikisql_tables_meta) as total_tables
                FROM wikisql_questions wq
            """)
            result = cursor.fetchone()
            stats['tables_with_questions'] = result['tables_with_questions']
            stats['total_tables'] = result['total_tables']
            stats['coverage_percentage'] = round(
                (result['tables_with_questions'] / result['total_tables']) * 100, 2
            ) if result['total_tables'] > 0 else 0
            
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
    # MySQL配置
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
    importer = WikiSQLQuestionsImporter(mysql_config, wikisql_data_path)
    
    try:
        # 1. 连接MySQL
        if not importer.connect_mysql():
            logger.error("❌ 无法连接MySQL，请检查服务状态")
            return 1
        
        # 2. 导入开发集问题数据
        logger.info("🚀 开始导入开发集问题数据...")
        if importer.import_questions('dev'):
            logger.info("✅ 开发集问题数据导入成功")
            
            # 3. 显示统计信息
            stats = importer.get_import_statistics()
            logger.info("📊 导入统计信息:")
            for key, value in stats.items():
                logger.info(f"   {key}: {value}")
            
        else:
            logger.error("❌ 开发集问题数据导入失败")
            return 1
        
        logger.info("🎉 WikiSQL问题数据导入完成！")
        return 0
        
    except Exception as e:
        logger.error(f"❌ 导入过程发生异常: {e}")
        return 1
    finally:
        importer.close_connection()


if __name__ == "__main__":
    sys.exit(main())
