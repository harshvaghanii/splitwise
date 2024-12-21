# **Project Report**

---

## **Project Overview**

### **Project Title**: **Splitwise Clone**

### **Description**:
The project implements a simplified version of Splitwise, a platform for tracking shared expenses and balances among users. It allows users to create accounts, log in securely, record transactions, and track balances with other users. The application is built with modern Java frameworks, including **Spring Boot**, and demonstrates best practices in authentication, authorization, and database management.

---

## **Key Features**

1. **User Management**:
    - Secure user registration and login with JWT-based authentication.
    - Users can update their profiles (secured by authorization checks).

2. **Transaction Management**:
    - Add and track transactions between users.
    - Automatically update balances based on transactions.
    - Maintain accurate balance amounts, where positive balances indicate money owed and negative balances indicate money due.

3. **Balance Management**:
    - Balances are maintained between user pairs using a unique identifier in the format `userID1_userID2` (sorted).
    - Real-time balance updates ensure consistency.

4. **JWT Authentication**:
    - Token-based authentication using JSON Web Tokens (JWT).
    - Secures endpoints and prevents unauthorized access.

5. **Role-Based Authorization**:
    - Ensures users can only access or modify data they own.
    - Protected transactions and balance updates with security checks in place.

---

## **Technologies Used**

### **Backend**:
- **Java 22**: Primary programming language.
- **Spring Boot 3.3.3**: For application configuration and dependency injection
- **Spring Data JPA**: For ORM and database interactions.
- **Spring Security**: For authentication and authorization.
- **JWT (JSON Web Tokens)**: For secure, stateless authentication.
- **ModelMapper**: For entity-to-DTO mapping.

### **Database**:
- **PostgreSQL**: For data persistence.
- **H2 (In-Memory)**: For local development and testing.

### **Tools**:
- **IntelliJ IDEA**: Integrated Development Environment (IDE).
- **Postman**: For API testing.
- **DBeaver**: For database management.
- **GitHub**: Version control and collaboration.

---

## **Architecture Overview**

### **Application Layers**:
1. **Controller Layer**:
    - Handles incoming HTTP requests.
    - Maps requests to service methods.
    - Example: `TransactionController`, `UserController`.

2. **Service Layer**:
    - Contains business logic.
    - Example: `TransactionServiceImpl`, `UserServiceImpl`.

3. **Repository Layer**:
    - Interfaces with the database using JPA.
    - Example: `UserRepository`, `BalanceRepository`.

4. **Entity Layer**:
    - Defines the database schema using `@Entity` annotations.
    - Example: `UserEntity`, `TransactionEntity`, `BalanceEntity`.

5. **Utility Layer**:
    - Contains reusable logic such as balance ID generation (`BalanceUtility`).

### **Authentication Flow**:
- User logs in with credentials.
- Server generates a JWT and returns it to the user.
- Subsequent requests include the JWT in the `Authorization` header.
- Spring Security validates the JWT and authenticates the user.

### **Authorization Flow**:
- Extracts the authenticated user's ID from the JWT.
- Validates that the user has permission to perform the requested action (e.g., modifying their own transactions or balances).

---

## **Database Design**

### **Tables**:
1. **Users Table**:
    - Stores user information such as `id`, `name`, `email`, and `password`.

2. **Transactions Table**:
    - Tracks transactions between users, including `paidBy`, `owedBy`, `amount`, and `description`.

3. **Balances Table**:
    - Maintains the net balance between two users using a unique identifier `balanceId`.

---

## **Key Implementations**

### **JWT-Based Authentication**:
- **JWTService**: Handles token generation and validation.
- **JWTAuthenticationFilter**: Intercepts requests, validates tokens, and sets the authentication context.

### **Balance Management Logic**:
- Unique `balanceId` (`userID1_userID2`) ensures one record per user pair.
- Balances are updated in real-time after each transaction.

### **Custom Exception Handling**:
- `UnauthorizedActionException`: Thrown when a user attempts an unauthorized action.
- `ResourceNotFoundException`: Thrown when requested data does not exist.
- Handled globally using `@ControllerAdvice`.

---

## **Testing**

### **Manual Testing**:
- **Postman**: Used to test all API endpoints, including user registration, login, transaction creation, and balance updates.

---

## **Challenges Faced**
1. **Repository Errors**:
    - Resolved issues with `JpaRepository` by aligning the Spring Boot version and ensuring JDK 17 compatibility.

2. **Authentication and Authorization**:
    - Implemented JWT and Spring Security to secure the application effectively.

3. **Database Synchronization**:
    - Maintained consistency between transactions and balances using atomic operations.

---

## **Future Improvements**
1. **UI Development**:
    - Integrate a frontend using React or Angular to provide a better user experience.
2. **Notifications**:
    - Add email or SMS notifications for transactions and balances.
3. **Multi-Currency Support**:
    - Allow users to perform transactions in different currencies.
4. **Enhanced Analytics**:
    - Provide detailed expense reports and summaries.

---

## **Conclusion**
The Splitwise Clone successfully implements core functionality for managing shared expenses and balances between users. By leveraging modern technologies like Spring Boot, JWT, and PostgreSQL, the project is secure, scalable, and ready for future enhancements. This project demonstrates strong adherence to clean architecture and industry best practices.

---