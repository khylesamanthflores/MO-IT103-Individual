/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MotorPHPayrollGUI extends JFrame {

    // ==========================================
    // BUSINESS ENGINE CONSTANTS (Replaces Magic Numbers)
    // ==========================================
    private static final double HOURS_PER_DAY = 8.0;
    private static final double DEFAULT_WORKING_DAYS = 21.75;
    private static final double FLAT_DEDUCTION_RATE = 0.10; // 10%

    // ==============================
    // EMPLOYEE MANAGEMENT COMPONENTS
    // ==============================
    private JTable tblEmployees;
    private DefaultTableModel tableModel;

    private JTextField regId;
    private JTextField regLastName;
    private JTextField regFirstName;
    private JTextField regBday;
    private JTextField regRate;
    private JTextField regSss;
    private JTextField regPhilHealth;
    private JTextField regTin;
    private JTextField regPagIbig;

    private JButton btnSaveRecord;
    private JButton btnUpdateRecord;
    private JButton btnDeleteRecord;
    private JButton btnClearForm;

    // ==============================
    // PAYROLL PROCESSING COMPONENTS
    // ==============================
    private JTextField txtDaysWorkedBatch;
    private JTextArea txtAreaSalaryReport;
    private JButton btnComputeMassSalaries;

    // ==============================
    // PAYROLL SUMMARY COMPONENTS
    // ==============================
    private JTextArea txtAreaPayrollSummary;
    private JButton btnGenerateSummary;

    // ==============================
    // CONSTRUCTOR
    // ==============================
    public MotorPHPayrollGUI() {
        setTitle("MotorPH Employee Management and Payroll System");
        setSize(1150, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("Employee Registry Management", createCrudTabPanel());
        tabs.addTab("Payroll Processing", createSalaryComputationTabPanel());
        tabs.addTab("Payroll Summary Dashboard", createPayrollSummaryPanel());

        add(tabs);

        setupEventHandlers();
        refreshTableData();
        setSelectionDependentButtonsEnabled(false); // Initially disabled until selected
    }

    // =================================================
    // EMPLOYEE CRUD TAB
    // =================================================
    private JPanel createCrudTabPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        String[] columns = {
            "Employee ID", "Last Name", "First Name", "Birthday", 
            "Hourly Rate", "SSS", "PhilHealth", "TIN", "Pag-IBIG"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                // Allows proper numerical sorting on the Hourly Rate column
                if (columnIndex == 4) return Double.class;
                return String.class;
            }
        };

        tblEmployees = new JTable(tableModel);
        tblEmployees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Bonus Feature: Native Row Sorting by clicking headers
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tblEmployees.setRowSorter(sorter);
        
        panel.add(new JScrollPane(tblEmployees), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new BorderLayout(5, 5));
        JPanel fields = new JPanel(new GridLayout(9, 2, 8, 8));
        fields.setBorder(BorderFactory.createTitledBorder("Employee Information"));

        regId = new JTextField();
        regLastName = new JTextField();
        regFirstName = new JTextField();
        regBday = new JTextField();
        regRate = new JTextField();
        regSss = new JTextField();
        regPhilHealth = new JTextField();
        regTin = new JTextField();
        regPagIbig = new JTextField();

        fields.add(new JLabel("Employee ID:")); fields.add(regId);
        fields.add(new JLabel("Last Name:")); fields.add(regLastName);
        fields.add(new JLabel("First Name:")); fields.add(regFirstName);
        fields.add(new JLabel("Birthday (YYYY-MM-DD):")); fields.add(regBday);
        fields.add(new JLabel("Hourly Rate (PHP):")); fields.add(regRate);
        fields.add(new JLabel("SSS Number:")); fields.add(regSss);
        fields.add(new JLabel("PhilHealth Number:")); fields.add(regPhilHealth);
        fields.add(new JLabel("TIN:")); fields.add(regTin);
        fields.add(new JLabel("Pag-IBIG Number:")); fields.add(regPagIbig);

        formPanel.add(fields, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout());
        btnSaveRecord = new JButton("Add");
        btnUpdateRecord = new JButton("Update");
        btnDeleteRecord = new JButton("Delete");
        btnClearForm = new JButton("Clear");

        buttons.add(btnSaveRecord);
        buttons.add(btnUpdateRecord);
        buttons.add(btnDeleteRecord);
        buttons.add(btnClearForm);

        formPanel.add(buttons, BorderLayout.SOUTH);
        panel.add(formPanel, BorderLayout.SOUTH);

        return panel;
    }

    // =================================================
    // PAYROLL PROCESSING TAB
    // =================================================
    private JPanel createSalaryComputationTabPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel controls = new JPanel(new FlowLayout());
        controls.add(new JLabel("Working Days:"));
        txtDaysWorkedBatch = new JTextField(String.valueOf(DEFAULT_WORKING_DAYS), 6);
        controls.add(txtDaysWorkedBatch);

        btnComputeMassSalaries = new JButton("Compute Payroll");
        controls.add(btnComputeMassSalaries);

        panel.add(controls, BorderLayout.NORTH);

        txtAreaSalaryReport = new JTextArea();
        txtAreaSalaryReport.setEditable(false);
        txtAreaSalaryReport.setFont(new Font("Monospaced", Font.PLAIN, 12));
        panel.add(new JScrollPane(txtAreaSalaryReport), BorderLayout.CENTER);

        return panel;
    }

    // =================================================
    // PAYROLL SUMMARY TAB
    // =================================================
    private JPanel createPayrollSummaryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnGenerateSummary = new JButton("Generate Payroll Summary");
        txtAreaPayrollSummary = new JTextArea();
        txtAreaPayrollSummary.setEditable(false);
        txtAreaPayrollSummary.setFont(new Font("Monospaced", Font.PLAIN, 14));

        panel.add(btnGenerateSummary, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtAreaPayrollSummary), BorderLayout.CENTER);

        return panel;
    }

    // =================================================
    // DATA REFRESH WITH TYPE CASTING
    // =================================================
    private void refreshTableData() {
        try {
            tableModel.setRowCount(0);
            List<String[]> employees = EmployeeDataHandler.readAllEmployees();
            for (String[] emp : employees) {
                // Parse hourly rate explicitly so RowSorter can order it mathematically instead of lexically
                Object[] rowData = new Object[emp.length];
                System.arraycopy(emp, 0, rowData, 0, emp.length);
                try {
                    rowData[4] = Double.parseDouble(emp[4]);
                } catch (NumberFormatException e) {
                    rowData[4] = 0.0; 
                }
                tableModel.addRow(rowData);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Database Error:\n" + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =================================================
    // EVENT HANDLERS & STATE CONTROL
    // =================================================
    private void setupEventHandlers() {
        // Table Selection Updates Form & Toggles State Buttons
        tblEmployees.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tblEmployees.getSelectedRow() != -1) {
                int selectedRow = tblEmployees.getSelectedRow();
                // Map view index to model index in case the view is currently sorted
                int modelRow = tblEmployees.convertRowIndexToModel(selectedRow);

                regId.setText(tableModel.getValueAt(modelRow, 0).toString());
                regId.setEditable(false);
                regLastName.setText(tableModel.getValueAt(modelRow, 1).toString());
                regFirstName.setText(tableModel.getValueAt(modelRow, 2).toString());
                regBday.setText(tableModel.getValueAt(modelRow, 3).toString());
                regRate.setText(tableModel.getValueAt(modelRow, 4).toString());
                regSss.setText(tableModel.getValueAt(modelRow, 5).toString());
                regPhilHealth.setText(tableModel.getValueAt(modelRow, 6).toString());
                regTin.setText(tableModel.getValueAt(modelRow, 7).toString());
                regPagIbig.setText(tableModel.getValueAt(modelRow, 8).toString());
                
                setSelectionDependentButtonsEnabled(true);
            }
        });

        // Add Employee Command
        btnSaveRecord.addActionListener(e -> {
            try {
                validateFormInputs();
                String id = regId.getText().trim();
                if (EmployeeDataHandler.isEmployeeIdDuplicate(id)) {
                    throw new Exception("Validation Failure: Employee ID '" + id + "' already exists in systems.");
                }

                String[] employee = getFormData();
                EmployeeDataHandler.saveValidatedEmployee(employee);
                JOptionPane.showMessageDialog(this, "Employee successfully committed to database.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearFormFields();
                refreshTableData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Registration Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Update Employee Command
        btnUpdateRecord.addActionListener(e -> {
            try {
                String id = regId.getText().trim();
                if (id.isEmpty()) {
                    throw new Exception("Operational Error: Action aborted. Please pick an employee record first.");
                }
                
                validateFormInputs();

                int selection = JOptionPane.showConfirmDialog(this, 
                        "Are you sure you want to update records for Employee ID: " + id + "?", 
                        "Confirm Modifications", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                
                if (selection == JOptionPane.YES_OPTION) {
                    String[] updated = getFormData();
                    EmployeeDataHandler.updateEmployeeRecord(id, updated);
                    JOptionPane.showMessageDialog(this, "Record successfully updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    refreshTableData();
                    clearFormFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Update Error", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Delete Employee Command
        btnDeleteRecord.addActionListener(e -> {
            try {
                String id = regId.getText().trim();
                if (id.isEmpty()) {
                    throw new Exception("Operational Error: Action aborted. Please pick an employee record first.");
                }
                
                int choice = JOptionPane.showConfirmDialog(this, 
                        "CRITICAL ACTION: Permanently wipe record for Employee " + id + "?", 
                        "Confirm Data Destruction", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

                if (choice == JOptionPane.YES_OPTION) {
                    EmployeeDataHandler.deleteEmployeeRecord(id);
                    JOptionPane.showMessageDialog(this, "Employee successfully scrubbed from persistent registry.");
                    refreshTableData();
                    clearFormFields();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Deletion Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnClearForm.addActionListener(e -> clearFormFields());
        btnGenerateSummary.addActionListener(e -> generatePayrollSummary());
        btnComputeMassSalaries.addActionListener(e -> computePayroll());
    }

    // =================================================
    // COMPREHENSIVE INPUT VALIDATION ENGINE
    // =================================================
    private void validateFormInputs() throws Exception {
        if (regId.getText().trim().isEmpty()) throw new Exception("Validation Error: Employee ID field is required.");
        if (regLastName.getText().trim().isEmpty()) throw new Exception("Validation Error: Last Name field is required.");
        if (regFirstName.getText().trim().isEmpty()) throw new Exception("Validation Error: First Name field is required.");
        
        // Date regex format verification (YYYY-MM-DD)
        String dateText = regBday.getText().trim();
        if (dateText.isEmpty()) {
            throw new Exception("Validation Error: Birthday field is required.");
        } else if (!dateText.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new Exception("Validation Error: Invalid Birthday Format. Expected structure: YYYY-MM-DD");
        }

        // Numerical format evaluation
        String rateText = regRate.getText().trim();
        if (rateText.isEmpty()) {
            throw new Exception("Validation Error: Hourly Rate is required.");
        }
        try {
            double parsedRate = Double.parseDouble(rateText);
            if (parsedRate < 0) throw new Exception("Validation Error: Base hourly compensation metrics cannot be negative.");
        } catch (NumberFormatException e) {
            throw new Exception("Parsing Error: Hourly Rate evaluation failed. Entered sequence must be a numeric floating point value.");
        }

        // Government IDs missing criteria evaluations
        if (regSss.getText().trim().isEmpty()) throw new Exception("Validation Error: SSS Identifier is required.");
        if (regPhilHealth.getText().trim().isEmpty()) throw new Exception("Validation Error: PhilHealth Identifier is required.");
        if (regTin.getText().trim().isEmpty()) throw new Exception("Validation Error: TIN Identifier is required.");
        if (regPagIbig.getText().trim().isEmpty()) throw new Exception("Validation Error: Pag-IBIG Identifier is required.");
    }

    private String[] getFormData() {
        return new String[]{
            regId.getText().trim(), regLastName.getText().trim(), regFirstName.getText().trim(),
            regBday.getText().trim(), regRate.getText().trim(), regSss.getText().trim(),
            regPhilHealth.getText().trim(), regTin.getText().trim(), regPagIbig.getText().trim()
        };
    }

    private void setSelectionDependentButtonsEnabled(boolean enabled) {
        btnUpdateRecord.setEnabled(enabled);
        btnDeleteRecord.setEnabled(enabled);
    }

    // =================================================
    // SUMMARY DASHBOARD ENGINE (Enriched Metrics)
    // =================================================
    private void generatePayrollSummary() {
        try {
            List<String[]> employees = EmployeeDataHandler.readAllEmployees();
            if (employees.isEmpty()) {
                txtAreaPayrollSummary.setText("No active system employee entities found. Operation failed.");
                return;
            }

            double totalGross = 0;
            double totalDeduction = 0;
            double totalNet = 0;
            double highestNet = Double.MIN_VALUE;
            double lowestNet = Double.MAX_VALUE;

            for (String[] emp : employees) {
                double hourlyRate = Double.parseDouble(emp[4]);
                double gross = hourlyRate * HOURS_PER_DAY * DEFAULT_WORKING_DAYS; 
                double deduction = gross * FLAT_DEDUCTION_RATE; 
                double net = gross - deduction;

                totalGross += gross;
                totalDeduction += deduction;
                totalNet += net;

                if (net > highestNet) highestNet = net;
                if (net < lowestNet) lowestNet = net;
            }

            double averageNet = totalNet / employees.size();
            double averageGross = totalGross / employees.size();

            txtAreaPayrollSummary.setText(
                "========================================================\n" +
                "               MOTORPH CORP. PAYROLL METRICS            \n" +
                "========================================================\n\n" +
                String.format(" Total Registered Active Personnel : %d Headcount\n", employees.size()) +
                String.format(" Total Accrued Operational Expense: PHP %,.2f\n", totalGross) +
                String.format(" Total Tax/Benefit Outlay Deductions: PHP %,.2f\n", totalDeduction) +
                "--------------------------------------------------------\n" +
                String.format(" Average Statistical Gross Compensation: PHP %,.2f\n", averageGross) +
                String.format(" Average Personnel Take-Home Yield (Net): PHP %,.2f\n", averageNet) +
                String.format(" Top Tier Compensated Net Benchmark     : PHP %,.2f\n", highestNet) +
                String.format(" Minimum Threshold Net Compensation     : PHP %,.2f\n", lowestNet) +
                "========================================================"
            );

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Calculation Engine Failure:\nVerify standard structure fields across employee registry strings.", "Analysis Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =================================================
    // PAYROLL BREAKDOWN ENGINE (Enhanced Layout Structure)
    // =================================================
    private void computePayroll() {
        try {
            String batchDaysText = txtDaysWorkedBatch.getText().trim();
            if(batchDaysText.isEmpty()) throw new Exception("Days Worked variable constraint absent.");
            
            double daysWorked;
            try {
                daysWorked = Double.parseDouble(batchDaysText);
                if (daysWorked < 0) throw new Exception();
            } catch (Exception ex) {
                throw new Exception("Input constraint violation: Working periods must be zero or positive integer-variant numerical formats.");
            }

            List<String[]> employees = EmployeeDataHandler.readAllEmployees();
            
            StringBuilder report = new StringBuilder();
            report.append(String.format("%-10s %-25s %-12s %-12s %-15s %-15s %-15s\n", 
                    "ID", "Employee Name", "Days Worked", "Hourly Rate", "Gross Pay", "Deductions", "Net Pay"));
            report.append("-----------------------------------------------------------------------------------------------------------------\n");
            
            for (String[] emp : employees) {
                String id = emp[0];
                String fullName = emp[1] + ", " + emp[2];
                double hourlyRate = Double.parseDouble(emp[4]);
                
                double gross = hourlyRate * HOURS_PER_DAY * daysWorked;
                double deduction = gross * FLAT_DEDUCTION_RATE;
                double net = gross - deduction;
                
                report.append(String.format("%-10s %-25s %-12.2f %-12.2f %-15s %-15s %-15s\n", 
                        id, 
                        fullName, 
                        daysWorked,
                        hourlyRate,
                        String.format("₱%,.2f", gross), 
                        String.format("₱%,.2f", deduction), 
                        String.format("₱%,.2f", net)));
            }
            
            txtAreaSalaryReport.setText(report.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Execution Engine Error", JOptionPane.WARNING_MESSAGE);
        }
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
        setSelectionDependentButtonsEnabled(false);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MotorPHPayrollGUI().setVisible(true);
        });
    }
}
