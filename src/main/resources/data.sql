-- Insert sample owners (without explicit IDs - let database generate them)
INSERT INTO owners (first_name, last_name, address, city, phone) VALUES
('John', 'Doe', '123 Main St', 'Springfield', '5551234567'),
('Jane', 'Smith', '456 Oak Ave', 'Springfield', '5559876543'),
('Bob', 'Johnson', '789 Pine Rd', 'Riverside', '5555555555'),
('Alice', 'Williams', '321 Elm St', 'Springfield', '5551112222'),
('Charlie', 'Brown', '654 Maple Dr', 'Riverside', '5553334444');

-- Insert sample pets (without explicit IDs - let database generate them)
INSERT INTO pets (name, birth_date, type, breed, owner_id) VALUES
('Buddy', '2020-01-15', 'Dog', 'Golden Retriever', 1),
('Whiskers', '2019-03-22', 'Cat', 'Persian', 1),
('Max', '2021-06-10', 'Dog', 'German Shepherd', 2),
('Luna', '2020-08-05', 'Cat', 'Siamese', 3),
('Rocky', '2018-12-01', 'Dog', 'Bulldog', 3),
('Bella', '2021-02-14', 'Cat', 'Maine Coon', 4),
('Rex', '2019-09-30', 'Dog', 'Labrador', 5);

-- Insert sample consultations (without explicit IDs - let database generate them)
INSERT INTO consultations (consultation_date, description, fee, treatment, pet_id) VALUES
('2023-01-15 10:00:00', 'Annual checkup and vaccinations', 125.00, 'Vaccinations, physical exam', 1),
('2023-02-20 14:30:00', 'Dental cleaning', 200.00, 'Dental scaling and cleaning', 1),
('2023-01-25 11:15:00', 'Skin irritation treatment', 85.00, 'Topical medication prescribed', 2),
('2023-03-10 09:45:00', 'Hip dysplasia examination', 150.00, 'X-rays and pain medication', 3),
('2023-02-05 16:00:00', 'Routine checkup', 75.00, 'General health examination', 4),
('2023-03-15 13:20:00', 'Breathing difficulties', 180.00, 'Respiratory treatment', 5),
('2023-01-30 10:30:00', 'Spay surgery', 300.00, 'Spay procedure completed', 6),
('2023-03-20 15:45:00', 'Vaccination booster', 60.00, 'Annual vaccination booster', 7),
('2023-04-01 11:00:00', 'Eye infection treatment', 95.00, 'Antibiotic eye drops prescribed', 2),
('2023-04-10 14:15:00', 'Nail trimming and grooming', 45.00, 'Nail care and basic grooming', 4);
