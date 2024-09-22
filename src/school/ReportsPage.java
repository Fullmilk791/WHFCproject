package school;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;

/**
 * ReportsPage provides various data visualizations based on the database schema.
 */
public class ReportsPage extends JFrame {

    public ReportsPage() {
        setTitle("Reports");
        setSize(1000, 800); // Increased size for better visibility
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create Main Tabbed Pane for Categories
        JTabbedPane mainTabbedPane = new JTabbedPane();

        // Category 1: Child Reports
        JPanel childReportsPanel = createChildReportsPanel();
        mainTabbedPane.addTab("Child Reports", childReportsPanel);

        // Category 2: Family Reports
        JPanel familyReportsPanel = createFamilyReportsPanel();
        mainTabbedPane.addTab("Family Reports", familyReportsPanel);

        // Category 3: Program Reports
        JPanel programReportsPanel = createProgramReportsPanel();
        mainTabbedPane.addTab("Program Reports", programReportsPanel);

        // Category 4: Education Reports
        JPanel educationReportsPanel = createEducationReportsPanel();
        mainTabbedPane.addTab("Education Reports", educationReportsPanel);

        // Category 5: Health Reports
        JPanel healthReportsPanel = createHealthReportsPanel();
        mainTabbedPane.addTab("Health Reports", healthReportsPanel);

        // Category 6: Time Series Reports
        JPanel timeSeriesReportsPanel = createTimeSeriesReportsPanel();
        mainTabbedPane.addTab("Time Series Reports", timeSeriesReportsPanel);

        add(mainTabbedPane, BorderLayout.CENTER);
    }

    /**
     * Creates the Child Reports panel with subcategories.
     */
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

    /**
     * Creates the Family Reports panel with subcategories.
     */
    private JPanel createFamilyReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Subcategories as Tabbed Pane
        JTabbedPane subTabbedPane = new JTabbedPane();

        // Subcategory 1: Prosperity
        JPanel prosperityPanel = createFamilyProsperityReport();
        subTabbedPane.addTab("Prosperity", prosperityPanel);

        // Subcategory 2: Family Members
        JPanel familyMembersPanel = createFamilyMembersReport();
        subTabbedPane.addTab("Family Members", familyMembersPanel);

        // Add more subcategories as needed...

        panel.add(subTabbedPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates the Program Reports panel with subcategories.
     */
    private JPanel createProgramReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Subcategories as Tabbed Pane
        JTabbedPane subTabbedPane = new JTabbedPane();

        // Subcategory 1: Beneficiaries per Program
        JPanel beneficiariesPanel = createBeneficiariesPerProgramReport();
        subTabbedPane.addTab("Beneficiaries per Program", beneficiariesPanel);

        // Subcategory 2: Program Participation Duration
        JPanel participationDurationPanel = createProgramParticipationDurationReport();
        subTabbedPane.addTab("Participation Duration", participationDurationPanel);

        // Add more subcategories as needed...

        panel.add(subTabbedPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates the Education Reports panel with subcategories.
     */
    private JPanel createEducationReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Subcategories as Tabbed Pane
        JTabbedPane subTabbedPane = new JTabbedPane();

        // Subcategory 1: Student Achievements
        JPanel achievementsPanel = createStudentAchievementsReport();
        subTabbedPane.addTab("Student Achievements", achievementsPanel);

        // Subcategory 2: Education Year Distribution
        JPanel educationYearPanel = createEducationYearDistributionReport();
        subTabbedPane.addTab("Education Year Distribution", educationYearPanel);

        // Add more subcategories as needed...

        panel.add(subTabbedPane, BorderLayout.CENTER);
        return panel;
    }

    /**
     * Creates the Health Reports panel with subcategories.
     */
    private JPanel createHealthReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Subcategories as Tabbed Pane
        JTabbedPane subTabbedPane = new JTabbedPane();

        // Subcategory 1:

        // Subcategory 2: Health Monitoring
        JPanel healthMonitoringPanel = createHealthMonitoringReport();
        subTabbedPane.addTab("Health Monitoring", healthMonitoringPanel);

        // Add more subcategories as needed...

        panel.add(subTabbedPane, BorderLayout.CENTER);
        return panel;
    }

    // ==================== Child Reports ====================

    /**
     * Creates a pie chart showing the gender distribution of children.
     */
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

    /**
     * Creates a bar chart showing the enrollment status of children.
     */
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

    // ==================== Family Reports ====================

    /**
     * Creates a bar chart showing the prosperity status of families.
     */
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

    /**
     * Creates a pie chart showing the distribution of family sizes.
     */
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

    // ==================== Program Reports ====================

    /**
     * Creates a bar chart showing the number of beneficiaries per program.
     */
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

    /**
     * Creates a line chart showing the average participation duration in programs.
     */
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

    // ==================== Education Reports ====================

    /**
     * Creates a bar chart showing student achievements over the years.
     */
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

    /**
     * Creates a pie chart showing the distribution of education years.
     */
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

    // ==================== Health Reports ====================

    /**
     * Creates a line chart showing health monitoring over time.
     */
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

    /**
     * Creates the Time Series Reports panel with subcategories.
     */
    private JPanel createTimeSeriesReportsPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Subcategories as Tabbed Pane
        JTabbedPane subTabbedPane = new JTabbedPane();

        // Subcategory 1: Family Prosperity Over Time
        JPanel familyProsperityOverTimePanel = createFamilyProsperityOverTimeReport();
        subTabbedPane.addTab("Family Prosperity Over Time", familyProsperityOverTimePanel);

        // Subcategory 2: Child Education Scores Over Time
        JPanel childEducationScoresOverTimePanel = createChildEducationScoresOverTimeReport();
        subTabbedPane.addTab("Child Education Scores Over Time", childEducationScoresOverTimePanel);

        panel.add(subTabbedPane, BorderLayout.CENTER);
        return panel;
    }

    // ==================== Time Series Reports ====================

    /**
     * Creates a time series chart showing how family prosperity is increasing over time.
     */
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
    
    /**
     * Creates a time series chart showing child education scores over time.
     */
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
}