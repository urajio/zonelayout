package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class AddressPanelExample {
    public static void main(String[] args) {
        JTextField lastNameTF = new JTextField(10);
        JTextField firstNameTF = new JTextField(10);
        JTextField phoneTF = new JTextField(10);
        JTextField emailTF = new JTextField(10);
        JTextField address1TF = new JTextField(20);
        JTextField address2TF = new JTextField(20);
        JTextField cityTF = new JTextField(10);
        JTextField stateTF = new JTextField(2);
        JTextField postalCodeTF = new JTextField(15);
        JTextField countryTF = new JTextField(2);

        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();

        layout.addRow("g+*3..................");
        layout.addRow("....a>a2b-~b2c>c2d-~d", "normalRow");
        layout.addRow("....6................", "normalRow");
        layout.addRow("....e>e2f-~.........f", "wideRow");
        layout.addRow("....6................", "wideRow");
        layout.addRow("..g..........!.......");


        JPanel basePanel = new JPanel(layout);
        basePanel.add(new JTextArea(), "g");
        layout.insertTemplate("normalRow");
        basePanel.add(new JLabel("Last Name"), "a");
        basePanel.add(lastNameTF, "b");
        basePanel.add(new JLabel("First Name"), "c");
        basePanel.add(firstNameTF, "d");
        layout.insertTemplate("normalRow");
        basePanel.add(new JLabel("Phone"), "a");
        basePanel.add(phoneTF, "b");
        basePanel.add(new JLabel("Email"), "c");
        basePanel.add(emailTF, "d");
        layout.insertTemplate("wideRow");
        basePanel.add(new JLabel("Address 1"), "e");
        basePanel.add(address1TF, "f");
        layout.insertTemplate("wideRow");
        basePanel.add(new JLabel("Address 2"), "e");
        basePanel.add(address2TF, "f");
        layout.insertTemplate("normalRow");
        basePanel.add(new JLabel("City"), "a");
        basePanel.add(cityTF, "b");
        layout.insertTemplate("normalRow");
        basePanel.add(new JLabel("State"), "a");
        basePanel.add(stateTF, "b");
        basePanel.add(new JLabel("Postal Code"), "c");
        basePanel.add(postalCodeTF, "d");
        layout.insertTemplate("normalRow");
        basePanel.add(new JLabel("Country"), "a");
        basePanel.add(countryTF, "b");

        basePanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        final JFrame frame = new JFrame("Browser Example");
        frame.getContentPane().setLayout(new BorderLayout());

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
