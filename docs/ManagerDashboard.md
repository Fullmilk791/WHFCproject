# ManagerDashboard Documentation

This document provides a comprehensive explanation of the `ManagerDashboard.java` file, detailing its structure, functionalities, and key components.

---

## Table of Contents

1. [Introduction](#introduction)
2. [Class Overview](#class-overview)
3. [Menu Structure](#menu-structure)
4. [Main Panel Setup](#main-panel-setup)
5. [Core Functionalities](#core-functionalities)
   - [Viewing Records (`viewTable` Method)](#viewing-records-viewtable-method)
   - [Adding Records (`addRecord` Method)](#adding-records-addrecord-method)
   - [Record Selection Listener](#record-selection-listener)
   - [Retrieving ENUM Values (`getEnumValues` Method)](#retrieving-enum-values-getenumvalues-method)
6. [Event Handling](#event-handling)
7. [Application Entry Point](#application-entry-point)
8. [Conclusion](#conclusion)

---

## Introduction

The `ManagerDashboard` class is a Java Swing application designed to facilitate the management of various entities within a school management system. It provides functionalities for viewing and adding records related to Beneficiaries, Guardians, Locations, Family Status, Education, Income, Programs, Children, Siblings, and Users. Additionally, it includes a Reports module and a logout feature.

---

## Class Overview

```java:src/school/ManagerDashboard.java
public class ManagerDashboard extends JFrame {
    // Class members and methods...
}
```

- **Inheritance**: Extends `JFrame` to create a window-based application.
- **Purpose**: Acts as the main dashboard for managers to interact with different modules like Beneficiaries, Guardians, Locations, etc.

### Key Members

- **Menu Components**: `JMenuBar`, `JMenu`, and `JMenuItem` instances for navigation.
- **Main Panel**: Central panel displaying the logo and logout button.
- **Interface**: `RecordSelectionListener` for handling record selection events.

---

## Menu Structure

```java:src/school/ManagerDashboard.java
private JMenuBar menuBar;
private JMenu menuBeneficiaries, menuGuardians, //... other menus
private JMenuItem menuItemViewBeneficiaries, menuItemAddBeneficiary;
//... other menu items
```

- **Menu Bar**: A `JMenuBar` holds multiple menus for different entities.
- **Menus**: Includes menus for Beneficiaries, Guardians, Locations, Family Status, Education, Income, Programs, Children, Siblings, Users, and Reports.
- **Menu Items**: Each menu typically contains "View" and "Add" options to perform respective actions.

### Example: Reports Menu

```java:src/school/ManagerDashboard.java
JMenu menuReports = new JMenu("Reports");
JMenuItem menuItemViewReports = new JMenuItem("View Reports");
menuReports.add(menuItemViewReports);
menuBar.add(menuReports);
```

- **Reports**: A dedicated menu for viewing reports.
- **Action Listener**: Opens the `ReportsPage` when "View Reports" is selected.

---

## Main Panel Setup

```java:src/school/ManagerDashboard.java
private JPanel mainPanel;
//...
mainPanel = new JPanel(new BorderLayout());
```

- **Main Panel**: Serves as the central area of the dashboard.
- **Components**:
  - **Image**: Displays a logo using a `JLabel` with an `ImageIcon`.
  - **Logout Button**: Allows users to log out and return to the login page.

```java:src/school/ManagerDashboard.java
JLabel imageLabel = new JLabel(new ImageIcon("assets/images/logo.png"));
JButton logoutButton = new JButton("Logout");

JPanel topPanel = new JPanel(new BorderLayout());
topPanel.add(imageLabel, BorderLayout.CENTER);
topPanel.add(logoutButton, BorderLayout.SOUTH);

mainPanel.add(topPanel, BorderLayout.CENTER);
```

---

## Core Functionalities

### Viewing Records (`viewTable` Method)

```java:src/school/ManagerDashboard.java
private void viewTable(String tableName, String[] joinTables, RecordSelectionListener listener) {
    // Method implementation...
}
```

- **Purpose**: Displays records from a specified database table in a new window.
- **Parameters**:
  - `tableName`: Name of the main table to view.
  - `joinTables`: An array of tables to join with the main table for extended information.
  - `listener`: A callback interface for handling record selection (optional).
- **Features**:
  - **Search Functionality**: Allows users to search records using a search bar.
  - **Sorting**: Enables sorting of table columns.
  - **Selection Mode**: If a `RecordSelectionListener` is provided, a "Select" button appears to allow record selection.
  - **Dynamic Query Building**: Constructs SQL queries based on provided table names and join tables.
  - **Data Population**: Retrieves data from the database and populates the `JTable`.

### Adding Records (`addRecord` Method)

```java:src/school/ManagerDashboard.java
private void addRecord(String tableName) {
    // Method implementation...
}
```

- **Purpose**: Opens a form to add a new record to the specified table.
- **Parameters**:
  - `tableName`: Name of the table where the new record will be added.
- **Features**:
  - **Dynamic Form Generation**: Creates input fields based on the table's metadata.
  - **Foreign Key Handling**: For fields that are foreign keys, a "Select" button opens the `viewTable` method for selection.
  - **ENUM Handling**: Extracts ENUM values from the database to populate dropdowns.
  - **Data Submission**: Collects input data and inserts a new record into the database using prepared statements.
  - **Validation**: Ensures that essential fields are filled and handles errors gracefully.

### Record Selection Listener

```java:src/school/ManagerDashboard.java
public interface RecordSelectionListener {
    void onRecordSelected(int selectedId, String displayValue);
}
```

- **Purpose**: Defines a callback interface for handling actions when a record is selected from a `viewTable` window.
- **Usage**: Implemented when a specific record selection action is required, such as selecting a foreign key reference.

### Retrieving ENUM Values (`getEnumValues` Method)

```java:src/school/ManagerDashboard.java
private java.util.List<String> getEnumValues(Connection connection, String tableName, String columnName) throws SQLException {
    // Method implementation...
}
```

- **Purpose**: Extracts ENUM values from a specified column in a table.
- **Parameters**:
  - `connection`: Database connection object.
  - `tableName`: Name of the table containing the ENUM column.
  - `columnName`: Name of the ENUM column.
- **Returns**: A list of possible ENUM values.
- **Usage**: Populates `JComboBox` components for fields that use ENUM types.

---

## Event Handling

### Menu Item Action Listeners

Each menu item has an action listener that triggers the appropriate functionality (`viewTable` or `addRecord`). For example:

```java:src/school/ManagerDashboard.java
menuItemViewBeneficiaries.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        viewTable("Beneficiary", null, null);
    }
});

menuItemAddBeneficiary.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        addRecord("Beneficiary");
    }
});
```

- **View Actions**: Open a new window displaying the respective table's records.
- **Add Actions**: Open a form to add a new record to the respective table.

### Logout Button Action Listener

```java:src/school/ManagerDashboard.java
logoutButton.addActionListener(new ActionListener() {
    public void actionPerformed(ActionEvent e) {
        // go to login page
        login2 LoginFrame;
        LoginFrame = new login2();
        LoginFrame.setVisible(true);
        LoginFrame.setLocationRelativeTo(null);
        dispose(); // Close the current window
    }
});
```

- **Functionality**: Logs the user out by opening the login window and closing the dashboard.

---

## Application Entry Point

```java:src/school/ManagerDashboard.java
public static void main(String[] args) {
    ManagerDashboard dashboard = new ManagerDashboard();
    dashboard.setVisible(true);
}
```

- **Purpose**: Launches the `ManagerDashboard` application.
- **Process**:
  - Instantiates the `ManagerDashboard` class.
  - Sets the dashboard window to be visible.

---

## Conclusion

The `ManagerDashboard.java` class serves as the central hub for managing various aspects of the school management system. Its modular design, with dynamic methods for viewing and adding records, ensures scalability and ease of maintenance. Key features like search functionality, sorting, ENUM handling, and foreign key management enhance the user experience and data integrity. Understanding this structure enables developers to extend and customize the dashboard to meet evolving requirements.