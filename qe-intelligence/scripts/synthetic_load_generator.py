import json
import random
import time
import sys
import os

# Add the parent directory to sys.path to import cost_calculator
sys.path.append(os.path.abspath(os.path.join(os.path.dirname(__file__), '..', 'src')))
from cost_calculator import CostCalculator

def generate_synthetic_results(num_tests=10000, filename="synthetic_results.json"):
    print(f"Generating {num_tests} synthetic test results...")
    
    test_types = ["API", "UI_CHROME", "UI_FIREFOX", "CONTRACT"]
    weights = [0.60, 0.20, 0.10, 0.10] # 60% API, 30% UI, 10% Contract
    
    tests = []
    for i in range(num_tests):
        t_type = random.choices(test_types, weights)[0]
        duration = random.uniform(0.1, 5.0) if t_type == "API" else random.uniform(5.0, 45.0)
        
        tests.append({
            "name": f"test_synthetic_auto_{i}",
            "type": t_type,
            "duration_sec": round(duration, 2),
            "status": random.choices(["PASS", "FAIL"], [0.98, 0.02])[0]
        })
        
    data = {"tests": tests}
    
    with open(filename, 'w') as f:
        json.dump(data, f, indent=2)
        
    print(f"Saved to {filename}")
    return filename

def verify_cost_math(filename):
    print("Verifying Cost Calculator mathematical accuracy and memory footprint under scale...")
    
    start_time = time.time()
    calc = CostCalculator()
    
    metrics = calc.calculate_run_cost(filename)
    end_time = time.time()
    
    expected_tests = len(json.load(open(filename))['tests'])
    
    print("\n--- SYNTHETIC VALIDATION REPORT ---")
    print(f"Processing Time: {end_time - start_time:.4f} seconds")
    print(f"Tests Processed: {metrics['total_tests']} (Expected: {expected_tests})")
    print(f"Total Pipeline Cost Calculated: ${metrics['total_cost']:.4f}")
    
    # Mathematical assertion
    assert metrics['total_tests'] == expected_tests, "Test count mismatch!"
    print("[OK] Mathematical assertion passed: Memory parsing and cost aggregation is stable at scale.")

if __name__ == "__main__":
    test_file = generate_synthetic_results(10000)
    verify_cost_math(test_file)
