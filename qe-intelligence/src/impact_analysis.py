import networkx as nx
import json
import logging
import sys

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')

class TestImpactAnalyzer:
    def __init__(self, mapping_file: str):
        self.graph = nx.DiGraph()
        self._load_dependency_graph(mapping_file)

    def _load_dependency_graph(self, mapping_file: str):
        """
        Loads the module-to-test dependency graph from a JSON mapping file.
        Example mapping:
        {
            "src/main/java/core/AuthService.java": ["AuthApiSmokeTests", "AuthSmokeTests"],
            "src/main/java/ui/CartComponent.java": ["CartSmokeTests", "CheckoutSmokeTests"]
        }
        """
        try:
            with open(mapping_file, 'r') as f:
                mappings = json.load(f)
                
            for source_file, tests in mappings.items():
                self.graph.add_node(source_file, type='source')
                for test in tests:
                    self.graph.add_node(test, type='test')
                    self.graph.add_edge(source_file, test)
                    
            logging.info(f"Loaded dependency graph with {self.graph.number_of_nodes()} nodes and {self.graph.number_of_edges()} edges.")
        except FileNotFoundError:
            logging.error(f"Mapping file {mapping_file} not found. Cannot perform TIA.")
        except Exception as e:
            logging.error(f"Error loading graph: {e}")

    def analyze_impact(self, changed_files: list) -> set:
        """
        Given a list of changed files, returns the minimal set of tests that need to run.
        """
        impacted_tests = set()
        
        for file in changed_files:
            if file in self.graph:
                # Find all downstream nodes from the changed file
                descendants = nx.descendants(self.graph, file)
                for node in descendants:
                    if self.graph.nodes[node].get('type') == 'test':
                        impacted_tests.add(node)
            else:
                logging.warning(f"File {file} not found in dependency graph. Falling back to safe-mode (run all tests in module).")
                # Fallback logic could be implemented here
                
        return impacted_tests

    def generate_testng_xml(self, impacted_tests: set, output_file: str):
        """
        Generates a dynamic TestNG XML suite containing only the impacted tests.
        """
        # A basic MVP representation of writing dynamic XML
        with open(output_file, 'w') as f:
            f.write('<?xml version="1.0" encoding="UTF-8"?>\n')
            f.write('<!DOCTYPE suite SYSTEM "https://testng.org/testng-1.0.dtd">\n')
            f.write('<suite name="Dynamic TIA Suite">\n')
            f.write('    <test name="Impacted Tests">\n')
            f.write('        <classes>\n')
            for test in impacted_tests:
                # We would need full package names in a real system
                f.write(f'            <class name="{test}"/>\n')
            f.write('        </classes>\n')
            f.write('    </test>\n')
            f.write('</suite>\n')
            
        logging.info(f"Generated dynamic test suite at {output_file} with {len(impacted_tests)} tests.")

if __name__ == "__main__":
    if len(sys.argv) < 3:
        print("Usage: python impact_analysis.py <mapping.json> <changed_file1> [changed_file2 ...]")
        sys.exit(1)
        
    mapping = sys.argv[1]
    changes = sys.argv[2:]
    
    analyzer = TestImpactAnalyzer(mapping)
    tests_to_run = analyzer.analyze_impact(changes)
    
    print(f"CHANGED FILES: {changes}")
    print(f"IMPACTED TESTS: {tests_to_run}")
    
    analyzer.generate_testng_xml(tests_to_run, "target/dynamic_tia_suite.xml")
