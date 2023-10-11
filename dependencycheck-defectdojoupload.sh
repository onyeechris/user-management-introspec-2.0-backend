#!/bin/bash
var_version=`date +"Time"-"%H:%M:%S"`
data=$(date +"%Y-%m-%d")
# export data1=$data
# password='3lqa6PNkIKy4umKqt90kU9'
# username='admin'
# eng='DependencyCheck'
# pro='Payassyst-Backend-DependencyCheck'
# var-test='DependencyCheck'
# product_type='Payassyst-Backend'

curl -X 'POST' \
  'http://46.101.91.72:8080/api/v2/import-scan/' \
  -H 'accept: application/json' \
  -H 'Content-Type: multipart/form-data' \
  -H 'X-CSRFTOKEN: CnKXFc26hVaRqoPSsiWg6K9B60zA3UkrkZnHETAT5y9mh5E5OAMTVZBE7QMssX4J' \
  -F 'product_type_name=user-management-introspec-2.0-backend' \
  -F "scan_date=$data1" \
  -F 'active=true' \
  -F 'verified=true' \
  -F "engagement_end_date=$data1" \
  -F 'engagement_name=DependencyCheck-Scan' \
  -F 'deduplication_on_engagement=true' \
  -F 'minimum_severity=Info' \
  -F 'create_finding_groups_for_all_findings=true' \
  -F 'environment=Development' \
  -F 'group_by=finding_title' \
  -F "version=${var_version}" \
  -F 'test_title=DependencyCheck' \
  -F 'product_name=user-management-introspec-2.0-backend-DependencyCheck-Scan' \
  -F 'file=@dependency-check-report.xml;type=application/xml' \
  -F 'auto_create_context=true' \
  -F 'scan_type=Dependency Check Scan'   \
  -F 'close_old_findings=true' \
  -F 'close_old_findings_product_scope=true' \
  -F 'lead=1' \
  -F 'tags=DependencyCheck' \
  -F 'branch_tag=secops' \
  -F 'service=DependencyCheck' \
  --user 'admin:3lqa6PNkIKy4umKqt90kU9'
  # curl -X 'POST' \
  # 'https://vms.aetdevops.com/api/v2/import-scan/' \
  # -H 'accept: application/json' \
  # -H 'Content-Type: multipart/form-data' \
  # -H 'X-CSRFTOKEN: CnKXFc26hVaRqoPSsiWg6K9B60zA3UkrkZnHETAT5y9mh5E5OAMTVZBE7QMssX4J' \
  # -F "product_type_name=${product_type}" \
  # -F "scan_date=$data1" \
  # -F 'active=true' \
  # -F 'verified=true' \
  # -F "engagement_end_date=$data1" \
  # -F "engagement_name=${eng}" \
  # -F 'deduplication_on_engagement=true' \
  # -F 'minimum_severity=Info' \
  # -F 'create_finding_groups_for_all_findings=true' \
  # -F 'environment=Development' \
  # -F 'group_by=finding_title' \
  # -F "version=${var_version}" \
  # -F "test_title=${var_test}" \
  # -F "product_name=${pro}" 
  # -F 'file=@/target/dependency-check-report.xml;type=application/xml' \
  # -F 'auto_create_context=true' \
  # -F 'scan_type=Dependency Check Scan'   \
  # -F 'close_old_findings=true' \
  # -F 'close_old_findings_product_scope=true' \
  # -F 'lead=1' \
  # -F 'tags=Payassyst-Backend' \
  # -F 'branch_tag=SecOps' \
  # -F 'service=DependencyCheck' \
  #  --user "${username}:${password}"
