package school;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.awt.*;
import school.ReportsPage;

public class ManagerDashboard extends JFrame {
    private JMenuBar menuBar;
    private JMenu menuBeneficiaries, menuGuardians, menuLocations, menuFamilies, menuPrograms,
            menuChildren, menuHealth, menuProsperity, menuEducation, menuUsers, menuFamilyMembers;
    private JMenuItem menuItemViewBeneficiaries, menuItemAddBeneficiary;
    private JMenuItem menuItemViewGuardians, menuItemAddGuardian;
    private JMenuItem menuItemViewLocations, menuItemAddLocation;
    private JMenuItem menuItemViewFamilies, menuItemAddFamily;
    private JMenuItem menuItemViewPrograms, menuItemAddProgram;
    private JMenuItem menuItemViewChildren, menuItemAddChild;
    private JMenuItem menuItemViewHealth, menuItemAddHealth;
    private JMenuItem menuItemViewProsperity, menuItemAddProsperity;
    private JMenuItem menuItemViewEducation, menuItemAddEducation;
    private JMenuItem menuItemViewUsers, menuItemAddUser;
    private JMenuItem menuItemViewFamilyMembers, menuItemAddFamilyMember;
    private JPanel mainPanel;

    public interface RecordSelectionListener {
        void onRecordSelected(int selectedId, String displayValue);
    }

    private void viewTable(String tableName, String[] joinTables, RecordSelectionListener listener) {
        JFrame viewFrame = new JFrame("View " + tableName);
        viewFrame.setSize(1300, 700); // Increased size to accommodate search components
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        viewFrame.setLayout(new BorderLayout());

        // Create a panel for search
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel searchLabel = new JLabel("Search: ");
        JTextField searchField = new JTextField(30);
        JButton searchButton = new JButton("Search");
        JButton resetButton = new JButton("Reset");
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(resetButton);

        viewFrame.add(searchPanel, BorderLayout.NORTH);

        DefaultTableModel model = new DefaultTableModel();
        JTable table = new JTable(model);
        table.setAutoCreateRowSorter(true); // Enable sorting

        // RowSorter for filtering
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        JScrollPane scrollPane = new JScrollPane(table);
        viewFrame.add(scrollPane, BorderLayout.CENTER);

        // If in selection mode, add a Select button
        if (listener != null) {
            JButton selectButton = new JButton("Select");
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            buttonPanel.add(selectButton);
            viewFrame.add(buttonPanel, BorderLayout.SOUTH);

            selectButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int selectedRow = table.getSelectedRow();
                    if (selectedRow != -1) {
                        // Convert row index considering sorting
                        int modelRow = table.convertRowIndexToModel(selectedRow);
                        Object idValue = model.getValueAt(modelRow, 0); // ID must be in the first column
                        Object displayValue = model.getValueAt(modelRow, 1); // uses column next to ID as display value
                        if (idValue instanceof Integer) {
                            listener.onRecordSelected((Integer) idValue,
                                    displayValue != null ? displayValue.toString() : "");
                            viewFrame.dispose();
                        } else {
                            JOptionPane.showMessageDialog(viewFrame, "Invalid ID selected.", "Error",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(viewFrame, "Please select a record to proceed.", "No Selection",
                                JOptionPane.WARNING_MESSAGE);
                    }
                }
            });
        }

        StringBuilder query = new StringBuilder("SELECT * FROM " + tableName + " t1");

        if (joinTables != null) {
            int aliasIndex = 2; // Start alias index from 2 since t1 is used for the main table
            for (String joinTable : joinTables) {
                String joinAlias = "t" + aliasIndex;
                String joinColumn = "id"; // Primary key of the join table

                // Determine the foreign key column name
                String baseColumn = customForeignKeyMap.getOrDefault(joinTable, joinTable.toLowerCase() + "_id");

                query.append(" JOIN ").append(joinTable).append(" ").append(joinAlias)
                        .append(" ON t1.").append(baseColumn).append(" = ").append(joinAlias)
                        .append(".").append(joinColumn);
                aliasIndex++;
            }
        }

        try (Connection c = login2.getConnection();
                Statement stmt = c.createStatement();
                ResultSet rs = stmt.executeQuery(query.toString())) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            // Identify columns to include
            ArrayList<Integer> columnsToInclude = new ArrayList<>();
            ArrayList<String> columnNames = new ArrayList<>();

            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                String tableNameColumn = metaData.getTableName(i);

                // Include the main table's 'id'
                if ("id".equalsIgnoreCase(columnName) && tableNameColumn.equalsIgnoreCase(tableName)) {
                    columnsToInclude.add(i);
                    columnNames.add(columnName);
                }
                // Include all other columns except those ending with '_id' from joined tables
                else if (!"id".equalsIgnoreCase(columnName) || tableNameColumn.equalsIgnoreCase(tableName)) {
                    if (!columnName.toLowerCase().endsWith("_id")) {
                        columnsToInclude.add(i);
                        columnNames.add(tableNameColumn.equalsIgnoreCase(tableName) ? columnName
                                : tableNameColumn + "." + columnName);
                    }
                }
            }

            // Add column names to the table model
            for (String colName : columnNames) {
                model.addColumn(colName);
            }

            // Add rows to the table model
            while (rs.next()) {
                Object[] row = new Object[columnsToInclude.size()];
                for (int i = 0; i < columnsToInclude.size(); i++) {
                    row[i] = rs.getObject(columnsToInclude.get(i));
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(viewFrame, "Error fetching data: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        // Adjust column widths
        for (int col = 0; col < table.getColumnCount(); col++) {
            TableColumn tableColumn = table.getColumnModel().getColumn(col);
            TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
            Component headerComp = headerRenderer.getTableCellRendererComponent(table, tableColumn.getHeaderValue(),
                    false, false, 0, 0);
            int headerWidth = headerComp.getPreferredSize().width;
            tableColumn.setPreferredWidth(headerWidth + 20); // Add padding for better readability
        }

        // Search button action
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    sorter.setRowFilter(null);
                } else {
                    // (?i) for case-insensitive search
                    sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }
        });

        // Reset button action
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                sorter.setRowFilter(null);
            }
        });

        // Allow pressing Enter to trigger search
        searchField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchButton.doClick();
            }
        });

        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // Enable horizontal scrolling
        viewFrame.setVisible(true);
    }

    /**
     * Generic method to add a record to any specified table.
     *
     * @param tableName The name of the table to add a record to.
     */
    private void addRecord(String tableName) {
        JFrame addFrame = new JFrame("Add Record to " + tableName);
        addFrame.setSize(500, 600);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        java.util.Map<String, JComponent> fieldMap = new java.util.LinkedHashMap<>();
        java.util.Map<String, Integer> foreignKeyIds = new java.util.HashMap<>(); // To store selected foreign key IDs

        try (Connection c = login2.getConnection()) {
            DatabaseMetaData metaData = c.getMetaData();

            // Retrieve columns for the specified table
            ResultSet columns = metaData.getColumns(null, null, tableName, null);
            java.util.List<String> columnNames = new java.util.ArrayList<>();
            java.util.List<String> columnTypes = new java.util.ArrayList<>();
            java.util.List<String> foreignKeys = new java.util.ArrayList<>();

            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String dataType = columns.getString("TYPE_NAME");

                // Skip auto-increment primary keys
                try (ResultSet autoIncrementRS = metaData.getColumns(null, null, tableName, columnName)) {
                    boolean isAutoIncrement = false;
                    if (autoIncrementRS.next()) { // Move cursor to the first row
                        isAutoIncrement = "YES".equalsIgnoreCase(autoIncrementRS.getString("IS_AUTOINCREMENT"));
                    }
                    if (isAutoIncrement) {
                        continue;
                    } else {
                        columnNames.add(columnName);
                        columnTypes.add(dataType);
                    }
                }

                // Check if the column is a foreign key
                ResultSet fk = metaData.getImportedKeys(null, null, tableName);
                boolean isForeignKey = false;
                String fkTable = null;
                String fkColumn = null;
                while (fk.next()) {
                    String fkColumnName = fk.getString("FKCOLUMN_NAME");
                    if (fkColumnName.equals(columnName)) {
                        isForeignKey = true;
                        fkTable = fk.getString("PKTABLE_NAME");
                        fkColumn = fk.getString("PKCOLUMN_NAME");
                        break;
                    }
                }
                fk.close();
                if (isForeignKey) {
                    foreignKeys.add(fkTable);
                } else {
                    foreignKeys.add(null);
                }
            }
            columns.close();

            // Dynamically create input fields based on columns
            for (int i = 0; i < columnNames.size(); i++) {
                String colName = columnNames.get(i);
                String colType = columnTypes.get(i);
                String fkTable = foreignKeys.get(i);

                JLabel label = new JLabel(colName + ":");
                gbc.gridx = 0;
                gbc.weightx = 0.3;
                panel.add(label, gbc);

                JComponent field;

                if (fkTable != null) {
                    // Instead of JComboBox, use a button to open viewTable for selection
                    JPanel fkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
                    JTextField fkField = new JTextField(20);
                    fkField.setEditable(false);
                    JButton selectButton = new JButton("Select");
                    fkPanel.add(fkField);
                    fkPanel.add(selectButton);
                    field = fkPanel;

                    selectButton.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            viewTable(fkTable, null, new RecordSelectionListener() {
                                @Override
                                public void onRecordSelected(int selectedId, String displayValue) {
                                    fkField.setText("ID: " + selectedId + " (" + displayValue + " ...)");
                                    foreignKeyIds.put(colName, selectedId);
                                }
                            });
                        }
                    });

                } else if (colType.contains("INT")) {
                    field = new JTextField(20);
                } else if (colType.contains("DATE")) {
                    field = new JTextField(20);
                } else if (colType.contains("ENUM")) {
                    // Extract ENUM values
                    java.util.List<String> enumValues = getEnumValues(c, tableName, colName);
                    JComboBox<String> comboBox = new JComboBox<>(enumValues.toArray(new String[0]));
                    field = comboBox;
                } else if (colType.contains("BOOLEAN")) {
                    field = new JCheckBox();
                } else {
                    field = new JTextField(20);
                }

                fieldMap.put(colName, field);
                gbc.gridx = 1;
                gbc.weightx = 0.7;
                panel.add(field, gbc);
                gbc.gridy++;
            }

            // Add Submit Button
            JButton submitButton = new JButton("Add Record");
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            panel.add(submitButton, gbc);

            submitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    StringBuilder columnsPart = new StringBuilder();
                    StringBuilder valuesPart = new StringBuilder();
                    java.util.List<Object> values = new java.util.ArrayList<>();

                    for (java.util.Map.Entry<String, JComponent> entry : fieldMap.entrySet()) {
                        String colName = entry.getKey();
                        JComponent comp = entry.getValue();

                        columnsPart.append(colName).append(", ");

                        if (comp instanceof JPanel) { // Foreign key field
                            // Retrieve the selected ID from foreignKeyIds
                            Integer selectedId = foreignKeyIds.get(colName);
                            valuesPart.append("?, ");
                            values.add(selectedId);
                        } else if (comp instanceof JTextField) {
                            String text = ((JTextField) comp).getText();
                            valuesPart.append("?, ");
                            values.add(text.isEmpty() ? null : text);
                        } else if (comp instanceof JComboBox) {
                            Object selected = ((JComboBox<?>) comp).getSelectedItem();
                            valuesPart.append("?, ");
                            values.add(selected);
                        } else if (comp instanceof JCheckBox) {
                            boolean selected = ((JCheckBox) comp).isSelected();
                            valuesPart.append("?, ");
                            values.add(selected);
                        }
                    }

                    // Remove trailing commas
                    if (columnsPart.length() > 0)
                        columnsPart.setLength(columnsPart.length() - 2);
                    if (valuesPart.length() > 0)
                        valuesPart.setLength(valuesPart.length() - 2);

                    String sql = "INSERT INTO " + tableName + " (" + columnsPart + ") VALUES (" + valuesPart + ")";

                    try (Connection insertConn = login2.getConnection();
                            PreparedStatement pstmt = insertConn.prepareStatement(sql,
                                    Statement.RETURN_GENERATED_KEYS)) {
                        for (int i = 0; i < values.size(); i++) {
                            Object value = values.get(i);
                            if (value instanceof String) {
                                pstmt.setString(i + 1, (String) value);
                            } else if (value instanceof Integer) {
                                pstmt.setInt(i + 1, (Integer) value);
                            } else if (value instanceof Boolean) {
                                pstmt.setBoolean(i + 1, (Boolean) value);
                            } else {
                                pstmt.setObject(i + 1, value);
                            }
                        }

                        pstmt.executeUpdate();
                        ResultSet generatedKeys = pstmt.getGeneratedKeys();
                        if (generatedKeys.next()) {
                            JOptionPane.showMessageDialog(addFrame,
                                    "Record added successfully with ID: " + generatedKeys.getInt(1));
                        } else {
                            JOptionPane.showMessageDialog(addFrame, "Record added successfully.");
                        }
                        addFrame.dispose();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(addFrame, "Error adding record: " + ex.getMessage(), "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            });

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(addFrame, "Error retrieving table metadata: " + ex.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        addFrame.add(new JScrollPane(panel));
        addFrame.setVisible(true);
    }

    /**
     * Helper method to extract ENUM values from a column.
     *
     * @param connection The database connection.
     * @param tableName  The table name.
     * @param columnName The column name.
     * @return A list of ENUM values.
     * @throws SQLException If a database access error occurs.
     */
    private java.util.List<String> getEnumValues(Connection connection, String tableName, String columnName)
            throws SQLException {
        java.util.List<String> enumValues = new java.util.ArrayList<>();
        String sql = "SHOW COLUMNS FROM " + tableName + " LIKE '" + columnName + "'";
        try (Statement stmt = connection.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                String type = rs.getString("Type"); // e.g., enum('value1','value2')
                int start = type.indexOf('(');
                int end = type.lastIndexOf(')');
                if (start > -1 && end > -1 && end > start) {
                    String enumValuesStr = type.substring(start + 1, end);
                    String[] values = enumValuesStr.split(",");
                    for (String value : values) {
                        enumValues.add(value.trim().replace("'", ""));
                    }
                }
            }
        }
        return enumValues;
    }


    // mapping for custom foreign key column names
    private Map<String, String> customForeignKeyMap;

    public ManagerDashboard() {
        setTitle("Manager Dashboard");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Initialize custom foreign key maps
        customForeignKeyMap = new HashMap<>();
        customForeignKeyMap.put("Users", "recorded_by");
        customForeignKeyMap.put("Family_Member", "guardian_id");

        // Create the top menu bar
        menuBar = new JMenuBar();

        // Reports Menu
        JMenu menuReports = new JMenu("Reports");
        JMenuItem menuItemViewReports = new JMenuItem("View Reports");
        menuReports.add(menuItemViewReports);
        menuBar.add(menuReports);

        // Add action listener for the Reports menu item
        menuItemViewReports.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReportsPage reportsPage = new ReportsPage();
                reportsPage.setVisible(true);
            }
        });

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

        // Families Menu
        menuFamilies = new JMenu("Families");
        menuItemViewFamilies = new JMenuItem("View Families");
        menuItemAddFamily = new JMenuItem("Add Family");
        menuFamilies.add(menuItemViewFamilies);
        menuFamilies.add(menuItemAddFamily);
        menuBar.add(menuFamilies);

        // Family Members Menu
        menuFamilyMembers = new JMenu("Family Members");
        menuItemViewFamilyMembers = new JMenuItem("View Family Members");
        menuItemAddFamilyMember = new JMenuItem("Add Family Member");
        menuFamilyMembers.add(menuItemViewFamilyMembers);
        menuFamilyMembers.add(menuItemAddFamilyMember);
        menuBar.add(menuFamilyMembers);

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

        // Health Menu
        menuHealth = new JMenu("Health");
        menuItemViewHealth = new JMenuItem("View Health Records");
        menuItemAddHealth = new JMenuItem("Add Health Record");
        menuHealth.add(menuItemViewHealth);
        menuHealth.add(menuItemAddHealth);
        menuBar.add(menuHealth);

        // Prosperity Menu
        menuProsperity = new JMenu("Prosperity");
        menuItemViewProsperity = new JMenuItem("View Prosperity Records");
        menuItemAddProsperity = new JMenuItem("Add Prosperity Record");
        menuProsperity.add(menuItemViewProsperity);
        menuProsperity.add(menuItemAddProsperity);
        menuBar.add(menuProsperity);

        // Education Menu
        menuEducation = new JMenu("Education");
        menuItemViewEducation = new JMenuItem("View Education Records");
        menuItemAddEducation = new JMenuItem("Add Education Record");
        menuEducation.add(menuItemViewEducation);
        menuEducation.add(menuItemAddEducation);
        menuBar.add(menuEducation);

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

        // Action listeners for the menu items

        // Beneficiaries
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

        // Guardians
        menuItemViewGuardians.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Family_Member", null, null);
            }
        });

        menuItemAddGuardian.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord("Family_Member");
            }
        });

        // Locations
        menuItemViewLocations.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Location", null, null);
            }
        });

        menuItemAddLocation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord("Location");
            }
        });

        // Families
        menuItemViewFamilies.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Family", null, null);
            }
        });

        menuItemAddFamily.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord("Family");
            }
        });

        // Family Members
        menuItemViewFamilyMembers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Family_Member", new String[] { "Family" }, null);
            }
        });

        menuItemAddFamilyMember.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord("Family_Member");
            }
        });

        // Programs
        menuItemViewPrograms.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Program", null, null);
            }
        });

        menuItemAddProgram.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord("Program");
            }
        });

        // Children
        menuItemViewChildren.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Child", new String[] { "Beneficiary", "Family", "Family_Member", "Location", "Program" }, null);
            }
        });

        menuItemAddChild.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord("Child");
            }
        });

        // Health Records
        menuItemViewHealth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Health", new String[] { "Child" }, null);
            }
        });

        menuItemAddHealth.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord("Health");
            }
        });

        // Prosperity Records
        menuItemViewProsperity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Prosperity", new String[] { "Family" }, null);
            }
        });

        menuItemAddProsperity.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord("Prosperity");
            }
        });

        // Education Records
        menuItemViewEducation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Education", new String[] { "Child" }, null);
            }
        });

        menuItemAddEducation.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord("Education");
            }
        });

        // Users
        menuItemViewUsers.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                viewTable("Users", null, null);
            }
        });

        menuItemAddUser.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addRecord("Users");
            }
        });

        // Add action listener for the logout button
        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Go to login page
                login2 LoginFrame;
                LoginFrame = new login2();
                LoginFrame.setVisible(true);
                LoginFrame.setLocationRelativeTo(null);
                dispose(); // Close the current window
            }
        });
    }

    public static void main(String[] args) {
        ManagerDashboard dashboard = new ManagerDashboard();
        dashboard.setVisible(true);
    }
}