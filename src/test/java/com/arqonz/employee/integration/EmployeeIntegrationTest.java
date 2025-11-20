package com.arqonz.employee.integration;

import com.arqonz.employee.dto.CreateEmployeeRequest;
import com.arqonz.employee.dto.UpdateEmployeeRequest;
import com.arqonz.employee.dto.UpdatePhoneRequest;
import com.arqonz.employee.model.Employee;
import com.arqonz.employee.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class EmployeeIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    private Employee testEmployee;
    
    @BeforeEach
    void setUp() {
        employeeRepository.deleteAll();
        
        testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setEmail("john.doe@example.com");
        testEmployee.setPhone("1234567890");
        testEmployee.setAddress("123 Main St");
        testEmployee = employeeRepository.save(testEmployee);
    }
    
    @Test
    void testGetEmployeeByEmailUsingSpecifications_Integration() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/employees/email/{email}/specifications", testEmployee.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()))
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()));
    }
    
    @Test
    void testGetEmployeeByEmailUsingHQL_Integration() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/employees/email/{email}/hql", testEmployee.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()))
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()));
    }
    
    @Test
    void testGetEmployeeByEmailUsingNativeSQL_Integration() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/employees/email/{email}/native", testEmployee.getEmail()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()))
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()));
    }
    
    @Test
    void testGetEmployeeByNameUsingSpecifications_Integration() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/employees/name/{name}/specifications", testEmployee.getFirstName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()))
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()));
    }
    
    @Test
    void testGetEmployeeByNameUsingHQL_Integration() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/employees/name/{name}/hql", testEmployee.getFirstName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()))
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()));
    }
    
    @Test
    void testGetEmployeeByNameUsingNativeSQL_Integration() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/employees/name/{name}/native", testEmployee.getFirstName()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(testEmployee.getFirstName()))
                .andExpect(jsonPath("$.email").value(testEmployee.getEmail()));
    }
    
    @Test
    void testCreateEmployee_WithNameAndEmail_Integration() throws Exception {
        // Given
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName("Jane");
        request.setEmail("jane@example.com");
        
        // When & Then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"));
        
        // Verify employee was saved in database
        assertTrue(employeeRepository.existsByEmail("jane@example.com"));
    }
    
    @Test
    void testCreateEmployee_WithNameEmailAndPhone_Integration() throws Exception {
        // Given
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName("Bob");
        request.setEmail("bob@example.com");
        request.setPhone("9876543210");
        
        // When & Then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("bob@example.com"))
                .andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.phone").value("9876543210"));
        
        // Verify employee was saved in database
        assertTrue(employeeRepository.existsByEmail("bob@example.com"));
    }
    
    @Test
    void testCreateEmployee_DuplicateEmail_Integration() throws Exception {
        // Given
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName("Another John");
        request.setEmail(testEmployee.getEmail()); // Duplicate email
        
        // When & Then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testCreateEmployee_ValidationError_Integration() throws Exception {
        // Given - Missing required fields
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName(""); // Empty name
        
        // When & Then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    void testUpdateEmployee_Integration() throws Exception {
        // Given
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setLastName("Smith");
        request.setPhone("9999999999");
        request.setAddress("456 New St");
        
        // When & Then
        mockMvc.perform(put("/api/employees/{email}", testEmployee.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"))
                .andExpect(jsonPath("$.phone").value("9999999999"))
                .andExpect(jsonPath("$.address").value("456 New St"));
        
        // Verify update in database
        Employee updated = employeeRepository.findByEmailUsingHQL(testEmployee.getEmail()).orElseThrow();
        assertEquals("Smith", updated.getLastName());
        assertEquals("9999999999", updated.getPhone());
        assertEquals("456 New St", updated.getAddress());
    }
    
    @Test
    void testUpdateEmployeePhone_Integration() throws Exception {
        // Given
        UpdatePhoneRequest request = new UpdatePhoneRequest();
        request.setPhone("1111111111");
        
        // When & Then
        mockMvc.perform(patch("/api/employees/{email}/phone", testEmployee.getEmail())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("1111111111"));
        
        // Verify update in database
        Employee updated = employeeRepository.findByEmailUsingHQL(testEmployee.getEmail()).orElseThrow();
        assertEquals("1111111111", updated.getPhone());
    }
    
    @Test
    void testUpdateEmployee_NotFound_Integration() throws Exception {
        // Given
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setLastName("Smith");
        
        // When & Then
        mockMvc.perform(put("/api/employees/{email}", "notfound@example.com")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testDeleteEmployeeByEmail_Integration() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/employees/{email}", testEmployee.getEmail()))
                .andExpect(status().isNoContent());
        
        // Verify deletion from database
        assertFalse(employeeRepository.existsByEmail(testEmployee.getEmail()));
    }
    
    @Test
    void testDeleteEmployeeByEmail_NotFound_Integration() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/employees/{email}", "notfound@example.com"))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void testGetEmployeeByEmail_NotFound_Integration() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/employees/email/{email}/hql", "notfound@example.com"))
                .andExpect(status().isNotFound());
    }
}

