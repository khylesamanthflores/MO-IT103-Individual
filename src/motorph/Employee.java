/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

public class Employee {

    private String employeeId;
    private String lastName;
    private String firstName;
    private String birthday;
    private double hourlyRate;
    private String sss;
    private String philHealth;
    private String tin;
    private String pagIbig;

    // ==========================================
    // CONSTRUCTOR WITH SAFETY VALIDATION
    // ==========================================
    public Employee(String employeeId, String lastName, String firstName, String birthday,
                    double hourlyRate, String sss, String philHealth, String tin, String pagIbig) {
        
        if (employeeId == null || employeeId.trim().isEmpty()) throw new IllegalArgumentException("Employee ID cannot be empty.");
        if (lastName == null || lastName.trim().isEmpty()) throw new IllegalArgumentException("Last name cannot be empty.");
        if (firstName == null || firstName.trim().isEmpty()) throw new IllegalArgumentException("First name cannot be empty.");
        if (hourlyRate < 0) throw new IllegalArgumentException("Hourly rate cannot be negative.");

        this.employeeId = employeeId.trim();
        this.lastName = lastName.trim();
        this.firstName = firstName.trim();
        this.birthday = birthday != null ? birthday.trim() : "N/A";
        this.hourlyRate = hourlyRate;
        this.sss = sss != null ? sss.trim() : "N/A";
        this.philHealth = philHealth != null ? philHealth.trim() : "N/A";
        this.tin = tin != null ? tin.trim() : "N/A";
        this.pagIbig = pagIbig != null ? pagIbig.trim() : "N/A";
    }

    // ==========================================
    // FACTORY METHOD (For clean file parsing translation)
    // ==========================================
    public static Employee fromArray(String[] tokens) {
        if (tokens == null || tokens.length != EmployeeDataHandler.EXPECTED_COLUMNS) {
            throw new IllegalArgumentException("Data array layout mismatch for mapping Employee object.");
        }
        
        double rate;
        try {
            rate = Double.parseDouble(tokens[4].trim());
        } catch (NumberFormatException e) {
            rate = 0.0;
        }

        return new Employee(
            tokens[0], // ID
            tokens[1], // Last Name
            tokens[2], // First Name
            tokens[3], // Birthday
            rate,      // Hourly Rate
            tokens[5], // SSS
            tokens[6], // PhilHealth
            tokens[7], // TIN
            tokens[8]  // Pag-IBIG
        );
    }

    // ==========================================
    // DATA LAYOUT TRANSLATORS
    // ==========================================
    public String[] toArray() {
        return new String[] {
            employeeId, lastName, firstName, birthday, 
            String.valueOf(hourlyRate), sss, philHealth, tin, pagIbig
        };
    }

    public String toCsvRow() {
        return String.join(",", toArray());
    }

    // ==========================================
    // GETTERS & SETTERS (Encapsulation Compliant)
    // ==========================================
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getFormattedName() {
        return lastName + ", " + firstName;
    }

    public String getBirthday() { return birthday; }
    public void setBirthday(String birthday) { this.birthday = birthday; }

    public double getHourlyRate() { return hourlyRate; }
    public void setHourlyRate(double hourlyRate) { this.hourlyRate = hourlyRate; }

    public String getSss() { return sss; }
    public void setSss(String sss) { this.sss = sss; }

    public String getPhilHealth() { return philHealth; }
    public void setPhilHealth(String philHealth) { this.philHealth = philHealth; }

    public String getTin() { return tin; }
    public void setTin(String tin) { this.tin = tin; }

    public String getPagIbig() { return pagIbig; }
    public void setPagIbig(String pagIbig) { this.pagIbig = pagIbig; }
}
