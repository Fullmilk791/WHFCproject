package school;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.awt.*;

public class ManagerDashboard extends JFrame {
    private JMenuBar menuBar;
    private JMenu menuBeneficiaries, menuGuardians, menuLocations, menuFamilyStatus, menuEducation, menuIncome, menuPrograms, menuChildren, menuSiblings, menuUsers;
    private JMenuItem menuItemViewBeneficiaries, menuItemAddBeneficiary;
    private JMenuItem menuItemViewGuardians, menuItemAddGuardian;
    private JMenuItem menuItemViewLocations, menuItemAddLocation;
    private JMenuItem menuItemViewFamilyStatus, menuItemAddFamilyStatus;
    private JMenuItem menuItemViewEducation, menuItemAddEducation;
    private JMenuItem menuItemViewIncome, menuItemAddIncome;
    private JMenuItem menuItemViewPrograms, menuItemAddProgram;
    private JMenuItem menuItemViewChildren, menuItemAddChild;
    private JMenuItem menuItemViewSiblings, menuItemAddSibling;
    private JMenuItem menuItemViewUsers, menuItemAddUser;
    private JPanel mainPanel;
    
    private void viewTable(String tableName, String[] joinTables) {
        JFrame viewFrame = new JFrame("View " + tableName);
        viewFrame.setSize(800, 600);
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);

        StringBuilder query = new StringBuilder("SELECT * FROM " + tableName + " t1");

        if (joinTables != null) {
            int aliasIndex = 2; // Start alias index from 2 since t1 is used for the main table
            for (String joinTable : joinTables) {
                String joinAlias = "t" + aliasIndex;
                String joinColumn = "id";
                String baseColumn = joinTable + "_id";
                query.append(" JOIN ").append(joinTable).append(" ").append(joinAlias)
                     .append(" ON t1.").append(baseColumn).append(" = ").append(joinAlias).append(".").append(joinColumn);
                aliasIndex++;
            }
        }

        try (Connection c = login2.getConnection();
             Statement stmt = c.createStatement();
             ResultSet rs = stmt.executeQuery(query.toString())) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add column names to the table model
            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            // Add rows to the table model
            while (rs.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = rs.getObject(i);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(table);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Disable auto-resizing to enable horizontal scrolling
        viewFrame.add(scrollPane);
        viewFrame.setVisible(true);
    }

    private void viewBeneficiaries() {
        JFrame viewFrame = new JFrame("View Beneficiaries");
        viewFrame.setSize(800, 600);
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        String[] columnNames = { "Beneficiary ID", "Child Name", "Guardian Name", "Relation to Guardian", "Location",
                "Family Status", "Education", "Income", "Sex", "Year of Birth", "Age", "Year Joined", "Year of Stay",
                "Current Grade" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(model);

        try (Connection c = login2.getConnection();
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(
                        // Select the necessary columns from the joined tables
                        "SELECT " +
                                "b.beneficiary_id, " + // Beneficiary ID from the Beneficiary table
                                "c.name AS child_name, " + // Child's name from the Child table
                                "g.name AS guardian_name, " + // Guardian's name from the Guardian table
                                "c.relation_to_guardian, " + // Relation of the child to the guardian from the Child
                                                             // table
                                "CONCAT(l.sub_city, ', ', l.village) AS location, " + // Concatenated location (sub_city
                                                                                      // and village) from the Location
                                                                                      // table
                                "fs.status AS family_status, " + // Family status from the FamilyStatus table
                                "e.status AS education, " + // Education status from the Education table
                                "i.income_source, " + // Income source from the Income table
                                "c.sex, " + // Child's sex from the Child table
                                "c.yob, " + // Child's year of birth from the Child table
                                "c.year_joined, " + // Year the child joined from the Child table
                                "c.year_of_stay, " + // Year of stay from the Child table
                                "c.current_grade " + // Current grade of the child from the Child table
                                "FROM Child c " + // Main table is Child, aliased as 'c'

                                "JOIN Beneficiary b ON c.beneficiary_id = b.id " + // Join with Beneficiary table on
                                                                                   // beneficiary_id
                                "JOIN Guardian g ON c.guardian_id = g.id " + // Join with Guardian table on guardian_id
                                "JOIN Location l ON c.location_id = l.id " + // Join with Location table on location_id
                                "JOIN Family_Status fs ON c.family_status_id = fs.id " + // Join with FamilyStatus table
                                                                                        // on family_status_id
                                "JOIN Education e ON c.education_id = e.id " + // Join with Education table on
                                                                               // education_id
                                "JOIN Income i ON c.income_id = i.id " + // Join with Income table on income_id
                                "ORDER BY b.beneficiary_id" // Order the results by beneficiary_id
                )) {

            while (rs.next()) {
                String beneficiaryId = rs.getString("beneficiary_id");
                String childName = rs.getString("child_name");
                String guardianName = rs.getString("guardian_name");
                String relationToGuardian = rs.getString("relation_to_guardian");
                String location = rs.getString("location");
                String familyStatus = rs.getString("family_status");
                String education = rs.getString("education");
                String incomeSource = rs.getString("income_source");
                String sex = rs.getString("sex");
                Date yob = rs.getDate("yob");
                int age = rs.getInt("age");
                Date yearJoined = rs.getDate("year_joined");
                int yearOfStay = rs.getInt("year_of_stay");
                String currentGrade = rs.getString("current_grade");

                model.addRow(new Object[] { beneficiaryId, childName, guardianName, relationToGuardian, location,
                        familyStatus, education, incomeSource, sex, yob, age, yearJoined, yearOfStay, currentGrade });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(table);
        viewFrame.add(scrollPane);
        viewFrame.setVisible(true);
    }

    private void addBeneficiary() {
        JFrame addFrame = new JFrame("Add Beneficiary");
        addFrame.setSize(400, 200);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    
        JTextField beneficiaryIdField = new JTextField(20);
        JButton addButton = new JButton("Add");
    
        panel.add(new JLabel("Beneficiary ID:"));
        panel.add(beneficiaryIdField);
        panel.add(addButton);
    
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String beneficiaryId = beneficiaryIdField.getText();
                try (Connection c = login2.getConnection();
                     PreparedStatement stmt = c.prepareStatement("INSERT INTO Beneficiary (beneficiary_id) VALUES (?)")) {
                    stmt.setString(1, beneficiaryId);
                    stmt.executeUpdate();
                    JOptionPane.showMessageDialog(addFrame, "Beneficiary added successfully!");
                    addFrame.dispose();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
    
        addFrame.add(panel);
        addFrame.setVisible(true);
    }
    
    private void addChildToBeneficiary() {
        JFrame addFrame = new JFrame("Add Child to Beneficiary");
        addFrame.setSize(400, 400);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        JTextField beneficiaryIdField = new JTextField(20);
        JTextField childNameField = new JTextField(20);
        JTextField guardianNameField = new JTextField(20);
        JTextField relationToGuardianField = new JTextField(20);
        JTextField subCityField = new JTextField(20);
        JTextField villageField = new JTextField(20);

        JFormattedTextField yobField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        yobField.setColumns(20);
        JFormattedTextField yearJoinedField = new JFormattedTextField(new SimpleDateFormat("yyyy-MM-dd"));
        yearJoinedField.setColumns(20);

        JTextField yearOfStayField = new JTextField(20);
        JTextField currentGradeField = new JTextField(20);
        JButton addButton = new JButton("Add");

        // Dropdowns for enums
        String[] familyStatusOptions = {"Both parents", "Divorced", "Orphan", "Half orphan"};
        JComboBox<String> familyStatusDropdown = new JComboBox<>(familyStatusOptions);

        String[] educationOptions = {"Illiterate", "Primary", "Secondary", "Diploma", "Above"};
        JComboBox<String> educationDropdown = new JComboBox<>(educationOptions);

        JTextField incomeSourceField = new JTextField(20);

        String[] sexOptions = {"M", "F"};
        JComboBox<String> sexDropdown = new JComboBox<>(sexOptions);

        panel.add(new JLabel("Beneficiary ID:"));
        panel.add(beneficiaryIdField);
        panel.add(new JLabel("Child Name:"));
        panel.add(childNameField);
        panel.add(new JLabel("Guardian Name:"));
        panel.add(guardianNameField);
        panel.add(new JLabel("Relation to Guardian:"));
        panel.add(relationToGuardianField);
        panel.add(new JLabel("Sub City:"));
        panel.add(subCityField);
        panel.add(new JLabel("Village:"));
        panel.add(villageField);
        panel.add(new JLabel("Family Status:"));
        panel.add(familyStatusDropdown);
        panel.add(new JLabel("Education:"));
        panel.add(educationDropdown);
        panel.add(new JLabel("Income Source:"));
        panel.add(incomeSourceField);
        panel.add(new JLabel("Sex (M/F):"));
        panel.add(sexDropdown);
        panel.add(new JLabel("Year of Birth (YYYY-MM-DD):"));
        panel.add(yobField);
        panel.add(new JLabel("Year Joined (YYYY-MM-DD):"));
        panel.add(yearJoinedField);
        panel.add(new JLabel("Year of Stay:"));
        panel.add(yearOfStayField);
        panel.add(new JLabel("Current Grade:"));
        panel.add(currentGradeField);
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Connection c = null;
                try {
                    c = login2.getConnection();
                    c.setAutoCommit(false);

                    // Insert Guardian
                    String guardianName = guardianNameField.getText();
                    int guardianId;
                    try (PreparedStatement stmt = c.prepareStatement(
                            "INSERT INTO Guardian (name) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setString(1, guardianName);
                        stmt.executeUpdate();
                        ResultSet rs = stmt.getGeneratedKeys();
                        if (rs.next()) {
                            guardianId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to retrieve guardian ID.");
                        }
                    }

                    // Insert Location
                    String subCity = subCityField.getText();
                    String village = villageField.getText();
                    int locationId;
                    try (PreparedStatement stmt = c.prepareStatement(
                            "INSERT INTO Location (sub_city, village) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setString(1, subCity);
                        stmt.setString(2, village);
                        stmt.executeUpdate();
                        ResultSet rs = stmt.getGeneratedKeys();
                        if (rs.next()) {
                            locationId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to retrieve location ID.");
                        }
                    }

                    // Insert FamilyStatus
                    String familyStatus = (String) familyStatusDropdown.getSelectedItem();
                    int familyStatusId;
                    try (PreparedStatement stmt = c.prepareStatement(
                            "INSERT INTO Family_Status (status) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setString(1, familyStatus);
                        stmt.executeUpdate();
                        ResultSet rs = stmt.getGeneratedKeys();
                        if (rs.next()) {
                            familyStatusId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to retrieve family status ID.");
                        }
                    }

                    // Insert Education
                    String education = (String) educationDropdown.getSelectedItem();
                    int educationId;
                    try (PreparedStatement stmt = c.prepareStatement(
                            "INSERT INTO Education (status) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setString(1, education);
                        stmt.executeUpdate();
                        ResultSet rs = stmt.getGeneratedKeys();
                        if (rs.next()) {
                            educationId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to retrieve education ID.");
                        }
                    }

                    // Insert Income
                    String incomeSource = incomeSourceField.getText();
                    int incomeId;
                    try (PreparedStatement stmt = c.prepareStatement(
                            "INSERT INTO Income (income_source) VALUES (?)", Statement.RETURN_GENERATED_KEYS)) {
                        stmt.setString(1, incomeSource);
                        stmt.executeUpdate();
                        ResultSet rs = stmt.getGeneratedKeys();
                        if (rs.next()) {
                            incomeId = rs.getInt(1);
                        } else {
                            throw new SQLException("Failed to retrieve income ID.");
                        }
                    }

                    // Insert Child
                    try (PreparedStatement stmt = c.prepareStatement(
                            "INSERT INTO Child (beneficiary_id, name, guardian_id, relation_to_guardian, location_id, family_status_id, education_id, income_id, sex, yob, year_joined, year_of_stay, current_grade) " +
                                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
                        stmt.setInt(1, Integer.parseInt(beneficiaryIdField.getText()));
                        stmt.setString(2, childNameField.getText());
                        stmt.setInt(3, guardianId);
                        stmt.setString(4, relationToGuardianField.getText());
                        stmt.setInt(5, locationId);
                        stmt.setInt(6, familyStatusId);
                        stmt.setInt(7, educationId);
                        stmt.setInt(8, incomeId);
                        stmt.setString(9, (String) sexDropdown.getSelectedItem());
                        stmt.setDate(10, Date.valueOf(yobField.getText()));
                        stmt.setDate(11, Date.valueOf(yearJoinedField.getText()));
                        stmt.setInt(12, Integer.parseInt(yearOfStayField.getText()));
                        stmt.setString(13, currentGradeField.getText());
                        stmt.executeUpdate();
                    }

                    c.commit();
                    JOptionPane.showMessageDialog(addFrame, "Record inserted successfully!", "Success",
                    JOptionPane.INFORMATION_MESSAGE);
                    addFrame.dispose();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(addFrame, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        addFrame.add(panel);
        addFrame.pack(); // Adjust window size to fit content
        addFrame.setVisible(true);
    }


    public ManagerDashboard() {
        setTitle("Manager Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create the top menu bar
        menuBar = new JMenuBar();

        // Beneficiaries Menu
        menuBeneficiaries = new JMenu("Beneficiaries");
        menuItemViewBeneficiaries = new JMenuItem("View Beneficiaries");
        menuItemAddBeneficiary = new JMenuItem("Add Beneficiary");
        menuBeneficiaries.add(menuItemViewBeneficiaries);
        menuBeneficiaries.add(menuItemAddBeneficiary);
        menuBar.add(menuBeneficiaries);

        // Guardians Menu
        menuGuardians = new JMenu("Guardians");
        menuItemViewGuardians = new JMenuItem("View Guardians");
        menuItemAddGuardian = new JMenuItem("Add Guardian");
        menuGuardians.add(menuItemViewGuardians);
        menuGuardians.add(menuItemAddGuardian);
        menuBar.add(menuGuardians);

        // Locations Menu
        menuLocations = new JMenu("Locations");
        menuItemViewLocations = new JMenuItem("View Locations");
        menuItemAddLocation = new JMenuItem("Add Location");
        menuLocations.add(menuItemViewLocations);
        menuLocations.add(menuItemAddLocation);
        menuBar.add(menuLocations);

        // Family Status Menu
        menuFamilyStatus = new JMenu("Family Status");
        menuItemViewFamilyStatus = new JMenuItem("View Family Status");
        menuItemAddFamilyStatus = new JMenuItem("Add Family Status");
        menuFamilyStatus.add(menuItemViewFamilyStatus);
        menuFamilyStatus.add(menuItemAddFamilyStatus);
        menuBar.add(menuFamilyStatus);

        // Education Menu
        menuEducation = new JMenu("Education");
        menuItemViewEducation = new JMenuItem("View Education");
        menuItemAddEducation = new JMenuItem("Add Education");
        menuEducation.add(menuItemViewEducation);
        menuEducation.add(menuItemAddEducation);
        menuBar.add(menuEducation);

        // Income Menu
        menuIncome = new JMenu("Income");
        menuItemViewIncome = new JMenuItem("View Income");
        menuItemAddIncome = new JMenuItem("Add Income");
        menuIncome.add(menuItemViewIncome);
        menuIncome.add(menuItemAddIncome);
        menuBar.add(menuIncome);

        // Programs Menu
        menuPrograms = new JMenu("Programs");
        menuItemViewPrograms = new JMenuItem("View Programs");
        menuItemAddProgram = new JMenuItem("Add Program");
        menuPrograms.add(menuItemViewPrograms);
        menuPrograms.add(menuItemAddProgram);
        menuBar.add(menuPrograms);

        // Children Menu
        menuChildren = new JMenu("Children");
        menuItemViewChildren = new JMenuItem("View Children");
        menuItemAddChild = new JMenuItem("Add Child");
        menuChildren.add(menuItemViewChildren);
        menuChildren.add(menuItemAddChild);
        menuBar.add(menuChildren);

        // Siblings Menu
        menuSiblings = new JMenu("Siblings");
        menuItemViewSiblings = new JMenuItem("View Siblings");
        menuItemAddSibling = new JMenuItem("Add Sibling");
        menuSiblings.add(menuItemViewSiblings);
        menuSiblings.add(menuItemAddSibling);
        menuBar.add(menuSiblings);

        // Users Menu
        menuUsers = new JMenu("Users");
        menuItemViewUsers = new JMenuItem("View Users");
        menuItemAddUser = new JMenuItem("Add User");
        menuUsers.add(menuItemViewUsers);
        menuUsers.add(menuItemAddUser);
        menuBar.add(menuUsers);

        setJMenuBar(menuBar);

        // Create the main panel
        mainPanel = new JPanel(new BorderLayout());

        // Add an image and a logout button to the main panel
        JLabel imageLabel = new JLabel(new ImageIcon("assets/images/logo.png"));
        JButton logoutButton = new JButton("Logout");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(imageLabel, BorderLayout.CENTER);
        topPanel.add(logoutButton, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.CENTER);

        // Add the main panel to the frame
        getContentPane().add(mainPanel, BorderLayout.CENTER);

        //action listeners for the menu items
        menuItemViewBeneficiaries.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewBeneficiaries();
            }
        });

        menuItemAddBeneficiary.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addBeneficiary();
            }
        });

        menuItemAddChild.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addChildToBeneficiary();
            }
        });

        menuItemViewGuardians.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Guardian", null);
            }
        });

        menuItemAddGuardian.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement addGuardian();
            }
        });

        menuItemViewLocations.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Location", null);
            }
        });

        menuItemAddLocation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement addLocation();
            }
        });

        menuItemViewFamilyStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Family_Status", null);
            }
        });

        menuItemAddFamilyStatus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement addFamilyStatus();
            }
        });

        menuItemViewEducation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Education", null);
            }
        });

        menuItemAddEducation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement addEducation();
            }
        });

        menuItemViewIncome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Income", null);
            }
        });

        menuItemAddIncome.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement addIncome();
            }
        });

        menuItemViewPrograms.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Program", null);
            }
        });

        menuItemAddProgram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement addProgram();
            }
        });

        menuItemViewChildren.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Child", new String[]{"Beneficiary", "Guardian", "Location", "Family_Status", "Education", "Income", "program"});
            }
        });

        menuItemAddChild.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement addChild();
            }
        });

        menuItemViewSiblings.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Sibling", new String[]{"Child"});
            }
        });

        menuItemAddSibling.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement addSibling();
            }
        });

        menuItemViewUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Users", null);
            }
        });

        menuItemAddUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Implement addUser();
            }
        });

        // Add action listener for the logout button
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // go to login page
                login2 LoginFrame;
                LoginFrame = new login2();
                LoginFrame.setVisible(true);
                LoginFrame.setLocationRelativeTo(null);
                dispose(); // Close the signup window
            }
        });
    }

    public static void main(String[] args) {
        ManagerDashboard dashboard = new ManagerDashboard();
        dashboard.setVisible(true);
    }
}