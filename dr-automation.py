# dr_test.py
import boto3
import json
import logging
from datetime import datetime

class DRTester:
    def __init__(self, primary_region, secondary_region):
        self.primary_region = primary_region
        self.secondary_region = secondary_region
        self.setup_logging()

    def setup_logging(self):
        logging.basicConfig(level=logging.INFO)
        self.logger = logging.getLogger('DRTester')

    def test_rds_failover(self):
        rds = boto3.client('rds', region_name=self.primary_region)
        try:
            response = rds.failover_db_cluster(
                DBClusterIdentifier='idiomas-db-cluster'
            )
            self.logger.info(f"RDS failover initiated: {response}")
            return True
        except Exception as e:
            self.logger.error(f"RDS failover failed: {e}")
            return False

    def test_route53_failover(self):
        route53 = boto3.client('route53')
        try:
            response = route53.update_health_check(
                HealthCheckId='health-check-id',
                Disabled=True
            )
            self.logger.info("Route53 failover test initiated")
            return True
        except Exception as e:
            self.logger.error(f"Route53 failover test failed: {e}")
            return False

    def verify_replication(self):
        primary_ddb = boto3.client('dynamodb', region_name=self.primary_region)
        secondary_ddb = boto3.client('dynamodb', region_name=self.secondary_region)
        
        test_item = {
            'id': {'S': f'test-{datetime.now().isoformat()}'},
            'data': {'S': 'test-data'}
        }
        
        try:
            primary_ddb.put_item(
                TableName='idiomas-sessions',
                Item=test_item
            )
            
            # Wait for replication
            import time
            time.sleep(5)
            
            response = secondary_ddb.get_item(
                TableName='idiomas-sessions',
                Key={'id': test_item['id']}
            )
            
            return 'Item' in response
        except Exception as e:
            self.logger.error(f"Replication test failed: {e}")
            return False

# Lambda handler
def handler(event, context):
    dr_tester = DRTester(
        primary_region='us-east-1',
        secondary_region='us-west-2'
    )
    
    results = {
        'rds_failover': dr_tester.test_rds_failover(),
        'route53_failover': dr_tester.test_route53_failover(),
        'replication_test': dr_tester.verify_replication()
    }
    
    return {
        'statusCode': 200,
        'body': json.dumps(results)
    }
