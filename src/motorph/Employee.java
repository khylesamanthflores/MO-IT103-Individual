/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Employee {
    
    // 1. Attributes - employeeNumber must be a String to hold quotes and leading zeros
    private String employeeNumber; 
    private String employeeName;
    private LocalDate birthday;    
    private double hourlyRate;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // 2. Constructor - Expects a String for the employeeNumber
    public Employee(String employeeNumber, String employeeName, String birthdayStr, double hourlyRate) {
        this.employeeNumber = employeeNumber; // Properly maps String to String
        this.employeeName = employeeName;
        this.birthday = LocalDate.parse(birthdayStr, DATE_FORMATTER); 
        this.hourlyRate = hourlyRate;
    }

    // 4. Behavior
    public void displayEmployeeInfo() {
        System.out.println("=== MotorPH Employee Record ===");
        System.out.println("ID         : " + employeeNumber);
        System.out.println("Name       : " + employeeName);
        System.out.println("Birthday   : " + birthday.format(DATE_FORMATTER)); 
        System.out.println("Hourly Rate: PHP " + hourlyRate);
        System.out.println("===============================\n");
    }
    
    // 3. Getters
    public String getEmployeeNumber() { return employeeNumber; }
    public String getEmployeeName() { return employeeName; }
    public LocalDate getBirthday() { return birthday; }
    public double getHourlyRate() { return hourlyRate; }
}
