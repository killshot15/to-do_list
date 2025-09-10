# Task Management Application

A modern, secure task management web application built with Spring Boot, featuring user authentication and comprehensive task CRUD operations.

## Overview

This application allows users to register, sign in, and manage their personal tasks through a clean, responsive web interface. It demonstrates best practices in Spring Boot development, including layered architecture, security integration, and database management.

## Key Features

- **User Authentication**: Secure registration and login with encrypted passwords
- **Task Management**: Full create, read, update, delete operations for tasks
- **Responsive UI**: Mobile-friendly design with Bootstrap styling
- **Security**: Spring Security integration with session management
- **Database**: SQLite with JPA/Hibernate for data persistence
- **REST API**: JSON endpoints for programmatic access

## Technology Stack

- **Backend**: Java 17, Spring Boot 3.x
- **Security**: Spring Security with BCrypt encryption
- **Database**: SQLite with Spring Data JPA
- **Frontend**: Thymeleaf templates, Bootstrap 5
- **Build**: Maven
- **Architecture**: MVC pattern with service layer

## Project Structure

```
src/
├── main/
│   ├── java/com/taskwebsite/
│   │   ├── controller/     # Web controllers
│   │   ├── model/          # JPA entities
│   │   ├── repository/     # Data access layer
│   │   ├── service/        # Business logic
│   │   ├── config/         # Configuration classes
│   │   └── TaskWebsiteApplication.java
│   └── resources/
│       ├── templates/      # Thymeleaf views
│       ├── sql/            # Database initialization
│       └── application.properties
```

## Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+

### Running the Application

1. **Clone and navigate**:
   ```bash
   git clone <repository-url>
   cd task-management-app
   ```

2. **Build the project**:
   ```bash
   mvn clean compile
   ```

3. **Run the application**:
   ```bash
   mvn spring-boot:run
   ```

4. **Access the app**:
   - Open http://localhost:8080 in your browser
   - Register a new account or use test credentials

### Test Credentials
- Email: test@example.com
- Password: Test1234

## API Documentation

### Authentication Endpoints
- `GET /sign-in` - Login page
- `POST /login` - Process login
- `GET /sign-up` - Registration page
- `POST /sign-up` - Process registration
- `POST /logout` - Logout

### Task Endpoints
- `GET /tasks` - View tasks (authenticated)
- `POST /tasks` - Create task
- `GET /api/tasks` - Get tasks (JSON)
- `POST /api/tasks` - Create task (JSON)
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task

## Database Schema

### Users
```sql
CREATE TABLE users (
    id INTEGER PRIMARY KEY,
    username TEXT UNIQUE,
    password TEXT,
    full_name TEXT,
    email TEXT UNIQUE
);
```

### Tasks
```sql
CREATE TABLE tasks (
    id INTEGER PRIMARY KEY,
    title TEXT,
    user_id INTEGER,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## Security Implementation

- Password encryption using BCrypt
- Session-based authentication
- CSRF protection on forms
- User data isolation
- Secure logout handling

## Development Notes

- Database is auto-initialized on startup
- Test user is created automatically
- Logging configured for debugging
- Hot reload available for templates

## Contributing

1. Follow Java coding standards
2. Add tests for new features
3. Update documentation as needed
4. Ensure security best practices

## Contributors

- Jobin Jose - 2nd Year B.Tech CS Engineering student(3rd semester, A2CSE) at St. Thomas Institute of Science & Technology, Trivandrum, Kerala, India
- Niranjan Binu - 2nd Year B.Tech CS Engineering student(3rd semester, A2CSE) at St. Thomas Institute of Science & Technology, Trivandrum, Kerala, India
- Praful Suji - 2nd Year B.Tech CS Engineering student(3rd semester, A2CSE) at St. Thomas Institute of Science & Technology, Trivandrum, Kerala, India
- Sakkeer Hussain - 2nd Year B.Tech CS Engineering student(3rd semester, A2CSE) at St. Thomas Institute of Science & Technology, Trivandrum, Kerala, India
- Shibin Eugin - 2nd Year B.Tech CS Engineering student(3rd semester, A2CSE) at St. Thomas Institute of Science & Technology, Trivandrum, Kerala, India

## Licenses

### Project License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

### Dependencies
- **Spring Boot**: Apache License 2.0
- **Spring Security**: Apache License 2.0
- **Thymeleaf**: Apache License 2.0
- **SQLite JDBC**: Apache License 2.0
- **Hibernate ORM**: GNU Lesser General Public License (LGPL) 2.1 or later
- **Bootstrap** (if used in templates): MIT License

For full license texts, refer to the respective project repositories or Maven dependency information.