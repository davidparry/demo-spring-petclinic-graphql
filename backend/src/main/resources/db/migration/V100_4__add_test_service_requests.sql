-- Add sample service requests for testing statistics
-- Assuming pets and specialties exist from V100_2__fill_db.sql

-- Service requests for different pet types
INSERT INTO service_requests (pet_id, specialty_id, request_date, description, status) VALUES
(1, 1, '2024-01-15', 'Regular checkup', 'COMPLETED'),
(1, 2, '2024-02-20', 'Vaccination needed', 'COMPLETED'),
(2, 1, '2024-01-20', 'Health examination', 'COMPLETED'),
(2, 3, '2024-03-10', 'Surgery consultation', 'PENDING'),
(3, 2, '2024-02-05', 'Annual vaccination', 'COMPLETED'),
(3, 1, '2024-03-15', 'Routine checkup', 'COMPLETED'),
(4, 3, '2024-01-25', 'Dental surgery', 'COMPLETED'),
(4, 1, '2024-02-28', 'Post-surgery checkup', 'COMPLETED'),
(5, 2, '2024-03-01', 'Vaccination', 'COMPLETED'),
(6, 1, '2024-01-10', 'General checkup', 'COMPLETED'),
(6, 2, '2024-02-15', 'Booster shot', 'COMPLETED'),
(7, 3, '2024-03-20', 'Minor surgery', 'PENDING'),
(8, 1, '2024-01-30', 'Health screening', 'COMPLETED'),
(9, 2, '2024-02-10', 'Vaccination', 'COMPLETED'),
(10, 1, '2024-03-05', 'Wellness exam', 'COMPLETED');
