# GitHub Actions Directory (`.github/`)

## What is the use of this folder?
This folder contains all Continuous Integration and Continuous Deployment (CI/CD) pipelines configuration files for GitHub Actions.

## What it will do
It defines the automated pipelines that trigger when code is pushed or pull requests are opened. The workflows automatically spin up cloud runners, compile the code, run the multi-module tests, scale the grid, and generate reports.

## How to use it
- Add or edit YAML files inside `.github/workflows/` to create new CI gates.
- You don't execute these files locally; they are automatically consumed by GitHub when you push your code to the remote repository.

## Configured Workflows
- **`pr-validation.yml`**: Runs UI/API Smoke Tests on Pull Requests for fast feedback. Calculates cost and test impact (TIA) using `qe-intelligence`.
- **`nightly-regression.yml`**: Runs UI/API Full Regression Tests every night at 2:00 AM. Also executes flaky test detection.
- **`stress-test.yml`**: Designed to be triggered manually. Uses scaling Selenium Grid logic.
- **`release-gate.yml`**: Executes Gatling performance tests in `qe-load` to validate SLAs before a release.
