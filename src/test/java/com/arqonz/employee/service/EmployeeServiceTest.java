package com.arqonz.employee.service;

import com.arqonz.employee.dto.CreateEmployeeRequest;
import com.arqonz.employee.dto.EmployeeResponse;
import com.arqonz.employee.dto.UpdateEmployeeRequest;
import com.arqonz.employee.dto.UpdatePhoneRequest;
import com.arqonz.employee.model.Employee;
import com.arqonz.employee.repository.EmployeeRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.jpa.domain.Specification;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    
    @Mock
    private EmployeeRepository employeeRepository;
    
    @InjectMocks
    private EmployeeService employeeService;
    
    private Employee testEmployee;
    
    @BeforeEach
    void setUp() {
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("john.doe@example.com");
        testEmployee.setPhone("1234567890");
        testEmployee.setAddress("123 Main St");
    }
    
    @Test
    void testGetEmployeeByEmailUsingSpecifications_Success() {
        // Given
        String email = "john.doe@example.com";
        
        when(employeeRepository.findOne(any(Specification.class))).thenReturn(Optional.of(testEmployee));
        
        // When
        EmployeeResponse response = employeeService.getEmployeeByEmailUsingSpecifications(email);
        
        // Then
        assertNotNull(response);
        assertEquals(testEmployee.getEmail(), response.getEmail());
        assertEquals(testEmployee.getFirstName(), response.getFirstName());
        verify(employeeRepository, times(1)).findOne(any(Specification.class));
    }
    
    @Test
    void testGetEmployeeByEmailUsingSpecifications_NotFound() {
        // Given
        String email = "notfound@example.com";
        when(employeeRepository.findOne(any(Specification.class))).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            employeeService.getEmployeeByEmailUsingSpecifications(email);
        });
    }
    
    @Test
    void testGetEmployeeByEmailUsingHQL_Success() {
        // Given
        String email = "john.doe@example.com";
        when(employeeRepository.findByEmailUsingHQL(email)).thenReturn(Optional.of(testEmployee));
        
        // When
        EmployeeResponse response = employeeService.getEmployeeByEmailUsingHQL(email);
        
        // Then
        assertNotNull(response);
        assertEquals(testEmployee.getEmail(), response.getEmail());
        verify(employeeRepository, times(1)).findByEmailUsingHQL(email);
    }
    
    @Test
    void testGetEmployeeByEmailUsingNativeSQL_Success() {
        // Given
        String email = "john.doe@example.com";
        when(employeeRepository.findByEmailUsingNativeSQL(email)).thenReturn(Optional.of(testEmployee));
        
        // When
        EmployeeResponse response = employeeService.getEmployeeByEmailUsingNativeSQL(email);
        
        // Then
        assertNotNull(response);
        assertEquals(testEmployee.getEmail(), response.getEmail());
        verify(employeeRepository, times(1)).findByEmailUsingNativeSQL(email);
    }
    
    @Test
    void testGetEmployeeByNameUsingSpecifications_Success() {
        // Given
        String name = "John";
        when(employeeRepository.findOne(any(Specification.class))).thenReturn(Optional.of(testEmployee));
        
        // When
        EmployeeResponse response = employeeService.getEmployeeByNameUsingSpecifications(name);
        
        // Then
        assertNotNull(response);
        assertEquals(testEmployee.getFirstName(), response.getFirstName());
        verify(employeeRepository, times(1)).findOne(any(Specification.class));
    }
    
    @Test
    void testGetEmployeeByNameUsingHQL_Success() {
        // Given
        String name = "John";
        when(employeeRepository.findByNameUsingHQL(name)).thenReturn(Optional.of(testEmployee));
        
        // When
        EmployeeResponse response = employeeService.getEmployeeByNameUsingHQL(name);
        
        // Then
        assertNotNull(response);
        assertEquals(testEmployee.getFirstName(), response.getFirstName());
        verify(employeeRepository, times(1)).findByNameUsingHQL(name);
    }
    
    @Test
    void testGetEmployeeByNameUsingNativeSQL_Success() {
        // Given
        String name = "John";
        when(employeeRepository.findByNameUsingNativeSQL(name)).thenReturn(Optional.of(testEmployee));
        
        // When
        EmployeeResponse response = employeeService.getEmployeeByNameUsingNativeSQL(name);
        
        // Then
        assertNotNull(response);
        assertEquals(testEmployee.getFirstName(), response.getFirstName());
        verify(employeeRepository, times(1)).findByNameUsingNativeSQL(name);
    }
    
    @Test
    void testCreateEmployee_Success() {
        // Given
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName("Jane");
        request.setEmail("jane@example.com");
        request.setPhone("9876543210");
        
        Employee newEmployee = new Employee();
        newEmployee.setId(2L);
        newEmployee.setFirstName("Jane");
        newEmployee.setEmail("jane@example.com");
        newEmployee.setPhone("9876543210");
        
        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(newEmployee);
        
        // When
        EmployeeResponse response = employeeService.createEmployee(request);
        
        // Then
        assertNotNull(response);
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getName(), response.getFirstName());
        verify(employeeRepository, times(1)).existsByEmail(request.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
    
    @Test
    void testCreateEmployee_DuplicateEmail() {
        // Given
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName("Jane");
        request.setEmail("jane@example.com");
        
        when(employeeRepository.existsByEmail(request.getEmail())).thenReturn(true);
        
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> {
            employeeService.createEmployee(request);
        });
        
        verify(employeeRepository, times(1)).existsByEmail(request.getEmail());
        verify(employeeRepository, never()).save(any(Employee.class));
    }
    
    @Test
    void testUpdateEmployee_Success() {
        // Given
        String email = "john.doe@example.com";
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setLastName("Smith");
        request.setPhone("9999999999");
        request.setAddress("456 New St");
        
        when(employeeRepository.findByEmailUsingHQL(email)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);
        
        // When
        EmployeeResponse response = employeeService.updateEmployee(email, request);
        
        // Then
        assertNotNull(response);
        verify(employeeRepository, times(1)).findByEmailUsingHQL(email);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
    
    @Test
    void testUpdateEmployee_NotFound() {
        // Given
        String email = "notfound@example.com";
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        
        when(employeeRepository.findByEmailUsingHQL(email)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            employeeService.updateEmployee(email, request);
        });
    }
    
    @Test
    void testUpdateEmployeePhone_Success() {
        // Given
        String email = "john.doe@example.com";
        UpdatePhoneRequest request = new UpdatePhoneRequest();
        request.setPhone("1111111111");
        
        when(employeeRepository.findByEmailUsingHQL(email)).thenReturn(Optional.of(testEmployee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(testEmployee);
        
        // When
        EmployeeResponse response = employeeService.updateEmployeePhone(email, request);
        
        // Then
        assertNotNull(response);
        verify(employeeRepository, times(1)).findByEmailUsingHQL(email);
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }
    
    @Test
    void testDeleteEmployeeByEmail_Success() {
        // Given
        String email = "john.doe@example.com";
        when(employeeRepository.findByEmailUsingHQL(email)).thenReturn(Optional.of(testEmployee));
        doNothing().when(employeeRepository).delete(any(Employee.class));
        
        // When
        employeeService.deleteEmployeeByEmail(email);
        
        // Then
        verify(employeeRepository, times(1)).findByEmailUsingHQL(email);
        verify(employeeRepository, times(1)).delete(testEmployee);
    }
    
    @Test
    void testDeleteEmployeeByEmail_NotFound() {
        // Given
        String email = "notfound@example.com";
        when(employeeRepository.findByEmailUsingHQL(email)).thenReturn(Optional.empty());
        
        // When & Then
        assertThrows(EntityNotFoundException.class, () -> {
            employeeService.deleteEmployeeByEmail(email);
        });
        
        verify(employeeRepository, never()).delete(any(Employee.class));
    }
}

