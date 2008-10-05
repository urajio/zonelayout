package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class BrowserExample {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Browser Example");
        frame.getContentPane().setLayout(new BorderLayout());

        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("bfa-~a");
        layout.addRow("w+*...");
        layout.addRow(".....w");
        layout.addRow("s<...s");

        JPanel basePanel = new JPanel(layout);
        basePanel.add(new JButton("Back"), "b");
        basePanel.add(new JButton("Forward"), "f");
        basePanel.add(new JTextField(20), "a");
        JScrollPane jsp = new JScrollPane(new JTextPane());
        jsp.setPreferredSize(new Dimension(400, 200));
        basePanel.add(jsp, "w");
        basePanel.add(new JLabel("status"), "s");

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
