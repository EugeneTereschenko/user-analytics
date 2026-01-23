.PHONY: up down build logs restart clean help \
        doctorservice doctorservice-up doctorservice-down \
        billingservice billingservice-up billingservice-down \
        prescriptionservice prescriptionservice-up prescriptionservice-down \
        auth-service auth-service-up auth-service-down \
        kafka kafka-up kafka-down

help:
	@echo "Available commands:"
	@echo "  up        Start all services in detached mode"
	@echo "  down      Stop all services"
	@echo "  build     Build all services"
	@echo "  logs      Show logs for all services"
	@echo "  restart   Restart all services"
	@echo "  clean     Remove all services, volumes, and unused data"
	@echo "  postgres  Start only the postgres service"
	@echo "  kafka     Build only the kafka service"
	@echo "  kafka-up  Start only the kafka service"
	@echo "  kafka-down Stop only the kafka service"
	@echo "  patientservice  Build only the patient-service"
	@echo "  patientservice-up  Start only the patient-service"
	@echo "  patientservice-down  Stop only the patient-service"
	@echo "  eurekaserver Build only the eureka-server"
	@echo "  eurekaserver-up Start only the eureka-server"
	@echo "  eurekaserver-down Stop only the eureka-server"
	@echo "  analytics-service Build only the analytics-service"
	@echo "  analytics-service-up Start only the analytics-service"
	@echo "  analytics-service-down Stop only the analytics-service"
	@echo "  appointment-service        Build only the appointment-service"
	@echo "  appointment-service-up     Start only the appointment-service"
	@echo "  appointment-service-down   Stop only the appointment-service"
	@echo "  medicalrecordservice       Build only the medicalrecord-service"
	@echo "  medicalrecordservice-up    Start only the medicalrecord-service"
	@echo "  medicalrecordservice-down  Stop only the medicalrecord-service"
	@echo "  doctorservice              Build only the doctor-service"
	@echo "  doctorservice-up           Start only the doctor-service"
	@echo "  doctorservice-down         Stop only the doctor-service"
	@echo "  billingservice             Build only the billing-service"
	@echo "  billingservice-up          Start only the billing-service"
	@echo "  billingservice-down        Stop only the billing-service"
	@echo "  start     Start all services in foreground"
	@echo "  prescriptionservice             Build only the prescription-service"
	@echo "  prescriptionservice-up          Start only the prescription-service"
	@echo "  prescriptionservice-down        Stop only the prescription-service"
	@echo "  auth-service                   Build only the auth-service"
	@echo "  auth-service-up                Start only the auth-service"
	@echo "  auth-service-down              Stop only the auth-service"
	@echo "  notificationservice             Build only the notification-service"
	@echo "  notificationservice-up          Start only the notification-service"
	@echo "  notificationservice-down        Stop only the notification-service"




up:
	docker-compose up -d

down:
	docker-compose down

build:
	docker-compose build

logs:
	docker-compose logs -f --tail=100

restart:
	docker-compose down
	docker-compose up -d

clean:
	docker-compose down -v --remove-orphans
	docker system prune -f

postgres:
	docker-compose up -d postgres

kafka:
	docker-compose build kafka

kafka-up:
	docker-compose up -d kafka

kafka-down:
	docker-compose stop kafka

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

appointment-service:
	docker-compose build appointment-service

appointment-service-up:
	docker-compose up -d appointment-service

appointment-service-down:
	docker-compose stop appointment-service

medicalrecordservice:
	docker-compose build medicalrecord-service

medicalrecordservice-up:
	docker-compose up -d medicalrecord-service

medicalrecordservice-down:
	docker-compose stop medicalrecord-service

doctorservice:
	docker-compose build doctor-service

doctorservice-up:
	docker-compose up -d doctor-service

doctorservice-down:
	docker-compose stop doctor-service

billingservice:
	docker-compose build billing-service

billingservice-up:
	docker-compose up -d billing-service

billingservice-down:
	docker-compose stop billing-service

prescriptionservice:
	docker-compose build prescription-service

prescriptionservice-up:
	docker-compose up -d prescription-service

prescriptionservice-down:
	docker-compose stop prescription-service

auth-service:
	docker-compose build auth-service

auth-service-up:
	docker-compose up -d auth-service

auth-service-down:
	docker-compose stop auth-service

notificationservice:
	docker-compose build notification-service

notificationservice-up:
	docker-compose up -d notification-service

notificationservice-down:
	docker-compose stop notification-service

