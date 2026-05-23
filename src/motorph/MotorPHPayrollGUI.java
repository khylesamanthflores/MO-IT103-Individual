/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MotorPHPayrollGUI extends JFrame {

    private static final String EMPLOYEE_FILE = "employees.txt";

    private JTextField txtEmployeeNumber;
    private JTextField txtEmployeeName;
    private JTextField txtPayCoverage;
    private JButton btnCalculate;
    private JButton btnClear;
    private JTextArea txtAreaPayslip;
    private Payroll payrollSystem;

    public MotorPHPayrollGUI() {
        payrollSystem = new Payroll();

        setTitle("MotorPH Payroll Management System - MPHCR01");
        setSize(500, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout(10, 10));

        add(createInputPanel(), BorderLayout.NORTH);
        add(createOutputPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        setupEventHandlers();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("MPHCR01 Required Form Inputs"));

        panel.add(new JLabel("  Employee Number (Numeric):"));
        txtEmployeeNumber = new JTextField();
        panel.add(txtEmployeeNumber);

        panel.add(new JLabel("  Employee Name:"));
        txtEmployeeName = new JTextField();
        txtEmployeeName.setEditable(false);
        txtEmployeeName.setBackground(new Color(240, 240, 240));
        panel.add(txtEmployeeName);

        panel.add(new JLabel("  Pay Coverage (Days Worked):"));
        txtPayCoverage = new JTextField();
        panel.add(txtPayCoverage);

        JLabel lblHint = new JLabel("  *Accepts decimals like 10.5 days");
        lblHint.setFont(new Font("SansSerif", Font.ITALIC, 11));
        lblHint.setForeground(Color.GRAY);
        panel.add(lblHint);

        return panel;
    }

    private JPanel createOutputPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Generated Payslip"));
        txtAreaPayslip = new JTextArea();
        txtAreaPayslip.setEditable(false);
        txtAreaPayslip.setFont(new Font("Monospaced", Font.PLAIN, 12)); 
        panel.add(new JScrollPane(txtAreaPayslip), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        btnClear = new JButton("Clear");
        btnCalculate = new JButton("Calculate Payroll");
        panel.add(btnClear);
        panel.add(btnCalculate);
        return panel;
    }

    private void setupEventHandlers() {
        btnCalculate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String rawEmpNum = txtEmployeeNumber.getText().trim();
                    String rawPayCoverage = txtPayCoverage.getText().trim();

                    if (rawEmpNum.isEmpty() || rawPayCoverage.isEmpty()) {
                        throw new IllegalArgumentException("Validation Error: All operational input fields must be filled out.");
                    }

                    String cleanEmpId = validateEmployeeId(rawEmpNum);
                    double payCoverageValue = validatePayCoverage(rawPayCoverage);

                    Employee currentEmp = findEmployeeFromFile(EMPLOYEE_FILE, cleanEmpId);
                    if (currentEmp == null) {
                        throw new IllegalArgumentException("Database Error: Employee ID '" + cleanEmpId + "' does not exist in records.");
                    }

                    txtEmployeeName.setText(currentEmp.getEmployeeName());

                    Attendance summaryAttendance = new Attendance(currentEmp.getEmployeeNumber(), "2024-06-01", "08:00", "17:00"); 
                    double dailyGrossPay = payrollSystem.calculateDailyGrossPay(currentEmp, summaryAttendance);
                    double finalPeriodGrossPay = dailyGrossPay * payCoverageValue;

                    String formattedPayslip = generatePayslipText(currentEmp, payCoverageValue, finalPeriodGrossPay);
                    txtAreaPayslip.setText(formattedPayslip);

                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(MotorPHPayrollGUI.this, ex.getMessage(), "Input Validation Alert", JOptionPane.WARNING_MESSAGE);
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(MotorPHPayrollGUI.this, "System File Error: " + ex.getMessage(), "I/O Exception", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtEmployeeNumber.setText("");
                txtEmployeeName.setText("");
                txtPayCoverage.setText("");
                txtAreaPayslip.setText("");
            }
        });
    }

    private String validateEmployeeId(String rawId) {
        try {
            int parsedId = Integer.parseInt(rawId);
            if (parsedId <= 0) {
                throw new IllegalArgumentException("Employee ID must be a positive integer calculation baseline.");
            }
            return String.valueOf(parsedId);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Type Error: Employee Number must contain digits only.");
        }
    }

    private double validatePayCoverage(String rawCoverage) {
        try {
            double value = Double.parseDouble(rawCoverage);
            if (value <= 0) {
                throw new IllegalArgumentException("Pay Coverage days must be greater than zero.");
            }
            if (value > 31.0) {
                throw new IllegalArgumentException("Pay Coverage cannot exceed a maximum monthly threshold of 31 days.");
            }
            return value;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Type Error: Pay Coverage must be a numerical value (decimals allowed).");
        }
    }

    private Employee findEmployeeFromFile(String filepath, String targetId) throws IOException {
        int lineCounter = 0;
        java.io.InputStream in = getClass().getResourceAsStream("/motorph/employees.txt");
        
        if (in == null) {
            try (BufferedReader directBr = new BufferedReader(new FileReader(filepath))) {
                return scanBufferedReader(directBr, targetId);
            } catch (IOException ioEx) {
                throw new IOException("Critical System Error: 'employees.txt' missing from both resource path and root directory.");
            }
        }
        
        try (BufferedReader br = new BufferedReader(new java.io.InputStreamReader(in))) {
            return scanBufferedReader(br, targetId);
        }
    }

    private Employee scanBufferedReader(BufferedReader br, String targetId) throws IOException {
        String line;
        int lineCounter = 0;
        while ((line = br.readLine()) != null) {
            lineCounter++;
            String[] tokens = line.split(",");
            
            if (tokens.length < 4) {
                System.err.println("CRITICAL DATABASE WARNING: Corrupt or malformed layout detected on Line " + lineCounter + ". Row skipped.");
                continue; 
            }
            
            if (tokens[0].trim().equals(targetId)) {
                return parseEmployeeFromTokens(tokens);
            }
        }
        return null;
    }

    private Employee parseEmployeeFromTokens(String[] tokens) {
        String fileEmpId = tokens[0].trim();
        String name = tokens[1].trim();
        String birthday = tokens[2].trim();
        double rate = Double.parseDouble(tokens[3].trim());
        return new Employee(fileEmpId, name, birthday, rate);
    }

    private String generatePayslipText(Employee emp, double daysWorked, double finalGrossPay) {
        StringBuilder sb = new StringBuilder();
        sb.append("====================================================\n");
        sb.append("               MOTORPH PERIOD PAYSLIP               \n");
        sb.append("====================================================\n");
        sb.append(String.format("  Employee ID     : %s\n", emp.getEmployeeNumber()));
        sb.append(String.format("  Employee Name   : %s\n", emp.getEmployeeName()));
        sb.append(String.format("  Hourly Rate     : PHP %,.2f\n", emp.getHourlyRate()));
        sb.append(String.format("  Days Clocked    : %.2f days\n", daysWorked));
        sb.append("----------------------------------------------------\n");
        sb.append(String.format("  PERIOD GROSS PAY: PHP %,.2f\n", finalGrossPay));
        sb.append("====================================================\n");
        sb.append("           SYSTEM GENERATED - CONFIDENTIAL          \n");
        sb.append("====================================================\n");
        return sb.toString();
    }
}
