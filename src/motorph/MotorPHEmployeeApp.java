/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package motorph;

import javax.swing.SwingUtilities;

public class MotorPHEmployeeApp {
    public static void main(String[] args) {
        
        // This launches your Graphical User Interface safely on the Event Dispatch Thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Instantiates and pops up your new Swing window frame
                MotorPHPayrollGUI payrollInterface = new MotorPHPayrollGUI();
                payrollInterface.setVisible(true);
            }
        });
    }
}
