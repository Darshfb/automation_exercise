# Scripts Directory (`scripts/`)

## What is the use of this folder?
This folder stores lightweight Bash or PowerShell utility scripts used by developers or DevOps pipelines.

## What it will do
It provides shortcut commands for complex Maven operations, Docker setups, or database seeding. For example, a script to quickly format code, clear caches, or stand up the entire local QA environment in one click.

## How to use it
- Place `.sh` (Linux/Mac) or `.ps1` (Windows) files here.
- Ensure scripts have execution permissions (`chmod +x`).
- Execute from the root directory, e.g., `./scripts/run_all.sh`.
