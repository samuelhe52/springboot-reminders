# Reminder Assignment

Simple reminder management system for a course assignment.

## Structure

- `backend`: Spring Boot + MyBatis annotations + MySQL + Session
- `frontend`: Vue 3 + Vite + Element Plus

## Included

- User register, login, logout
- Category add, list, update, delete
- Todo add, list, update, delete
- Todo filter by category and status

## Quick Start

### 1) Database

Run [backend/src/main/resources/schema.sql](/Users/samuelhe/assignments/java26/springboot-reminders/backend/src/main/resources/schema.sql) in MySQL.

Then update DB account in [backend/src/main/resources/application.yml](/Users/samuelhe/assignments/java26/springboot-reminders/backend/src/main/resources/application.yml).

### 2) Start backend

```bash
cd backend
mvn spring-boot:run
```

### 3) Start frontend

```bash
cd frontend
npm install
npm run dev
```
