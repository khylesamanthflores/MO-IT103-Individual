/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

public class Payroll {
    private static final double LUNCH_BREAK_DEDUCTION = 1.0; 
    private static final double STANDARD_FULL_DAY_THRESHOLD = 8.0; 

    // Fixed naming convention: Explicitly states it calculates Gross Pay
    public double calculateDailyGrossPay(Employee employee, Attendance attendance) {
        double rawHours = attendance.calculateHoursWorked();
        double breakAdjustedHours = rawHours;

        if (rawHours >= STANDARD_FULL_DAY_THRESHOLD) {
            breakAdjustedHours = rawHours - LUNCH_BREAK_DEDUCTION;
        }

        return breakAdjustedHours * employee.getHourlyRate();
    }

    public void printDailyPayslip(Employee employee, Attendance attendance) {
        double rawHours = attendance.calculateHoursWorked();
        // Uses the newly renamed method
        double grossPay = calculateDailyGrossPay(employee, attendance); 

        System.out.println("======= MotorPH Daily Payslip =======");
        System.out.println("Employee ID   : " + employee.getEmployeeNumber());
        System.out.println("Employee Name : " + employee.getEmployeeName());
        System.out.println("Date          : " + attendance.getDate());
        System.out.println("Hourly Rate   : PHP " + employee.getHourlyRate());
        System.out.println("Raw Clocked   : " + rawHours + " hours");
        System.out.println("Net Paid Hours: " + (rawHours >= STANDARD_FULL_DAY_THRESHOLD ? (rawHours - LUNCH_BREAK_DEDUCTION) : rawHours) + " hours");
        System.out.println("-------------------------------------");
        System.out.println("GROSS PAY     : PHP " + String.format("%.2f", grossPay)); // Explicitly Gross
        System.out.println("=====================================\n");
    }
}
