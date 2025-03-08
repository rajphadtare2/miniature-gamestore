@echo off
setlocal

echo Starting Maven build...
call mvn clean package -DskipTests

echo Building and starting services with Docker Compose...
call docker-compose up --build -d

echo Deployment completed successfully!
endlocal
