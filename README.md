# DynamoDB CRUD Example with AWS SDK v2

This project demonstrates how to create a CRUD (Create, Read, Update, Delete) API to interact with AWS DynamoDB using AWS SDK version 2 and Spring Boot.

## Prerequisites

- Java 21
- Maven
- AWS Account with DynamoDB access
- AWS Access Key, Secret Key, and Session Token

## Configuration

Before running the application, you need to configure your AWS credentials in the `application.properties` file:

```properties
aws.accessKey=your_access_key_here
aws.secretKey=your_secret_key_here
aws.sessionToken=your_session_token_here
aws.region=us-east-1
```

Replace the placeholder values with your actual AWS credentials.

## Running the Application

To run the application, use the following command:

```bash
mvn spring-boot:run
```

The application will start on port 8080.

## API Endpoints

The application provides the following REST endpoints for CRUD operations on users:

### Create a User

```
POST /api/users
```

Request Body:
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "age": 30,
  "address": "123 Main St"
}
```

### Get a User by ID

```
GET /api/users/{id}
```

### Get All Users

```
GET /api/users
```

### Update a User

```
PUT /api/users/{id}
```

Request Body:
```json
{
  "name": "John Doe Updated",
  "email": "john.updated@example.com",
  "age": 31,
  "address": "456 New St"
}
```

### Delete a User

```
DELETE /api/users/{id}
```

## Example Usage

### Create a User

```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 30,
    "address": "123 Main St"
  }'
```

### Get a User by ID

```bash
curl -X GET http://localhost:8080/api/users/{id}
```

### Get All Users

```bash
curl -X GET http://localhost:8080/api/users
```

### Update a User

```bash
curl -X PUT http://localhost:8080/api/users/{id} \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe Updated",
    "email": "john.updated@example.com",
    "age": 31,
    "address": "456 New St"
  }'
```

### Delete a User

```bash
curl -X DELETE http://localhost:8080/api/users/{id}
```

## Project Structure

- `config/DynamoDBConfig.java`: Configuration for AWS DynamoDB client
- `model/User.java`: Model class for User entity
- `repository/UserRepository.java`: Repository layer for CRUD operations
- `service/UserService.java`: Service layer for business logic
- `controller/UserController.java`: Controller layer for REST endpoints