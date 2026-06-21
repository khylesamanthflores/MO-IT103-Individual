/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package motorph;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDataHandler {
    private static final String FILE_NAME = "employees.txt";
    public static final int EXPECTED_COLUMNS = 9; 

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

                // Fail-Fast: Reject and flag row corruptions explicitly
                if (tokens.length != EXPECTED_COLUMNS) {
                    corruptedCount++;
                    System.err.println("❌ INTERNAL DATABASE ERROR: Column mismatch on line " 
                                        + lineCounter + ". Expected " + EXPECTED_COLUMNS + ", found " + tokens.length);
                    continue; // Skip this row to protect the calculation engine from bad parsing
                }
                records.add(tokens);
            }
        }

        if (corruptedCount > 0) {
            throw new IOException("Data Degradation Notice: Found " + corruptedCount + 
                                  " corrupted rows inside 'employees.txt'. These records were skipped.");
        }

        return records;
    }

    private static String[] sanitizeAndValidateFields(String[] fields) {
        if (fields == null || fields.length != EXPECTED_COLUMNS) {
            throw new IllegalArgumentException("Database Error: Array dimensions must exactly equal " + EXPECTED_COLUMNS + " entries.");
        }
        String[] sanitized = new String[EXPECTED_COLUMNS];
        for (int i = 0; i < fields.length; i++) {
            sanitized[i] = fields[i] != null ? fields[i].replace(",", " ") : "N/A";
        }
        return sanitized;
    }

    public static void saveValidatedEmployee(String[] dataFields) throws IOException {
        String[] sanitized = sanitizeAndValidateFields(dataFields);
        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(FILE_NAME, true)))) {
            out.println(String.join(",", sanitized));
        }
    }

    public static boolean updateEmployeeRecord(String targetId, String[] updatedFields) throws IOException {
        String[] sanitizedUpdates = sanitizeAndValidateFields(updatedFields);
        List<String[]> records = readAllEmployees();
        boolean recordFound = false;
        
        File productionFile = new File(FILE_NAME);
        File tempFile = new File("employees.tmp");
        File backupFile = new File("employees.bak");

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tempFile, false)))) {
            out.println("ID,Last Name,First Name,Birthday,Hourly Rate,SSS,PhilHealth,TIN,Pag-Ibig");
            for (String[] row : records) {
                if (row[0].trim().equals(targetId.trim())) {
                    out.println(String.join(",", sanitizedUpdates));
                    recordFound = true;
                } else {
                    out.println(String.join(",", row));
                }
            }
        }

        // Rolling 3-Step Backup Swap Operations
        if (recordFound) {
            if (backupFile.exists()) backupFile.delete();
            if (productionFile.exists() && !productionFile.renameTo(backupFile)) {
                tempFile.delete();
                throw new IOException("OS Operational Access Interruption: Unable to protect previous file copy.");
            }
            if (!tempFile.renameTo(productionFile)) {
                if (backupFile.exists()) backupFile.renameTo(productionFile); // Automatic Rollback
                throw new IOException("Write Transaction Interrupted: Data safely rolled back to prevent file corruption.");
            }
            backupFile.delete(); 
        } else {
            tempFile.delete(); 
        }
        return recordFound;
    }

    public static boolean deleteEmployeeRecord(String targetId) throws IOException {
        List<String[]> records = readAllEmployees();
        boolean recordFound = false;
        
        File productionFile = new File(FILE_NAME);
        File tempFile = new File("employees.tmp");
        File backupFile = new File("employees.bak");

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(tempFile, false)))) {
            out.println("ID,Last Name,First Name,Birthday,Hourly Rate,SSS,PhilHealth,TIN,Pag-Ibig");
            for (String[] row : records) {
                if (!row[0].trim().equals(targetId.trim())) {
                    out.println(String.join(",", row));
                } else {
                    recordFound = true;
                }
            }
        }

        if (recordFound) {
            if (backupFile.exists()) backupFile.delete();
            if (productionFile.exists() && !productionFile.renameTo(backupFile)) {
                tempFile.delete();
                throw new IOException("OS File System Block: Unable to archive file elements.");
            }
            if (!tempFile.renameTo(productionFile)) {
                if (backupFile.exists()) backupFile.renameTo(productionFile);
                throw new IOException("Deletion Execution Interrupted: Safely restored initial database layer.");
            }
            backupFile.delete();
        } else {
            tempFile.delete();
        }
        return recordFound;
    }

    public static boolean isEmployeeIdDuplicate(String empId) throws IOException {
        try {
            List<String[]> currentRecords = readAllEmployees();
            for (String[] row : currentRecords) {
                if (row.length > 0 && row[0].trim().equals(empId.trim())) return true;
            }
        } catch (IOException ignored) {} // Allow check to clear if reading empty fresh database files
        return false;
    }
}
            