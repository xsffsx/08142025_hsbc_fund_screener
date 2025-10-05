#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
抽取Markdown表格中的前三列：No.、问题、AI回答
从SGH文件中提取Q&A数据

使用方法:
python extract_qa_columns.py
"""

import re
import os
from datetime import datetime


def extract_table_data(file_path):
    """
    从Markdown文件中抽取表格的前三列数据
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 查找表格部分
        table_start = content.find('## 数据表格')
        if table_start == -1:
            print("未找到数据表格部分")
            return []
        
        # 提取表格内容
        table_section = content[table_start:]
        lines = table_section.split('\n')
        
        # 找到表格行
        table_data = []
        header_found = False
        
        for line in lines:
            line = line.strip()
            if not line or not line.startswith('|'):
                continue
            
            # 跳过分隔符行
            if '---' in line:
                continue
            
            # 分割表格行
            cells = [cell.strip() for cell in line.split('|')]
            
            # 移除首尾空元素
            if cells and cells[0] == '':
                cells = cells[1:]
            if cells and cells[-1] == '':
                cells = cells[:-1]
            
            if len(cells) >= 3:
                # 只取前三列
                row_data = {
                    'no': cells[0],
                    'question': cells[1],
                    'answer': cells[2]
                }
                table_data.append(row_data)
        
        return table_data
        
    except Exception as e:
        print(f"读取文件失败: {str(e)}")
        return []


def create_qa_markdown(data, output_file):
    """
    创建Q&A格式的Markdown文件
    """
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    
    markdown_content = f"""# SGH Fund Questions 问答抽取

**生成时间**: {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}
**源文件**: 20250806003428_05_Technical_Fund Questions v2_SGH.md
**抽取列**: No. | 问题 | AI回答

---

## 问答列表

"""
    
    for i, item in enumerate(data, 1):
        # 跳过表头
        if item['no'].lower() == 'no.' or item['no'].lower() == 'no':
            continue
        
        # 清理内容
        no = item['no'].strip()
        question = item['question'].strip()
        answer = item['answer'].strip()
        
        # 如果编号为空，跳过
        if not no or no == '':
            continue
        
        markdown_content += f"""### Q{no}: {question}

**回答**: {answer}

---

"""
    
    # 添加统计信息
    valid_count = len([item for item in data if item['no'].strip() and item['no'].lower() not in ['no.', 'no']])
    markdown_content += f"""
## 统计信息

- **总问题数**: {valid_count}
- **抽取时间**: {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}
- **文件格式**: Markdown Q&A格式

---
*由问答抽取工具生成*
"""
    
    # 写入文件
    try:
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(markdown_content)
        return True
    except Exception as e:
        print(f"写入文件失败: {str(e)}")
        return False


def main():
    """主函数"""
    input_file = "20250806003428_05_Technical_Fund Questions v2_SGH.md"
    
    if not os.path.exists(input_file):
        print(f"文件不存在: {input_file}")
        return
    
    print(f"开始抽取文件: {input_file}")
    
    # 抽取数据
    table_data = extract_table_data(input_file)
    
    if not table_data:
        print("未找到表格数据")
        return
    
    print(f"找到 {len(table_data)} 行数据")
    
    # 生成输出文件名
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    output_file = f"{timestamp}_01_Business_SGH_Fund_QA_Extract.md"
    
    # 创建Q&A文件
    if create_qa_markdown(table_data, output_file):
        print(f"✓ 成功生成: {output_file}")
        
        # 显示前几个问题预览
        print("\n前3个问题预览:")
        count = 0
        for item in table_data:
            if item['no'].strip() and item['no'].lower() not in ['no.', 'no'] and count < 3:
                print(f"Q{item['no']}: {item['question'][:50]}...")
                count += 1
    else:
        print("✗ 生成文件失败")


if __name__ == "__main__":
    main()
