package com.banking;

import com.banking.ui.MainWindow;
import javax.swing.SwingUtilities;

public class Main {
    public static BankingSystem bankingSystem;

    public static void main(String[] args) {
        bankingSystem = new BankingSystem();
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                MainWindow mainWindow = new MainWindow();
                mainWindow.setVisible(true);
            }
        });
    }
}
