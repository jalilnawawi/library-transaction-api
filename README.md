# 🚀 Backend Application – Spring Boot & Docker

Backend application built with **Java Spring Boot** and containerized using **Docker & Docker Compose**.  
This project demonstrates clean environment configuration, container orchestration, and best practices for secret management.

---

## 🛠 Tech Stack

- Java 21+
- Spring Boot
- PostgreSQL
- Docker & Docker Compose
- Maven

---

## 📦 Prerequisites

Make sure you have installed:

- **Docker** (>= 20.x)
- **Docker Compose** (v2)
- **Git**

---

## ⚙️ Environment Configuration

This project uses **environment variables** for configuration.  
Sensitive values are **not committed** to the repository.

### Step 1 — Create `.env` file

Copy the environment template:

```bash
cp .env.example .env
```

### Step 2 — Fill in required variables

Edit `.env` according to your local setup:

```env
SPRING_DATASOURCE_URL=jdbc:postgresql://your-db-host/your-db-name
SPRING_DATASOURCE_USERNAME=your_db_username
SPRING_DATASOURCE_PASSWORD=your_db_password
```

> `.env.example` is provided as a reference.  
> `.env` is intentionally excluded from version control for security reasons.

---

## ▶️ How to Run the Application

⚠️ Make sure the `.env` file is created and properly configured before running Docker Compose.

Start all services using Docker Compose:

```bash
docker compose up -d
```

The application will be available at:

```text
http://localhost:8080
```

API documentation can be accessed at:

```text
http://localhost:8080/swagger-ui/index.html
```

To stop the application:

```bash
docker compose down
```

---

## 🧪 (Optional) Running Without Docker

If you want to run the application locally without Docker:

```bash
./mvnw spring-boot:run
```

Make sure the required environment variables are set in your system.

---

## 🔐 Security Notes

- Sensitive credentials are **never hardcoded**
- Secrets are managed using environment variables
- `.env` files are excluded from version control

This setup follows common **industry best practices** for application configuration and security.

---

## 👤 Author

**Abdul Jalil Nawawi**  
Backend Developer

🌐 Portfolio: https://jalilnawawi.netlify.app/

---

## 📝 Notes for Reviewer

This repository is intended for **technical assessment / submission** purposes.

The application requires a `.env` file for configuration.  
Please refer to `.env.example` for the required environment variables before running the application.

Docker is used to ensure a consistent runtime environment across different machines.
