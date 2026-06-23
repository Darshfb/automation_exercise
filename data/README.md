# Data Directory (`data/`)

## What is the use of this folder?
This folder serves as the centralized repository for test data, fixtures, and payload templates used by the automation suites.

## What it will do
It isolates test data from the test logic. It holds static JSON, CSV, or Excel files that represent different user states, mock API responses, or bulk load-testing inputs.

## How to use it
- Place static files here (e.g., `users.json`, `products.csv`).
- In your test modules (like `qe-api` or `qe-ui`), use file readers or DataProviders to read from `../../data/[filename]`.
- Do not store environment-specific configurations here; those belong in `qe-core`'s properties files. This is purely for raw payload/input data.
