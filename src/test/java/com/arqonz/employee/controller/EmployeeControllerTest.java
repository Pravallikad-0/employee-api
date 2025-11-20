package com.arqonz.employee.controller;

import com.arqonz.employee.dto.CreateEmployeeRequest;
import com.arqonz.employee.dto.EmployeeResponse;
import com.arqonz.employee.dto.UpdateEmployeeRequest;
import com.arqonz.employee.dto.UpdatePhoneRequest;
import com.arqonz.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private EmployeeService employeeService;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testGetEmployeeByEmailUsingSpecifications() throws Exception {
        // Given
        String email = "john.doe@example.com";
        EmployeeResponse response = new EmployeeResponse(1L, "John", "Doe", email, "1234567890", "123 Main St");
        
        when(employeeService.getEmployeeByEmailUsingSpecifications(email)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/employees/email/{email}/specifications", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value("John"));
        
        verify(employeeService, times(1)).getEmployeeByEmailUsingSpecifications(email);
    }
    
    @Test
    void testGetEmployeeByEmailUsingHQL() throws Exception {
        // Given
        String email = "john.doe@example.com";
        EmployeeResponse response = new EmployeeResponse(1L, "John", "Doe", email, "1234567890", "123 Main St");
        
        when(employeeService.getEmployeeByEmailUsingHQL(email)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/employees/email/{email}/hql", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
        
        verify(employeeService, times(1)).getEmployeeByEmailUsingHQL(email);
    }
    
    @Test
    void testGetEmployeeByEmailUsingNativeSQL() throws Exception {
        // Given
        String email = "john.doe@example.com";
        EmployeeResponse response = new EmployeeResponse(1L, "John", "Doe", email, "1234567890", "123 Main St");
        
        when(employeeService.getEmployeeByEmailUsingNativeSQL(email)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/employees/email/{email}/native", email))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email));
        
        verify(employeeService, times(1)).getEmployeeByEmailUsingNativeSQL(email);
    }
    
    @Test
    void testGetEmployeeByNameUsingSpecifications() throws Exception {
        // Given
        String name = "John";
        EmployeeResponse response = new EmployeeResponse(1L, name, "Doe", "john.doe@example.com", "1234567890", "123 Main St");
        
        when(employeeService.getEmployeeByNameUsingSpecifications(name)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/employees/name/{name}/specifications", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(name));
        
        verify(employeeService, times(1)).getEmployeeByNameUsingSpecifications(name);
    }
    
    @Test
    void testGetEmployeeByNameUsingHQL() throws Exception {
        // Given
        String name = "John";
        EmployeeResponse response = new EmployeeResponse(1L, name, "Doe", "john.doe@example.com", "1234567890", "123 Main St");
        
        when(employeeService.getEmployeeByNameUsingHQL(name)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/employees/name/{name}/hql", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(name));
        
        verify(employeeService, times(1)).getEmployeeByNameUsingHQL(name);
    }
    
    @Test
    void testGetEmployeeByNameUsingNativeSQL() throws Exception {
        // Given
        String name = "John";
        EmployeeResponse response = new EmployeeResponse(1L, name, "Doe", "john.doe@example.com", "1234567890", "123 Main St");
        
        when(employeeService.getEmployeeByNameUsingNativeSQL(name)).thenReturn(response);
        
        // When & Then
        mockMvc.perform(get("/api/employees/name/{name}/native", name))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(name));
        
        verify(employeeService, times(1)).getEmployeeByNameUsingNativeSQL(name);
    }
    
    @Test
    void testCreateEmployee_Success() throws Exception {
        // Given
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        request.setName("Jane");
        request.setEmail("jane@example.com");
        request.setPhone("9876543210");
        
        EmployeeResponse response = new EmployeeResponse(2L, "Jane", null, "jane@example.com", "9876543210", null);
        
        when(employeeService.createEmployee(any(CreateEmployeeRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("jane@example.com"))
                .andExpect(jsonPath("$.firstName").value("Jane"));
        
        verify(employeeService, times(1)).createEmployee(any(CreateEmployeeRequest.class));
    }
    
    @Test
    void testCreateEmployee_ValidationError() throws Exception {
        // Given
        CreateEmployeeRequest request = new CreateEmployeeRequest();
        // Missing required fields
        
        // When & Then
        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
        
        verify(employeeService, never()).createEmployee(any());
    }
    
    @Test
    void testUpdateEmployee_Success() throws Exception {
        // Given
        String email = "john.doe@example.com";
        UpdateEmployeeRequest request = new UpdateEmployeeRequest();
        request.setLastName("Smith");
        request.setPhone("9999999999");
        request.setAddress("456 New St");
        
        EmployeeResponse response = new EmployeeResponse(1L, "John", "Smith", email, "9999999999", "456 New St");
        
        when(employeeService.updateEmployee(eq(email), any(UpdateEmployeeRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(put("/api/employees/{email}", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Smith"));
        
        verify(employeeService, times(1)).updateEmployee(eq(email), any(UpdateEmployeeRequest.class));
    }
    
    @Test
    void testUpdateEmployeePhone_Success() throws Exception {
        // Given
        String email = "john.doe@example.com";
        UpdatePhoneRequest request = new UpdatePhoneRequest();
        request.setPhone("1111111111");
        
        EmployeeResponse response = new EmployeeResponse(1L, "John", "Doe", email, "1111111111", "123 Main St");
        
        when(employeeService.updateEmployeePhone(eq(email), any(UpdatePhoneRequest.class))).thenReturn(response);
        
        // When & Then
        mockMvc.perform(patch("/api/employees/{email}/phone", email)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("1111111111"));
        
        verify(employeeService, times(1)).updateEmployeePhone(eq(email), any(UpdatePhoneRequest.class));
    }
    
    @Test
    void testDeleteEmployeeByEmail_Success() throws Exception {
        // Given
        String email = "john.doe@example.com";
        doNothing().when(employeeService).deleteEmployeeByEmail(email);
        
        // When & Then
        mockMvc.perform(delete("/api/employees/{email}", email))
                .andExpect(status().isNoContent());
        
        verify(employeeService, times(1)).deleteEmployeeByEmail(email);
    }
}

