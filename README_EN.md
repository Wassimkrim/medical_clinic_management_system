# Medical Clinic Management - English Version

This is a complete Java Swing + MySQL project for managing a medical clinic.
It includes the practical application and the theoretical report required by the database mini-project.

## Main features

- Login with roles: ADMIN, DOCTOR, SECRETARY
- Patient management: add, update, delete, search
- Appointment planning by the secretary
- Medical consultation management by the doctor
- Prescription management with medicines and dosage instructions
- Medical certificates and sick leave management
- Medical tests / lab results management
- Patient medical history
- Printing / previewing output documents
- User account management by the admin

## Requirements

- JDK 17 or newer
- Eclipse or NetBeans
- MySQL / XAMPP
- MySQL Connector/J, included through Maven dependency

## How to run

1. Start MySQL using XAMPP or MySQL Server.
2. Open phpMyAdmin.
3. Run the file `create_database.sql`.
4. Import this folder in Eclipse:
   - File > Import > Maven > Existing Maven Projects
   - Select this project folder
   - Finish
5. Run:
   - `src/main/java/com/clinicmanagement/App.java`
   - Run As > Java Application

## Database connection

The connection settings are in:

`src/main/java/com/clinicmanagement/db/DB.java`

Default settings:

```java
URL = "jdbc:mysql://localhost:3306/medical_clinic";
USER = "root";
PASSWORD = "";
```

If your MySQL has a password, put it in `PASSWORD`.

## Test accounts

| Role | Username | Password |
|---|---|---|
| Admin | admin | admin123 |
| Doctor | doctor | doc123 |
| Secretary | secretary | sec123 |

## Work distribution for 3 members

- Member 1: database, ERD/MCD, relational model, report
- Member 2: login, patients, appointments, secretary interface
- Member 3: consultations, prescriptions, certificates, medical tests, patient history, printing
