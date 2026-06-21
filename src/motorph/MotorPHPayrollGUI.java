/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.io.File;

public class MotorPHPayrollGUI extends JFrame {

    private JTable tblEmployees;
    private DefaultTableModel tableModel;
    
    private JTextField regId, regLastName, regFirstName, regBday, regRate;
    private JTextField regSss, regPhilHealth, regTin, regPagIbig;
    private JButton btnSaveRecord, btnUpdateRecord, btnDeleteRecord, btnClearForm;
    
    private JTextField txtDaysWorkedBatch;
    private JTextArea txtAreaSalaryReport;
    private JButton btnComputeMassSalaries;

    public MotorPHPayrollGUI() {
        setTitle("MotorPH Enterprise Management Console Suite");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane mainTabs = new JTabbedPane();
        mainTabs.addTab("Employee Registry Management Ledger (CRUD)", createCrudTabPanel());
        mainTabs.addTab("Mass Payroll Processing Workspace (Batch Engine)", createSalaryComputationTabPanel());
        add(mainTabs);

        setupEventHandlers();
        refreshTableData();
    }

    private JPanel createCrudTabPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        String[] columnNames = {"Emp ID", "Last Name", "First Name", "Birthday", "Hourly Rate", "SSS #", "PhilHealth #", "TIN", "Pag-IBIG #"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tblEmployees = new JTable(tableModel);
        tblEmployees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(tblEmployees), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new BorderLayout(5, 5));
        JPanel gridFields = new JPanel(new GridLayout(5, 4, 8, 8));
        gridFields.setBorder(BorderFactory.createTitledBorder("Active Employee Profile Details Form"));

        gridFields.add(new JLabel(" Employee ID * :")); regId = new JTextField(); gridFields.add(regId);
        gridFields.add(new JLabel(" Last Name * :")); regLastName = new JTextField(); gridFields.add(regLastName);
        gridFields.add(new JLabel(" First Name * :")); regFirstName = new JTextField(); gridFields.add(regFirstName);
        gridFields.add(new JLabel(" Birthday (YYYY-MM-DD):")); regBday = new JTextField(); gridFields.add(regBday);
        gridFields.add(new JLabel(" Hourly Rate * (PHP):")); regRate = new JTextField(); gridFields.add(regRate);
        gridFields.add(new JLabel(" SSS ID Number:")); regSss = new JTextField(); gridFields.add(regSss);
        gridFields.add(new JLabel(" PhilHealth Number:")); regPhilHealth = new JTextField(); gridFields.add(regPhilHealth);
        gridFields.add(new JLabel(" Tax Identification Number:")); regTin = new JTextField(); gridFields.add(regTin);
        gridFields.add(new JLabel(" Pag-IBIG ID Number:")); regPagIbig = new JTextField(); gridFields.add(regPagIbig);

        formPanel.add(gridFields, BorderLayout.CENTER);

        JPanel stripButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        btnSaveRecord = new JButton("Add Record");
        btnUpdateRecord = new JButton("Update Selected");
        btnDeleteRecord = new JButton("Delete Record");
        btnClearForm = new JButton("Clear Input Form");

        stripButtons.add(btnSaveRecord);
        stripButtons.add(btnUpdateRecord);
        stripButtons.add(btnDeleteRecord);
        stripButtons.add(btnClearForm);
        formPanel.add(stripButtons, BorderLayout.SOUTH);

        panel.add(formPanel, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createSalaryComputationTabPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel topControlBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        topControlBar.add(new JLabel("Target Execution Period Working Days Baseline: "));
        txtDaysWorkedBatch = new JTextField("21.75", 6);
        topControlBar.add(txtDaysWorkedBatch);
        
        btnComputeMassSalaries = new JButton("⚡ Run Enterprise Mass Salary Computations Engine");
        topControlBar.add(btnComputeMassSalaries);
        panel.add(topControlBar, BorderLayout.NORTH);

        txtAreaSalaryReport = new JTextArea();
        txtAreaSalaryReport.setEditable(false);
        txtAreaSalaryReport.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(txtAreaSalaryReport), BorderLayout.CENTER);

        return panel;
    }

    private void refreshTableData() {
        try {
            tableModel.setRowCount(0);
            List<String[]> lines = EmployeeDataHandler.readAllEmployees();
            for (String[] row : lines) {
                tableModel.addRow(row);
            }
        } catch (IOException ex) {
            // Trap and display data corruption notices clearly to users on runtime ingress initialization loops
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Database File Degradation Notice", JOptionPane.WARNING_MESSAGE);
            
            // Re-render valid rows that managed to pass filtration checks cleanly
            try {
                tableModel.setRowCount(0);
                File f = new File("employees.txt");
                if(f.exists()){
                     // Standard non-blocking catch reload
                     for(String[] validRow : EmployeeDataHandler.readAllEmployees()){
                          tableModel.addRow(validRow);
                     }
                }
            } catch(Exception structuralIgnored){}
        }
    }

    private void setupEventHandlers() {
        
        tblEmployees.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblEmployees.getSelectedRow() != -1) {
                int row = tblEmployees.getSelectedRow();
                regId.setText((String) tableModel.getValueAt(row, 0));
                regId.setEditable(false); 
                regLastName.setText((String) tableModel.getValueAt(row, 1));
                regFirstName.setText((String) tableModel.getValueAt(row, 2));
                regBday.setText((String) tableModel.getValueAt(row, 3));
                regRate.setText((String) tableModel.getValueAt(row, 4));
                regSss.setText((String) tableModel.getValueAt(row, 5));
                regPhilHealth.setText((String) tableModel.getValueAt(row, 6));
                regTin.setText((String) tableModel.getValueAt(row, 7));
                regPagIbig.setText((String) tableModel.getValueAt(row, 8));
            }
        });

        btnSaveRecord.addActionListener(e -> {
            try {
                String id = regId.getText().trim();
                String last = regLastName.getText().trim();
                String first = regFirstName.getText().trim();
                String rateRaw = regRate.getText().trim();

                if (id.isEmpty() || last.isEmpty() || first.isEmpty() || rateRaw.isEmpty()) {
                    throw new IllegalArgumentException("Fields with (*) are required parameters.");
                }
                
                int parsedId = Integer.parseInt(id);
                if (parsedId <= 0) throw new IllegalArgumentException("Employee ID must be a positive integer.");
                if (EmployeeDataHandler.isEmployeeIdDuplicate(id)) {
                    throw new IllegalArgumentException("Constraint Conflict: Employee ID already exists.");
                }

                Double.parseDouble(rateRaw); 

                String[] rowFields = { id, last, first, regBday.getText().trim().isEmpty() ? "N/A" : regBday.getText().trim(), rateRaw, regSss.getText().trim().isEmpty() ? "N/A" : regSss.getText().trim(), regPhilHealth.getText().trim().isEmpty() ? "N/A" : regPhilHealth.getText().trim(), regTin.getText().trim().isEmpty() ? "N/A" : regTin.getText().trim(), regPagIbig.getText().trim().isEmpty() ? "N/A" : regPagIbig.getText().trim() };
                EmployeeDataHandler.saveValidatedEmployee(rowFields);
                
                JOptionPane.showMessageDialog(this, "Employee Added and Saved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFormFields();
                refreshTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Input Format Validation Notice", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnUpdateRecord.addActionListener(e -> {
            try {
                String targetId = regId.getText().trim();
                if (targetId.isEmpty()) throw new IllegalArgumentException("Select a row line item record from the table list view first.");

                String rateRaw = regRate.getText().trim();
                Double.parseDouble(rateRaw); 

                String[] updatedFields = { targetId, regLastName.getText().trim(), regFirstName.getText().trim(), regBday.getText().trim(), rateRaw, regSss.getText().trim(), regPhilHealth.getText().trim(), regTin.getText().trim(), regPagIbig.getText().trim() };
                
                boolean success = EmployeeDataHandler.updateEmployeeRecord(targetId, updatedFields);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Employee Profile Updated Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearFormFields();
                    refreshTableData();
                } else {
                    JOptionPane.showMessageDialog(this, "Target Record not found on database storage index.", "Notice", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Validation Notice", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDeleteRecord.addActionListener(e -> {
            try {
                String targetId = regId.getText().trim();
                if (targetId.isEmpty()) throw new IllegalArgumentException("Please click an active row line from the grid dashboard to process deletion.");

                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to permanently delete Employee Profile " + targetId + "?", "Verify Action", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = EmployeeDataHandler.deleteEmployeeRecord(targetId);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "Record successfully purged from registry.");
                        clearFormFields();
                        refreshTableData();
                    } else {
                        JOptionPane.showMessageDialog(this, "Deletion failed. Target ID was missing from database lists.");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error Block", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnClearForm.addActionListener(e -> clearFormFields());

        btnComputeMassSalaries.addActionListener(e -> {
            try {
                String rawDays = txtDaysWorkedBatch.getText().trim();
                if (rawDays.isEmpty()) throw new IllegalArgumentException("Input Error: Days worked threshold entry bounds cannot be empty.");
                
                double validatedDaysWorked = Double.parseDouble(rawDays);
                if (validatedDaysWorked <= 0 || validatedDaysWorked > 31) throw new IllegalArgumentException("Operational Bounds Error: Period working metrics must run between 1 and 31 days.");

                // Reload tax parameter properties from file right before calculations trigger
                SalaryComputationModule.loadSystemTaxConfigurations();

                List<String[]> activeRecords = EmployeeDataHandler.readAllEmployees();
                int size = activeRecords.size();
                if (size == 0) {
                    txtAreaSalaryReport.setText("DATABASE LOG: No profiles detected inside tracking arrays.");
                    return;
                }

                double[] ratesArray = new double[size];
                double[] daysWorkedArray = new double[size];
                String[] namesArray = new String[size];
                String[] idsArray = new String[size];

                for (int i = 0; i < size; i++) {
                    String[] row = activeRecords.get(i);
                    idsArray[i] = row[0];
                    namesArray[i] = row[2] + " " + row[1]; 
                    daysWorkedArray[i] = validatedDaysWorked;
                    try {
                        ratesArray[i] = Double.parseDouble(row[4].trim());
                    } catch (NumberFormatException nfe) {
                        ratesArray[i] = 0.0; 
                    }
                }

                double[] grossPays = SalaryComputationModule.computeGrossPay(ratesArray, daysWorkedArray);
                double[] sss = SalaryComputationModule.computeSSS(grossPays);
                double[] ph = SalaryComputationModule.computePhilHealth(grossPays);
                double[] pb = SalaryComputationModule.computePagIBIG(grossPays);
                double[] statDeductions = SalaryComputationModule.computeDeductions(sss, ph, pb);
                double[] tax = SalaryComputationModule.computeWithholdingTax(grossPays, statDeductions);
                double[] netPays = SalaryComputationModule.computeNetPay(grossPays, statDeductions, tax);

                StringBuilder report = new StringBuilder();
                report.append("========================================================================================================================\n");
                report.append("                                           MOTORPH SYSTEM ENTERPRISE MANAGEMENT MASS PAYROLL REPORT                     \n");
                report.append("========================================================================================================================\n");
                report.append(String.format("%-6s | %-24s | %-12s | %-12s | %-11s | %-11s | %-11s | %-12s\n", "ID", "EMPLOYEE FULL NAME", "RATE/DAY", "GROWS PAY", "SSS DED.", "PHILHEALTH", "WITH. TAX", "NET PAYOUT"));
                report.append("------------------------------------------------------------------------------------------------------------------------\n");

                for (int i = 0; i < size; i++) {
                    report.append(String.format("%-6s | %-24s | PHP %-8.2f | PHP %-8.2f | PHP %-7.2f | PHP %-7.2f | PHP %-7.2f | PHP %-8.2f\n",
                            idsArray[i], namesArray[i], ratesArray[i], grossPays[i], sss[i], ph[i], tax[i], netPays[i]));
                }
                report.append("========================================================================================================================\n");

                txtAreaSalaryReport.setText(report.toString());
                JOptionPane.showMessageDialog(this, "Batch Calculations Engine Task Completed Successfully!", "Success Summary Run", JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Batch Core Exception: " + ex.getMessage(), "Execution Failure", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void clearFormFields() {
        regId.setText("");
        regId.setEditable(true); 
        regLastName.setText("");
        regFirstName.setText("");
        regBday.setText("");
        regRate.setText("");
        regSss.setText("");
        regPhilHealth.setText("");
        regTin.setText("");
        regPagIbig.setText("");
        tblEmployees.clearSelection();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MotorPHPayrollGUI().setVisible(true));
    }
}
