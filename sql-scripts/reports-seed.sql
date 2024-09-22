-- Select the database
USE whfc;

-- Disable foreign key checks temporarily
SET FOREIGN_KEY_CHECKS = 0;

-- Empty tables
TRUNCATE TABLE Users;
TRUNCATE TABLE Location;
TRUNCATE TABLE Family;
TRUNCATE TABLE Family_Member;
TRUNCATE TABLE Beneficiary;
TRUNCATE TABLE program;
TRUNCATE TABLE Child;
TRUNCATE TABLE Education;
TRUNCATE TABLE Health;
TRUNCATE TABLE Prosperity;

-- Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;


-- Insert data into Users table
INSERT INTO Users (username, password, first_name, last_name, email, user_type, phone)
VALUES
    ('manager@whfc.com', 'abc123', 'John', 'Doe', 'manager@whfc.com', 'manager', '251-930-586-155'),
    ('client@whfc.com', 'abc123', 'Jane', 'Smith', 'client@whfc.com', 'client', '251-930-586-156'),
    ('donor@whfc.com', 'abc123', 'Alice', 'Johnson', 'donor@whfc.com', 'donor', '251-930-586-157');

-- Insert data into Location table
INSERT INTO Location (sub_city, village)
VALUES 
    ('A/wondo', 'Bultuma'),
    ('Hawassa', 'Tabor'),
    ('Sidama', 'Yirgalem'),
    ('Addis Ababa', 'Bole'),
    ('Dire Dawa', 'Gende Kore');

-- Insert data into Family table
INSERT INTO Family (specificHardship, family_size, all_children_in_school, income_source, business_type, working_space, year_of_establishment, capital_amount, support_received, remarks)
VALUES
    ('Financial difficulties due to crop failure', 8, TRUE, 'Farming', 'Agriculture', TRUE, '2010-05-20', 50000.00, TRUE, 'Receiving government support'),
    ('Single parent household', 5, FALSE, 'Small business', 'Retail', FALSE, '2015-03-15', 25000.00, TRUE, 'Needs additional support'),
    ('Unemployment', 6, TRUE, 'Day labor', 'Construction', FALSE, '2018-09-01', 10000.00, FALSE, 'Seeking stable employment'),
    ('Medical expenses', 4, TRUE, 'Teaching', 'Education', TRUE, '2012-01-10', 75000.00, TRUE, 'Recovering from major surgery'),
    ('Natural disaster impact', 7, FALSE, 'Fishing', 'Agriculture', TRUE, '2019-11-30', 30000.00, TRUE, 'Rebuilding after flood');

-- Insert data into Family_Member table (simplified for brevity)
INSERT INTO Family_Member (family_id, name, relationship_to_child, sex, yob)
VALUES 
    (1, 'Tesfaye Muntasha', 'Uncle', 'M', '1975-04-15'),
    (2, 'Almaz Bekele', 'Mother', 'F', '1980-07-22'),
    (3, 'Dawit Haile', 'Father', 'M', '1978-11-05'),
    (4, 'Tigist Mengistu', 'Aunt', 'F', '1982-03-18'),
    (5, 'Yohannes Tadesse', 'Grandfather', 'M', '1950-09-30');

-- Insert data into Beneficiary table
INSERT INTO Beneficiary (beneficiary_id_number)
VALUES 
    ('ET-03-01-337'),
    ('ET-03-01-338'),
    ('ET-03-01-339'),
    ('ET-03-01-340'),
    ('ET-03-01-341');

-- Insert data into program table
INSERT INTO program (name, start_date, end_date, description)
VALUES
    ('Sidama Program 2024', '2024-01-01', '2024-12-31', 'Comprehensive support for Sidama region'),
    ('Hawassa Education Initiative', '2024-01-01', '2024-12-31', 'Focus on education in Hawassa'),
    ('Addis Ababa Health Program', '2024-01-01', '2024-12-31', 'Health support in Addis Ababa'),
    ('Dire Dawa Economic Empowerment', '2024-01-01', '2024-12-31', 'Economic support in Dire Dawa'),
    ('National Youth Development', '2024-01-01', '2024-12-31', 'Youth empowerment across Ethiopia');

-- Insert data into Child table
INSERT INTO Child (beneficiary_id, name, parents_status, guardian_id, location_id, program_id, sex, yob, year_joined, year_of_stay, current_grade)
VALUES
    (1, 'Managidosh Marsamo', 'Half orphan', 1, 1, 1, 'F', '2001-09-11', '2006-12-18', 18, 'TVET'),
    (2, 'Abebe Kebede', 'Both parents', 2, 2, 2, 'M', '2005-03-22', '2010-01-15', 14, 'Grade 10'),
    (3, 'Fatima Mohammed', 'Single parent', 3, 3, 3, 'F', '2008-11-07', '2015-09-01', 9, 'Grade 7'),
    (4, 'Yonas Hailu', 'Orphan', 4, 4, 4, 'M', '2003-06-30', '2008-02-20', 16, 'Grade 12'),
    (5, 'Hiwot Gebre', 'Both parents', 5, 5, 5, 'F', '2010-12-25', '2017-08-10', 7, 'Grade 5');

-- Insert data into Education table
INSERT INTO Education (child_id, student_achievement, performance_index, education_year, recorded_by, date_of_recording)
VALUES
    (1, 8, 6, 2023, 1, '2023-10-01'),
    (2, 7, 5, 2023, 1, '2023-10-02'),
    (3, 9, 7, 2023, 1, '2023-10-03'),
    (4, 6, 4, 2023, 1, '2023-10-04'),
    (5, 8, 6, 2023, 1, '2023-10-05');

-- Insert data into Health table
INSERT INTO Health (child_id, weight, height, recorded_by, date_of_recording)
VALUES
    (1, '70kg', '170cm', 1, '2023-10-01'),
    (2, '65kg', '165cm', 1, '2023-10-02'),
    (3, '55kg', '160cm', 1, '2023-10-03'),
    (4, '75kg', '175cm', 1, '2023-10-04'),
    (5, '50kg', '155cm', 1, '2023-10-05');

-- Insert data into Prosperity table
INSERT INTO Prosperity (family_id, floor_type, prosperity_index, recorded_by, date_of_recording)
VALUES
    (1, 4, 2, 1, '2024-10-01'),
    (2, 3, 1, 1, '2023-10-02'),
    (3, 2, 3, 1, '2022-10-03'),
    (4, 5, 9, 1, '2021-10-04'),
    (5, 1, 4, 1, '2020-10-05');

-- Add more historical data for time series reports
INSERT INTO Prosperity (family_id, floor_type, prosperity_index, recorded_by, date_of_recording)
VALUES
    (1, 3, 4, 1, '2024-10-01'),
    (1, 2, 3, 1, '2023-10-01'),
    (2, 2, 1, 1, '2022-10-02'),
    (2, 1, 2, 1, '2021-10-02'),
    (3, 1, 5, 1, '2020-10-03'),
    (3, 1, 8, 1, '2019-10-03'),
    (4, 4, 7, 1, '2018-10-04'),
    (4, 3, 4, 1, '2017-10-04'),
    (5, 1, 4, 1, '2016-10-05'),
    (5, 1, 3, 1, '2015-10-05');

INSERT INTO Education (child_id, student_achievement, performance_index, education_year, recorded_by, date_of_recording)
VALUES
    (1, 7, 0, 2024, 1, '2024-10-01'),
    (1, 6, 4, 2023, 1, '2023-10-01'),
    (2, 6, 1, 2022, 1, '2022-10-02'),
    (2, 5, 3, 2021, 1, '2021-10-02'),
    (3, 8, 3, 2020, 1, '2020-10-03'),
    (3, 7, 5, 2019, 1, '2019-10-03'),
    (4, 5, 3, 2018, 1, '2018-10-04'),
    (4, 4, 9, 2017, 1, '2017-10-04'),
    (5, 7, 5, 2016, 1, '2017-10-05'),
    (5, 6, 7, 2015, 1, '2016-10-05');