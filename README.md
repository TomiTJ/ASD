# Bank Admin Application

A comprehensive Spring Boot banking administration system with user management, customer accounts, transactions, audit logging, and loan management.

## 👥 Team & Features

- **User & Role Management** - Lesandu
- **Customer and Account Management** - Krish
- **Transactions** - Brandon
- **Audit & Reporting** - Tomi
- **Loan & Credit** - Joel

---

## 🚀 Deployment

**Live Application:** `https://bank-admin-app.azurewebsites.net` *(Update with your actual URL)*

See [AZURE_DEPLOYMENT.md](AZURE_DEPLOYMENT.md) for complete deployment guide.

Quick start: [DEPLOYMENT_QUICK_START.md](DEPLOYMENT_QUICK_START.md)

---

## 💻 Local Development

### Prerequisites

- **Java 17+** (Temurin/Adoptium)
  - macOS: `brew install --cask temurin`
  - Ubuntu: `sudo apt-get install -y temurin-17-jdk`
  - Windows: Install Temurin 17 MSI

- **Maven 3.9+**
  - macOS: `brew install maven`
  - Ubuntu: `sudo apt-get install -y maven`
  - Windows: `choco install maven`

- **PostgreSQL 14+**
  - macOS: Postgres.app or `brew install postgresql@14`
  - Ubuntu: `sudo apt-get install -y postgresql`
  - Windows: Installer from postgresql.org

### Setup & Run

1. **Create PostgreSQL Database**
   ```sql
   CREATE DATABASE bankdatabase;
   CREATE USER bankadmin WITH PASSWORD '123';
   GRANT ALL PRIVILEGES ON DATABASE bankdatabase TO bankadmin;
   ```

2. **Start PostgreSQL**
   ```bash
   # macOS/Linux
   pg_ctl -D /usr/local/var/postgres start
   
   # Or use GUI tool
   ```

3. **Configure Application**
   - Open `src/main/resources/application.properties`
   - Update database name, username, and password if needed
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/bankdatabase
   spring.datasource.username=bankadmin
   spring.datasource.password=123
   ```

4. **Run Application**
   ```bash
   # Option 1: Maven
   mvn spring-boot:run
   
   # Option 2: IntelliJ IDEA
   # Open project → Click Run button
   
   # Option 3: Build and run JAR
   mvn clean package
   java -jar target/BankAdmin-0.0.1-SNAPSHOT.jar
   ```

5. **Access Application**
   - URL: `http://localhost:8080/login`
   - Default credentials:
     - Email: `admin@bank.local`
     - Password: `password`

---

## 🧪 Testing

```bash
# Run all tests
mvn test

# Run specific test
mvn test -Dtest=AuthControllerTests

# Run with coverage
mvn clean test jacoco:report
```

---

## 📦 Project Structure

```
src/
├── main/
│   ├── java/com/asd/
│   │   ├── config/          # Security configuration
│   │   ├── controller/      # REST & Page controllers
│   │   ├── dto/             # Data transfer objects
│   │   ├── model/           # Entity models
│   │   ├── repository/      # Data access layer
│   │   └── services/        # Business logic
│   └── resources/
│       ├── static/
│       │   ├── css/         # Stylesheets
│       │   └── icons/       # SVG icons
│       ├── templates/       # Thymeleaf HTML templates
│       ├── application.properties
│       └── data.sql         # Database seed data
└── test/                    # Unit & integration tests
```

---

## 🔑 Default User Credentials

| Email | Password | Role | Status |
|-------|----------|------|--------|
| admin@bank.local | password | ADMIN | Active |
| viewer@bank.local | password | READ_ONLY | Active |
| lesandu@gmail.com | password | ADMIN | Active |

---

## 🛠️ Technology Stack

- **Backend:** Spring Boot 3.5.4
- **Language:** Java 17
- **Database:** PostgreSQL 14+
- **ORM:** Hibernate/JPA
- **Security:** Spring Security with BCrypt
- **Template Engine:** Thymeleaf
- **Build Tool:** Maven
- **CI/CD:** Azure DevOps Pipelines
- **Deployment:** Azure App Service + PostgreSQL

---

## 📊 CI/CD Pipeline

Our Azure DevOps pipeline automatically:
1. ✅ Builds the application
2. ✅ Runs all tests
3. ✅ Deploys to Azure App Service
4. ✅ Runs on every push to `master` branch

Pipeline status: *(Add your pipeline badge here)*

---

## 🔒 Security Features

- BCrypt password hashing
- Session-based authentication
- Role-based access control (RBAC)
- CSRF protection
- SQL injection prevention (JPA)
- Audit logging for all actions

---

## 📝 API Endpoints

### Authentication
- `POST /login` - User login
- `POST /logout` - User logout

### Users (Admin only)
- `GET /users` - List all users
- `GET /users/create` - Create user form
- `POST /users/create` - Create new user
- `GET /users/edit/{id}` - Edit user form
- `POST /users/edit/{id}` - Update user
- `POST /users/delete/{id}` - Deactivate user

### Dashboard
- `GET /dashboard` - Main dashboard
- `GET /api/dashboard/metrics` - Dashboard metrics API

### Transactions
- `GET /transactions` - Transaction list
- `GET /api/transactions` - Transactions API

### Audit
- `GET /audit` - Audit log viewer

---

## 🐛 Troubleshooting

### Database Connection Issues
```bash
# Check PostgreSQL is running
pg_isready

# Check connection
psql -U bankadmin -d bankdatabase
```

### Port Already in Use
```bash
# Find process on port 8080
lsof -i :8080

# Kill process
kill -9 <PID>
```

### Build Failures
```bash
# Clean and rebuild
mvn clean install

# Skip tests
mvn clean install -DskipTests
```

---

## 📚 Documentation

- [Azure Deployment Guide](AZURE_DEPLOYMENT.md)
- [Quick Start Guide](DEPLOYMENT_QUICK_START.md)
- [API Documentation](docs/API.md) *(if you create one)*
- [Architecture Diagram](docs/architecture.png) *(if you create one)*

---

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Run tests: `mvn test`
4. Push and create Pull Request
5. Wait for pipeline to pass

---

## 📄 License

This project is for educational purposes as part of an academic assignment.

---

## 👨‍💻 Development Team

- Lesandu Perera
- Krish
- Brandon
- Tomi
- Joel

**Institution:** *(Your University)*  
**Course:** *(Your Course Code)*  
**Semester:** *(Current Semester)*

---

## 📞 Support

For issues or questions:
- Create an issue in the repository
- Contact team members
- Refer to documentation

---

**Built with ❤️ using Spring Boot**
