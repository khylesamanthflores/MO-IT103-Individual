/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MotorPHPayrollGUI extends JFrame {

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
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        setLayout(new BorderLayout(10, 10));

        add(createInputPanel(), BorderLayout.NORTH);
        add(createOutputPanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        setupEventHandlers();
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createTitledBorder("MPHCR01 Required Form Inputs"));

        panel.add(new JLabel("  Employee Number (Numeric):"));
        txtEmployeeNumber = new JTextField();
        panel.add(txtEmployeeNumber);

        panel.add(new JLabel("  Employee Name:"));
        txtEmployeeName = new JTextField();
        panel.add(txtEmployeeName);

        panel.add(new JLabel("  Pay Coverage:"));
        txtPayCoverage = new JTextField();
        panel.add(txtPayCoverage);

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
                    String empNumRaw = txtEmployeeNumber.getText().trim();
                    String empName = txtEmployeeName.getText().trim();
                    String payCoverageRaw = txtPayCoverage.getText().trim();

                    if (empNumRaw.isEmpty() || empName.isEmpty() || payCoverageRaw.isEmpty()) {
                        throw new IllegalArgumentException("All fields must be filled out.");
                    }

                    // Enforce structural rules from Change Request
                    int empNum = Integer.parseInt(empNumRaw);
                    double payCoverageValue = Double.parseDouble(payCoverageRaw);

                    if (empNum != 10001) {
                        throw new NullPointerException("Employee ID record not found.");
                    }

                    Employee currentEmp = new Employee(String.valueOf(empNum), empName, "10/11/1983", 535.71);
                    Attendance summaryAttendance = new Attendance(String.valueOf(empNum), "2024-06-01", "08:00", "17:00"); 

                    double grossPay = payrollSystem.calculateDailyGrossPay(currentEmp, summaryAttendance);

                    txtAreaPayslip.setText(""); 
                    txtAreaPayslip.append("=======================================\n");
                    txtAreaPayslip.append("        MOTORPH PAYSLIP REPORT        \n");
                    txtAreaPayslip.append("=======================================\n");
                    txtAreaPayslip.append("Employee ID     : " + empNum + "\n");
                    txtAreaPayslip.append("Employee Name   : " + currentEmp.getEmployeeName() + "\n");
                    txtAreaPayslip.append("Hourly Rate     : PHP " + currentEmp.getHourlyRate() + "\n");
                    txtAreaPayslip.append("---------------------------------------\n");
                    txtAreaPayslip.append(String.format("GROSS PAY TOTAL : PHP %.2f\n", grossPay));
                    txtAreaPayslip.append("=======================================\n");

                } catch (IllegalArgumentException | NullPointerException ex) {
                    JOptionPane.showMessageDialog(MotorPHPayrollGUI.this, ex.getMessage(), "Validation Error", JOptionPane.WARNING_MESSAGE);
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
}

