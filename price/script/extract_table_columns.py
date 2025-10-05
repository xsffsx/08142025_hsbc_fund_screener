#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
抽取Markdown表格中的指定列，保持表格格式
从SGH文件中提取前三列：No. | Frequently/commonly asked questions | Expected Response from AI model

使用方法:
python extract_table_columns.py
"""

import re
import os
from datetime import datetime


def extract_table_columns(file_path, columns_to_extract=3):
    """
    从Markdown文件中抽取表格的指定列数，保持表格格式
    """
    try:
        with open(file_path, 'r', encoding='utf-8') as f:
            content = f.read()
        
        # 查找表格部分
        table_start = content.find('## 数据表格')
        if table_start == -1:
            print("未找到数据表格部分")
            return None, None
        
        # 提取表格内容
        table_section = content[table_start:]
        lines = table_section.split('\n')
        
        # 找到表格行
        table_lines = []
        header_line = None
        separator_line = None
        
        for line in lines:
            line_stripped = line.strip()
            if not line_stripped or not line_stripped.startswith('|'):
                # 如果已经开始收集表格，遇到非表格行就停止
                if table_lines:
                    break
                continue
            
            # 分割表格行
            cells = [cell.strip() for cell in line_stripped.split('|')]
            
            # 移除首尾空元素
            if cells and cells[0] == '':
                cells = cells[1:]
            if cells and cells[-1] == '':
                cells = cells[:-1]
            
            # 只保留指定数量的列
            if len(cells) >= columns_to_extract:
                extracted_cells = cells[:columns_to_extract]
                
                # 检查是否是分隔符行
                if all('---' in cell or cell == '---' for cell in extracted_cells):
                    separator_line = "| " + " | ".join(extracted_cells) + " |"
                else:
                    # 重新构建表格行
                    new_line = "| " + " | ".join(extracted_cells) + " |"
                    
                    # 如果是第一行（表头），记录下来
                    if not header_line and not any('---' in cell for cell in extracted_cells):
                        header_line = new_line
                    
                    table_lines.append(new_line)
        
        return table_lines, separator_line
        
    except Exception as e:
        print(f"读取文件失败: {str(e)}")
        return None, None


def create_extracted_markdown(table_lines, separator_line, output_file, source_file):
    """
    创建抽取后的Markdown文件，保持表格格式
    """
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    
    markdown_content = f"""# SGH Fund Questions 表格抽取

**生成时间**: {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}
**源文件**: {os.path.basename(source_file)}
**抽取列**: No. | Frequently/commonly asked questions | Expected Response from AI model

---

## 数据概览

- **总行数**: {len(table_lines) - 1}  (不含表头)
- **列数**: 3
- **工作表名称**: SGH

### 列信息

1. **No.** - 类型: `编号`, 问题序号
2. **Frequently/commonly asked questions** - 类型: `文本`, 常见问题
3. **Expected Response from AI model** - 类型: `文本`, AI模型预期回答

## 数据表格

"""
    
    # 添加表格内容
    if table_lines:
        # 添加表头
        if table_lines:
            markdown_content += table_lines[0] + "\n"
        
        # 添加分隔符
        if separator_line:
            markdown_content += separator_line + "\n"
        else:
            # 如果没有找到分隔符，创建一个
            markdown_content += "| --- | --- | --- |\n"
        
        # 添加数据行（跳过表头）
        for line in table_lines[1:]:
            markdown_content += line + "\n"
    
    markdown_content += "\n"
    
    # 添加统计信息
    data_rows = len(table_lines) - 1 if table_lines else 0
    markdown_content += f"""
## 数据统计

### 基本统计
- **数据行数**: {data_rows}
- **有效问题数**: {data_rows}
- **抽取时间**: {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}

### 问题分类统计
*注：基于问题内容的简单分类*

---
*由表格抽取工具生成于 {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}*
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
    input_file = "raw_questions/20250806003428_05_Technical_Fund Questions v2_SGH.md"
    
    if not os.path.exists(input_file):
        print(f"文件不存在: {input_file}")
        return
    
    print(f"开始抽取文件: {input_file}")
    print("抽取前三列: No. | Frequently/commonly asked questions | Expected Response from AI model")
    
    # 抽取表格列
    table_lines, separator_line = extract_table_columns(input_file, columns_to_extract=3)
    
    if not table_lines:
        print("未找到表格数据")
        return
    
    print(f"找到 {len(table_lines)} 行数据（含表头）")
    
    # 生成输出文件名
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    output_file = f"{timestamp}_01_Technical_SGH_Fund_Questions_3Columns.md"
    
    # 创建抽取后的文件
    if create_extracted_markdown(table_lines, separator_line, output_file, input_file):
        print(f"✓ 成功生成: {output_file}")
        
        # 显示表格预览
        print("\n表格预览:")
        if table_lines:
            print(table_lines[0])  # 表头
            if separator_line:
                print(separator_line)
            else:
                print("| --- | --- | --- |")
            
            # 显示前3行数据
            for i, line in enumerate(table_lines[1:4], 1):
                print(line)
            
            if len(table_lines) > 4:
                print("...")
                print(f"(还有 {len(table_lines) - 4} 行数据)")
    else:
        print("✗ 生成文件失败")


if __name__ == "__main__":
    main()
