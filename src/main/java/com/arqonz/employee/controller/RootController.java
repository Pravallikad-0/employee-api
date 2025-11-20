package com.arqonz.employee.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RootController {

    @GetMapping("/")
    public ResponseEntity<Map<String, Object>> getApiInfo() {
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("message", "Employee Management REST API");
        apiInfo.put("version", "1.0.0");
        apiInfo.put("status", "Running");
        apiInfo.put("baseUrl", "/api/employees");
        apiInfo.put("endpoints", Map.of(
                "getAllEmployees", "GET /api/employees",
                "getEmployeeByEmail", "GET /api/employees/email/{email}/{specifications|hql|native}",
                "getEmployeeByName", "GET /api/employees/name/{name}/{specifications|hql|native}",
                "createEmployee", "POST /api/employees",
                "updateEmployee", "PUT /api/employees/{email}",
                "updatePhone", "PATCH /api/employees/{email}/phone",
                "deleteEmployee", "DELETE /api/employees/{email}"
        ));
        apiInfo.put("h2Console", "http://localhost:8080/h2-console");

        return ResponseEntity.ok(apiInfo);
    }
}

print("hello");