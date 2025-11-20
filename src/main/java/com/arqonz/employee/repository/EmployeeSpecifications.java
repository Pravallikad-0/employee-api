package com.arqonz.employee.repository;

import com.arqonz.employee.model.Employee;
import org.springframework.data.jpa.domain.Specification;

public class EmployeeSpecifications {
    
    public static Specification<Employee> hasEmail(String email) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("email"), email);
    }
    
    public static Specification<Employee> hasName(String name) {
        return (root, query, criteriaBuilder) -> 
            criteriaBuilder.equal(root.get("firstName"), name);
    }
}

