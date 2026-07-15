/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package motorph;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDataHandler {
    private static final String FILE_NAME = "employees.txt";
    public static final int EXPECTED_COLUMNS = 9; 

    // ==========================================
    // DATA EXTRACTION (READ ENGINE)
    // ==========================================
    public static List<String[]> readAllEmployees() throws IOException {
        List<String[]> records = new ArrayList<>();
        File targetFile = new File(FILE_NAME);

        if (!targetFile.exists()) {
            targetFile.createNewFile();
            return records; 
        }

        int lineCounter = 0;
        int corruptedCount = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(targetFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                lineCounter++;
                String[] tokens = line.split(",", -1);
                
                String firstTokenClean = tokens[0].trim().toLowerCase();
                if (firstTokenClean.equals("employee id") || firstTokenClean.equals("emp id") || 
                    firstTokenClean.equals("id")          || firstTokenClean.equals("employee number")) {
                    continue; 
                }

                // Fail-Fast Check for structural data integrity
                if (tokens.length != EXPECTED_COLUMNS) {
                    corruptedCount++;
                    System.err.println("Database formatting error on line " 
                                        + lineCounter + ". Expected " + EXPECTED_COLUMNS + ", found " + tokens.length);
                    continue; 
                }
                
                // Trim individual cells to prevent layout trailing space mismatch issues
                for (int i = 0; i < tokens.length; i++) {
                    tokens[i] = tokens[i].trim();
                }
                records.add(tokens);
            }
        }

        if (corruptedCount > 0) {
            throw new IOException("Unable to process " + corruptedCount + " corrupted rows found inside the file.");
        }

        return records;
    }

    // ==========================================
    // RIGOROUS SANITIZATION & VALIDATION ENGINE
    // ==========================================
    private static String[] sanitizeAndValidateFields(String[] fields) {
        if (fields == null || fields.length != EXPECTED_COLUMNS) {
            throw new IllegalArgumentException("Internal Error: Employee data formatting mismatch.");
        }
        
        String[] sanitized = new String[EXPECTED_COLUMNS];
        
        // Loop, trim data values, and scrub user input commas to avoid CSV layout parsing errors
        for (int i = 0; i < fields.length; i++) {
            if (fields[i] == null || fields[i].trim().isEmpty()) {
                sanitized[i] = "N/A";
            } else {
                sanitized[i] = fields[i].replace(",", " ").trim();
            }
        }

        // Concrete Business Requirement Content Validation
        if (sanitized[0].equalsIgnoreCase("N/A")) {
            throw new IllegalArgumentException("Employee ID is a required field.");
        }
        if (sanitized[1].equalsIgnoreCase("N/A")) {
            throw new IllegalArgumentException("Last Name is a required field.");
        }
        if (sanitized[2].equalsIgnoreCase("N/A")) {
            throw new IllegalArgumentException("First Name is a required field.");
        }

        // Numeric parsing confirmation for Hourly Rate metrics
        String testRate = sanitized[4];
        try {
            Double.parseDouble(testRate);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Hourly Rate must be a valid numeric value.");
        }

        return sanitized;
    }

    // ==========================================
    // PERSISTENCE WRITE / EDIT METHODS
    // ==========================================
    public static void saveValidatedEmployee(String[] dataFields) throws IOException {
        String[] sanitized = sanitizeAndValidateFields(dataFields);
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME, true)))) {
            out.println(String.join(",", sanitized));
        } catch (IOException e) {
            System.err.println("Fatal Write Interrupt Trace: " + e.getMessage());
            throw new IOException("Unable to save employee data to the file.");
        }
    }

    public static boolean updateEmployeeRecord(String targetId, String[] updatedFields) throws IOException {
        String[] sanitizedUpdates = sanitizeAndValidateFields(updatedFields);
        List<String[]> records = readAllEmployees();
        boolean recordFound = false;
        
        File productionFile = new File(FILE_NAME);
        File tempFile = new File("employees.tmp");

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tempFile, false)))) {
            out.println("ID,Last Name,First Name,Birthday,Hourly Rate,SSS,PhilHealth,TIN,Pag-Ibig");
            for (String[] row : records) {
                if (row[0].equals(targetId.trim())) {
                    out.println(String.join(",", sanitizedUpdates));
                    recordFound = true;
                } else {
                    out.println(String.join(",", row));
                }
            }
        }

        // Modern, Cross-OS Atomic Replacement Protocol
        if (recordFound) {
            try {
                Files.move(tempFile.toPath(), productionFile.toPath(), 
                           StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                if (tempFile.exists()) tempFile.delete();
                System.err.println("OS File IO Move Error: " + e.getMessage());
                throw new IOException("Unable to update employee record due to a system file error.");
            }
        } else {
            if (tempFile.exists()) tempFile.delete();
        }
        return recordFound;
    }

    public static boolean deleteEmployeeRecord(String targetId) throws IOException {
        List<String[]> records = readAllEmployees();
        boolean recordFound = false;
        
        File productionFile = new File(FILE_NAME);
        File tempFile = new File("employees.tmp");

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tempFile, false)))) {
            out.println("ID,Last Name,First Name,Birthday,Hourly Rate,SSS,PhilHealth,TIN,Pag-Ibig");
            for (String[] row : records) {
                if (!row[0].equals(targetId.trim())) {
                    out.println(String.join(",", row));
                } else {
                    recordFound = true;
                }
            }
        }

        if (recordFound) {
            try {
                Files.move(tempFile.toPath(), productionFile.toPath(), 
                           StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (IOException e) {
                if (tempFile.exists()) tempFile.delete();
                System.err.println("OS File IO Move Error: " + e.getMessage());
                throw new IOException("Unable to delete employee record due to a system file error.");
            }
        } else {
            if (tempFile.exists()) tempFile.delete();
        }
        return recordFound;
    }

    // ==========================================
    // INTEGRITY / UTILITY OPERATIONS
    // ==========================================
    public static boolean isEmployeeIdDuplicate(String empId) throws IOException {
        try {
            List<String[]> currentRecords = readAllEmployees();
            for (String[] row : currentRecords) {
                // Streamlined structural check without the redundant row.length validation rule
                if (row[0].equals(empId.trim())) return true;
            }
        } catch (IOException e) {
            // No longer ignored silently; structural trace output sent to log console stream
            System.err.println("Notice: Check skipped while processing duplicate constraints: " + e.getMessage());
            throw new IOException("Unable to complete employee verification processes.");
        }
        return false;
    }
}
