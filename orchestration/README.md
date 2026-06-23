# Orchestration Directory (`orchestration/`)

## What is the use of this folder?
This folder manages the deployment and scaling of the automation infrastructure, primarily the Selenium Grid.

## What it will do
It provides the Docker Compose files and Kubernetes manifests needed to spin up isolated browsers (Chrome, Firefox, Edge) in the cloud. It ensures tests have a distributed environment to run in parallel without overloading a single machine.

## How to use it
- Use `docker-compose -f orchestration/grid.yml up -d` (or similar configs stored here) to start the Selenium Hub and its browser nodes.
- Scale nodes via CLI: `docker-compose up --scale chrome=10`.
- All `qe-ui` tests should be configured to point to the Hub URL defined by this orchestration logic.
