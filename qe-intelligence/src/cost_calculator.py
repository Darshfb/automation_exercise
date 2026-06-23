import json
import logging
import sys

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(message)s')

class CostCalculator:
    def __init__(self):
        # Base unit costs based on Spot/Preemptible Instances from implementation_plan.md
        self.COST_TABLE = {
            "API": 0.003,      # API Test: $0.003 per run
            "UI_CHROME": 0.10, # UI Test Chrome: $0.10 per run
            "UI_FIREFOX": 0.12,# UI Test Firefox: $0.12 per run
            "CONTRACT": 0.0001 # Contract Test: $0.0001 per run
        }

    def calculate_run_cost(self, results_json: str) -> dict:
        """
        Calculates the execution cost based on the test results JSON.
        Expected format:
        {
            "tests": [
                {"name": "testValidLoginApi", "type": "API", "duration_sec": 3.2},
                {"name": "testRegisterUser", "type": "UI_CHROME", "duration_sec": 12.5}
            ]
        }
        """
        try:
            with open(results_json, 'r') as f:
                data = json.load(f)
                
            total_cost = 0.0
            breakdown = {"API": 0, "UI_CHROME": 0, "UI_FIREFOX": 0, "CONTRACT": 0}
            
            for test in data.get("tests", []):
                t_type = test.get("type", "API") # Default to cheapest if unknown
                cost = self.COST_TABLE.get(t_type, 0.01)
                total_cost += cost
                breakdown[t_type] += 1
                
            return {
                "total_cost": total_cost,
                "breakdown": breakdown,
                "total_tests": len(data.get("tests", []))
            }
            
        except FileNotFoundError:
            logging.error(f"Results file {results_json} not found.")
            return {"total_cost": 0.0, "breakdown": {}, "total_tests": 0}

    def generate_pr_comment_md(self, cost_data: dict, tia_savings: dict, output_file: str):
        """
        Generates a Markdown file that can be posted as a PR comment via GitHub Actions.
        """
        md = f"""## 🧠 QE Platform Intelligence Report

### 💰 Cost Execution Summary
* **Total Pipeline Cost**: `${cost_data['total_cost']:.4f}`
* **Tests Executed**: `{cost_data['total_tests']}`

**Cost Breakdown:**
* **UI Tests (Chrome)**: {cost_data['breakdown'].get('UI_CHROME', 0)} (≈ `${cost_data['breakdown'].get('UI_CHROME', 0) * self.COST_TABLE['UI_CHROME']:.2f}`)
* **API Tests**: {cost_data['breakdown'].get('API', 0)} (≈ `${cost_data['breakdown'].get('API', 0) * self.COST_TABLE['API']:.4f}`)

### 🔍 Test Impact Analysis (TIA)
* **Original Suite Size**: `{tia_savings['original_size']}`
* **Impacted Tests Run**: `{cost_data['total_tests']}`
* **Tests Skipped**: `{tia_savings['original_size'] - cost_data['total_tests']}`
* **Cost Saved by TIA**: `~${tia_savings['saved_amount']:.4f}`

> [!TIP]
> The Flaky Detection Engine is actively monitoring these results. Tests with an FI > 0.05 will be auto-quarantined.
"""
        with open(output_file, 'w', encoding='utf-8') as f:
            f.write(md)
            
        logging.info(f"Generated PR comment Markdown at {output_file}")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python cost_calculator.py <results.json> <output.md>")
        sys.exit(1)
        
    results_file = sys.argv[1]
    output_md = sys.argv[2]
    
    # In a real run, TIA savings would be passed from the TIA component
    # Here we mock it for the MVP
    mock_tia_savings = {
        "original_size": 120,
        "saved_amount": 10.50 
    }
    
    calc = CostCalculator()
    cost_metrics = calc.calculate_run_cost(results_file)
    calc.generate_pr_comment_md(cost_metrics, mock_tia_savings, output_md)
