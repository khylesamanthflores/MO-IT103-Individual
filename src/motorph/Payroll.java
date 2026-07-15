/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

public class Payroll {
    private static final double LUNCH_BREAK_DEDUCTION = 1.0; 
    private static final double STANDARD_FULL_DAY_THRESHOLD = 8.0; 

    /**
     * Helper method to compute net paid hours after applying standard break policies.
     */
    public double calculateNetPaidHours(Attendance attendance) {
        double rawHours = attendance.calculateHoursWorked();
        if (rawHours >= STANDARD_FULL_DAY_THRESHOLD) {
            return rawHours - LUNCH_BREAK_DEDUCTION;
        }
        return rawHours;
    }

    /**
     * Calculates the Gross Pay for a single day based on adjusted paid hours.
     */
    public double calculateDailyGrossPay(Employee employee, Attendance attendance) {
        double netPaidHours = calculateNetPaidHours(attendance);
        return netPaidHours * employee.getHourlyRate();
    }

    /**
     * Prints a cleanly formatted payslip breakdown to the system console.
     */
    public void printDailyPayslip(Employee employee, Attendance attendance) {
        double rawHours = attendance.calculateHoursWorked();
        double netPaidHours = calculateNetPaidHours(attendance);
        double grossPay = calculateDailyGrossPay(employee, attendance); 

        System.out.println("======= MotorPH Daily Payslip =======");
        System.out.println("Employee ID   : " + employee.getEmployeeId());
        System.out.println("Employee Name : " + employee.getFormattedName());
        System.out.println("Date          : " + attendance.getDate());
        System.out.println("Hourly Rate   : PHP " + String.format("%,.2f", employee.getHourlyRate()));
        System.out.println("Raw Clocked   : " + String.format("%.2f", rawHours) + " hours");
        System.out.println("Net Paid Hours: " + String.format("%.2f", netPaidHours) + " hours");
        System.out.println("-------------------------------------");
        System.out.println("GROSS PAY     : PHP " + String.format("%,.2f", grossPay));
        System.out.println("=====================================\n");
    }
}
