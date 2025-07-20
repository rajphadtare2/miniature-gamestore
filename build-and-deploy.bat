@echo off
setlocal

echo Starting Maven build...
call mvn clean package -DskipTests

echo Building and starting services with Docker Compose...
call docker-compose up --build -d

REM Wait for Kafka to initialize
echo Waiting for Kafka to start...
timeout /t 5 >nul

REM Create Kafka topic
echo Creating Kafka topic: order-topic
docker exec -it broker /opt/kafka/bin/kafka-topics.sh --bootstrap-server broker:29092 --create --topic order-topic --partitions 1 --replication-factor 1

docker exec -it broker /opt/kafka/bin/kafka-topics.sh \ --bootstrap-server broker:29092 \ --list

echo Deployment completed successfully!
endlocal