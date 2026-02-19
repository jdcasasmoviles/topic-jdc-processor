# Topic JDC Processor

A Quarkus-based microservice that implements hexagonal architecture for processing and publishing messages to Kafka topics with Avro serialization.

## Architecture

This project follows hexagonal architecture (ports and adapters) with clear separation of concerns:

- **Domain**: Core business logic and entities
- **Application**: Use cases and DTOs
- **Infrastructure**: External adapters (REST, Kafka, etc.)
- **API**: OpenAPI contract and generated interfaces

## Features

- ✅ Hexagonal architecture implementation
- ✅ Virtual threads for improved performance
- ✅ Mutiny reactive programming
- ✅ OpenAPI 3.0 contract with all required status codes (400, 404, 429, 500, 504)
- ✅ Kafka integration with Avro schema
- ✅ Comprehensive validation
- ✅ Global exception handling
- ✅ Unit and integration tests
- ✅ Docker Compose setup

## Flow

1. Client sends POST request to `/api/v1/messages`
2. Request is validated (syntax and business rules)
3. Message is mapped to domain model
4. Domain service processes the message
5. Message is published to Kafka topic `topic-jdc` using Avro schema
6. Response is returned with status and metadata

## Prerequisites

- Java 21
- Maven 3.9+
- Docker and Docker Compose
- Kafka (provided in docker-compose)

## Quick Start

### 1. Generate OpenAPI and Avro classes

```bash
mvn clean generate-sources