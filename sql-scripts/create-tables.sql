-- Drop database if it exists
DROP DATABASE IF EXISTS whfc;

-- create database whfc;
CREATE DATABASE whfc;

-- Select the database
USE whfc;

-- Create table for Guardian
CREATE TABLE Guardian (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create table for Location
CREATE TABLE Location (
    id INT AUTO_INCREMENT PRIMARY KEY,
    sub_city VARCHAR(100) NOT NULL,
    village VARCHAR(100) NOT NULL
);

-- Create table for FamilyStatus
CREATE TABLE Family_Status (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('Both parents', 'Divorced', 'Orphan', 'Half orphan') NOT NULL
);

-- Create table for Education
CREATE TABLE Education (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('Illiterate', 'Primary', 'Secondary', 'Diploma', 'Above') NOT NULL
);

-- Create table for Income
CREATE TABLE Income (
    id INT AUTO_INCREMENT PRIMARY KEY,
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

-- Create table for Beneficiary
CREATE TABLE Beneficiary (
    id INT AUTO_INCREMENT PRIMARY KEY,
    beneficiary_id VARCHAR(20) NOT NULL
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
    guardian_id INT,
    relation_to_guardian VARCHAR(50) NOT NULL,
    location_id INT,
    family_status_id INT,
    education_id INT,
    income_id INT,
    program_id INT,
    FOREIGN KEY (guardian_id) REFERENCES Guardian(id),
    FOREIGN KEY (location_id) REFERENCES Location(id),
    FOREIGN KEY (family_status_id) REFERENCES Family_Status(id),
    FOREIGN KEY (education_id) REFERENCES Education(id),
    FOREIGN KEY (income_id) REFERENCES Income(id),
    sex ENUM('M', 'F') NOT NULL,
    yob DATE NOT NULL,
    year_joined DATE NOT NULL,
    year_of_stay INT NOT NULL,
    current_grade VARCHAR(50),
    FOREIGN KEY (beneficiary_id) REFERENCES Beneficiary(id),
    FOREIGN KEY (program_id) REFERENCES program(id)
);



-- Create table for Sibling
CREATE TABLE Sibling (
    id INT AUTO_INCREMENT PRIMARY KEY,
    child_id INT,
    name VARCHAR(100) NOT NULL,
    sex ENUM('M', 'F') NOT NULL,
    yob DATE NOT NULL,
    current_grade VARCHAR(50),
    relation VARCHAR(50),
    FOREIGN KEY (child_id) REFERENCES Child(id)
);

-- Create table for Users
CREATE TABLE Users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    user_type ENUM('manager', 'client', 'donor') NOT NULL,
    phone VARCHAR(15) NOT NULL
);
