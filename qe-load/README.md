# QE Load Module (`qe-load/`)

## What is the use of this folder?
This module is dedicated entirely to performance, load, and stress testing.

## What it will do
Instead of validating *if* a feature works (functional testing), this module validates *how fast* it works under stress. It uses Gatling (Scala/Java) to simulate hundreds or thousands of concurrent virtual users hitting the API endpoints simultaneously to measure latency, throughput, and system bottlenecks.

## How to use it
- Write Gatling simulation classes in `src/test/java/` or `src/test/scala/`.
- Do not use Surefire or standard TestNG to run this. It executes via the Gatling Maven plugin.
- Execute via: `mvn gatling:test -pl qe-load`.

## Available Simulations
- **`SpikeLoadSimulation`**: Injects 200 virtual users instantly to evaluate system survival during massive traffic bursts.
- **`StressLoadSimulation`**: Gradually ramps up 500 users over a few minutes to monitor memory/latency degradation.
- **`UserJourneySimulation`**: Simulates a full, stateful user workflow (browsing, searching, API requests) under constant load.
- **`BasicLoadSimulation`**: A lightweight smoke test for basic connectivity and performance checks.
