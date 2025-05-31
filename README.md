# 🏋️ Workout API

Workout API — is a simple API for managing your workouts and goals. Built using Spring Boot and oriented on personal using or for implementing in the bigger systems.

---

## 🚀 Functions

- To login and to sing up new users (using JWT)
- CRUD operations for your workouts
- Goals managing
- Authorization to secure all endpoints
- You are the only one who could access your personal data

---

## ⚙️ Technologies

- Java 21
- Spring Boot 3.5
- Spring Security (JWT)
- Spring Data JPA
- PostgreSQL
- Gradle

---
## 🛠️ Project Structure

```
org.api.workout
├── advice               #Global Exception Handler (@ExceptionHandler(Exception.class))
├── controllers          #REST Controllers
├── dto                  #Data Tranfer Objects
├── entities             #JPA Entities
├── exceptions           #Custom Exceptions
├── repositories         #JPA Data interfaces (Spring Data JPA)
├── security             #Security Configs, Filters and Custom User Details
├── services             #Buisness Logic Classes
└── WorkoutApplication.java   #Application Entrypoint
```

---
## 🗄️ Database Tables
Source file : `src/main/resources/static/sql/init.sql`

### 🧍‍♂️ users
```
+------------+--------------+------------------------------+
| Column     | Type         | Description                  |
+------------+--------------+------------------------------+
| id         | BIGSERIAL    | Primary key                  |
| username   | VARCHAR(15)  | Unique, not null             |
| pass_hash  | VARCHAR      | Hashed password              |
| created_at | TIMESTAMPTZ  | Account creation timestamp   |
| last_login | TIMESTAMPTZ  | Last login timestamp         |
+------------+--------------+------------------------------+
```


### 🏋️ workouts
```
+------------+--------------+------------------------------+
| Column     | Type         | Description                  |
+------------+--------------+------------------------------+
| id         | BIGSERIAL    | Primary key                  |
| author_id  | BIGSERIAL    | FK → users(id), not null     |
| date       | TIMESTAMPTZ  | Workout date                 |
| is_done    | BOOLEAN      | Completion status            |
| type       | VARCHAR      | Optional workout type        |
| duration   | INT          | Optional duration (minutes)  |
| created_at | TIMESTAMPTZ  | Record creation time         |
+------------+--------------+------------------------------+
```


### 🎯 goals
```
+------------+--------------+------------------------------+
| Column     | Type         | Description                  |
+------------+--------------+------------------------------+
| id         | BIGSERIAL    | Primary key                  |
| author_id  | BIGSERIAL    | FK → users(id), not null     |
| text       | TEXT         | Goal description             |
| is_done    | BOOLEAN      | Completion status            |
| deadline   | TIMESTAMPTZ  | Optional deadline            |
| created_at | TIMESTAMPTZ  | Goal creation time           |
+------------+--------------+------------------------------+
```


### 🛡️ user_roles
```
+------------+--------------+------------------------------+
| Column     | Type         | Description                  |
+------------+--------------+------------------------------+
| id         | BIGSERIAL    | Primary key                  |
| author_id  | BIGSERIAL    | FK → users(id), not null     |
| role       | VARCHAR(10)  | User role (e.g., ADMIN)      |
| created_at | TIMESTAMPTZ  | Record creation time         |
+------------+--------------+------------------------------+
```

---

## 🌱 Environment Variables Needed
```
DB_USER: username
DB_PASS: password
DB_URL: jdbc:postgresqlP://database_host:port/database_name
```

---
## 🌐 Endpoints
if an exception occurred, API returns the JSON object

```json
{
  "error": "http_res_name",
  "message": "err_message"
}
```

- <h3>POST `api/users/register`, Without authorization ✅</h3>
Controller for creating new users.<br>
Accepts a JSON body with user information: 
```json
{
  "username": "username", 
  "password": "password"
} 
```
Return id of the new user and token if successful:

<h4 style="color: green;">201 CREATED</h4>

```json
{
  "userId": 0,
  "token": "new_token"
}
```
Username must be unique (<b style="color: red;">409</b>).

- <h3>POST `api/users/login`, Without authorization ✅</h3>

Controller for authorize in an existing account.<br>
  Accepts a JSON body with user information:
```json
{
  "username": "username", 
  "password": "password"
} 
```
Return the token if successful:

<h4 style="color: green;">200 OK</h4>

```json
{
  "token": "new_token"
}
```
Username must exist (<b style="color: red;">404</b>), and passwords match (<b style="color: red;">400</b>)

- <h3>GET `api/users/{id}`, Without authorization ❌</h3>

Controller for getting information about user by its `id` field.<br>
Accepts no params <br>
Return the user's JSON object if successful:

<h4 style="color: green;"> 200 OK</h4>

```json
{
  "id": 0,
  "username": "username",
  "createdAt": "2025-05-31T14:30:00.123457",
  "workouts": [
    {
      "id": 0,
      "authorId": 0,
      "date": "2025-06-31T14:30:00.123457",
      "isDone": false,
      "type": "STRENGTH",
      "duration": 60,
      "createdAt": "2025-05-31T15:30:00.123457"
    }
  ],
  "goals": [
    {
      "id": 0,
      "authorId": 0,
      "text": "my goal",
      "isDone": true,
      "deadline": "2025-05-31T15:00:00.123457",
      "createdAt": "2025-05-31T14:30:00.123457"
    }
  ]
}
```

{id} must exist (<b style="color: red;">404</b>) and be your user id, or you must have the role "ROLE_ADMIN" (<b style="color: red;">403</b>)

- <h3>GET `api/workouts`, Without authorization ❌</h3>
Controller for getting a list of your workout by filter.<br>
Workout type enum `WorkoutTypes.java`:
```java
package org.api.workout.entities.workout;

public enum WorkoutType {
    STRENGTH,
    CARDIO,
    GYMNASTICS,
    TRX,
    YOGA,
    OTHER
}
```
Accepts params:

```json
{
  "authorId": 0,
  "isDone": false,
  "dateFrom": "2025-05-30T14:30:00.123457",
  "dateTo": "2025-05-31T15:30:00.123457",
  "type": "STRENGTH" 
}
```

Return a list of workout JSON objects if success:

<h4 style="color: green;"> 200 OK</h4>

```json
[
  {
    "id": 0, 
    "authorId": 0, 
    "date": "2025-06-31T14:30:00.123457", 
    "isDone": false, 
    "type": "STRENGTH", 
    "duration": 60, 
    "createdAt": "2025-05-31T15:30:00.123457",
  }
]
```
You could have any param to `null` except `authorId` to not have any filters, 
this must be your account id if you do not have `ROLE_ADMIN`(<b style="color: red;">403</b>),
`type` must be one from the `WorkoutTypes.java`(<b style="color: red;">400</b>)
and you must specify neither `dateFrom` nor `dateTo` or both of them (<b style="color: red;">400</b>)

- <h3>POST `api/workouts`, Without authorization ❌</h3>

Controller for creating new workout.<br>
Accepts JSON body with information of new workout:

```json
{
  "date": "2025-06-31T14:30:00.123457",
  "type": "STRENGTH",
  "duration": 60 //could be null
}
```

Return a workout id if success:

<h4 style="color: green;"> 201 CREATED</h4>

```
0
```
`date` and `type` must be specified
and `duration` must be or equal `15` (<b style="color: red"> 400 </b>)

- <h3>GET `api/workouts/{id}`, Without authorization ❌</h3>
Controller for getting information about workout by id.<br>
Accept no params.<br>
Return JSON object of workout:
<h4 style="color: green;"> 200 OK</h4>

```json
{
  "id": 0,
  "authorId": 0,
  "date": "2025-06-31T14:30:00.123457",
  "isDone": false,
  "type": "STRENGTH",
  "duration": 60,
  "createdAt": "2025-05-31T15:30:00.123457"
}
```

You must create the workout you are trying to get (<b style="color: red;">403</b>)

- <h3>PUT `api/workouts/{id}`, Without authorization ❌</h3>
Controller for update existing workout by id.<br>
Accept JSON body:

```json
{
  "date": "2025-06-31T14:30:00.123457",
  "isDone": false,
  "type": "STRENGTH",
  "duration": 60
}
```
Return JSON object of the updated workout:

<h4 style="color: green;"> 200 OK</h4>

```json
{
  "id": 0,
  "authorId": 0,
  "date": "2025-06-31T14:30:00.123457",
  "isDone": false,
  "type": "STRENGTH",
  "duration": 60,
  "createdAt": "2025-05-31T15:30:00.123457"
}
```
You must create the workout you are trying to get (<b style="color: red;">403</b>).
You could specify any field as null. `duration` must be more or equal `15` (<b style="color: red;">400</b>).

- <h3>DELETE `api/workouts/{id}`, Without authorization ❌</h3>
Controller for deleting workout by id.<br>
Accepts no params.
Return empty string

<h4 style="color: green;"> 200 OK</h4>

```
```

You must create the workout you are trying to get (<b style="color: red;">403</b>).

- <h3>GET `api/goals`, Without authorization ❌</h3>
Controller for getting a list of goals by filter.<br>
Accepts params:

```json
{
  "authorId": "0",
  "isDone": false,
  "deadlineDateFrom": "2025-05-30T14:30:00.123457",
  "deadlineDateTo": "2025-05-31T15:30:00.123457",
  "createdAtFrom": "2025-05-30T14:30:00.123457",
  "createdAtTo": "2025-05-31T15:30:00.123457"
}
```
Return a list of goal JSON objects if successful:
<h4 style="color: green;"> 200 OK</h4>

```json
[
  {
    "id": 0,
    "authorId": 0,
    "text": "my goal",
    "isDone": false,
    "deadline": "2025-05-31T15:00:00.123457",
    "createdAt": "2025-05-31T14:30:00.123457"
  }
]
```
You can have any param as `null` to not have filters. If specified, `authorId` must be your account id unless you have `ROLE_ADMIN` (<span style="color: red">**403**</span>). You must specify both `deadlineDateFrom` and `deadlineDateTo` or neither (<span style="color: red">**400**</span>). The same applies for `createdAtFrom` and `createdAtTo`.

- ### GET `api/goals/{id}`, Without authorization ❌
Controller for getting information about a goal by id.<br>
Accepts no params.<br>
Returns JSON object of the goal:

<h4 style="color: green;"> 200 OK</h4>

```json
{
  "id": 0,
  "authorId": 0,
  "text": "my goal",
  "isDone": false,
  "deadline": "2025-05-31T15:00:00.123457",
  "createdAt": "2025-05-31T14:30:00.123457"
}
```
You must be the creator of the goal or have role `ROLE_ADMIN` to access it (<span style="color: red">**403**</span>).

- ### POST `api/goals`, Without authorization ❌
Controller for creating a new goal.
Accepts JSON body with information of the new goal:
```json
{
  "text": "my new goal",
  "deadline": "2025-05-31T15:00:00.123457" // optional
}
```
Returns the goal id if successful:
<h4 style="color: green;"> 201 CREATED</h4>

``` 
0
```
The `text` field must be specified (<span style="color: red">**400**</span>).
- ### PUT `api/goals/{id}`, Without authorization ❌
Controller for updating the existing goal by id.
Accepts JSON body:
```json
{
  "text": "updated goal text",
  "isDone": true,
  "deadline": "2025-05-31T15:00:00.123457"
}
```
Returns JSON object of the updated goal:
<h4 style="color: green;"> 200 OK</h4>

```json
{
  "id": 0,
  "authorId": 0,
  "text": "updated goal text",
  "isDone": true,
  "deadline": "2025-05-31T15:00:00.123457",
  "createdAt": "2025-05-31T14:30:00.123457"
}
```
You must be the creator of the goal or have role `ROLE_ADMIN` to update it (<span style="color: red">**403**</span>). All fields are optional.
- ### DELETE `api/goals/{id}`, Without authorization ❌
Controller for deleting goal by id.
Accepts no params.
Returns an empty response:
<h4 style="color: green;"> 200 OK</h4>

```
```

You must be the creator of the goal or have role `ROLE_ADMIN` to delete it (<span style="color: red">**403**</span>). 

- <h3>GET `api/stats/workouts`, Without authorization ❌</h3>
Controller for getting workout statistics for the authenticated user.<br>
Accepts no params.<br>
Returns JSON object containing workout statistics:
<h4 style="color: green;"> 200 OK</h4>

```json 
{ 
  "authorId": 0, 
  "totalWorkouts": 10, 
  "completedWorkouts": 5, 
  "allByType": 
  { 
    "strengthWorkouts": 3, 
    "cardioWorkouts": 2, 
    "gymnasticsWorkouts": 1, 
    "trxWorkouts": 2, 
    "yogaWorkouts": 1, 
    "otherWorkouts": 1
  }, 
  "completedByType": 
  { 
    "strengthWorkouts": 2, 
    "cardioWorkouts": 1, 
    "gymnasticsWorkouts": 0, 
    "trxWorkouts": 1, 
    "yogaWorkouts": 1, 
    "otherWorkouts": 0 }, 
  "lastWorkout": 
  { 
    "id": 0,
    "authorId": 0,
    "date": "2025-05-31T14:30:00.123457",
    "isDone": true,
    "type": "STRENGTH",
    "duration": 60,
    "createdAt": "2025-05-31T14:30:00.123457"
  },
  "nextWorkout": 
  { 
    "id": 1, 
    "authorId": 0,
    "date": "2025-06-01T14:30:00.123457", 
    "isDone": false, 
    "type": "CARDIO", 
    "duration": 45, 
    "createdAt": "2025-05-31T14:30:00.123457",
  }
}
``` 
`lastWorkout` and `nextWorkout` can be `null` if there are no completed workouts or upcoming workouts respectively.

- <h3>GET `api/stats/goals`, Without authorization ❌</h3>
Controller for getting goal statistics for the authenticated user.<br>
Accepts no params.<br>
Returns JSON object containing goal statistics:
<h4 style="color: green;"> 200 OK</h4>

```json 
{ 
  "authorId": 0, 
  "totalGoals": 5, 
  "completedGoals": 2, 
  "nearestGoal": 
  { 
    "id": 0, 
    "authorId": 0, 
    "text": "my goal", 
    "isDone": false, 
    "deadline": "2025-05-31T15:00:00.123457", 
    "createdAt": "2025-05-31T14:30:00.123457"
  }
}
``` 
`nearestGoal` can be `null` if there are no upcoming goals with deadlines.

---

