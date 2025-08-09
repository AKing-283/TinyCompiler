# 🚀 Flask MongoDB CRUD API

A cleanly-architected, scalable RESTful API built with Flask and MongoDB for managing user resources. Built with Docker support and production best practices for rapid deployment, security, and modularity.

---

## 🔧 Features

- ✅ RESTful CRUD endpoints
- ✅ MongoDB integration via PyMongo
- ✅ Data validation using Marshmallow
- ✅ Secure password hashing with Bcrypt
- ✅ Centralized JSON logging
- ✅ Scalable Docker setup with Compose
- ✅ Custom error handling and exceptions
- ✅ CORS support for frontend compatibility

---

## 📦 Prerequisites

- [Docker](https://docs.docker.com/get-docker/)
- [Docker Compose](https://docs.docker.com/compose/)
- Python 3.9+ (only for local non-Docker dev)
- MongoDB (local or containerized)

---

## 🚀 Quick Start (with Docker)

```bash
git clone <your-repo-url>
cd <your-project-folder>
docker-compose up --build
```

- API Base URL: `http://0.0.0.0:5000/users` (or `http://localhost:5000/users`)
- MongoDB URI (Docker): `mongodb://mongodb:27017/user_db`

---

## 📮 API Endpoints

### 🔍 Get All Users
- `GET /users`
- ✅ 200 OK

### 🔍 Get a User by ID
- `GET /users/<id>`
- ✅ 200 OK | ❌ 404 Not Found

### ➕ Create a User
- `POST /users`
- Body:
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secure_password"
}
```
- ✅ 201 Created | ❌ 409 Conflict (duplicate email)

### 🔄 Update a User
- `PUT /users/<id>`
- Partial or full update:
```json
{
  "name": "Updated Name",
  "email": "new@example.com"
}
```
- ✅ 200 OK | ❌ 404 Not Found

### ❌ Delete a User
- `DELETE /users/<id>`
- ✅ 204 No Content | ❌ 404 Not Found

---

## 🧪 Testing with Postman

You can use the API manually or import a Postman collection.

Example requests:

### ✅ Test - Get All Users
- `GET http://0.0.0.0:5000/users`

### ✅ Test - Create User
- `POST http://0.0.0.0:5000/users`
```json
{
  "name": "Alice",
  "email": "alice@example.com",
  "password": "password123"
}
```

### ✅ Test - Get User by ID
- `GET http://0.0.0.0:5000/users/<user_id>`

### ✅ Test - Update User
- `PUT http://0.0.0.0:5000/users/<user_id>`

### ✅ Test - Delete User
- `DELETE http://0.0.0.0:5000/users/<user_id>`

---

## 🗂 Project Structure

```
.
├── app/
│   ├── models/            # MongoDB data models
│   ├── routes/            # API route handlers
│   ├── services/          # Business logic layer
│   ├── utils/             # Logger, exceptions, config
│   └── main.py            # App entry point
├── Dockerfile             # Flask container
├── docker-compose.yml     # Compose for Flask + MongoDB
├── .env                   # Environment variables
├── requirements.txt       # Python dependencies
├── test_api.py            # API test cases
├── test_mongo.py          # Mongo connection test
└── README.md              # Project documentation
```

---

## ⚠️ Error Handling & Status Codes

| Code | Meaning                  |
|------|--------------------------|
| 200  | Success                  |
| 201  | Resource Created         |
| 204  | Deleted Successfully     |
| 400  | Bad Request              |
| 404  | Not Found                |
| 409  | Conflict (duplicate)     |
| 500  | Internal Server Error    |

---

## 🔐 Security

- Passwords hashed via `bcrypt`
- `.env` file for all secrets (excluded from repo)
- Input validation with Marshmallow
- CORS support for frontend integrations

---

## 🧪 Local Development (Optional)

```bash
python -m venv venv
source venv/bin/activate       # Mac/Linux
venv\Scripts\activate        # Windows

pip install -r requirements.txt

# Setup env vars
export MONGODB_URI="mongodb://localhost:27017/userdb"
export JWT_SECRET_KEY="your_secret"

python -m app.main
```

---

## 🤝 Contributing

1. Fork the repo
2. Create a new branch (`git checkout -b feature-xyz`)
3. Make your changes
4. Push the branch (`git push origin feature-xyz`)
5. Create a Pull Request

---

## 📜 License

MIT License

---

## 🌐 Author

Built with ❤️ by [Your Name](https://github.com/your-username)
# TinyCompiler
