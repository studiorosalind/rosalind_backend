Rosalind Project
A Spring Boot application with multi-module architecture.

Project Structure
The project is divided into two main modules:

rosalind-common: Contains domain entities, repositories, and services
rosalind-api: Contains REST controllers, security config, and business logic
Technologies
Java 11
Spring Boot 2.6.7
Spring Security with JWT
Spring Data JPA
PostgreSQL
Gradle
Getting Started
Prerequisites
JDK 11
PostgreSQL
Gradle
Database Setup
Create a PostgreSQL database named rosalind
Update database connection properties in application.properties if needed
Running the Application
bash
./gradlew bootRun
The application will start on http://localhost:8080

API Endpoints
Authentication
POST /api/auth/signup: Register a new user
POST /api/auth/signin: Authenticate and get JWT token
Test Endpoints
GET /api/test/all: Public access
GET /api/test/user: For users with role USER
GET /api/test/mod: For users with role MODERATOR
GET /api/test/admin: For users with role ADMIN
Security
The application uses JWT (JSON Web Token) for authentication. When a user successfully logs in, a JWT is generated and returned to the client. This token must be included in the Authorization header of subsequent requests.

Development
Adding a New Module
Create a new directory for your module
Add the module to settings.gradle
Configure dependencies in the module's build.gradle
Update the main application to scan components from the new module
Building the Project
bash
./gradlew clean build
Running Tests
bash
./gradlew test
