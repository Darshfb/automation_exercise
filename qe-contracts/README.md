# QE Contracts Module (`qe-contracts/`)

## What is the use of this folder?
This module is dedicated to Consumer-Driven Contract (CDC) testing using the Pact framework.

## What it will do
It validates the API contracts between microservices or between the frontend and backend. It ensures that the schema, status codes, and data types returned by the API strictly match what the Consumer (the UI or client) expects. If a backend developer changes an API payload format, this module fails instantly.

## How to use it
- Write Pact tests in `src/test/java/`.
- Annotate them with `@Pact` and build the expected request/response interactions.
- Run via `mvn test -pl qe-contracts`.
- This ensures breaking API changes are caught before full integration testing.
