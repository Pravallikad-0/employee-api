package com.arqonz.employee.repository;

import com.arqonz.employee.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    
    // HQL Query - Find by Email
    @Query("SELECT e FROM Employee e WHERE e.email = :email")
    Optional<Employee> findByEmailUsingHQL(@Param("email") String email);
    
    // HQL Query - Find by Name (first name)
    @Query("SELECT e FROM Employee e WHERE e.firstName = :name")
    Optional<Employee> findByNameUsingHQL(@Param("name") String name);
    
    // Native SQL Query - Find by Email
    @Query(value = "SELECT * FROM employees WHERE email = :email", nativeQuery = true)
    Optional<Employee> findByEmailUsingNativeSQL(@Param("email") String email);
    
    // Native SQL Query - Find by Name (first name)
    @Query(value = "SELECT * FROM employees WHERE first_name = :name", nativeQuery = true)
    Optional<Employee> findByNameUsingNativeSQL(@Param("name") String name);
    
    // Standard JPA method for checking existence
    boolean existsByEmail(String email);
}

