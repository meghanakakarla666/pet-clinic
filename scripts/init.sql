-- MySQL initialization script for Pet Clinic
CREATE DATABASE IF NOT EXISTS petclinic;
USE petclinic;

-- Create owners table
CREATE TABLE IF NOT EXISTS owners (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(80) NOT NULL,
    phone VARCHAR(20)
);

-- Create pets table
CREATE TABLE IF NOT EXISTS pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30) NOT NULL,
    birth_date DATE,
    type VARCHAR(20) NOT NULL,
    breed VARCHAR(50),
    owner_id BIGINT NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES owners(id)
);

-- Create consultations table
CREATE TABLE IF NOT EXISTS consultations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    consultation_date DATETIME NOT NULL,
    description VARCHAR(500) NOT NULL,
    fee DECIMAL(10,2) NOT NULL,
    treatment VARCHAR(100),
    pet_id BIGINT NOT NULL,
    FOREIGN KEY (pet_id) REFERENCES pets(id)
);

-- Insert sample data
INSERT IGNORE INTO owners (id, first_name, last_name, address, city, phone) VALUES
(1, 'John', 'Doe', '123 Main St', 'Springfield', '5551234567'),
(2, 'Jane', 'Smith', '456 Oak Ave', 'Springfield', '5559876543'),
(3, 'Bob', 'Johnson', '789 Pine Rd', 'Riverside', '5555555555'),
(4, 'Alice', 'Williams', '321 Elm St', 'Springfield', '5551112222'),
(5, 'Charlie', 'Brown', '654 Maple Dr', 'Riverside', '5553334444');

INSERT IGNORE INTO pets (id, name, birth_date, type, breed, owner_id) VALUES
(1, 'Buddy', '2020-01-15', 'Dog', 'Golden Retriever', 1),
(2, 'Whiskers', '2019-03-22', 'Cat', 'Persian', 1),
(3, 'Max', '2021-06-10', 'Dog', 'German Shepherd', 2),
(4, 'Luna', '2020-08-05', 'Cat', 'Siamese', 3),
(5, 'Rocky', '2018-12-01', 'Dog', 'Bulldog', 3),
(6, 'Bella', '2021-02-14', 'Cat', 'Maine Coon', 4),
(7, 'Rex', '2019-09-30', 'Dog', 'Labrador', 5);

INSERT IGNORE INTO consultations (id, consultation_date, description, fee, treatment, pet_id) VALUES
(1, '2023-01-15 10:00:00', 'Annual checkup and vaccinations', 125.00, 'Vaccinations, physical exam', 1),
(2, '2023-02-20 14:30:00', 'Dental cleaning', 200.00, 'Dental scaling and cleaning', 1),
(3, '2023-01-25 11:15:00', 'Skin irritation treatment', 85.00, 'Topical medication prescribed', 2),
(4, '2023-03-10 09:45:00', 'Hip dysplasia examination', 150.00, 'X-rays and pain medication', 3),
(5, '2023-02-05 16:00:00', 'Routine checkup', 75.00, 'General health examination', 4);
