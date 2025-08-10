package com.example.nl2sql.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 员工实体类 - 支持pgvector向量字段
 * 
 * @author AI Assistant
 * @version 1.0
 */
@Entity
@Table(name = "employees", indexes = {
    @Index(name = "employees_skills_vector_idx", columnList = "skills_vector")
})
public class Employee {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "email", unique = true, length = 150)
    private String email;
    
    @Column(name = "department", length = 50)
    private String department;
    
    @Column(name = "salary", precision = 10, scale = 2)
    private BigDecimal salary;
    
    @Column(name = "hire_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate hireDate = LocalDate.now();
    
    /**
     * 技能向量字段 (384维)
     * 使用pgvector扩展存储向量数据
     */
    @Column(name = "skills_vector", columnDefinition = "vector(384)")
    @Type(type = "com.example.nl2sql.type.VectorType")
    private float[] skillsVector;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    // 默认构造函数
    public Employee() {}
    
    // 带参构造函数
    public Employee(String name, String email, String department, BigDecimal salary) {
        this.name = name;
        this.email = email;
        this.department = department;
        this.salary = salary;
    }
    
    // Getter和Setter方法
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getDepartment() {
        return department;
    }
    
    public void setDepartment(String department) {
        this.department = department;
    }
    
    public BigDecimal getSalary() {
        return salary;
    }
    
    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }
    
    public LocalDate getHireDate() {
        return hireDate;
    }
    
    public void setHireDate(LocalDate hireDate) {
        this.hireDate = hireDate;
    }
    
    public float[] getSkillsVector() {
        return skillsVector;
    }
    
    public void setSkillsVector(float[] skillsVector) {
        this.skillsVector = skillsVector;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", department='" + department + '\'' +
                ", salary=" + salary +
                ", hireDate=" + hireDate +
                ", skillsVector=" + (skillsVector != null ? "vector[" + skillsVector.length + "]" : "null") +
                ", createdAt=" + createdAt +
                '}';
    }
}
