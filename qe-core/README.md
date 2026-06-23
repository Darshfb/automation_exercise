# QE Core Module (`qe-core/`)

## What is the use of this folder?
This is the foundational framework library. All other modules (`qe-ui`, `qe-api`, etc.) depend on this module.

## What it will do
It centralizes shared utilities, environment configuration readers (parsing `-Denv=qa`), logging formatters, custom assertions, and base test classes. It enforces uniformity across the entire Quality Engineering platform.

## How to use it
- Do NOT write test cases in this folder.
- Add utility classes (e.g., `ConfigReader.java`, `LogUtil.java`) to `src/main/java/`.
- Maintain environment properties (e.g., `qa.properties`, `stg.properties`) in `src/main/resources/`.
- When building new tools, put them here so all other modules inherit them automatically.
