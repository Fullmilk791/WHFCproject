# ReportsPage Documentation

This document provides a comprehensive explanation of the `ReportsPage.java` file, detailing its structure, functionalities, and key components.

---

## Table of Contents

1. [Introduction](#introduction)
2. [Class Overview](#class-overview)
3. [UI Structure](#ui-structure)
    - [Main Tabbed Pane](#main-tabbed-pane)
    - [Category Panels](#category-panels)
4. [Report Generation Methods](#report-generation-methods)
    - [Child Reports](#child-reports)
        - [Child Demographics Report](#child-demographics-report)
        - [Child Enrollment Report](#child-enrollment-report)
    - [Family Reports](#family-reports)
        - [Family Prosperity Report](#family-prosperity-report)
        - [Family Members Report](#family-members-report)
    - [Program Reports](#program-reports)
        - [Beneficiaries Per Program Report](#beneficiaries-per-program-report)
        - [Program Participation Duration Report](#program-participation-duration-report)
    - [Education Reports](#education-reports)
        - [Student Achievements Report](#student-achievements-report)
        - [Education Year Distribution Report](#education-year-distribution-report)
    - [Health Reports](#health-reports)
        - [Health Monitoring Report](#health-monitoring-report)
    - [Time Series Reports](#time-series-reports)
        - [Family Prosperity Over Time Report](#family-prosperity-over-time-report)
        - [Child Education Scores Over Time Report](#child-education-scores-over-time-report)
5. [Database Interaction](#database-interaction)
6. [Error Handling](#error-handling)
7. [Chart Customization](#chart-customization)
8. [Conclusion](#conclusion)

---

## Introduction

The `ReportsPage` class is a Java Swing application designed to provide various data visualizations based on the school's database schema. It extends `JFrame` and utilizes the JFreeChart library to create interactive charts and graphs that offer insights into different aspects of the school's operations, such as child demographics, family prosperity, program participation, education achievements, and health monitoring.

---

## Class Overview

```java:src/school/ReportsPage.java
public class ReportsPage extends JFrame {
    // Class members and methods...
}
```

- **Inheritance**: Extends `JFrame` to create a window-based application.
- **Purpose**: Serves as the main interface for displaying various reports through charts and graphs.

### Key Members

- **Tabbed Panes**: Utilizes `JTabbedPane` to categorize reports into different sections.
- **Database Connection**: Interacts with the database using JDBC to fetch required data for reports.
- **Chart Panels**: Employs `ChartPanel` from JFreeChart to embed charts within the UI.

---

## UI Structure

### Main Tabbed Pane

```java:src/school/ReportsPage.java
public ReportsPage() {
    setTitle("Reports");
    setSize(1000, 800);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    setLayout(new BorderLayout());

    // Create Main Tabbed Pane for Categories
    JTabbedPane mainTabbedPane = new JTabbedPane();

    // Adding Category Panels
    JPanel childReportsPanel = createChildReportsPanel();
    mainTabbedPane.addTab("Child Reports", childReportsPanel);

    JPanel familyReportsPanel = createFamilyReportsPanel();
    mainTabbedPane.addTab("Family Reports", familyReportsPanel);

    // ... other categories

    add(mainTabbedPane, BorderLayout.CENTER);
}
```

- **Main Window Setup**: Sets the title, size, default close operation, and layout.
- **Tabbed Pane**: Central component that holds different categories of reports as tabs.

### Category Panels

Each main category (e.g., Child Reports, Family Reports) is represented by a dedicated method that creates a `JPanel` containing subcategories as their own `JTabbedPane`.

```java:src/school/ReportsPage.java
private JPanel createChildReportsPanel() {
    JPanel panel = new JPanel(new BorderLayout());

    // Subcategories as Tabbed Pane
    JTabbedPane subTabbedPane = new JTabbedPane();

    // Subcategory 1: Demographics
    JPanel demographicsPanel = createChildDemographicsReport();
    subTabbedPane.addTab("Demographics", demographicsPanel);

    // Subcategory 2: Enrollment Status
    JPanel enrollmentPanel = createChildEnrollmentReport();
    subTabbedPane.addTab("Enrollment Status", enrollmentPanel);

    // Add more subcategories as needed...

    panel.add(subTabbedPane, BorderLayout.CENTER);
    return panel;
}
```

- **Subcategories**: Each main category contains subcategories, each responsible for a specific type of report.
- **Modularity**: Enhances maintainability by separating different report types into individual methods.

---

## Report Generation Methods

The `ReportsPage` class contains several methods, each responsible for generating a specific type of report. These methods typically perform the following steps:

1. **Data Retrieval**: Execute SQL queries to fetch relevant data.
2. **Dataset Creation**: Populate datasets compatible with JFreeChart.
3. **Chart Creation**: Generate charts (e.g., PieChart, BarChart, LineChart) using JFreeChart.
4. **Chart Panel Embedding**: Return a `ChartPanel` that can be embedded into the UI.

### Child Reports

#### Child Demographics Report

```java:src/school/ReportsPage.java
private JPanel createChildDemographicsReport() {
    DefaultPieDataset dataset = new DefaultPieDataset();

    String query = "SELECT sex, COUNT(*) AS count FROM Child GROUP BY sex";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            String sex = rs.getString("sex");
            int count = rs.getInt("count");
            dataset.setValue(sex, count);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Child Demographics data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    JFreeChart pieChart = ChartFactory.createPieChart(
            "Gender Distribution of Children",
            dataset,
            true, true, false
    );

    return new ChartPanel(pieChart);
}
```

- **Purpose**: Displays the gender distribution of children using a pie chart.
- **SQL Query**: Groups children by sex and counts the number in each category.
- **Error Handling**: Catches and displays SQL exceptions.

#### Child Enrollment Report

```java:src/school/ReportsPage.java
private JPanel createChildEnrollmentReport() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    String query = "SELECT current_grade, COUNT(*) AS count FROM Child GROUP BY current_grade";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            String grade = rs.getString("current_grade");
            int count = rs.getInt("count");
            dataset.addValue(count, "Students", grade);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Child Enrollment data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    JFreeChart barChart = ChartFactory.createBarChart(
            "Enrollment Status of Children",
            "Current Grade",
            "Number of Students",
            dataset
    );

    return new ChartPanel(barChart);
}
```

- **Purpose**: Illustrates the number of students in each grade using a bar chart.
- **SQL Query**: Groups children by their current grade and counts the number in each grade.

### Family Reports

#### Family Prosperity Report

```java:src/school/ReportsPage.java
private JPanel createFamilyProsperityReport() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    String query = "SELECT floor_type, COUNT(*) AS count FROM Prosperity GROUP BY floor_type";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            String floorType = rs.getString("floor_type");
            int count = rs.getInt("count");
            dataset.addValue(count, "Families", floorType);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Family Prosperity data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    JFreeChart barChart = ChartFactory.createBarChart(
            "Family Prosperity Status",
            "Floor Type",
            "Number of Families",
            dataset
    );

    return new ChartPanel(barChart);
}
```

- **Purpose**: Shows the distribution of family prosperity based on floor types using a bar chart.
- **SQL Query**: Groups families by `floor_type` and counts the number in each category.

#### Family Members Report

```java:src/school/ReportsPage.java
private JPanel createFamilyMembersReport() {
    DefaultPieDataset dataset = new DefaultPieDataset();

    String query = "SELECT family_size, COUNT(*) AS count FROM Family GROUP BY family_size";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            int familySize = rs.getInt("family_size");
            int count = rs.getInt("count");
            dataset.setValue("Family Size " + familySize, count);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Family Members data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    JFreeChart pieChart = ChartFactory.createPieChart(
            "Distribution of Family Sizes",
            dataset,
            true, true, false
    );

    return new ChartPanel(pieChart);
}
```

- **Purpose**: Depicts the distribution of family sizes using a pie chart.
- **SQL Query**: Groups families by `family_size` and counts the number in each category.

### Program Reports

#### Beneficiaries Per Program Report

```java:src/school/ReportsPage.java
private JPanel createBeneficiariesPerProgramReport() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    String query = "SELECT p.name, COUNT(c.beneficiary_id) AS beneficiary_count " +
                   "FROM program p LEFT JOIN Child c ON p.id = c.program_id " +
                   "GROUP BY p.name";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            String programName = rs.getString("name");
            int count = rs.getInt("beneficiary_count");
            dataset.addValue(count, "Beneficiaries", programName);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Beneficiaries per Program data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    JFreeChart barChart = ChartFactory.createBarChart(
            "Number of Beneficiaries per Program",
            "Program",
            "Number of Beneficiaries",
            dataset
    );

    return new ChartPanel(barChart);
}
```

- **Purpose**: Displays the number of beneficiaries in each program using a bar chart.
- **SQL Query**: Performs a left join between `program` and `Child` tables to count beneficiaries per program.

#### Program Participation Duration Report

```java:src/school/ReportsPage.java
private JPanel createProgramParticipationDurationReport() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    String query = "SELECT p.name, AVG(c.year_of_stay) AS avg_duration " +
                   "FROM program p LEFT JOIN Child c ON p.id = c.program_id " +
                   "GROUP BY p.name";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            String programName = rs.getString("name");
            double avgDuration = rs.getDouble("avg_duration");
            dataset.addValue(avgDuration, "Average Duration (Years)", programName);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Program Participation Duration data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    JFreeChart lineChart = ChartFactory.createLineChart(
            "Average Participation Duration per Program",
            "Program",
            "Average Duration (Years)",
            dataset
    );

    return new ChartPanel(lineChart);
}
```

- **Purpose**: Illustrates the average duration of participation in each program using a line chart.
- **SQL Query**: Calculates the average `year_of_stay` for beneficiaries in each program.

### Education Reports

#### Student Achievements Report

```java:src/school/ReportsPage.java
private JPanel createStudentAchievementsReport() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    String query = "SELECT education_year, COUNT(student_achievement) AS achievement_count " +
                   "FROM Education GROUP BY education_year";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            int year = rs.getInt("education_year");
            int count = rs.getInt("achievement_count");
            dataset.addValue(count, "Achievements", Integer.toString(year));
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Student Achievements data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    JFreeChart barChart = ChartFactory.createBarChart(
            "Student Achievements Over Years",
            "Year",
            "Number of Achievements",
            dataset
    );

    return new ChartPanel(barChart);
}
```

- **Purpose**: Shows the number of student achievements per year using a bar chart.
- **SQL Query**: Groups education records by `education_year` and counts achievements.

#### Education Year Distribution Report

```java:src/school/ReportsPage.java
private JPanel createEducationYearDistributionReport() {
    DefaultPieDataset dataset = new DefaultPieDataset();

    String query = "SELECT education_year, COUNT(*) AS count FROM Education GROUP BY education_year";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            int year = rs.getInt("education_year");
            int count = rs.getInt("count");
            dataset.setValue("Year " + year, count);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Education Year Distribution data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    JFreeChart pieChart = ChartFactory.createPieChart(
            "Distribution of Education Years",
            dataset,
            true, true, false
    );

    return new ChartPanel(pieChart);
}
```

- **Purpose**: Depicts the distribution of education years using a pie chart.
- **SQL Query**: Groups education records by `education_year` and counts the number in each year.

### Health Reports

#### Health Monitoring Report

```java:src/school/ReportsPage.java
private JPanel createHealthMonitoringReport() {
    DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    String query = "SELECT date_of_recording, COUNT(*) AS health_records FROM Health GROUP BY date_of_recording";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            Date date = rs.getDate("date_of_recording");
            int count = rs.getInt("health_records");
            dataset.addValue(count, "Health Records", date.toString());
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Health Monitoring data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    JFreeChart lineChart = ChartFactory.createLineChart(
            "Health Monitoring Over Time",
            "Date",
            "Number of Health Records",
            dataset
    );

    return new ChartPanel(lineChart);
}
```

- **Purpose**: Displays health records over time using a line chart.
- **SQL Query**: Groups health records by `date_of_recording` and counts the number per date.

### Time Series Reports

#### Family Prosperity Over Time Report

```java:src/school/ReportsPage.java
private JPanel createFamilyProsperityOverTimeReport() {
    TimeSeries series = new TimeSeries("Family Prosperity");

    String query = "SELECT YEAR(date_of_recording) AS year, AVG(prosperity_index) AS avg_prosperity " +
                   "FROM Prosperity GROUP BY YEAR(date_of_recording) ORDER BY year";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            int year = rs.getInt("year");
            double avgProsperity = rs.getDouble("avg_prosperity");
            series.add(new Year(year), avgProsperity);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Family Prosperity data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    TimeSeriesCollection dataset = new TimeSeriesCollection(series);
    JFreeChart timeSeriesChart = ChartFactory.createTimeSeriesChart(
            "Family Prosperity Over Time",
            "Year",
            "Average Prosperity Index",
            dataset,
            true, true, false
    );

    return new ChartPanel(timeSeriesChart);
}
```

- **Purpose**: Illustrates the trend of family prosperity over the years using a time series chart.
- **SQL Query**: Calculates the average `prosperity_index` per year.

#### Child Education Scores Over Time Report

```java:src/school/ReportsPage.java
private JPanel createChildEducationScoresOverTimeReport() {
    TimeSeries series = new TimeSeries("Child Education Scores");

    String query = "SELECT education_year, AVG(performance_index) AS avg_score " +
                   "FROM Education GROUP BY education_year ORDER BY education_year";

    try (Connection conn = login2.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(query)) {

        while (rs.next()) {
            int year = rs.getInt("education_year");
            double avgScore = rs.getDouble("avg_score");
            series.add(new Year(year), avgScore);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error fetching Child Education Scores data: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
    }

    TimeSeriesCollection dataset = new TimeSeriesCollection(series);
    JFreeChart timeSeriesChart = ChartFactory.createTimeSeriesChart(
            "Child Education Scores Over Time",
            "Year",
            "Average Education Score",
            dataset,
            true, true, false
    );

    return new ChartPanel(timeSeriesChart);
}
```

- **Purpose**: Shows the progression of child education scores over several years using a time series chart.
- **SQL Query**: Calculates the average `performance_index` per `education_year`.

---

## Database Interaction

All report generation methods interact with the database using JDBC. The following steps are common across these methods:

1. **Establish Connection**: Uses `login2.getConnection()` to connect to the database.
2. **Create Statement**: Creates a `Statement` object to execute SQL queries.
3. **Execute Query**: Runs the SQL query to fetch data.
4. **Process ResultSet**: Iterates through the `ResultSet` to extract data.
5. **Populate Dataset**: Adds the extracted data to a dataset compatible with JFreeChart.
6. **Handle Exceptions**: Catches `SQLException` and displays error messages to the user.

**Example: Establishing Connection and Executing Query**

```java:src/school/ReportsPage.java
try (Connection conn = login2.getConnection();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery(query)) {

    while (rs.next()) {
        // Process data
    }

} catch (SQLException e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Error fetching data: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
}
```

---

## Error Handling

Each method includes robust error handling to ensure that any issues during data retrieval or chart generation are gracefully managed.

- **Try-With-Resources**: Ensures that database resources are closed automatically.
- **Exception Logging**: Prints stack traces to the console for debugging purposes.
- **User Notifications**: Displays `JOptionPane` dialogs to inform users of any errors encountered.

**Example: Error Handling in Data Retrieval**

```java:src/school/ReportsPage.java
catch (SQLException e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(this, "Error fetching Child Demographics data: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
}
```

---

## Chart Customization

The `ReportsPage` class leverages JFreeChart's customization capabilities to create informative and visually appealing charts.

- **Chart Types**: Utilizes various chart types including PieChart, BarChart, LineChart, and TimeSeriesChart.
- **Titles and Labels**: Each chart includes descriptive titles and axis labels for clarity.
- **Legends**: Legends are enabled to help users interpret the data.
- **Chart Panels**: Integrates charts into the UI using `ChartPanel`, which allows for interactive features like tooltips and zooming.

**Example: Creating a Pie Chart**

```java:src/school/ReportsPage.java
JFreeChart pieChart = ChartFactory.createPieChart(
        "Gender Distribution of Children",
        dataset,
        true, true, false
);
```

- **Parameters**:
    - **Title**: "Gender Distribution of Children"
    - **Dataset**: Data populated from the SQL query.
    - **Legend**: Enabled.
    - **Tooltips**: Enabled.
    - **URLs**: Disabled (set to `false`).

---

**Key Highlights:**

- **Modular Design**: Separates concerns by categorizing reports into distinct panels and methods.
- **Dynamic Data Handling**: Fetches and processes data in real-time from the database to ensure up-to-date reporting.
- **User-Friendly UI**: Employs intuitive UI components like tabbed panes and interactive charts for enhanced user experience.
- **Robust Error Handling**: Ensures system stability by managing exceptions and informing users of any issues.
