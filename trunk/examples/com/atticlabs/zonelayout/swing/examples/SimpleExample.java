package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class SimpleExample {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Simple Example");
        frame.getContentPane().setLayout(new BorderLayout());

        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("a>b-");
        layout.addRow(".a~b");

        JPanel basePanel = new JPanel(layout);
        basePanel.add(new JLabel("Name:"), "a");
        basePanel.add(new JTextField(20), "b");

        frame.getContentPane().add(basePanel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
