# Employee Management REST API

A RESTful API built with Spring Boot for managing employee information. This API supports CRUD operations and implements search functionality using three different approaches: JPA Specifications, HQL (Hibernate Query Language), and Native SQL queries.

## Features

- Full CRUD operations for employee records
- Multiple search implementations (JPA Specifications, HQL, Native SQL)
- Input validation for required fields
- Comprehensive error handling with meaningful HTTP status codes
- Unit and integration tests using Mockito and JUnit
- H2 in-memory database for development and testing

## Technology Stack

- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (in-memory)
- **Lombok** (for reducing boilerplate code)
- **JUnit 5** and **Mockito** (for testing)
- **Java 17**

## API Endpoints

### 1. Fetch Employee Details by Email

#### Using JPA Specifications
```
GET /api/employees/email/{email}/specifications
```

#### Using HQL
```
GET /api/employees/email/{email}/hql
```

#### Using Native SQL
```
GET /api/employees/email/{email}/native
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/employees/email/john.doe@example.com/hql
```

**Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "phone": "1234567890",
  "address": "123 Main St"
}
```

### 2. Fetch Employee Details by Name

#### Using JPA Specifications
```
GET /api/employees/name/{name}/specifications
```

#### Using HQL
```
GET /api/employees/name/{name}/hql
```

#### Using Native SQL
```
GET /api/employees/name/{name}/native
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/employees/name/John/hql
```

### 3. Create Employee (Name and Email) - Required

```
POST /api/employees
Content-Type: application/json

{
  "name": "Jane",
  "email": "jane@example.com"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"Jane","email":"jane@example.com"}'
```

**Response:** `201 Created`

### 4. Create Employee (Name, Email, and Phone) - Required

```
POST /api/employees
Content-Type: application/json

{
  "name": "Bob",
  "email": "bob@example.com",
  "phone": "9876543210"
}
```

**Example:**
```bash
curl -X POST http://localhost:8080/api/employees \
  -H "Content-Type: application/json" \
  -d '{"name":"Bob","email":"bob@example.com","phone":"9876543210"}'
```

**Response:** `201 Created`

### 5. Update Employee Details (Last Name, Phone, and Address)

```
PUT /api/employees/{email}
Content-Type: application/json

{
  "lastName": "Smith",
  "phone": "9999999999",
  "address": "456 New St"
}
```

**Example:**
```bash
curl -X PUT http://localhost:8080/api/employees/john.doe@example.com \
  -H "Content-Type: application/json" \
  -d '{"lastName":"Smith","phone":"9999999999","address":"456 New St"}'
```

**Response:** `200 OK`

### 6. Update Employee Phone Only

```
PATCH /api/employees/{email}/phone
Content-Type: application/json

{
  "phone": "1111111111"
}
```

**Example:**
```bash
curl -X PATCH http://localhost:8080/api/employees/john.doe@example.com/phone \
  -H "Content-Type: application/json" \
  -d '{"phone":"1111111111"}'
```

**Response:** `200 OK`

### 7. Delete Employee by Email

```
DELETE /api/employees/{email}
```

**Example:**
```bash
curl -X DELETE http://localhost:8080/api/employees/john.doe@example.com
```

**Response:** `204 No Content`

### Bonus: Get All Employees

```
GET /api/employees
```

**Example:**
```bash
curl -X GET http://localhost:8080/api/employees
```

## Error Responses

The API returns meaningful error messages with appropriate HTTP status codes:

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Employee with email already exists",
  "path": "/api/employees"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Employee not found with email: notfound@example.com",
  "path": "/api/employees/email/notfound@example.com/hql"
}
```

### Validation Errors (400 Bad Request)
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "errors": {
    "name": "Name is required",
    "email": "Email is required"
  },
  "path": "/api/employees"
}
```

## Running the Application

### Prerequisites
- Java 17 or higher
- Maven 3.6+

### Build and Run
```bash
cd backend-spring
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Access H2 Console
The H2 database console is available at:
```
http://localhost:8080/h2-console
```

**Connection Details:**
- JDBC URL: `jdbc:h2:mem:employeedb`
- Username: `sa`
- Password: (empty)

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Unit Tests Only
```bash
mvn test -Dtest=EmployeeServiceTest,EmployeeControllerTest
```

### Run Integration Tests Only
```bash
mvn test -Dtest=EmployeeIntegrationTest
```

## Project Structure

```
backend-spring/
├── src/
│   ├── main/
│   │   ├── java/com/arqonz/employee/
│   │   │   ├── controller/          # REST controllers
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Exception handlers
│   │   │   ├── model/               # Entity models
│   │   │   ├── repository/          # Data access layer
│   │   │   └── service/             # Business logic
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── java/com/arqonz/employee/
│       │   ├── controller/          # Controller unit tests
│       │   ├── integration/         # Integration tests
│       │   └── service/             # Service unit tests
│       └── resources/
│           └── application-test.properties
└── pom.xml
```

## Search Implementation Approaches

The API implements three different approaches for searching employees:

### 1. JPA Specifications
- Dynamic query building using Spring Data JPA Specifications
- Type-safe and flexible
- Endpoints: `/specifications`

### 2. HQL (Hibernate Query Language)
- Object-oriented query language
- Database-agnostic
- Endpoints: `/hql`

### 3. Native SQL Queries
- Raw SQL queries for direct database interaction
- Database-specific optimizations possible
- Endpoints: `/native`

## Validation Rules

- **Name**: Required, cannot be blank
- **Email**: Required, must be a valid email format, must be unique
- **Phone**: Optional
- **Last Name**: Optional
- **Address**: Optional

## Testing

The project includes comprehensive test coverage:

- **Unit Tests**: Test individual components in isolation using Mockito
- **Integration Tests**: Test the full application stack with real database interactions

Test files:
- `EmployeeServiceTest.java` - Service layer unit tests
- `EmployeeControllerTest.java` - Controller layer unit tests
- `EmployeeIntegrationTest.java` - Full integration tests

## License

This project is part of an assessment/assignment.

