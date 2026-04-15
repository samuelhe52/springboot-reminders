BACKEND_DIR := backend
FRONTEND_DIR := frontend

.PHONY: help install build build-backend build-frontend test clean format format-frontend db-reset backend frontend

help:
	@echo "Available targets:"
	@echo "  make install         - install frontend dependencies"
	@echo "  make build           - build backend and frontend"
	@echo "  make build-backend   - build backend jar"
	@echo "  make build-frontend  - build frontend assets"
	@echo "  make test            - run backend tests"
	@echo "  make format          - format frontend files"
	@echo "  make db-reset        - recreate MySQL container and data volume"
	@echo "  make backend         - start database and run Spring Boot"
	@echo "  make frontend        - start Vite dev server"
	@echo "  make clean           - remove build output"

install:
	cd $(FRONTEND_DIR) && npm install

build: build-backend build-frontend

build-backend:
	cd $(BACKEND_DIR) && mvn clean package

build-frontend:
	cd $(FRONTEND_DIR) && npm run build

test:
	cd $(BACKEND_DIR) && mvn test

clean:
	rm -rf $(BACKEND_DIR)/target $(FRONTEND_DIR)/dist

format:
	cd $(FRONTEND_DIR) && npx prettier --write "src/**/*.{js,vue}" index.html vite.config.js

db-reset:
	docker compose down -v
	docker compose up -d mysql

backend:
	docker compose up -d mysql
	cd $(BACKEND_DIR) && mvn spring-boot:run

frontend:
	cd $(FRONTEND_DIR) && npm run dev
