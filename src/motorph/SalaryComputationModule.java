/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

import java.io.*;
import java.util.Properties;

public class SalaryComputationModule {
    
    // Dynamic policy thresholds loaded at execution runtime
    public static double sssMaxThreshold = 24750.00;
    public static double sssMaxContribution = 1125.00;
    public static double sssMedThreshold = 15000.00;
    public static double sssMedContribution = 900.00;
    public static double sssMinContribution = 500.00;
    public static double philhealthRate = 0.04;
    public static double pagibigMaxCappedValue = 100.00;

    static {
        loadSystemTaxConfigurations();
    }

    public static void loadSystemTaxConfigurations() {
        Properties props = new Properties();
        File configFile = new File("config.properties");
        
        if (configFile.exists()) {
            try (InputStream input = new FileInputStream(configFile)) {
                props.load(input);
                sssMaxThreshold = Double.parseDouble(props.getProperty("sss.max.threshold", "24750.00"));
                sssMaxContribution = Double.parseDouble(props.getProperty("sss.max.contribution", "1125.00"));
                sssMedThreshold = Double.parseDouble(props.getProperty("sss.med.threshold", "15000.00"));
                sssMedContribution = Double.parseDouble(props.getProperty("sss.med.contribution", "900.00"));
                sssMinContribution = Double.parseDouble(props.getProperty("sss.min.contribution", "500.00"));
                philhealthRate = Double.parseDouble(props.getProperty("philhealth.rate", "0.04"));
                pagibigMaxCappedValue = Double.parseDouble(props.getProperty("pagibig.max.capped", "100.00"));
                System.out.println("✅ Configuration tables loaded successfully from external config.properties file.");
            } catch (Exception e) {
                System.err.println("⚠️ Properties parsing failed; falling back to hardcoded default configurations.");
            }
        }
    }

    public static double[] computeGrossPay(double[] ratesPerDay, double[] daysWorked) {
        double[] grossPays = new double[ratesPerDay.length];
        for (int i = 0; i < ratesPerDay.length; i++) {
            grossPays[i] = ratesPerDay[i] * daysWorked[i];
        }
        return grossPays;
    }

    public static double[] computeSSS(double[] grossPays) {
        double[] sss = new double[grossPays.length];
        for (int i = 0; i < grossPays.length; i++) {
            if (grossPays[i] >= sssMaxThreshold) sss[i] = sssMaxContribution;
            else if (grossPays[i] >= sssMedThreshold) sss[i] = sssMedContribution;
            else sss[i] = sssMinContribution;
        }
        return sss;
    }

    public static double[] computePhilHealth(double[] grossPays) {
        double[] ph = new double[grossPays.length];
        for (int i = 0; i < grossPays.length; i++) {
            ph[i] = (grossPays[i] * philhealthRate) / 2;
        }
        return ph;
    }

    public static double[] computePagIBIG(double[] grossPays) {
        double[] pagibig = new double[grossPays.length];
        for (int i = 0; i < grossPays.length; i++) {
            pagibig[i] = grossPays[i] >= 1500 ? pagibigMaxCappedValue : grossPays[i] * 0.01;
        }
        return pagibig;
    }

    public static double[] computeWithholdingTax(double[] grossPays, double[] totalDeductions) {
        double[] tax = new double[grossPays.length];
        for (int i = 0; i < grossPays.length; i++) {
            double taxableIncome = grossPays[i] - totalDeductions[i];
            tax[i] = taxableIncome > 20833 ? (taxableIncome - 20833) * 0.20 : 0.0;
        }
        return tax;
    }

    public static double[] computeDeductions(double[] sss, double[] ph, double[] pb) {
        double[] total = new double[sss.length];
        for (int i = 0; i < sss.length; i++) {
            total[i] = sss[i] + ph[i] + pb[i];
        }
        return total;
    }

    public static double[] computeNetPay(double[] grossPays, double[] statutoryDeductions, double[] withholdingTaxes) {
        double[] netPays = new double[grossPays.length];
        for (int i = 0; i < grossPays.length; i++) {
            netPays[i] = grossPays[i] - (statutoryDeductions[i] + withholdingTaxes[i]);
        }
        return netPays;
    }
}
