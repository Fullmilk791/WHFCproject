-- Select the database
USE whfc;

-- Insert data into Users table
INSERT INTO Users (username, password, first_name, last_name, email, user_type, phone)
VALUES
    ('manager@whfc.com', 'abc123', 'John', 'Doe', 'manager@whfc.com', 'manager', '251-930-586-155'),
    ('client@whfc.com', 'abc123', 'Jane', 'Smith', 'client@whfc.com', 'client', '251-930-586-156'),
    ('donor@whfc.com', 'abc123', 'Alice', 'Johnson', 'donor@whfc.com', 'donor', '251-930-586-157');

-- Insert data into Location table
INSERT INTO Location (sub_city, village) VALUES ('A/wondo', 'Bultuma');

-- Insert data into Family table
INSERT INTO Family (
    specificHardship, 
    family_size, 
    all_children_in_school, 
    income_source, 
    business_type, 
    working_space, 
    year_of_establishment, 
    capital_amount, 
    support_received, 
    remarks
) 
VALUES (
    'Financial difficulties due to crop failure', 
    8, 
    TRUE, 
    'Farming', 
    'Agriculture', 
    TRUE, 
    '2010-05-20', 
    50000.00, 
    TRUE, 
    'Receiving government support'
);

-- Insert data into Family_Member table
INSERT INTO Family_Member (
    family_id, 
    name, 
    relationship_to_child, 
    sex, 
    yob, 
    current_grade, 
    year_joined, 
    year_of_stay
) 
VALUES 
    (
        (SELECT id FROM Family WHERE specificHardship = 'Financial difficulties due to crop failure'), 
        'Tesfaye Muntasha', 
        'Uncle', 
        'M', 
        '1975-04-15', 
        NULL, 
        NULL, 
        NULL
    );

-- Insert data into Beneficiary table
INSERT INTO Beneficiary (beneficiary_id_number)
VALUES ('ET-03-01-337');

-- Insert data into program table
INSERT INTO program (name, start_date, end_date, description)
VALUES
    ('program 1', '2024-01-01', '2024-12-31', 'Sidama program 2024'),
    ('program 2', '2024-01-01', '2024-12-31', 'Hawassa program 2024');

-- Insert data into Child table
INSERT INTO Child (
    beneficiary_id, 
    name, 
    parents_status, 
    guardian_id, 
    location_id, 
    program_id, 
    sex, 
    yob, 
    year_joined, 
    year_of_stay, 
    current_grade
) 
VALUES (
    (SELECT id FROM Beneficiary WHERE beneficiary_id_number = 'ET-03-01-337'), 
    'Managidosh Marsamo', 
    'Half orphan', 
    (SELECT id FROM Family_Member WHERE name = 'Tesfaye Muntasha'), 
    (SELECT id FROM Location WHERE sub_city = 'A/wondo' AND village = 'Bultuma'), 
    (SELECT id FROM program WHERE name = 'program 1'), 
    'F', 
    '2001-09-11', 
    '2006-12-18', 
    18, 
    'TVET'
);

-- Insert data into Education table
INSERT INTO Education (
    child_id, 
    student_achievement, 
    performance_index,
    education_year, 
    recorded_by, 
    date_of_recording
) 
VALUES (
    (SELECT id FROM Child WHERE name = 'Managidosh Marsamo'), 
    8, 
    6, 
    2023, 
    (SELECT id FROM Users WHERE username = 'manager@whfc.com'), 
    '2023-10-01'
);

-- Insert data into Health table
INSERT INTO Health (
    child_id, 
    weight, 
    height, 
    recorded_by, 
    date_of_recording
) 
VALUES (
    (SELECT id FROM Child WHERE name = 'Managidosh Marsamo'), 
    '70kg', 
    '170cm', 
    (SELECT id FROM Users WHERE username = 'manager@whfc.com'), 
    '2023-10-01'
);

-- Insert data into Prosperity table
INSERT INTO Prosperity (
    family_id, 
    floor_type, 
    prosperity_index,
    recorded_by, 
    date_of_recording
) 
VALUES (
    (SELECT id FROM Family WHERE specificHardship = 'Financial difficulties due to crop failure'), 
    4, 
    5, 
    (SELECT id FROM Users WHERE username = 'manager@whfc.com'), 
    '2023-10-01'
);