package com.atticlabs.zonelayout.swing.examples;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.factories.ButtonBarFactory;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.BorderLayout;

public class FormLayoutComparison {
    public static final Object[] values = new Object[]{
            "Item 1", "Item 2", "Item 3", "Item 4", "Item 5"
    };

    public JPanel buildPanel() {
        FormLayout layout = new FormLayout(
                "d:g",
                "d, 3dlu, f:d:g, 3dlu, d"

        );

        FormLayout nameLayout = new FormLayout(
                "r:d, 3dlu, d:g, 7dlu, r:d, 3dlu, d:g",
                "d, 3dlu, d, 3dlu, d"

        );

        FormLayout emailLayout = new FormLayout(
                "r:d, 3dlu, d:g, 3dlu, d",
                "d, 3dlu, d, 3dlu, d, 3dlu, d, f:d:g"

        );

        PanelBuilder builder = new PanelBuilder(nameLayout);
        CellConstraints cc = new CellConstraints();
        builder.addLabel("First Name:", cc.xy(1, 1));
        builder.add(new JTextField(15), cc.xy(3, 1));
        builder.addLabel("Last Name:", cc.xy(5, 1));
        builder.add(new JTextField(15), cc.xy(7, 1));
        builder.addLabel("Title:", cc.xy(1, 3));
        builder.add(new JTextField(15), cc.xy(3, 3));
        builder.addLabel("Nickname:", cc.xy(5, 3));
        builder.add(new JTextField(15), cc.xy(7, 3));
        builder.addLabel("Display Format:", cc.xy(1, 5));
        builder.add(new JComboBox(values), cc.xyw(3, 5, 5));
        JPanel namePanel = builder.getPanel();
        namePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("Name"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        builder = new PanelBuilder(emailLayout);
        cc = new CellConstraints();
        builder.addLabel("Email Address:", cc.xy(1, 1));
        builder.add(new JTextField(15), cc.xy(3, 1));
        builder.add(new JButton("Add"), cc.xy(5, 1));

        builder.add(new JList(values), cc.xywh(1, 3, 3, 6));

        builder.add(new JButton("Edit"), cc.xy(5, 3));
        builder.add(new JButton("Remove"), cc.xy(5, 5));
        builder.add(new JButton("Advanced"), cc.xy(5, 7));

        JPanel emailPanel = builder.getPanel();
        emailPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("E-mail"),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        builder = new PanelBuilder(layout);
        builder.add(namePanel, cc.xy(1, 1));
        builder.add(emailPanel, cc.xy(1, 3));
        builder.add(ButtonBarFactory.buildOKCancelBar(
                new JButton("OK"), new JButton("Cancel")),
                cc.xywh(1, 5, 1, 1, "right, bottom"));
        JPanel p = builder.getPanel();
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        return p;
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("FormLayout");

        frame.getContentPane().add(
                new FormLayoutComparison().buildPanel(), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
