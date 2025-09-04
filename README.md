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
