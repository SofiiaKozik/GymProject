# Gym Project

## Description

Gym Project is a Java application designed to manage gym operations, including trainees, trainers, and training sessions.

The application follows a layered architecture and uses Spring Framework for dependency injection and component management.

## Technologies

- Java
- Spring Framework
- Maven
- JUnit 
- Mockito
- JSON

## Project Structure

The project is organized into the following main packages:

- `model` — domain entities, including Trainee, Trainer, and Training.
- `repository` — DAO interfaces and in-memory repository implementations.
- `service` — business logic and service interfaces.
- `facade` — provides a unified interface for application operations.
- `generator` — username and password generation.
- `storage` — in-memory storage configuration, initialization, and existing username management.

## Features

- Trainee and trainer management.
- Training session management.
- Username and password generation.
- In-memory data storage.
- Initial data loading from JSON files.
- Unit testing of application components.

## Running Tests

To run the tests, execute the following Maven command:

```bash
mvn test
```

## Building the Project

To build the project, execute:

```bash
mvn clean package
```