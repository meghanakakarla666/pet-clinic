-- ==========================================
-- Pet Clinic Database Initialization Script
-- For MySQL 8.0+
-- ==========================================

-- Create database if not exists
CREATE DATABASE IF NOT EXISTS petclinicdb CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE petclinicdb;

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS consultations;
DROP TABLE IF EXISTS pets;
DROP TABLE IF EXISTS owners;

-- Create owners table
CREATE TABLE owners (
    id BIGINT NOT NULL AUTO_INCREMENT,
    first_name VARCHAR(30) NOT NULL,
    last_name VARCHAR(30) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(50) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_owner_name (last_name, first_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create pets table
CREATE TABLE pets (
    id BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(30) NOT NULL,
    birth_date DATE,
    type VARCHAR(20) NOT NULL,
    breed VARCHAR(50),
    owner_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE,
    INDEX idx_pet_name (name),
    INDEX idx_pet_owner (owner_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create consultations table
CREATE TABLE consultations (
    id BIGINT NOT NULL AUTO_INCREMENT,
    consultation_date DATETIME NOT NULL,
    description VARCHAR(500) NOT NULL,
    fee DECIMAL(10,2) NOT NULL,
    treatment VARCHAR(500) NOT NULL,
    pet_id BIGINT NOT NULL,
    PRIMARY KEY (id),
    FOREIGN KEY (pet_id) REFERENCES pets(id) ON DELETE CASCADE,
    INDEX idx_consultation_date (consultation_date),
    INDEX idx_consultation_pet (pet_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample owners
INSERT INTO owners (first_name, last_name, address, city, phone) VALUES
('John', 'Doe', '123 Main St', 'Springfield', '5551234567'),
('Jane', 'Smith', '456 Oak Ave', 'Springfield', '5559876543'),
('Bob', 'Johnson', '789 Pine Rd', 'Riverside', '5555555555'),
('Alice', 'Williams', '321 Elm St', 'Springfield', '5551112222'),
('Charlie', 'Brown', '654 Maple Dr', 'Riverside', '5553334444');

-- Insert sample pets
INSERT INTO pets (name, birth_date, type, breed, owner_id) VALUES
('Buddy', '2020-01-15', 'Dog', 'Golden Retriever', 1),
('Whiskers', '2019-03-22', 'Cat', 'Persian', 2),
('Max', '2021-06-10', 'Dog', 'German Shepherd', 3),
('Luna', '2020-08-05', 'Cat', 'Siamese', 4),
('Rocky', '2018-11-30', 'Dog', 'Bulldog', 5),
('Bella', '2020-04-18', 'Cat', 'Maine Coon', 1),
('Charlie', '2019-09-25', 'Bird', 'Parrot', 2);

-- Insert sample consultations
INSERT INTO consultations (consultation_date, description, fee, treatment, pet_id) VALUES
('2023-01-15 10:00:00', 'Annual checkup and vaccinations', 125.00, 'Vaccinations, physical exam', 1),
('2023-02-20 14:30:00', 'Dental cleaning', 200.00, 'Dental scaling and cleaning', 2),
('2023-01-25 11:15:00', 'Skin irritation treatment', 85.00, 'Topical medication prescribed', 3),
('2023-03-10 09:45:00', 'Hip dysplasia examination', 150.00, 'X-rays and pain medication', 4),
('2023-02-05 13:00:00', 'Respiratory infection', 95.00, 'Antibiotics prescribed', 5),
('2023-03-15 10:30:00', 'Routine checkup', 75.00, 'Physical examination, all clear', 6),
('2023-01-18 15:45:00', 'Feather plucking behavior', 110.00, 'Behavioral consultation', 7),
('2023-02-28 09:00:00', 'Spay surgery', 250.00, 'Surgical procedure completed', 2),
('2023-03-05 16:00:00', 'Arthritis management', 120.00, 'Pain management plan', 5),
('2023-01-22 11:00:00', 'Ear infection treatment', 90.00, 'Ear drops prescribed', 1),
('2023-03-20 14:00:00', 'Weight management consultation', 80.00, 'Diet plan provided', 3);

-- Verify data insertion
SELECT 'Owners inserted:' AS 'Status', COUNT(*) AS 'Count' FROM owners
UNION ALL
SELECT 'Pets inserted:', COUNT(*) FROM pets
UNION ALL
SELECT 'Consultations inserted:', COUNT(*) FROM consultations;

-- Show database structure
SHOW TABLES;

