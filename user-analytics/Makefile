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
