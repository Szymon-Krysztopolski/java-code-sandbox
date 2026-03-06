#!/bin/bash
set -e

echo "Listing all secrets in LocalStack Secrets Manager..."
awslocal secretsmanager list-secrets

echo
echo "Printing values for all secrets..."
for secret in $(awslocal secretsmanager list-secrets --query 'SecretList[*].Name' --output text); do
  echo "Secret: $secret"
  awslocal secretsmanager get-secret-value --secret-id "$secret"
  echo "-----------------------------"
done
