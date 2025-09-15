# Banking App (Spring Boot)

A simple banking REST API built with Spring Boot 3, exposing endpoints to manage customers and accounts, perform deposits/withdrawals, and transfer funds between accounts. Uses Spring Data JPA with PostgreSQL.

## Tech Stack
- Java 17
- Spring Boot 3 (Web, Data JPA)
- PostgreSQL
- Jackson (JSR-310 module)
- Maven

## Project Structure
```
Banking/
  pom.xml
  src/main/java/com/example/Banking/
    BankingApplication.java
    config/CorsConfig.java
    Controller/
      AccountController.java
      CustomerController.java
      TransactionController.java
    dto/
      ApiResponse.java
      AccountRequest.java
      TransferRequest.java
    exception/
      ApiExceptionHandler.java
      InsufficientFoundException.java
      ResourceNotFoundException.java
    model/
      Account.java
      Customer.java
      TransactionRecord.java
    repository/
      AccountRepository.java
      CustomerRepository.java
      TransactionRepository.java
    service/
      BankingService.java
      CustomerService.java
      TransactionService.java
  src/main/resources/
    application.properties
    schema.sql (optional)
    data.sql (optional)
```

## Domain Model
- `Customer`: id, name, email (unique)
- `Account`: id, accountNumber (unique), customer (ManyToOne), balance
- `TransactionRecord`: id, fromAccount, toAccount, amount, type (DEPOSIT/WITHDRAW/TRANSFER), timestamp

## DTOs
- `ApiResponse { message: string, data: any }`
- `AccountRequest { customerId: number, initialDeposit: number }`
- `TransferRequest { fromAccount: string, toAccount: string, amount: number }`

## Services
- `CustomerService`: create/list/get customers
- `BankingService`: create accounts, get/list accounts, deposit, withdraw, transfer (transactional, validation, records)
- `TransactionService`: list/get transaction records

## Controllers and Endpoints
Base URL: `http://localhost:8080`

### Customers (`/api/customers`)
- `POST /api/customers?name={name}&email={email}`
  - Creates a customer.
  - Response: `ApiResponse` with created `Customer`.
- `GET /api/customers`
  - Lists all customers.
- `GET /api/customers/{id}`
  - Fetch a single customer by id.

### Accounts (`/api/accounts`)
- `POST /api/accounts/customers?name={name}&email={email}`
  - Also creates a customer (via `BankingService`).
- `POST /api/accounts/accounts`
  - Body: `AccountRequest`
  - Creates an account for a customer, optionally with initial deposit.
- `GET /api/accounts/accounts`
  - Lists all accounts.
- `GET /api/accounts/accounts/{id}`
  - Fetch a single account by id.
- `POST /api/accounts/accounts/{accountNumber}/deposit?amount={amount}`
  - Deposit into an account.
- `POST /api/accounts/accounts/{accountNumber}/withdraw?amount={amount}`
  - Withdraw from an account.
- `POST /api/accounts/transfer`
  - Body: `TransferRequest`
  - Transfer funds between accounts.

### Transactions (`/api/transactions`)
- `GET /api/transactions`
  - Lists transactions.
- `GET /api/transactions/{id}`
  - Fetch a transaction by id.

## Error Handling
Global exception handling via `ApiExceptionHandler`:
- `ResourceNotFoundException` -> 404
- `InsufficientFoundException`, `IllegalArgumentException` -> 400
- Generic exceptions -> 500 with message

## Configuration
Edit `src/main/resources/application.properties` to match your PostgreSQL setup:
```
spring.datasource.url=jdbc:postgresql://localhost:5432/banking_bd
spring.datasource.username=postgres
spring.datasource.password=renu
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
server.port=8080
```

If you prefer schema/data scripts, you can place `schema.sql` and `data.sql` in `src/main/resources` and enable Spring SQL init if needed.

## Running Locally
Prerequisites:
- JDK 17+
- Maven 3.9+
- PostgreSQL running and reachable

Steps:
1. Create a database (e.g., `banking_bd`).
2. Update `application.properties` credentials.
3. From the `Banking` directory, run:
   - Unix/macOS: `./mvnw spring-boot:run`
   - Windows: `mvnw.cmd spring-boot:run`

The app starts on `http://localhost:8080`.

## Example Requests
Using curl:
```bash
# Create customer
curl -X POST "http://localhost:8080/api/customers?name=Alice&email=alice@example.com"

# Create account
curl -X POST "http://localhost:8080/api/accounts/accounts" \
  -H "Content-Type: application/json" \
  -d '{"customerId":1, "initialDeposit":1000}'

# Deposit
curl -X POST "http://localhost:8080/api/accounts/accounts/{ACCOUNT_NUMBER}/deposit?amount=250"

# Withdraw
curl -X POST "http://localhost:8080/api/accounts/accounts/{ACCOUNT_NUMBER}/withdraw?amount=100"

# Transfer
curl -X POST "http://localhost:8080/api/accounts/transfer" \
  -H "Content-Type: application/json" \
  -d '{"fromAccount":"AAAA...","toAccount":"BBBB...","amount":50}'

# List transactions
curl "http://localhost:8080/api/transactions"
```

## CORS
`config/CorsConfig.java` configures Cross-Origin Resource Sharing for web clients.

## Build Artifact
Create a runnable jar:
```bash
mvn clean package
```
Run the jar:
```bash
java -jar target/banking-app-1.0.0.jar
```

## Testing
Run tests:
```bash
mvn test
```

## Notes
- Account numbers are generated as 16-char uppercase strings.
- Money values use `BigDecimal`.
- Transfers and balance updates are transactional.

## License
This project is provided as-is; add a license of your choice if publishing publicly.
