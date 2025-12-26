# Makefile

.PHONY: up down build logs restart clean help

help:
	@echo "Available commands:"
	@echo "  up        Start all services in detached mode"
	@echo "  down      Stop all services"
	@echo "  build     Build all services"
	@echo "  logs      Show logs for all services"
	@echo "  restart   Restart all services"
	@echo "  clean     Remove all services, volumes, and unused data"
	@echo "  postgres  Start only the postgres service"
	@echo "  patientservice  Build only the patient-service"
	@echo "  patientservice-up  Start only the patient-service"
	@echo "  patientservice-down  Stop only the patient-service"
	@echo "  eurekaserver Build only the eureka-server"
	@echo "  eurekaserver-up Start only the eureka-server"
	@echo "  eurekaserver-down Stop only the eureka-server"
	@echo "  analytics-service Build only the analytics-service"
	@echo "  analytics-service-up Start only the analytics-service"
	@echo "  analytics-service-down Stop only the analytics-service"
	@echo "  start     Start all services in foreground"


up:
	docker-compose up -d

down:
	docker-compose down

build:
	docker-compose build

logs:
	docker-compose logs -f

restart:
	docker-compose down
	docker-compose up -d

clean:
	docker-compose down -v --remove-orphans
	docker system prune -f

postgres:
	docker-compose up -d postgres

patientservice:
	docker-compose build patient-service

patientservice-up:
	docker-compose up -d patient-service

patientservice-down:
	docker-compose stop patient-service

eurekaserver:
	docker-compose build eureka-server

eurekaserver-up:
	docker-compose up -d eureka-server

eurekaserver-down:
	docker-compose stop eureka-server

analytics-service:
	docker-compose build analytics-service

analytics-service-up:
	docker-compose up -d analytics-service

analytics-service-down:
	docker-compose stop analytics-service




