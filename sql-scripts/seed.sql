-- Select the database
USE whfc;

-- Insert data into Guardian table
INSERT INTO Guardian (name) VALUES ('Tesfaye Muntasha');

-- Insert data into Location table
INSERT INTO Location (sub_city, village) VALUES ('A/wondo', 'Bultuma');

-- Insert data into Family_Status table
INSERT INTO Family_Status (status) VALUES ('Half orphan');

-- Insert data into Education table
INSERT INTO Education (status) VALUES ('Secondary');

-- Insert data into Income table
INSERT INTO Income (family_size, all_children_in_school, income_source, business_type, working_space, year_of_establishment, capital_amount, support_received, remarks)
VALUES (8, TRUE, 'Farming', NULL, FALSE, NULL, NULL, FALSE, NULL);

-- Insert data into Beneficiary table
INSERT INTO Beneficiary (beneficiary_id)
VALUES
('ET-03-01-337');

-- Insert data into program table
INSERT INTO program (name, start_date, end_date, description)
VALUES
('program 1', '2024-01-01', '2024-12-31', 'Sidama program 2024'),
('program 2', '2024-01-01', '2024-12-31', 'Hawassa rogram 2024');

-- Insert data into Child table
INSERT INTO Child (beneficiary_id, name, guardian_id, relation_to_guardian, location_id, family_status_id, education_id, income_id, sex, yob, year_joined, year_of_stay, current_grade, program_id)
VALUES
((SELECT id FROM Beneficiary WHERE beneficiary_id = 'ET-03-01-337'), 'Managidosh Marsamo', (SELECT id FROM Guardian WHERE name = 'Tesfaye Muntasha'), 
'Uncle', 
(SELECT id FROM Location WHERE sub_city = 'A/wondo' AND village = 'Bultuma'), 
(SELECT id FROM Family_Status WHERE status = 'Half orphan'), 
(SELECT id FROM Education WHERE status = 'Secondary'), 
(SELECT id FROM Income WHERE family_size = 8 AND income_source = 'Farming'), 'F', '2001-09-11', '2006-12-18', 18, 'TVET', 
(SELECT id FROM program WHERE name = 'program 1'));

-- Insert data into Sibling table
INSERT INTO Sibling (child_id, name, sex, yob, current_grade, relation)
VALUES
((SELECT id FROM Child WHERE name = 'Managidosh Marsamo'), 'Tamirat Tesfaye', 'M', '2003-09-11', 'College', 'Father'),
((SELECT id FROM Child WHERE name = 'Managidosh Marsamo'), 'Timar Tesaye', 'F', '2005-09-11', '10', 'Father'),
((SELECT id FROM Child WHERE name = 'Managidosh Marsamo'), 'Bogalech Tesaye', 'F', '2007-09-11', '11', 'Father'),
((SELECT id FROM Child WHERE name = 'Managidosh Marsamo'), 'Gedeon Tesfaye', 'M', '2010-09-11', '7', 'Father'),
((SELECT id FROM Child WHERE name = 'Managidosh Marsamo'), 'Tewabech Tesfaye', 'F', '2015-09-11', '3', 'Father'),
((SELECT id FROM Child WHERE name = 'Managidosh Marsamo'), 'Genet Marisamo', 'F', '1999-09-11', '12', 'Uncle');

-- Insert data into Users table
INSERT INTO Users (username, password, first_name, last_name, email, user_type, phone)
VALUES
('manager@whfc.com', 'abc123', 'John', 'Doe', 'manager@whfc.com', 'manager', '251-930-586-155'),
('client@whfc.com', 'abc123', 'Jane', 'Smith', 'client@whfc.com', 'client', '251-930-586-156'),
('donor@whfc.com', 'abc123', 'Alice', 'Johnson', 'donor@whfc.com', 'donor', '251-930-586-157');


