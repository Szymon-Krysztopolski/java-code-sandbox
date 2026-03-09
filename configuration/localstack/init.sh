#!/bin/bash
set -e

echo "Initializing LocalStack Secrets Manager..."
REGION="${AWS_DEFAULT_REGION}"

declare -A secrets=(
  ["/my-secret/test-1"]='{
    "username": "admin",
    "password": "123"
  }'

  ["/my-secret/test-2"]='{
    "username": "todo-user"
  }'
)

for SECRET_NAME in "${!secrets[@]}"; do
  echo "Processing secret: $SECRET_NAME"

  if awslocal secretsmanager describe-secret \
      --secret-id "$SECRET_NAME" \
      --region "$REGION" >/dev/null 2>&1; then
    echo "Secret $SECRET_NAME already exists"
  else
    echo "Creating secret $SECRET_NAME"
    awslocal secretsmanager create-secret \
      --name "$SECRET_NAME" \
      --region "$REGION" \
      --secret-string "${secrets[$SECRET_NAME]}"
  fi

done

echo "All secrets in LocalStack:"
# List all secrets and show their values
for SECRET_NAME in $(awslocal secretsmanager list-secrets --region "$REGION" --query 'SecretList[].Name' --output text); do
  echo "Secret: $SECRET_NAME"
  VALUE=$(awslocal secretsmanager get-secret-value --secret-id "$SECRET_NAME" --region "$REGION" --query 'SecretString' --output text)
  echo "$VALUE"
  echo "------------------------"
done

echo "LocalStack initialization finished"