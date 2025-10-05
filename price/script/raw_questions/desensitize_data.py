#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
数据脱敏工具
将Markdown文件中的敏感信息替换为dummy词汇

使用方法:
python desensitize_data.py
"""

import os
import re
import glob
from datetime import datetime


def create_desensitization_mapping():
    """
    创建脱敏映射表
    """
    mapping = {
        # 1. 公司身份信息
        'HSBC Malaysia': 'DummyBank Malaysia',
        'HSBC Singapore': 'DummyBank Singapore', 
        'HSBC platform': 'DummyBank platform',
        'HSBC': 'DummyBank',
        
        # 2. 内部系统名称
        'WPC': 'WPS',  # Wealth Product System
        'WPS Next Gen Leaders': 'WPS NextGen System',
        
        # 3. 员工信息
        'IshaniK': 'UserA',
        'Shawn': 'UserB',
        '@shawn': '@UserB',
        '<IshaniK>': '<UserA>',
        
        # 4. 其他可能的敏感信息
        'Morning *': 'DataSource*',
        'Morningstar': 'DataSource',
    }
    
    return mapping


def desensitize_text(text, mapping):
    """
    对文本进行脱敏处理
    
    Args:
        text (str): 原始文本
        mapping (dict): 脱敏映射表
    
    Returns:
        str: 脱敏后的文本
    """
    desensitized_text = text
    
    # 按照长度排序，先替换长的词汇，避免部分替换问题
    sorted_items = sorted(mapping.items(), key=lambda x: len(x[0]), reverse=True)
    
    for original, replacement in sorted_items:
        # 使用正则表达式进行精确匹配，避免误替换
        pattern = re.escape(original)
        desensitized_text = re.sub(pattern, replacement, desensitized_text, flags=re.IGNORECASE)
    
    return desensitized_text


def desensitize_file(file_path, mapping, backup=True):
    """
    对单个文件进行脱敏处理
    
    Args:
        file_path (str): 文件路径
        mapping (dict): 脱敏映射表
        backup (bool): 是否创建备份
    
    Returns:
        bool: 处理是否成功
    """
    try:
        # 读取原文件
        with open(file_path, 'r', encoding='utf-8') as f:
            original_content = f.read()
        
        # 创建备份
        if backup:
            backup_path = file_path + '.backup'
            with open(backup_path, 'w', encoding='utf-8') as f:
                f.write(original_content)
            print(f"✓ 已创建备份: {os.path.basename(backup_path)}")
        
        # 脱敏处理
        desensitized_content = desensitize_text(original_content, mapping)
        
        # 检查是否有变化
        if original_content == desensitized_content:
            print(f"- 无需处理: {os.path.basename(file_path)}")
            return True
        
        # 写入脱敏后的内容
        with open(file_path, 'w', encoding='utf-8') as f:
            f.write(desensitized_content)
        
        print(f"✓ 已脱敏: {os.path.basename(file_path)}")
        return True
        
    except Exception as e:
        print(f"✗ 处理失败 {os.path.basename(file_path)}: {str(e)}")
        return False


def generate_desensitization_report(mapping, processed_files):
    """
    生成脱敏报告
    """
    timestamp = datetime.now().strftime("%Y%m%d%H%M%S")
    report_path = f"desensitization_report_{timestamp}.md"
    
    report_content = f"""# 数据脱敏报告

**处理时间**: {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}

## 脱敏映射表

| 原始信息 | 脱敏后 | 类型 |
|---------|--------|------|
"""
    
    # 按类别组织映射信息
    categories = {
        'DummyBank': '公司身份信息',
        'WPS': '内部系统名称', 
        'User': '员工信息',
        'DataSource': '数据源信息'
    }
    
    for original, replacement in mapping.items():
        category = '其他'
        for key, cat in categories.items():
            if key in replacement:
                category = cat
                break
        report_content += f"| {original} | {replacement} | {category} |\n"
    
    report_content += f"""

## 处理文件列表

共处理 {len(processed_files)} 个文件：

"""
    
    for file_path in processed_files:
        report_content += f"- {os.path.basename(file_path)}\n"
    
    report_content += f"""

## 脱敏说明

1. **公司身份信息**: 将具体银行名称替换为通用的"DummyBank"
2. **内部系统名称**: 使用通用系统名称替换
3. **员工信息**: 使用UserA、UserB等匿名标识替换
4. **数据源信息**: 统一使用DataSource标识

## 备份信息

所有原始文件已备份为 `.backup` 后缀文件，如需恢复可使用备份文件。

---
*由数据脱敏工具生成于 {datetime.now().strftime("%Y-%m-%d %H:%M:%S")}*
"""
    
    with open(report_path, 'w', encoding='utf-8') as f:
        f.write(report_content)
    
    return report_path


def main():
    """主函数"""
    print("开始数据脱敏处理...")
    
    # 创建脱敏映射表
    mapping = create_desensitization_mapping()
    
    # 查找所有需要处理的Markdown文件
    pattern = "20250806000833_*_Technical_Fund Questions v2_*.md"
    markdown_files = glob.glob(pattern)
    
    if not markdown_files:
        print("未找到需要处理的Markdown文件")
        return
    
    print(f"找到 {len(markdown_files)} 个文件需要处理")
    
    # 处理每个文件
    processed_files = []
    success_count = 0
    
    for file_path in markdown_files:
        if desensitize_file(file_path, mapping, backup=True):
            processed_files.append(file_path)
            success_count += 1
    
    # 生成报告
    report_path = generate_desensitization_report(mapping, processed_files)
    
    print(f"\n✓ 脱敏处理完成!")
    print(f"  - 成功处理: {success_count}/{len(markdown_files)} 个文件")
    print(f"  - 脱敏报告: {report_path}")
    print(f"  - 备份文件: *.backup")


if __name__ == "__main__":
    main()
