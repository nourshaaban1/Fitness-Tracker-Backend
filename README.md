# 🏋️ Fitness Tracker Backend

A **Spring Boot + PostgreSQL** backend for a fitness tracking app.  
Supports users, workouts, health metrics, goals, social interactions, and offline sync.  

---

## 🚀 Features
- **User Management**
  - Registration & login
  - Roles (`USER`, `ADMIN`)
  - Profile & preferences
- **Workouts & Exercises**
  - Predefined & custom workouts
  - Exercise categories (`strength`, `cardio`, `flexibility`, `yoga`, `other`)
  - Workout logs with sync support
- **Health Metrics**
  - Weight, height, BMI
  - Body measurements (JSON)
- **Goals Tracking**
  - Track progress toward health/fitness goals
- **Social Features**
  - Posts, comments, likes
  - Admin announcements
- **Offline Sync**
  - `sync_status` field for pending / synced / failed data

---

## 🛠 Tech Stack
- **Backend:** Spring Boot 3, Spring Data JPA, Lombok
- **Database:** PostgreSQL (with UUID primary keys & JSONB support)
- **Auth:** Spring Security (to be added)
- **Build Tool:** Maven
---

## 🔗 Frontend
This repository contains the backend API only. The corresponding frontend (React) repository is available here:

https://github.com/hen1133/FitnessTracker

Clone or browse that repo to run the web client that pairs with this backend.

---

## 🏁 Quick start (Backend)
1. Install prerequisites:
   - Java 17 or later
   - Maven (or use the included `mvnw` / `mvnw.cmd` wrapper)
   - PostgreSQL database

2. Create a PostgreSQL database and user. Example:
   - Database: fitness_tracker
   - User: fitness_user
   - Password: secret

3. Configure the connection in `src/main/resources/application.yml` or via environment variables. Example properties:
   spring.datasource.url: jdbc:postgresql://localhost:5432/fitness_tracker
   spring.datasource.username: fitness_user
   spring.datasource.password: secret

4. Run the app (Windows):
   mvnw.cmd spring-boot:run

   Or build and run the jar:
   mvnw.cmd -DskipTests package ; java -jar target/fitness_tracker-0.0.1-SNAPSHOT.jar

5. API docs / Postman:
   - Postman collections are included in the repo root: `FitnessTrackerFinalVersion.postman_collection.json` and `Fitness Tracker.postman_collection.json`.

---

## 🗄 Database Schema (PostgreSQL)

All tables use **UUID primary keys** and follow audit fields:
- `created_at`
- `updated_at`
- `deleted_at` (soft delete support)

### Main Tables
- `users`
- `exercises`
- `workouts`
- `workout_exercises`
- `workout_logs`
- `health_metrics`
- `goals`
- `posts`
- `comments`
- `post_likes`
- `announcements`

### Enum Types
- `role_enum` → `user`, `admin`
- `category_enum` → `strength`, `cardio`, `flexibility`, `yoga`, `other`
- `sync_status_enum` → `pending`, `synced`, `failed`
- `tracking_mode_enum` → `reps`, `time`, `both`
- `goal_status_enum` → `active`, `completed`, `expired`
- `nutrition_source_enum` → `usda`, `openfoodfacts`, `custom`

---

## 🧪 Tests
Unit and integration tests are defined under `src/test/java` and can be run with:

mvnw.cmd test

(Adjust command for non-Windows shells by using `./mvnw`.)

---

## 🛠 Development notes
- The frontend expects the backend API to run at the address configured in the frontend repo. If running both locally, start the backend first and then the frontend.
- See Postman collections for sample requests and auth flows.


