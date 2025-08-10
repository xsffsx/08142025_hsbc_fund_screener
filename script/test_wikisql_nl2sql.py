#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
WikiSQL NL2SQL测试脚本
测试导入的数据和语义映射功能
作者: Augment Agent
日期: 2025-02-04
"""

import mysql.connector
from mysql.connector import Error
import logging
import re
from typing import Dict, List, Optional

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

class WikiSQLNL2SQLTester:
    def __init__(self, mysql_config: Dict):
        self.mysql_config = mysql_config
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
    
    def search_tables_by_keywords(self, keywords: str) -> List[Dict]:
        """根据关键词搜索相关表"""
        if not self.mysql_conn:
            return []
        
        try:
            cursor = self.mysql_conn.cursor(dictionary=True)
            
            # 搜索页面标题中包含关键词的表
            query = """
            SELECT 
                original_table_id,
                mysql_table_name,
                page_title,
                section_title,
                description,
                column_count
            FROM wikisql_tables_meta 
            WHERE page_title LIKE %s 
               OR description LIKE %s
               OR section_title LIKE %s
            ORDER BY 
                CASE 
                    WHEN page_title LIKE %s THEN 1
                    WHEN description LIKE %s THEN 2
                    ELSE 3
                END
            LIMIT 10
            """
            
            like_pattern = f"%{keywords}%"
            cursor.execute(query, (like_pattern, like_pattern, like_pattern, like_pattern, like_pattern))
            
            results = cursor.fetchall()
            cursor.close()
            
            return results
            
        except Error as e:
            logger.error(f"❌ 搜索表失败: {e}")
            return []
    
    def get_table_columns(self, table_meta_id: int) -> List[Dict]:
        """获取表的列信息"""
        if not self.mysql_conn:
            return []
        
        try:
            cursor = self.mysql_conn.cursor(dictionary=True)
            
            query = """
            SELECT 
                column_index,
                original_column_name,
                mysql_column_name,
                data_type,
                description
            FROM wikisql_columns_meta 
            WHERE table_meta_id = %s
            ORDER BY column_index
            """
            
            cursor.execute(query, (table_meta_id,))
            results = cursor.fetchall()
            cursor.close()
            
            return results
            
        except Error as e:
            logger.error(f"❌ 获取列信息失败: {e}")
            return []
    
    def preview_table_data(self, mysql_table_name: str, limit: int = 5) -> List[Dict]:
        """预览表数据"""
        if not self.mysql_conn:
            return []
        
        try:
            cursor = self.mysql_conn.cursor(dictionary=True)
            
            # 动态构建查询，排除id和created_time列
            cursor.execute(f"DESCRIBE {mysql_table_name}")
            columns = cursor.fetchall()
            
            # 过滤掉系统列
            data_columns = [col['Field'] for col in columns 
                          if col['Field'] not in ['id', 'created_time']]
            
            if not data_columns:
                return []
            
            query = f"SELECT {', '.join(data_columns)} FROM {mysql_table_name} LIMIT %s"
            cursor.execute(query, (limit,))
            
            results = cursor.fetchall()
            cursor.close()
            
            return results
            
        except Error as e:
            logger.error(f"❌ 预览表数据失败: {e}")
            return []
    
    def find_matching_columns(self, table_meta_id: int, column_keywords: str) -> List[Dict]:
        """查找匹配的列"""
        columns = self.get_table_columns(table_meta_id)
        
        matching_columns = []
        keywords_lower = column_keywords.lower()
        
        for col in columns:
            original_name = col['original_column_name'].lower()
            mysql_name = col['mysql_column_name'].lower()
            
            # 检查是否匹配
            if (keywords_lower in original_name or 
                keywords_lower in mysql_name or
                any(keyword.strip() in original_name for keyword in keywords_lower.split(',')) or
                any(keyword.strip() in mysql_name for keyword in keywords_lower.split(','))):
                matching_columns.append(col)
        
        return matching_columns
    
    def generate_simple_sql(self, table_name: str, columns: List[Dict], 
                          question: str) -> str:
        """生成简单的SQL查询"""
        
        question_lower = question.lower()
        
        # 分析问题类型
        if any(word in question_lower for word in ['多少', 'how many', 'count', '数量']):
            # 计数查询
            return f"SELECT COUNT(*) as count FROM {table_name}"
        
        elif any(word in question_lower for word in ['什么', 'what', '哪些', 'which']):
            # 查询特定信息
            if columns:
                col_names = [col['mysql_column_name'] for col in columns[:3]]
                return f"SELECT {', '.join(col_names)} FROM {table_name} LIMIT 10"
            else:
                return f"SELECT * FROM {table_name} LIMIT 10"
        
        elif any(word in question_lower for word in ['所有', 'all', '全部']):
            # 查询所有
            return f"SELECT * FROM {table_name} LIMIT 20"
        
        else:
            # 默认查询
            return f"SELECT * FROM {table_name} LIMIT 10"
    
    def process_nl_question(self, question: str) -> Dict:
        """处理自然语言问题"""
        logger.info(f"🤔 处理问题: {question}")
        
        # 1. 提取关键词
        # 简化的关键词提取
        keywords = []
        
        # 常见的表相关关键词 (中英文)
        table_keywords = ['toronto', 'raptors', 'basketball', 'player', 'team',
                         'election', 'government', 'leader', 'country',
                         '多伦多', '猛龙', '篮球', '球员', '运动员', '队伍',
                         '选举', '政府', '领导', '国家', 'indycar', 'formula',
                         'season', '赛季', 'hawks', '老鹰', 'lakers', '湖人']
        
        question_lower = question.lower()
        for keyword in table_keywords:
            if keyword in question_lower:
                keywords.append(keyword)
        
        if not keywords:
            # 如果没有找到特定关键词，使用问题中的名词
            words = re.findall(r'\b\w+\b', question_lower)
            keywords = [word for word in words if len(word) > 2][:3]

            # 添加中文关键词提取
            chinese_words = re.findall(r'[\u4e00-\u9fff]+', question)
            keywords.extend([word for word in chinese_words if len(word) > 1][:2])
        
        # 2. 搜索相关表 - 使用多种搜索策略
        matching_tables = []

        # 策略1: 使用提取的关键词
        if keywords:
            for keyword in keywords[:3]:
                tables = self.search_tables_by_keywords(keyword)
                matching_tables.extend(tables)

        # 策略2: 如果没有找到，尝试更宽泛的搜索
        if not matching_tables:
            search_term = question_lower[:20]
            matching_tables = self.search_tables_by_keywords(search_term)

        # 策略3: 特殊关键词映射
        keyword_mappings = {
            '多伦多': 'Toronto',
            '猛龙': 'Raptors',
            '湖人': 'Lakers',
            '老鹰': 'Hawks',
            '赛季': 'season',
            '季后赛': 'Playoffs'
        }

        if not matching_tables:
            for chinese, english in keyword_mappings.items():
                if chinese in question:
                    tables = self.search_tables_by_keywords(english)
                    matching_tables.extend(tables)

        # 去重
        seen_ids = set()
        unique_tables = []
        for table in matching_tables:
            if table['original_table_id'] not in seen_ids:
                unique_tables.append(table)
                seen_ids.add(table['original_table_id'])

        matching_tables = unique_tables[:5]  # 限制前5个结果
        
        if not matching_tables:
            return {
                'success': False,
                'error': f'未找到与问题相关的表。关键词: {keywords}',
                'question': question
            }
        
        # 3. 选择最佳表
        best_table = matching_tables[0]
        table_meta_id = self.get_table_meta_id(best_table['original_table_id'])
        
        # 4. 获取列信息
        columns = self.get_table_columns(table_meta_id)
        
        # 5. 查找相关列
        column_keywords = []
        if 'position' in question_lower or '位置' in question_lower or '职位' in question_lower:
            column_keywords.append('position')
        if 'player' in question_lower or '球员' in question_lower or '运动员' in question_lower:
            column_keywords.append('player')
        if 'name' in question_lower or '姓名' in question_lower or '名字' in question_lower:
            column_keywords.append('name')
        if 'country' in question_lower or '国家' in question_lower or '国籍' in question_lower:
            column_keywords.append('nationality')
        
        matching_columns = []
        if column_keywords:
            for keyword in column_keywords:
                matching_cols = self.find_matching_columns(table_meta_id, keyword)
                matching_columns.extend(matching_cols)
        
        # 6. 生成SQL
        sql_query = self.generate_simple_sql(
            best_table['mysql_table_name'], 
            matching_columns, 
            question
        )
        
        # 7. 预览数据
        preview_data = self.preview_table_data(best_table['mysql_table_name'], 3)
        
        return {
            'success': True,
            'question': question,
            'search_keywords': ' '.join(keywords) if keywords else question[:20],
            'matched_table': best_table,
            'matched_columns': matching_columns,
            'sql_query': sql_query,
            'preview_data': preview_data,
            'explanation': f"在表 '{best_table['page_title']}' 中查询相关信息"
        }
    
    def get_table_meta_id(self, original_table_id: str) -> Optional[int]:
        """获取表的meta_id"""
        if not self.mysql_conn:
            return None
        
        try:
            cursor = self.mysql_conn.cursor()
            cursor.execute(
                "SELECT id FROM wikisql_tables_meta WHERE original_table_id = %s",
                (original_table_id,)
            )
            result = cursor.fetchone()
            cursor.close()
            
            return result[0] if result else None
            
        except Error as e:
            logger.error(f"❌ 获取表meta_id失败: {e}")
            return None
    
    def execute_sql_query(self, sql_query: str) -> List[Dict]:
        """执行SQL查询"""
        if not self.mysql_conn:
            return []
        
        try:
            cursor = self.mysql_conn.cursor(dictionary=True)
            cursor.execute(sql_query)
            results = cursor.fetchall()
            cursor.close()
            
            return results
            
        except Error as e:
            logger.error(f"❌ 执行SQL查询失败: {e}")
            return []
    
    def run_test_cases(self):
        """运行测试案例"""
        test_questions = [
            "多伦多猛龙队有多少个球员？",
            "Toronto Raptors有哪些球员？",
            "显示多伦多猛龙队球员信息",
            "What is the Toronto Raptors roster?",
            "IndyCar 1981赛季有哪些比赛？",
            "Show me Formula One 2000 season races",
            "湖人队2007-08赛季的季后赛情况"
        ]
        
        logger.info("🚀 开始NL2SQL测试...")
        
        for i, question in enumerate(test_questions, 1):
            logger.info(f"\n{'='*60}")
            logger.info(f"测试案例 {i}: {question}")
            logger.info(f"{'='*60}")
            
            result = self.process_nl_question(question)
            
            if result['success']:
                logger.info(f"✅ 匹配成功!")
                logger.info(f"📊 匹配表: {result['matched_table']['page_title']}")
                logger.info(f"🔍 搜索关键词: {result['search_keywords']}")
                logger.info(f"📋 匹配列: {[col['original_column_name'] for col in result['matched_columns']]}")
                logger.info(f"💻 生成SQL: {result['sql_query']}")
                
                # 执行SQL查询
                query_results = self.execute_sql_query(result['sql_query'])
                if query_results:
                    logger.info(f"📈 查询结果 (前3条):")
                    for j, row in enumerate(query_results[:3], 1):
                        logger.info(f"   {j}. {dict(row)}")
                else:
                    logger.info("📈 查询无结果")
                
            else:
                logger.error(f"❌ 处理失败: {result['error']}")
    
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
        'charset': 'utf8mb4'
    }
    
    # 创建测试器
    tester = WikiSQLNL2SQLTester(mysql_config)
    
    try:
        # 连接数据库
        if not tester.connect_mysql():
            logger.error("❌ 无法连接MySQL")
            return 1
        
        # 运行测试
        tester.run_test_cases()
        
        logger.info("\n🎉 NL2SQL测试完成！")
        return 0
        
    except Exception as e:
        logger.error(f"❌ 测试过程发生异常: {e}")
        return 1
    finally:
        tester.close_connection()


if __name__ == "__main__":
    import sys
    sys.exit(main())
