-- Oracle数据库功能验证脚本
-- 测试基本功能和表创建

-- 显示当前用户和数据库信息
SELECT USER as current_user FROM dual;
SELECT SYS_CONTEXT('USERENV', 'DB_NAME') as database_name FROM dual;

-- 创建测试表
CREATE TABLE test_employees (
    id NUMBER(10) PRIMARY KEY,
    name VARCHAR2(50) NOT NULL,
    email VARCHAR2(100) UNIQUE,
    salary NUMBER(10,2),
    hire_date DATE DEFAULT SYSDATE,
    department VARCHAR2(30)
);

-- 插入测试数据
INSERT INTO test_employees (id, name, email, salary, department) 
VALUES (1, 'John Doe', 'john.doe@company.com', 50000.00, 'IT');

INSERT INTO test_employees (id, name, email, salary, department) 
VALUES (2, 'Jane Smith', 'jane.smith@company.com', 55000.00, 'HR');

INSERT INTO test_employees (id, name, email, salary, department) 
VALUES (3, 'Bob Johnson', 'bob.johnson@company.com', 60000.00, 'Finance');

-- 提交事务
COMMIT;

-- 查询测试数据
SELECT 'Test data inserted successfully' as status FROM dual;
SELECT * FROM test_employees ORDER BY id;

-- 测试聚合函数
SELECT 
    department,
    COUNT(*) as employee_count,
    AVG(salary) as avg_salary,
    MAX(salary) as max_salary,
    MIN(salary) as min_salary
FROM test_employees 
GROUP BY department
ORDER BY department;

-- 显示表结构
DESC test_employees;

-- 清理测试数据（可选）
-- DROP TABLE test_employees;

SELECT 'Oracle database verification completed successfully!' as final_status FROM dual;
