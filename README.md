# ✈️ Travel Buddy – Trip Planner (Backend)

## 📌 Overview
**Travel Buddy** is a backend application designed to help users organize trips, manage daily schedules, and track travel expenses efficiently. The system integrates with an external API to enrich user plans with real-time hotel data.

This project focuses on handling complex business logic, relational data modeling, and seamless integration with third-party services.

---

## 🚀 Key Features
✅ **Trip & Itinerary Management** – Create trips, organize daily plans, and schedule activities.  
✅ **Smart Expense Tracking** – Monitor budget and categorization of expenses per trip.  
✅ **External API Integration** – Fetches real-time hotel data using the **Amadeus Places API**.  
✅ **User-Centric Design** – Ensures data privacy so users access only their own travel plans.  
✅ **Relational Data Modeling** – Handles complex dependencies between Trips, Days, Activities, and Expenses.

---

## 🧠 Technologies Used

| Category | Stack |
| :--- | :--- |
| **Language** | Java 17+ |
| **Framework** | Spring Boot 3 (Web, Data JPA) |
| **Database** | PostgreSQL |
| **Integrations** | Amadeus Places API (REST) |
| **Security** | Spring Security (Session-based) |
| **Tools** | Maven, Git, Docker (Basic) |

---

## 🧱 Architecture & Design
The application follows a **layered backend architecture** to ensure modularity and testability:
* **Controller Layer:** Exposes RESTful endpoints.
* **Service Layer:** Contains business logic and external API communication.
* **Repository Layer:** Manages database interactions using Hibernate/JPA.

### 🌍 External API Integration
* Integrated **Amadeus Places API** for fetching accommodation data.
* Implemented data mapping to convert external JSON responses into internal domain entities.
