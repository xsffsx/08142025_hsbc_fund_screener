# 数据脱敏报告

**处理时间**: 2025-08-06 00:15:15

## 脱敏映射表

| 原始信息 | 脱敏后 | 类型 |
|---------|--------|------|
| HSBC Malaysia | DummyBank Malaysia | 公司身份信息 |
| HSBC Singapore | DummyBank Singapore | 公司身份信息 |
| HSBC platform | DummyBank platform | 公司身份信息 |
| HSBC | DummyBank | 公司身份信息 |
| WPC | WPS | 内部系统名称 |
| WPS Next Gen Leaders | WPS NextGen System | 内部系统名称 |
| IshaniK | UserA | 员工信息 |
| Shawn | UserB | 员工信息 |
| @shawn | @UserB | 员工信息 |
| <IshaniK> | <UserA> | 员工信息 |
| Morning * | DataSource* | 数据源信息 |
| Morningstar | DataSource | 数据源信息 |


## 处理文件列表

共处理 7 个文件：

- 20250806000833_02_Technical_Fund Questions v2_INM.md
- 20250806000833_03_Technical_Fund Questions v2_Working Sheet.md
- 20250806000833_07_Technical_Fund Questions v2_Sheet1.md
- 20250806000833_01_Technical_Fund Questions v2_MYH.md
- 20250806000833_05_Technical_Fund Questions v2_SGH.md
- 20250806000833_06_Technical_Fund Questions v2_Common Questions - All markets.md
- 20250806000833_04_Technical_Fund Questions v2_Question Groupings.md


## 脱敏说明

1. **公司身份信息**: 将具体银行名称替换为通用的"DummyBank"
2. **内部系统名称**: 使用通用系统名称替换
3. **员工信息**: 使用UserA、UserB等匿名标识替换
4. **数据源信息**: 统一使用DataSource标识

## 备份信息

所有原始文件已备份为 `.backup` 后缀文件，如需恢复可使用备份文件。

---
*由数据脱敏工具生成于 2025-08-06 00:15:15*
