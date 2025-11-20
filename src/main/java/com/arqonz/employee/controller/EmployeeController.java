package com.arqonz.employee.controller;

import com.arqonz.employee.dto.CreateEmployeeRequest;
import com.arqonz.employee.dto.EmployeeResponse;
import com.arqonz.employee.dto.UpdateEmployeeRequest;
import com.arqonz.employee.dto.UpdatePhoneRequest;
import com.arqonz.employee.service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {
    
    private final EmployeeService employeeService;
    
    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    
    // 1. Fetch Employee Details by Email (using JPA Specifications)
    @GetMapping("/email/{email}/specifications")
    public ResponseEntity<EmployeeResponse> getEmployeeByEmailUsingSpecifications(@PathVariable String email) {
        EmployeeResponse response = employeeService.getEmployeeByEmailUsingSpecifications(email);
        return ResponseEntity.ok(response);
    }
    
    // 1. Fetch Employee Details by Email (using HQL)
    @GetMapping("/email/{email}/hql")
    public ResponseEntity<EmployeeResponse> getEmployeeByEmailUsingHQL(@PathVariable String email) {
        EmployeeResponse response = employeeService.getEmployeeByEmailUsingHQL(email);
        return ResponseEntity.ok(response);
    }
    
    // 1. Fetch Employee Details by Email (using Native SQL)
    @GetMapping("/email/{email}/native")
    public ResponseEntity<EmployeeResponse> getEmployeeByEmailUsingNativeSQL(@PathVariable String email) {
        EmployeeResponse response = employeeService.getEmployeeByEmailUsingNativeSQL(email);
        return ResponseEntity.ok(response);
    }
    
    // 2. Fetch Employee Details by Name (using JPA Specifications)
    @GetMapping("/name/{name}/specifications")
    public ResponseEntity<EmployeeResponse> getEmployeeByNameUsingSpecifications(@PathVariable String name) {
        EmployeeResponse response = employeeService.getEmployeeByNameUsingSpecifications(name);
        return ResponseEntity.ok(response);
    }
    
    // 2. Fetch Employee Details by Name (using HQL)
    @GetMapping("/name/{name}/hql")
    public ResponseEntity<EmployeeResponse> getEmployeeByNameUsingHQL(@PathVariable String name) {
        EmployeeResponse response = employeeService.getEmployeeByNameUsingHQL(name);
        return ResponseEntity.ok(response);
    }
    
    // 2. Fetch Employee Details by Name (using Native SQL)
    @GetMapping("/name/{name}/native")
    public ResponseEntity<EmployeeResponse> getEmployeeByNameUsingNativeSQL(@PathVariable String name) {
        EmployeeResponse response = employeeService.getEmployeeByNameUsingNativeSQL(name);
        return ResponseEntity.ok(response);
    }
    
    // 3. Create Employee (Name and Email) - Required
    @PostMapping
    public ResponseEntity<EmployeeResponse> createEmployee(@Valid @RequestBody CreateEmployeeRequest request) {
        EmployeeResponse response = employeeService.createEmployee(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    // 4. Create Employee (Name, Email, and Phone) - Required
    // This uses the same endpoint as #3, but phone is optional in the request
    
    // 5. Update Employee Details (Last Name, Phone, and Address)
    @PutMapping("/{email}")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable String email,
            @Valid @RequestBody UpdateEmployeeRequest request) {
        EmployeeResponse response = employeeService.updateEmployee(email, request);
        return ResponseEntity.ok(response);
    }
    
    // 6. Update Employee Phone Only
    @PatchMapping("/{email}/phone")
    public ResponseEntity<EmployeeResponse> updateEmployeePhone(
            @PathVariable String email,
            @Valid @RequestBody UpdatePhoneRequest request) {
        EmployeeResponse response = employeeService.updateEmployeePhone(email, request);
        return ResponseEntity.ok(response);
    }
    
    // 7. Delete Employee by Email
    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteEmployeeByEmail(@PathVariable String email) {
        employeeService.deleteEmployeeByEmail(email);
        return ResponseEntity.noContent().build();
    }
    
    // Bonus: Get all employees
    @GetMapping
    public ResponseEntity<List<EmployeeResponse>> getAllEmployees() {
        List<EmployeeResponse> employees = employeeService.getAllEmployees();
        return ResponseEntity.ok(employees);
    }
}

