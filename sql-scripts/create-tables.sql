-- Drop database if it exists
DROP DATABASE IF EXISTS whfc;

-- create database whfc;
CREATE DATABASE whfc;

-- Select the database
USE whfc;

-- Create table for Users
CREATE TABLE User (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    user_type ENUM('manager', 'client', 'donor') NOT NULL,
    phone VARCHAR(15) NOT NULL
);

-- ?one-to-many or one-to-one relationship with child table?
-- Create table for Family
CREATE TABLE Family (
    id INT AUTO_INCREMENT PRIMARY KEY,
    specificHardship TEXT,
    family_size INT,
    all_children_in_school BOOLEAN,
    income_source VARCHAR(100),
    business_type VARCHAR(100),
    working_space BOOLEAN,
    year_of_establishment DATE,
    capital_amount DECIMAL(10, 2),
    support_received BOOLEAN,
    remarks TEXT
);

-- Create table for Family_Member
CREATE TABLE Family_Member (
    id INT AUTO_INCREMENT PRIMARY KEY,
    family_id INT,
    name VARCHAR(100) NOT NULL,
    relationship_to_child VARCHAR(50) NOT NULL,
    sex ENUM('M', 'F') NOT NULL,
    yob DATE NOT NULL,
    current_grade VARCHAR(50),
    year_joined DATE,
    year_of_stay INT,
    FOREIGN KEY (family_id) REFERENCES Family(id)
);

-- Create table for Location
CREATE TABLE Location (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sub_city VARCHAR(100) NOT NULL,
    village VARCHAR(100) NOT NULL
);

-- Create table for Beneficiary
CREATE TABLE Beneficiary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    beneficiary_id_number VARCHAR(20) NOT NULL
);

-- Create table for program
CREATE TABLE program (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE,
    description TEXT
);

-- Create table for Child
CREATE TABLE Child (
    id INT AUTO_INCREMENT PRIMARY KEY,
    beneficiary_id INT,
    name VARCHAR(100) NOT NULL,
    parents_status ENUM('Both parents', 'Divorced', 'Orphan', 'Half orphan') NOT NULL,
    family_id INT,
    guardian_id INT,
    location_id INT,
    program_id INT,
    sex ENUM('M', 'F') NOT NULL,
    yob DATE NOT NULL,
    year_joined DATE NOT NULL,
    year_of_stay INT NOT NULL,
    current_grade VARCHAR(50),
    FOREIGN KEY (family_id) REFERENCES Family(id),
    FOREIGN KEY (guardian_id) REFERENCES Family_Member(id),
    FOREIGN KEY (location_id) REFERENCES Location(id),
    FOREIGN KEY (beneficiary_id) REFERENCES Beneficiary(id),
    FOREIGN KEY (program_id) REFERENCES program(id)
);

-- ?use view for points column?
-- Create table for Education
CREATE TABLE Education (
    id INT AUTO_INCREMENT PRIMARY KEY,
    child_id INT,
    student_achievement INT,
    performance_index INT,
    education_year INT,
    recorded_by INT,
    date_of_recording DATE,
    FOREIGN KEY (child_id) REFERENCES Child(id),
    FOREIGN KEY (recorded_by) REFERENCES User(id)
);

-- ?use view to show BMI?
-- ?what about family_member (child) health?
-- Create table for Health (Many-to-One with Child)
CREATE TABLE Health (
    id INT AUTO_INCREMENT PRIMARY KEY,
    child_id INT,
    weight VARCHAR(100),
    height VARCHAR(100),
    recorded_by INT,
    date_of_recording DATE,
    FOREIGN KEY (child_id) REFERENCES Child(id),
    FOREIGN KEY (recorded_by) REFERENCES User(id)
);

-- ?use view for points column?
-- Create table for Prosperity (Many-to-One with Family)
CREATE TABLE Prosperity (
    id INT AUTO_INCREMENT PRIMARY KEY,
    family_id INT,
    floor_type INT,
    prosperity_index INT,
    recorded_by INT,
    date_of_recording DATE,
    FOREIGN KEY (family_id) REFERENCES Family(id),
    FOREIGN KEY (recorded_by) REFERENCES User(id)
);
