package com.arqonz.employee.service;

import com.arqonz.employee.dto.CreateEmployeeRequest;
import com.arqonz.employee.dto.EmployeeResponse;
import com.arqonz.employee.dto.UpdateEmployeeRequest;
import com.arqonz.employee.dto.UpdatePhoneRequest;
import com.arqonz.employee.model.Employee;
import com.arqonz.employee.repository.EmployeeRepository;
import com.arqonz.employee.repository.EmployeeSpecifications;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    // Fetch Employee by Email - Using JPA Specifications
    public EmployeeResponse getEmployeeByEmailUsingSpecifications(String email) {
        Specification<Employee> spec = EmployeeSpecifications.hasEmail(email);
        Optional<Employee> employee = employeeRepository.findOne(spec);
        return employee.map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
    }
    
    // Fetch Employee by Email - Using HQL
    public EmployeeResponse getEmployeeByEmailUsingHQL(String email) {
        Employee employee = employeeRepository.findByEmailUsingHQL(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
        return mapToResponse(employee);
    }
    
    // Fetch Employee by Email - Using Native SQL
    public EmployeeResponse getEmployeeByEmailUsingNativeSQL(String email) {
        Employee employee = employeeRepository.findByEmailUsingNativeSQL(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
        return mapToResponse(employee);
    }
    
    // Fetch Employee by Name - Using JPA Specifications
    public EmployeeResponse getEmployeeByNameUsingSpecifications(String name) {
        Specification<Employee> spec = EmployeeSpecifications.hasName(name);
        Optional<Employee> employee = employeeRepository.findOne(spec);
        return employee.map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with name: " + name));
    }
    
    // Fetch Employee by Name - Using HQL
    public EmployeeResponse getEmployeeByNameUsingHQL(String name) {
        Employee employee = employeeRepository.findByNameUsingHQL(name)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with name: " + name));
        return mapToResponse(employee);
    }
    
    // Fetch Employee by Name - Using Native SQL
    public EmployeeResponse getEmployeeByNameUsingNativeSQL(String name) {
        Employee employee = employeeRepository.findByNameUsingNativeSQL(name)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with name: " + name));
        return mapToResponse(employee);
    }
    
    // Create Employee (Name and Email)
    public EmployeeResponse createEmployee(CreateEmployeeRequest request) {
        if (employeeRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Employee with email " + request.getEmail() + " already exists");
        }
        
        Employee employee = new Employee();
        employee.setFirstName(request.getName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        
        Employee savedEmployee = employeeRepository.save(employee);
        return mapToResponse(savedEmployee);
    }
    
    // Update Employee Details (Last Name, Phone, and Address)
    public EmployeeResponse updateEmployee(String email, UpdateEmployeeRequest request) {
        Employee employee = employeeRepository.findByEmailUsingHQL(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
        
        if (request.getLastName() != null) {
            employee.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            employee.setPhone(request.getPhone());
        }
        if (request.getAddress() != null) {
            employee.setAddress(request.getAddress());
        }
        
        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToResponse(updatedEmployee);
    }
    
    // Update Employee Phone Only
    public EmployeeResponse updateEmployeePhone(String email, UpdatePhoneRequest request) {
        Employee employee = employeeRepository.findByEmailUsingHQL(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
        
        employee.setPhone(request.getPhone());
        Employee updatedEmployee = employeeRepository.save(employee);
        return mapToResponse(updatedEmployee);
    }
    
    // Delete Employee by Email
    public void deleteEmployeeByEmail(String email) {
        Employee employee = employeeRepository.findByEmailUsingHQL(email)
                .orElseThrow(() -> new EntityNotFoundException("Employee not found with email: " + email));
        employeeRepository.delete(employee);
    }
    
    // Get all employees
    public List<EmployeeResponse> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private EmployeeResponse mapToResponse(Employee employee) {
        return new EmployeeResponse(
                employee.getId(),
                employee.getFirstName(),
                employee.getLastName(),
                employee.getEmail(),
                employee.getPhone(),
                employee.getAddress()
        );
    }
}

