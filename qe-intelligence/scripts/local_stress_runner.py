import os
import sys
import subprocess
import time
import json
import argparse
import xml.etree.ElementTree as ET
from pathlib import Path

def parse_testng_results(workspace_dir):
    """Parses testng-results.xml from all modules."""
    results = {"passed": 0, "failed": 0, "skipped": 0, "tests": []}
    modules = ["qe-api", "qe-ui", "qe-contracts"]
    
    for module in modules:
        report_path = os.path.join(workspace_dir, module, "target", "surefire-reports", "testng-results.xml")
        if os.path.exists(report_path):
            try:
                tree = ET.parse(report_path)
                root = tree.getroot()
                results["passed"] += int(root.attrib.get("passed", 0))
                results["failed"] += int(root.attrib.get("failed", 0))
                results["skipped"] += int(root.attrib.get("skipped", 0))
                
                for test_method in root.findall(".//test-method"):
                    name = test_method.attrib.get("name")
                    status = test_method.attrib.get("status")
                    if name and status:
                        results["tests"].append({
                            "module": module,
                            "name": name,
                            "status": status
                        })
            except Exception as e:
                print(f"Failed to parse {report_path}: {e}")
                
    return results

def main():
    parser = argparse.ArgumentParser(description="Local Stress & Concurrency Runner")
    parser.add_argument("--iterations", type=int, default=20, help="Number of iterations per thread count")
    parser.add_argument("--threads", type=str, default="1,2,4", help="Comma separated thread counts")
    args = parser.parse_args()

    thread_counts = [int(t.strip()) for t in args.threads.split(',')]
    workspace_dir = os.path.abspath(os.path.join(os.path.dirname(__file__), "..", ".."))
    
    all_runs = []
    
    print(f"Starting Stress Test: {args.iterations} iterations across thread counts {thread_counts}")
    
    for threads in thread_counts:
        print(f"\n{'='*50}\nTesting with {threads} Concurrent Threads\n{'='*50}")
        for i in range(1, args.iterations + 1):
            run_id = f"threads_{threads}_run_{i}"
            print(f"[{time.strftime('%H:%M:%S')}] Starting Run {i}/{args.iterations} for {threads} threads...", flush=True)
            
            start_time = time.time()
            
            mvn_exec = "mvn.cmd" if os.name == "nt" else "mvn"
            cmd = [
                mvn_exec, "clean", "test",
                "-Denv=qa",
                "-Dheadless=true",
                f"-DthreadCount={threads}",
                "-fae"
            ]
            
            # Execute Maven (suppress stdout to keep console clean, only print on error or capture)
            result = subprocess.run(cmd, cwd=workspace_dir, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
            duration = time.time() - start_time
            
            run_data = {
                "run_id": run_id,
                "threads": threads,
                "iteration": i,
                "duration_seconds": duration,
                "maven_exit_code": result.returncode,
                "test_results": parse_testng_results(workspace_dir)
            }
            
            all_runs.append(run_data)
            print(f"[{time.strftime('%H:%M:%S')}] Run {i} completed in {duration:.2f}s. "
                  f"(Pass: {run_data['test_results']['passed']}, Fail: {run_data['test_results']['failed']})", flush=True)

    # Dump raw data for the aggregator
    output_dir = os.path.join(workspace_dir, "qe-intelligence", "data")
    os.makedirs(output_dir, exist_ok=True)
    out_file = os.path.join(output_dir, "raw_stress_data.json")
    with open(out_file, "w") as f:
        json.dump(all_runs, f, indent=2)
        
    print(f"\nAll iterations complete. Raw data saved to {out_file}")

if __name__ == "__main__":
    main()
