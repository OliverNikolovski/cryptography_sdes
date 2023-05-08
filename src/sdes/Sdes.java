package sdes;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Sdes extends JFrame {

    public Sdes() {

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new SdesUI(), BorderLayout.CENTER);

        // Make window-close button active as only way to exit
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        Sdes mainFrame = new Sdes();
        mainFrame.setSize(SdesUI.BESTSIZE);
        mainFrame.setTitle("SDES Calculator (v1.0)");
        mainFrame.setVisible(true);
    }
}
