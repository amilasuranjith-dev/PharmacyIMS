# PharmacyIMS

PharmacyIMS is a JavaFX-based Pharmacy Inventory Management System built with Maven and Java 17.  
It helps manage medicines, suppliers, sales, and dashboard insights for day-to-day pharmacy operations.

## Features

- Dashboard with:
  - total medicines
  - low stock count
  - expiring medicines count
  - today’s sales summary
- Medicine management (add, edit, delete, search)
- Supplier management (add, edit, delete, search)
- Sales management:
  - create sales
  - view sales history
  - search by invoice

## Tech Stack

- Java 17
- JavaFX 21 (`javafx-controls`, `javafx-fxml`)
- Maven
- MySQL
- Lombok
- JFoenix

## Project Structure

- `src/main/java/edu/icet/controller` – JavaFX controllers
- `src/main/java/edu/icet/service` – business/service layer
- `src/main/java/edu/icet/repository` – data access layer
- `src/main/java/edu/icet/model` – entities and DTOs
- `src/main/resources/view` – FXML UI files

## Prerequisites

- JDK 17
- Maven 3.8+
- MySQL Server

## Database Configuration

Database connection is currently configured in:

- `src/main/java/edu/icet/db/DBConnection.java`

Default values:

- URL: `jdbc:mysql://localhost:3306/pharmacy_ims`
- User: `root`
- Password: `1234`

Update these values to match your local database before running the app.

## Build and Run

From the project root:

```bash
mvn clean compile
```

To run tests:

```bash
mvn test
```

To start the application, run the main class:

- `edu.icet.Main`

(For most setups, this is easiest through your IDE’s run configuration.)

## Notes

- Ensure the required MySQL schema and tables exist before using the application.
- The JavaFX UI entry flow starts from `edu.icet.Main` and loads `/view/main-form.fxml`.
