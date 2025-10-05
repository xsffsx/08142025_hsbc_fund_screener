#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
SGH基金问题验证系统 - DDL更新自动化验证脚本
创建时间: 2025-08-07 08:30:00
目的: 自动化验证DDL更新是否正确反映在Price和Product系统中
"""

import requests
import json
import time
import sys
from datetime import datetime
from typing import Dict, List, Any, Optional
import logging

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler(f'ddl_verification_{datetime.now().strftime("%Y%m%d_%H%M%S")}.log'),
        logging.StreamHandler(sys.stdout)
    ]
)

logger = logging.getLogger(__name__)

class DDLVerificationTool:
    """DDL验证工具类"""
    
    def __init__(self):
        """初始化配置"""
        self.config = {
            'price_base_url': 'http://localhost:9099',
            'product_graphql_url': 'http://localhost:8080/graphql',
            'product_utils_url': 'http://localhost:8081',
            'timeout': 30,
            'retry_count': 3
        }
        
        self.headers = {
            'Content-Type': 'application/json',
            'Accept': 'application/json',
            'channelId': 'OHI',
            'countryCode': 'HK',
            'groupMember': 'HBAP'
        }
        
        self.verification_results = {
            'timestamp': datetime.now().isoformat(),
            'systems': {},
            'tests': [],
            'summary': {
                'total_tests': 0,
                'passed_tests': 0,
                'failed_tests': 0,
                'warnings': 0
            }
        }
    
    def make_request(self, method: str, url: str, data: Optional[Dict] = None, 
                    headers: Optional[Dict] = None) -> Optional[Dict]:
        """发送HTTP请求"""
        try:
            req_headers = {**self.headers, **(headers or {})}
            
            for attempt in range(self.config['retry_count']):
                try:
                    if method.upper() == 'GET':
                        response = requests.get(url, headers=req_headers, 
                                              timeout=self.config['timeout'])
                    elif method.upper() == 'POST':
                        response = requests.post(url, json=data, headers=req_headers,
                                               timeout=self.config['timeout'])
                    else:
                        raise ValueError(f"不支持的HTTP方法: {method}")
                    
                    if response.status_code == 200:
                        try:
                            return response.json()
                        except json.JSONDecodeError:
                            return {'raw_response': response.text}
                    else:
                        logger.warning(f"请求失败 {url}: HTTP {response.status_code}")
                        return None
                        
                except requests.exceptions.RequestException as e:
                    logger.warning(f"请求异常 {url} (尝试 {attempt + 1}/{self.config['retry_count']}): {e}")
                    if attempt < self.config['retry_count'] - 1:
                        time.sleep(2 ** attempt)  # 指数退避
                    
            return None
            
        except Exception as e:
            logger.error(f"请求错误 {url}: {e}")
            return None
    
    def test_price_system_health(self) -> bool:
        """测试Price系统健康状态"""
        logger.info("测试Price系统健康状态...")
        
        # 测试基础健康检查
        health_url = f"{self.config['price_base_url']}/actuator/health"
        health_response = self.make_request('GET', health_url)
        
        health_status = False
        if health_response and health_response.get('status') == 'UP':
            health_status = True
            logger.info("✅ Price系统健康检查通过")
        else:
            logger.error("❌ Price系统健康检查失败")
        
        # 测试基础接口
        greeting_url = f"{self.config['price_base_url']}/greeting"
        greeting_response = self.make_request('GET', greeting_url)
        
        greeting_status = False
        if greeting_response and 'wmds-fund-app' in str(greeting_response):
            greeting_status = True
            logger.info("✅ Price系统基础接口正常")
        else:
            logger.error("❌ Price系统基础接口异常")
        
        # 记录测试结果
        self.verification_results['systems']['price_system'] = {
            'health_check': health_status,
            'basic_interface': greeting_status,
            'overall_status': health_status and greeting_status
        }
        
        self.add_test_result('Price系统健康检查', health_status and greeting_status)
        return health_status and greeting_status
    
    def test_price_system_ut_data(self) -> bool:
        """测试Price系统UT基金数据"""
        logger.info("测试Price系统UT基金数据...")
        
        # 测试基金报价摘要
        quote_summary_param = {
            "productType": "UT",
            "prodAltNum": "U50002", 
            "prodCdeAltClassCde": "M",
            "market": "HK"
        }
        
        quote_url = f"{self.config['price_base_url']}/wmds/fundQuoteSummary"
        quote_response = self.make_request('GET', f"{quote_url}?body={json.dumps(quote_summary_param)}")
        
        quote_status = False
        if quote_response and 'summary' in quote_response:
            quote_status = True
            logger.info("✅ 基金报价摘要查询正常")
            
            # 检查关键字段
            summary = quote_response.get('summary', {})
            if 'prodName' in summary:
                logger.info("✅ prodName字段存在")
            else:
                logger.warning("⚠️ prodName字段不存在")
                
        else:
            logger.error("❌ 基金报价摘要查询失败")
        
        # 测试基金搜索
        search_param = {
            "productType": "UT",
            "returnOnlyNumberOfMatches": False,
            "sortBy": "name",
            "sortOrder": "asc",
            "startDetail": "1",
            "endDetail": "10"
        }
        
        search_url = f"{self.config['price_base_url']}/wmds/fundSearchResult"
        search_response = self.make_request('GET', f"{search_url}?body={json.dumps(search_param)}")
        
        search_status = False
        if search_response and 'results' in search_response:
            search_status = True
            logger.info("✅ 基金搜索查询正常")
        else:
            logger.error("❌ 基金搜索查询失败")
        
        self.add_test_result('Price系统UT数据查询', quote_status and search_status)
        return quote_status and search_status
    
    def test_product_system_graphql(self) -> bool:
        """测试Product系统GraphQL接口"""
        logger.info("测试Product系统GraphQL接口...")
        
        # 测试TB_PROD表查询
        product_query = {
            "query": """
                query productByFilter($filter: JSON!) {
                    productByFilter(filter: $filter, limit: 5) {
                        prodId
                        prodName
                        prodTypeCde
                        riskLvlCde
                        prodStatCde
                        allowBuyUtProdInd
                        allowSellUtProdInd
                        prdRtrnAvgNum
                        rtrnVoltlAvgPct
                        yieldEnhnProdInd
                        asetClassCde
                        geoRiskInd
                    }
                }
            """,
            "variables": {
                "filter": {
                    "prodTypeCde": "UT",
                    "prodStatCde": "A"
                }
            }
        }
        
        graphql_response = self.make_request('POST', self.config['product_graphql_url'], product_query)
        
        graphql_status = False
        if graphql_response and 'data' in graphql_response:
            products = graphql_response.get('data', {}).get('productByFilter', [])
            if products:
                graphql_status = True
                logger.info("✅ Product GraphQL查询正常")
                
                # 检查关键字段
                first_product = products[0]
                key_fields = ['prodName', 'riskLvlCde', 'allowBuyUtProdInd', 'allowSellUtProdInd']
                for field in key_fields:
                    if field in first_product:
                        logger.info(f"✅ {field}字段存在")
                    else:
                        logger.warning(f"⚠️ {field}字段不存在")
            else:
                logger.warning("⚠️ 查询返回空结果")
        else:
            logger.error("❌ Product GraphQL查询失败")
            if graphql_response and 'errors' in graphql_response:
                logger.error(f"GraphQL错误: {graphql_response['errors']}")
        
        self.add_test_result('Product系统GraphQL查询', graphql_status)
        return graphql_status
    
    def test_database_connectivity(self) -> bool:
        """测试数据库连接性"""
        logger.info("测试数据库连接性...")
        
        # 通过Price系统健康检查测试数据库连接
        probing_url = f"{self.config['price_base_url']}/healthcheck/probing"
        probing_response = self.make_request('GET', probing_url)
        
        price_db_status = False
        if probing_response and 'success' in str(probing_response).lower():
            price_db_status = True
            logger.info("✅ Price系统数据库连接正常")
        else:
            logger.error("❌ Price系统数据库连接异常")
        
        # 通过GraphQL简单查询测试Product数据库连接
        simple_query = {
            "query": "query { productByFilter(filter: {}, limit: 1) { prodId } }"
        }
        
        simple_response = self.make_request('POST', self.config['product_graphql_url'], simple_query)
        
        product_db_status = False
        if simple_response and 'data' in simple_response:
            product_db_status = True
            logger.info("✅ Product系统数据库连接正常")
        else:
            logger.error("❌ Product系统数据库连接异常")
        
        self.add_test_result('数据库连接性', price_db_status and product_db_status)
        return price_db_status and product_db_status
    
    def add_test_result(self, test_name: str, passed: bool, details: Optional[str] = None):
        """添加测试结果"""
        self.verification_results['tests'].append({
            'name': test_name,
            'passed': passed,
            'timestamp': datetime.now().isoformat(),
            'details': details
        })
        
        self.verification_results['summary']['total_tests'] += 1
        if passed:
            self.verification_results['summary']['passed_tests'] += 1
        else:
            self.verification_results['summary']['failed_tests'] += 1
    
    def generate_report(self) -> str:
        """生成验证报告"""
        report_filename = f"ddl_verification_report_{datetime.now().strftime('%Y%m%d_%H%M%S')}.json"
        
        # 计算成功率
        total = self.verification_results['summary']['total_tests']
        passed = self.verification_results['summary']['passed_tests']
        success_rate = (passed / total * 100) if total > 0 else 0
        
        self.verification_results['summary']['success_rate'] = round(success_rate, 2)
        self.verification_results['summary']['recommendations'] = self.get_recommendations()
        
        # 保存报告
        with open(report_filename, 'w', encoding='utf-8') as f:
            json.dump(self.verification_results, f, ensure_ascii=False, indent=2)
        
        logger.info(f"验证报告已生成: {report_filename}")
        return report_filename
    
    def get_recommendations(self) -> List[str]:
        """获取建议"""
        recommendations = []
        
        if self.verification_results['summary']['failed_tests'] > 0:
            recommendations.append("检查失败的测试项，确认系统服务是否正常运行")
            recommendations.append("验证数据库连接配置是否正确")
            recommendations.append("确认DDL更新脚本是否已正确执行")
        
        if not self.verification_results['systems'].get('price_system', {}).get('overall_status', False):
            recommendations.append("Price系统异常，请检查服务状态和配置")
        
        recommendations.extend([
            "定期运行此验证脚本确保系统稳定性",
            "监控系统性能指标",
            "建立自动化告警机制"
        ])
        
        return recommendations
    
    def run_verification(self):
        """运行完整验证流程"""
        logger.info("开始DDL更新验证...")
        logger.info("=" * 50)
        
        try:
            # 执行各项测试
            self.test_price_system_health()
            self.test_price_system_ut_data()
            self.test_product_system_graphql()
            self.test_database_connectivity()
            
            # 生成报告
            report_file = self.generate_report()
            
            # 输出摘要
            summary = self.verification_results['summary']
            logger.info("=" * 50)
            logger.info("验证完成摘要:")
            logger.info(f"总测试数: {summary['total_tests']}")
            logger.info(f"通过测试: {summary['passed_tests']}")
            logger.info(f"失败测试: {summary['failed_tests']}")
            logger.info(f"成功率: {summary['success_rate']}%")
            logger.info(f"报告文件: {report_file}")
            logger.info("=" * 50)
            
            return summary['success_rate'] >= 80  # 80%以上认为验证通过
            
        except Exception as e:
            logger.error(f"验证过程中发生错误: {e}")
            return False

def main():
    """主函数"""
    print("SGH基金问题验证系统 - DDL更新自动化验证")
    print("=" * 50)
    
    verifier = DDLVerificationTool()
    success = verifier.run_verification()
    
    if success:
        print("✅ DDL验证通过")
        sys.exit(0)
    else:
        print("❌ DDL验证失败")
        sys.exit(1)

if __name__ == "__main__":
    main()
