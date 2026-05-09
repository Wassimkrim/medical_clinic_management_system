DROP DATABASE IF EXISTS medical_clinic;
CREATE DATABASE medical_clinic CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE medical_clinic;

CREATE TABLE users (
    id_user INT PRIMARY KEY AUTO_INCREMENT,
    last_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('ADMIN', 'DOCTOR', 'SECRETARY') NOT NULL
);

CREATE TABLE patients (
    id_patient INT PRIMARY KEY AUTO_INCREMENT,
    last_name VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    birth_date DATE,
    gender ENUM('Male', 'Female'),
    phone VARCHAR(20),
    address VARCHAR(150),
    blood_group VARCHAR(5),
    allergies TEXT,
    chronic_diseases TEXT
);

CREATE TABLE appointments (
    id_appointment INT PRIMARY KEY AUTO_INCREMENT,
    id_patient INT NOT NULL,
    id_secretary INT,
    appointment_date DATE NOT NULL,
    appointment_time TIME NOT NULL,
    reason VARCHAR(150),
    status ENUM('Planned', 'Cancelled', 'Completed') DEFAULT 'Planned',
    FOREIGN KEY (id_patient) REFERENCES patients(id_patient)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_secretary) REFERENCES users(id_user)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE consultations (
    id_consultation INT PRIMARY KEY AUTO_INCREMENT,
    id_patient INT NOT NULL,
    id_doctor INT,
    consultation_date DATE NOT NULL,
    symptoms TEXT,
    diagnosis TEXT,
    treatment TEXT,
    report TEXT,
    FOREIGN KEY (id_patient) REFERENCES patients(id_patient)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_doctor) REFERENCES users(id_user)
        ON DELETE SET NULL ON UPDATE CASCADE
);

CREATE TABLE prescriptions (
    id_prescription INT PRIMARY KEY AUTO_INCREMENT,
    id_consultation INT NOT NULL,
    prescription_date DATE NOT NULL,
    note TEXT,
    FOREIGN KEY (id_consultation) REFERENCES consultations(id_consultation)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medicines (
    id_medicine INT PRIMARY KEY AUTO_INCREMENT,
    medicine_name VARCHAR(100) NOT NULL,
    form VARCHAR(50),
    dosage VARCHAR(50)
);

CREATE TABLE prescription_lines (
    id_line INT PRIMARY KEY AUTO_INCREMENT,
    id_prescription INT NOT NULL,
    id_medicine INT NOT NULL,
    dosage_instructions VARCHAR(150),
    duration VARCHAR(50),
    FOREIGN KEY (id_prescription) REFERENCES prescriptions(id_prescription)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (id_medicine) REFERENCES medicines(id_medicine)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE certificates (
    id_certificate INT PRIMARY KEY AUTO_INCREMENT,
    id_consultation INT NOT NULL,
    certificate_type ENUM('Medical Certificate', 'Sick Leave') NOT NULL,
    start_date DATE,
    duration_days INT,
    description TEXT,
    FOREIGN KEY (id_consultation) REFERENCES consultations(id_consultation)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE medical_tests (
    id_test INT PRIMARY KEY AUTO_INCREMENT,
    id_consultation INT NOT NULL,
    test_type VARCHAR(100),
    result TEXT,
    test_date DATE,
    FOREIGN KEY (id_consultation) REFERENCES consultations(id_consultation)
        ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO users(last_name, first_name, username, password, role) VALUES
('System', 'Admin', 'admin', 'admin123', 'ADMIN'),
('Ahmed', 'Benali', 'doctor', 'doc123', 'DOCTOR'),
('Sara', 'Kaci', 'secretary', 'sec123', 'SECRETARY');

INSERT INTO patients(last_name, first_name, birth_date, gender, phone, address, blood_group, allergies, chronic_diseases) VALUES
('Bouzid', 'Karim', '2002-05-12', 'Male', '0555000000', 'Skikda', 'O+', 'None', 'None'),
('Mansouri', 'Lina', '1999-10-20', 'Female', '0666000000', 'Constantine', 'A+', 'Penicillin', 'Asthma');

INSERT INTO appointments(id_patient, id_secretary, appointment_date, appointment_time, reason) VALUES
(1, 3, '2026-05-10', '09:00:00', 'General consultation'),
(2, 3, '2026-05-10', '10:00:00', 'Chest pain');

INSERT INTO consultations(id_patient, id_doctor, consultation_date, symptoms, diagnosis, treatment, report) VALUES
(1, 2, '2026-05-10', 'Fever and headache', 'Seasonal flu', 'Rest and paracetamol', 'Patient should rest for 3 days.'),
(2, 2, '2026-05-10', 'Shortness of breath', 'Asthma episode', 'Inhaler treatment', 'Follow-up required in one week.');

INSERT INTO prescriptions(id_consultation, prescription_date, note) VALUES
(1, '2026-05-10', 'Take medicines after meals');

INSERT INTO medicines(medicine_name, form, dosage) VALUES
('Paracetamol', 'Tablet', '500mg'),
('Amoxicillin', 'Capsule', '1g'),
('Ventolin', 'Inhaler', '100mcg');

INSERT INTO prescription_lines(id_prescription, id_medicine, dosage_instructions, duration) VALUES
(1, 1, '1 tablet three times daily', '5 days');

INSERT INTO certificates(id_consultation, certificate_type, start_date, duration_days, description) VALUES
(1, 'Medical Certificate', '2026-05-10', 3, 'The patient needs rest.');

INSERT INTO medical_tests(id_consultation, test_type, result, test_date) VALUES
(2, 'Blood test', 'Normal result', '2026-05-10');
