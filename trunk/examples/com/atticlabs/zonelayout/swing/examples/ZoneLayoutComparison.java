package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;
import com.jgoodies.forms.factories.ButtonBarFactory;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class ZoneLayoutComparison {
    public static final Object[] values = new Object[] {
            "Item 1", "Item 2", "Item 3", "Item 4", "Item 5"
    } ;

    public JPanel buildPanel() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("a-~a");
        layout.addRow("b+*b");
        layout.addRow("6...");
        layout.addRow("c>.c");

        ZoneLayout aLayout = ZoneLayoutFactory.newZoneLayout();
        aLayout.addRow("a>a2b-~b3c>c2d-~d", "valueRow");
        aLayout.addRow("6................", "valueRow");
        aLayout.addRow("e>e2f......-~...f");

        ZoneLayout bLayout = ZoneLayoutFactory.newZoneLayout();
        bLayout.addRow("a>a2b-~b2c-c");
        bLayout.addRow("...........6");
        bLayout.addRow("g........d-d");
        bLayout.addRow("...........6");
        bLayout.addRow("...+*....e-e");
        bLayout.addRow("...........6");
        bLayout.addRow(".........f-f");
        bLayout.addRow(".......g...!");

        JPanel namePanel = new JPanel(aLayout);
        aLayout.insertTemplate("valueRow");
        namePanel.add(new JLabel("First Name:"), "a");
        namePanel.add(new JTextField(15), "b");
        namePanel.add(new JLabel("Last Name:"), "c");
        namePanel.add(new JTextField(15), "d");
        System.out.println(new JTextField(15).getMinimumSize());
        aLayout.insertTemplate("valueRow");
        namePanel.add(new JLabel("Title:"), "a");
        namePanel.add(new JTextField(15), "b");
        namePanel.add(new JLabel("Nickname:"), "c");
        namePanel.add(new JTextField(15), "d");
        namePanel.add(new JLabel("Display Format:"), "e");
        namePanel.add(new JComboBox(values), "f");
        namePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Name"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        JPanel emailPanel = new JPanel(bLayout);
        emailPanel.add(new JLabel("Email Address:"), "a");
        emailPanel.add(new JTextField(15), "b");
        emailPanel.add(new JButton("Add"), "c");
        emailPanel.add(new JButton("Edit"), "d");
        emailPanel.add(new JButton("Remove"), "e");
        emailPanel.add(new JButton("Advanced"), "f");
        emailPanel.add(new JList(values), "g");

        emailPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("E-mail"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        JPanel p = new JPanel(layout);

        p.add(namePanel, "a");
        p.add(emailPanel, "b");
        p.add(ButtonBarFactory.buildOKCancelBar(
                new JButton("OK"),
                new JButton("Cancel")), "c");

        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return p;
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("ZoneLayout");

        frame.getContentPane().add(
                new ZoneLayoutComparison().buildPanel(), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
