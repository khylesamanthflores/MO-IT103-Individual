/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Attendance {
    private String employeeNumber; 
    private LocalDate date;
    private LocalTime timeIn;
    private LocalTime timeOut;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public Attendance(String employeeNumber, String dateStr, String timeInStr, String timeOutStr) {
        this.employeeNumber = employeeNumber;
        this.date = LocalDate.parse(dateStr, DATE_FORMATTER);
        this.timeIn = LocalTime.parse(timeInStr, TIME_FORMATTER);
        this.timeOut = LocalTime.parse(timeOutStr, TIME_FORMATTER);
    }

    public double calculateHoursWorked() {
        java.time.Duration duration = java.time.Duration.between(timeIn, timeOut);
        return duration.toMinutes() / 60.0;
    }

    public void displayAttendanceInfo() {
        System.out.println("=== Attendance Entry ===");
        System.out.println("Employee ID : " + employeeNumber);
        System.out.println("Date        : " + date);
        System.out.println("Time In     : " + timeIn);
        System.out.println("Time Out    : " + timeOut);
        System.out.println("Hours Worked: " + calculateHoursWorked() + " hrs");
        System.out.println("========================\n");
    }

    public String getEmployeeNumber() { return employeeNumber; }
    public LocalDate getDate() { return date; }
    public LocalTime getTimeIn() { return timeIn; }
    public LocalTime getTimeOut() { return timeOut; }
}

