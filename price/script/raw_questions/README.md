# Excel转Markdown工具

这个Python脚本可以将Excel文件的所有工作表转换为Markdown格式的文档。

## 功能特性

- ✅ 支持读取Excel文件的所有工作表
- ✅ 自动生成规范的Markdown文档
- ✅ 包含数据概览、表格和统计信息
- ✅ 遵循项目文档命名规范
- ✅ 处理空数据和异常情况
- ✅ 支持大数据量（自动截断显示）

## 安装依赖

```bash
pip install -r requirements.txt
```

或者单独安装：

```bash
pip install pandas openpyxl tabulate
```

## 使用方法

### 基本用法

```bash
python excel_to_markdown.py "Fund Questions v2.xlsx"
```

### 指定输出目录

```bash
python excel_to_markdown.py "Fund Questions v2.xlsx" -o ./output
```

### 帮助信息

```bash
python excel_to_markdown.py -h
```

## 输出文件格式

生成的Markdown文件遵循项目命名规范：

```
yyyymmddhhmiss_编号_Technical_文件名_工作表名.md
```

例如：
- `20250105143025_01_Technical_Fund_Questions_v2_Sheet1.md`
- `20250105143025_02_Technical_Fund_Questions_v2_Data.md`

## 文档结构

每个生成的Markdown文档包含：

1. **文档头部** - 创建时间、源文件信息
2. **目录** - 文档结构导航
3. **数据概览** - 行数、列数、列信息
4. **数据表格** - 完整的数据表格（Markdown格式）
5. **数据统计** - 数值列统计、文本列信息

## 示例输出

```markdown
# Sheet1

**文档创建时间**: 2025-01-05 14:30:25  
**源文件**: Fund Questions v2.xlsx  
**工作表**: 1/3

## 目录
- [数据概览](#数据概览)
- [数据表格](#数据表格)
- [数据统计](#数据统计)

## 数据概览

- **行数**: 150
- **列数**: 8
- **工作表名称**: Sheet1

### 列信息
1. **问题ID** - 类型: `int64`, 非空值: 150
2. **问题内容** - 类型: `object`, 非空值: 148
...
```

## 注意事项

- 支持的Excel格式：`.xlsx`, `.xls`
- 大数据量文件会自动截断显示（默认1000行）
- 自动处理空值和特殊字符
- 生成的文件使用UTF-8编码

## 错误处理

脚本包含完善的错误处理机制：
- 文件不存在检查
- Excel格式验证
- 工作表读取异常处理
- 文件写入权限检查
