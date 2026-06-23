# QE API Module (`qe-api/`)

## What is the use of this folder?
This is a dedicated Maven module strictly for REST API automation testing.

## What it will do
It holds all the endpoint wrappers, JSON deserialization logic, and REST-Assured test classes. It validates the backend services independently of the UI.

## How to use it
- Write your API client classes in `src/main/java/` (using RestAssured specs).
- Write your tests in `src/test/java/` (using TestNG).
- Execute independently via Maven: `mvn test -pl qe-api`.
- Never include Selenium or UI logic in this folder.

## Coverage Status
As of the latest implementation, this module covers **100% of the documented API endpoints (1-14)** for Automation Exercise.
This includes:
- `ProductApiTests`: Endpoints 1-6 (GET, POST, PUT, DELETE for Products/Brands)
- `AuthApiSmokeTests` & `AccountApiTests`: Endpoints 7-14 (Account creation, verification, login, deletion)

All tests handle both positive status checks (200) and negative assertion scenarios (400, 404, 405).
