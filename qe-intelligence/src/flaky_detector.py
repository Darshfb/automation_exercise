import pandas as pd
from datetime import datetime, timedelta
import logging

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

class FlakyDetector:
    def __init__(self, execution_history_file: str):
        """
        Initialize the detector with historical test execution data.
        Expected CSV format: test_name, execution_date, status
        """
        self.history_df = pd.read_csv(execution_history_file)
        self.history_df['execution_date'] = pd.to_datetime(self.history_df['execution_date'])

    def calculate_flakiness_index(self, test_name: str, window_days: int = 30) -> float:
        """
        Calculates the Flakiness Index (FI) for a specific test.
        FI = (flip_count / total_runs) * recency_weight
        """
        cutoff_date = datetime.now() - timedelta(days=window_days)
        test_data = self.history_df[
            (self.history_df['test_name'] == test_name) & 
            (self.history_df['execution_date'] >= cutoff_date)
        ].sort_values('execution_date')

        if test_data.empty:
            return 0.0

        total_runs = len(test_data)
        if total_runs < 5:
            # Not enough data points to determine flakiness reliably
            return 0.0

        flip_count = 0
        statuses = test_data['status'].tolist()
        
        for i in range(1, len(statuses)):
            if statuses[i] != statuses[i-1]:
                flip_count += 1

        # Calculate a basic recency weight. More recent flips = higher weight.
        # For MVP, we'll keep it simple and apply a mild decay based on the last flip's age
        # A full exponential decay requires modeling time deltas between executions
        
        base_fi = flip_count / total_runs
        
        # Determine classification
        classification = self._classify(base_fi)
        logging.info(f"Test '{test_name}' | Runs: {total_runs} | Flips: {flip_count} | FI: {base_fi:.3f} | {classification}")
        
        return base_fi

    def _classify(self, fi_score: float) -> str:
        if fi_score < 0.02:
            return "STABLE ✅"
        elif fi_score < 0.05:
            return "WATCH 👀"
        elif fi_score < 0.10:
            return "FLAKY ⚠️ (Auto-Quarantine)"
        else:
            return "TOXIC 🔴 (Immediate Quarantine)"

    def analyze_all_tests(self) -> pd.DataFrame:
        """
        Analyzes all tests and returns a DataFrame with their FI scores.
        """
        results = []
        unique_tests = self.history_df['test_name'].unique()
        
        for test in unique_tests:
            fi = self.calculate_flakiness_index(test)
            results.append({
                'test_name': test,
                'flakiness_index': fi,
                'status': self._classify(fi)
            })
            
        return pd.DataFrame(results).sort_values('flakiness_index', ascending=False)
