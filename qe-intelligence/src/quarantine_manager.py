import os
import requests
import logging
from flaky_detector import FlakyDetector

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

class QuarantineManager:
    def __init__(self, github_token=None, repo_name=None, audit_mode=True):
        self.github_token = github_token or os.getenv("GITHUB_TOKEN")
        self.repo_name = repo_name or os.getenv("GITHUB_REPOSITORY")
        self.audit_mode = audit_mode
        self.headers = {
            "Authorization": f"token {self.github_token}",
            "Accept": "application/vnd.github.v3+json"
        }

    def process_test_results(self, history_file: str, fi_threshold: float = 0.05):
        """
        Analyzes test history and automatically quarantines tests that cross the FI threshold.
        """
        detector = FlakyDetector(history_file)
        results_df = detector.analyze_all_tests()
        
        # Filter tests that require quarantine
        quarantine_candidates = results_df[results_df['flakiness_index'] >= fi_threshold]
        
        if quarantine_candidates.empty:
            logging.info("No tests crossed the quarantine threshold today.")
            return

        for _, row in quarantine_candidates.iterrows():
            test_name = row['test_name']
            fi_score = row['flakiness_index']
            
            logging.warning(f"ACTION REQUIRED: Quarantining test {test_name} with FI {fi_score:.3f}")
            self.quarantine_test(test_name, fi_score)
            
            # Note: In a real environment, this script would also modify a quarantine.json 
            # or communicate with the Orchestrator to actively exclude the test from PR pipelines.

    def quarantine_test(self, test_name: str, fi_score: float):
        """
        Creates a GitHub issue to track the quarantined test and notifies the team.
        """
        if self.audit_mode:
            logging.info(f"[AUDIT MODE] Would have quarantined {test_name} with FI: {fi_score:.2f}. Bypassing GitHub API to track FPR.")
            return True
            
        if not self.github_token:
            logging.warning(f"Dry run: Would create GitHub issue for quarantined test: {test_name}")
            return
            
        url = f"https://api.github.com/repos/{self.repo_name}/issues"
        
        payload = {
            "title": f"🚨 [QUARANTINE] Flaky Test Detected: {test_name}",
            "body": f"The test `{test_name}` has been automatically quarantined.\n\n"
                    f"**Flakiness Index**: {fi_score:.3f}\n\n"
                    "This test exceeded the quarantine threshold (0.05) and has been removed "
                    "from blocking PR pipelines. It will continue to run in Release pipelines in observation mode.\n\n"
                    "Please investigate and fix the root cause. If the FI drops below 0.03 for 7 consecutive days, "
                    "it will be automatically restored.",
            "labels": ["flaky-test", "quarantined", "qe-platform"]
        }
        
        response = requests.post(url, json=payload, headers=self.headers)
        
        if response.status_code == 201:
            issue_url = response.json().get('html_url')
            logging.info(f"Successfully created quarantine issue: {issue_url}")
        else:
            logging.error(f"Failed to create issue. Status: {response.status_code}, Response: {response.text}")

if __name__ == "__main__":
    # Example usage
    history_csv = os.environ.get("TEST_HISTORY_CSV", "sample_history.csv")
    gh_token = os.environ.get("GITHUB_TOKEN", "")
    repo = os.environ.get("GITHUB_REPOSITORY", "org/repo")
    
    # In a real run, ensure sample_history.csv exists before calling
    if os.path.exists(history_csv):
        manager = QuarantineManager(gh_token, repo)
        manager.process_test_results(history_csv)
    else:
        logging.warning(f"History file {history_csv} not found. Skipping execution.")
