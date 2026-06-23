import os
import json
import statistics
from collections import defaultdict

def calculate_metrics(raw_data_file, output_file):
    if not os.path.exists(raw_data_file):
        print(f"Error: {raw_data_file} not found.")
        return

    with open(raw_data_file, "r") as f:
        runs = json.load(f)

    if not runs:
        print("No run data found.")
        return

    total_runs = len(runs)
    durations = [r["duration_seconds"] for r in runs]
    
    # Execution Metrics
    avg_duration = statistics.mean(durations)
    median_duration = statistics.median(durations)
    std_dev = statistics.stdev(durations) if len(durations) > 1 else 0.0
    
    # Calculate P95
    durations.sort()
    p95_index = int(len(durations) * 0.95)
    p95_duration = durations[p95_index] if p95_index < len(durations) else durations[-1]

    # Reliability Metrics
    total_tests_run = 0
    total_passed = 0
    test_history = defaultdict(list)
    
    for r in runs:
        results = r["test_results"]
        total_tests_run += results["passed"] + results["failed"]
        total_passed += results["passed"]
        
        for t in results["tests"]:
            key = f"{t['module']}.{t['name']}"
            test_history[key].append(t["status"])

    pass_rate = (total_passed / total_tests_run * 100) if total_tests_run > 0 else 0.0

    # Flaky Rate Calculation
    # A test is flaky if it has both "PASS" and "FAIL" in its history across the runs
    flaky_tests = []
    for test_key, history in test_history.items():
        unique_statuses = set(history)
        if "PASS" in unique_statuses and "FAIL" in unique_statuses:
            flaky_tests.append(test_key)

    total_unique_tests = len(test_history)
    flaky_rate = (len(flaky_tests) / total_unique_tests * 100) if total_unique_tests > 0 else 0.0

    # TIA Projected ROI
    # Assume TIA reduces test execution scope by 60% based on code changes
    projected_time_savings = avg_duration * 0.60
    projected_avg_duration = avg_duration - projected_time_savings

    report = {
        "metadata": {
            "total_runs_analyzed": total_runs,
            "total_unique_tests": total_unique_tests,
            "total_test_executions": total_tests_run
        },
        "execution_performance": {
            "average_time_sec": round(avg_duration, 2),
            "median_time_sec": round(median_duration, 2),
            "p95_time_sec": round(p95_duration, 2),
            "standard_deviation_sec": round(std_dev, 2)
        },
        "reliability_metrics": {
            "overall_pass_rate_percent": round(pass_rate, 2),
            "flaky_test_count": len(flaky_tests),
            "flaky_rate_percent": round(flaky_rate, 2),
            "flaky_tests": flaky_tests
        },
        "intelligence_projections": {
            "tia_assumed_reduction_percent": 60,
            "projected_avg_time_with_tia_sec": round(projected_avg_duration, 2),
            "projected_time_savings_per_run_sec": round(projected_time_savings, 2)
        }
    }

    with open(output_file, "w") as f:
        json.dump(report, f, indent=2)

    print(f"Metrics aggregation complete. Report saved to {output_file}")
    print(json.dumps(report, indent=2))

if __name__ == "__main__":
    workspace_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    raw_data = os.path.join(workspace_dir, "qe-intelligence", "data", "raw_stress_data.json")
    output_report = os.path.join(workspace_dir, "qe-intelligence", "data", "analytics_report.json")
    calculate_metrics(raw_data, output_report)
