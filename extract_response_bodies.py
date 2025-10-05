#!/usr/bin/env python3
"""
提取所有基金数据中的 response_body 内容
从 data_20250813_212612 目录中的所有 *.response.json 文件提取 response_body
输出到 output 目录
"""

import json
import os
import glob
from pathlib import Path
import logging

# 设置日志
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)

def extract_response_body(json_file_path):
    """从 JSON 文件中提取 response_body"""
    try:
        with open(json_file_path, 'r', encoding='utf-8') as f:
            data = json.load(f)
        
        # 检查文件结构
        if 'responses' in data and isinstance(data['responses'], list) and len(data['responses']) > 0:
            response = data['responses'][0]
            if 'response_body' in response:
                return response['response_body']
        
        logger.warning(f"未找到 response_body: {json_file_path}")
        return None
        
    except Exception as e:
        logger.error(f"处理文件 {json_file_path} 时出错: {e}")
        return None

def main():
    # 设置路径
    data_dir = Path("data_20250813_212612")
    output_dir = Path("output")
    
    # 创建输出目录
    output_dir.mkdir(exist_ok=True)
    
    # 查找所有 .response.json 文件
    response_files = glob.glob(str(data_dir / "*" / "*.response.json"))
    
    logger.info(f"找到 {len(response_files)} 个 response.json 文件")
    
    # 统计信息
    success_count = 0
    error_count = 0
    
    for file_path in response_files:
        file_path = Path(file_path)
        
        # 提取基金代码和API名称
        fund_folder = file_path.parent.name  # 例如: 001_U43051.done
        api_name = file_path.stem.replace('.response', '')  # 例如: wmds_advanceChart
        
        # 提取 response_body
        response_body = extract_response_body(file_path)
        
        if response_body:
            # 创建输出文件名
            output_filename = f"{fund_folder}_{api_name}_response_body.json"
            output_path = output_dir / output_filename
            
            try:
                # 尝试解析 response_body 为 JSON（如果是字符串格式）
                if isinstance(response_body, str):
                    parsed_body = json.loads(response_body)
                else:
                    parsed_body = response_body
                
                # 保存到文件
                with open(output_path, 'w', encoding='utf-8') as f:
                    json.dump(parsed_body, f, ensure_ascii=False, indent=2)
                
                success_count += 1
                logger.info(f"成功提取: {output_filename}")
                
            except json.JSONDecodeError:
                # 如果不是有效的 JSON，直接保存为文本
                with open(output_path.with_suffix('.txt'), 'w', encoding='utf-8') as f:
                    f.write(response_body)
                
                success_count += 1
                logger.info(f"保存为文本: {output_filename}.txt")
                
            except Exception as e:
                logger.error(f"保存文件 {output_filename} 时出错: {e}")
                error_count += 1
        else:
            error_count += 1
    
    # 输出统计信息
    logger.info(f"提取完成!")
    logger.info(f"成功: {success_count} 个文件")
    logger.info(f"失败: {error_count} 个文件")
    logger.info(f"输出目录: {output_dir.absolute()}")

if __name__ == "__main__":
    main()
