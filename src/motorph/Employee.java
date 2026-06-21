/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

public class Employee {
    private String employeeNumber;
    private String employeeName;
    private String birthday;
    private double hourlyRate;

    public Employee(String employeeNumber, String employeeName, String birthday, double hourlyRate) {
        this.employeeNumber = employeeNumber;
        this.employeeName = employeeName;
        this.birthday = birthday;
        this.hourlyRate = hourlyRate;
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public String getEmployeeName() { return employeeName; }
    public String getBirthday() { return birthday; }
    public double getHourlyRate() { return hourlyRate; }
}
