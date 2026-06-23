# Automation Exercise - Advanced QE Platform 🚀

Welcome to the **Automation Exercise** Quality Engineering repository! This project is a highly advanced, multi-module test automation platform built in Java. It encompasses comprehensive UI, REST API, Performance, and Contract testing using the latest industry standards.

## 🏗️ Architecture

This repository is built using a strict modular architecture (Maven Multi-Module).

- **[`qe-core/`](./qe-core/)**: The central engine. Handles WebDriver factories, Environment Configurations, Logging, and Allure report setups.
- **[`qe-ui/`](./qe-ui/)**: Selenium-based functional UI testing. Implements the Page Object Model (POM) and covers everything from Smoke to exhaustive Negative scenarios.
- **[`qe-api/`](./qe-api/)**: REST-Assured framework testing 100% of the backend endpoints.
- **[`qe-load/`](./qe-load/)**: Gatling-based performance testing simulations (Spike, Stress, User Journey) designed to ensure the system survives high-traffic bursts.
- **[`qe-intelligence/`](./qe-intelligence/)**: AI-powered telemetry and analytics (Test Impact Analysis, Cloud Cost Calculation, and Flakiness Detection).
- **[`qe-contracts/`](./qe-contracts/)**: Consumer-Driven Contract testing via Pact.
- **[`observability/`](./observability/)**: Dockerized ELK stack and Prometheus configuration for real-time grid monitoring.

## ⚙️ Running the Tests Locally

You can execute specific modules via Maven:

**Run UI Tests:**
```bash
mvn test -pl qe-ui -Denv=qa -Dheadless=true
```

**Run API Tests:**
```bash
mvn test -pl qe-api -Denv=qa
```

**Run Performance Tests:**
```bash
mvn gatling:test -pl qe-load
```

## ☁️ Continuous Integration (GitHub Actions)

This repository is fully integrated with GitHub Actions (`.github/workflows/`):
- **Pull Requests (`pr-validation.yml`)**: Instantly executes UI/API Smoke tests to block bad code.
- **Nightly (`nightly-regression.yml`)**: Executes the massive Regression suites and flaky test detection every night.
- **Pre-Release (`release-gate.yml`)**: Executes the Gatling performance load test before any production deployment.
