set -e

docker build -t karthikdasari05/expense-service:latest .
docker push karthikdasari05/expense-service:latest
cd /Users/karthikdasari/workspace/my-projects/expense-tracker
docker compose up -d expense-service
