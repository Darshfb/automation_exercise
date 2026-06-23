import json
import logging
from datetime import datetime
from elasticsearch import Elasticsearch

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(message)s')

class MetricPublisher:
    def __init__(self, es_url: str = "http://localhost:9200"):
        try:
            self.es = Elasticsearch([es_url])
            if self.es.ping():
                logging.info(f"Connected to Elasticsearch at {es_url}")
            else:
                logging.error("Could not connect to Elasticsearch.")
        except Exception as e:
            logging.error(f"Elasticsearch connection failed: {e}")
            self.es = None

    def publish_flakiness_metrics(self, fi_results_df):
        """
        Publishes FI scores to ELK.
        """
        if not self.es:
            return
            
        index_name = f"qe-intelligence-flakiness-{datetime.now().strftime('%Y.%m')}"
        
        success_count = 0
        for _, row in fi_results_df.iterrows():
            doc = {
                "timestamp": datetime.utcnow().isoformat(),
                "test_name": row['test_name'],
                "flakiness_index": row['flakiness_index'],
                "status": row['status'],
                "metric_type": "flakiness"
            }
            try:
                self.es.index(index=index_name, document=doc)
                success_count += 1
            except Exception as e:
                logging.error(f"Failed to index FI metric for {row['test_name']}: {e}")
                
        logging.info(f"Published {success_count} FI metrics to {index_name}")

    def publish_cost_metrics(self, cost_data: dict, tia_savings: dict):
        """
        Publishes cost tracking and TIA savings to ELK.
        """
        if not self.es:
            return
            
        index_name = f"qe-intelligence-costs-{datetime.now().strftime('%Y.%m')}"
        
        doc = {
            "timestamp": datetime.utcnow().isoformat(),
            "metric_type": "pipeline_cost",
            "total_cost": cost_data.get('total_cost', 0),
            "total_tests": cost_data.get('total_tests', 0),
            "ui_chrome_count": cost_data.get('breakdown', {}).get('UI_CHROME', 0),
            "api_count": cost_data.get('breakdown', {}).get('API', 0),
            "tia_tests_skipped": tia_savings.get('original_size', 0) - cost_data.get('total_tests', 0),
            "tia_dollars_saved": tia_savings.get('saved_amount', 0)
        }
        
        try:
            self.es.index(index=index_name, document=doc)
            logging.info(f"Published cost metrics to {index_name}")
        except Exception as e:
            logging.error(f"Failed to index cost metrics: {e}")

if __name__ == "__main__":
    # Example usage for manual trigger
    import pandas as pd
    
    mock_df = pd.DataFrame([
        {'test_name': 'testValidLoginApi', 'flakiness_index': 0.01, 'status': 'STABLE'},
        {'test_name': 'testSearchExistingProductApi', 'flakiness_index': 0.08, 'status': 'FLAKY'}
    ])
    
    publisher = MetricPublisher()
    publisher.publish_flakiness_metrics(mock_df)
    
    mock_cost = {"total_cost": 0.403, "total_tests": 4, "breakdown": {"API": 2, "UI_CHROME": 2}}
    mock_tia = {"original_size": 120, "saved_amount": 10.50}
    
    publisher.publish_cost_metrics(mock_cost, mock_tia)
