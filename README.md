# 🏦 Midas - Financial Transaction System

### JPMorgan Chase & Co. | Software Engineering Virtual Experience (via Forage)

**Midas** is a distributed banking system designed to process high-volume financial transactions in real-time. This project was developed as part of the JPMorgan Chase Virtual Internship to demonstrate backend engineering skills using **Java Spring Boot, Apache Kafka, and Docker.**

---

## 🛠 Tech Stack & Tools
* **Core Framework:** Java 17, Spring Boot 3
* **Messaging Queue:** Apache Kafka (for asynchronous communication)
* **Database:** H2 Database (In-memory SQL), Spring Data JPA
* **Containerization:** Docker & Docker Compose
* **API Integration:** REST APIs (RestTemplate)
* **Build Tool:** Maven

---

## 🚀 Implementation Journey (How I Built This)

This project was built in 5 major phases, simulating a real-world enterprise environment:

### 1. Environment Setup & Infrastructure
* Configured the local development environment using **Docker Desktop** to containerize Kafka and Zookeeper services.
* Managed dependencies using **Maven** (`pom.xml`) to include Spring Kafka, JPA, and H2 drivers.

### 2. Real-time Messaging with Kafka
* Implemented a **Kafka Consumer** in Midas Core to listen to the `financial-transactions` topic.
* Decoupled the frontend and backend to handle high-throughput transaction streams asynchronously.

### 3. Database Design & Validation Logic
* Designed the database schema using **Spring Data JPA** entities (`UserRecord`, `TransactionRecord`).
* Implemented business logic to strictly validate transactions (e.g., ensuring the sender has sufficient balance) before processing.
* Ensured ACID compliance by updating sender/recipient balances atomically.

### 4. External API Integration (Incentives)
* Integrated an external **Incentive REST API** to fetch bonus rewards for eligible transactions.
* Used `RestTemplate` to make synchronous calls to the incentive service and updated transaction records dynamically.

### 5. Exposing Data via REST API
* Developed a `BalanceController` to expose a GET endpoint (`/balance`).
* Allowed users/admins to query real-time account balances via HTTP requests on a specific port (`33400`).

---

## ⚙️ How to Run Locally

**Prerequisites:**
* Java 17 SDK
* Docker Desktop (Running)

**Steps:**

1.  **Start Infrastructure:**
    Navigate to the project directory and spin up the Docker containers (Kafka/Zookeeper):
    ```bash
    cd services
    docker-compose up -d
    ```

2.  **Start the Incentive API:**
    Run the external incentive service jar:
    ```bash
    java -jar services/transaction-incentive-api.jar
    ```

3.  **Run Midas Core:**
    Run the Spring Boot application via IntelliJ IDEA or command line:
    ```bash
    ./mvnw spring-boot:run
    ```

---

## 🧪 Testing
The project includes a comprehensive Test Suite (`TaskOneTests` to `TaskFiveTests`) using **Embedded Kafka** and **JUnit** to verify:
* Application Context loading.
* Kafka message consumption.
* Database transaction integrity.
* API integration and logic verification.

---

### 🏆 Key Takeaways
Working on Midas provided hands-on experience with **Event-Driven Architecture**. I learned how to bridge Microservices using Kafka, handle data consistency in banking systems, and manage complex dependency injections in Spring Boot.
