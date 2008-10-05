package com.atticlabs.zonelayout.swing.examples;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class BorderLayoutComparison {
    public static final Object[] values = new Object[]{
            "Item 1", "Item 2", "Item 3", "Item 4", "Item 5"
    };

    public JPanel buildPanelWithFormLayout() {
        JButton north = new JButton("north");
        JButton south = new JButton("south");
        JButton east = new JButton("east");
        JButton west = new JButton("west");
        JButton center = new JButton("center");

        FormLayout layout = new FormLayout(
                "f:d, f:d:g, f:d",
                "f:d, f:d:g, f:d"
        );

        JPanel p = new JPanel(layout);
        CellConstraints cc = new CellConstraints();
        p.add(north, cc.xyw(1, 1, 3));
        p.add(south, cc.xyw(1, 3, 3));
        p.add(east, cc.xy(3, 2));
        p.add(west, cc.xy(1, 2));
        p.add(center, cc.xy(2, 2));

        return p;
    }

    public JPanel buildPanelWithGridBagLayout() {
        JButton north = new JButton("north");
        JButton south = new JButton("south");
        JButton east = new JButton("east");
        JButton west = new JButton("west");
        JButton center = new JButton("center");

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 0;
        gc.gridwidth = 3;
        gc.fill = GridBagConstraints.HORIZONTAL;
        p.add(north, gc);
        gc.gridx = 0;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.VERTICAL;
        p.add(west, gc);
        gc.gridx = 2;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.VERTICAL;
        p.add(east, gc);
        gc.gridx = 0;
        gc.gridy = 2;
        gc.gridwidth = 3;
        gc.fill = GridBagConstraints.HORIZONTAL;
        p.add(south, gc);
        gc.gridx = 1;
        gc.gridy = 1;
        gc.gridwidth = 1;
        gc.fill = GridBagConstraints.BOTH;
        gc.weightx = 1.0;
        gc.weighty = 1.0;
        p.add(center, gc);

        return p;
    }

    public JPanel buildPanelWithZoneLayout() {
        JButton north = new JButton("north");
        JButton south = new JButton("south");
        JButton east = new JButton("east");
        JButton west = new JButton("west");
        JButton center = new JButton("center");

        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        JPanel p = new JPanel(layout);
        layout.addRow("n..-~n");
        layout.addRow("wc...e");
        layout.addRow("|.*+.!");
        layout.addRow("!....|");
        layout.addRow("w...ce");
        layout.addRow("s..-~s");

        p.add(north, "n");
        p.add(south, "s");
        p.add(east, "e");
        p.add(west, "w");
        p.add(center, "c");

        return p;
    }

    public JPanel buildPanelWithBorderLayout() {
        JButton north = new JButton("north");
        JButton south = new JButton("south");
        JButton east = new JButton("east");
        JButton west = new JButton("west");
        JButton center = new JButton("center");

        JPanel p = new JPanel(new BorderLayout());
        p.add(north, BorderLayout.NORTH);
        p.add(south, BorderLayout.SOUTH);
        p.add(east, BorderLayout.EAST);
        p.add(west, BorderLayout.WEST);
        p.add(center, BorderLayout.CENTER);

        return p;
    }

    public static void main(String[] args) {
        final JFrame formLayoutFrame = new JFrame("FormLayout");
        formLayoutFrame.getContentPane().add(
                new BorderLayoutComparison().buildPanelWithFormLayout(), BorderLayout.CENTER);

        formLayoutFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                formLayoutFrame.pack();
                formLayoutFrame.setVisible(true);
            }
        });

        final JFrame gridBagLayoutFrame = new JFrame("GridBagLayout");
        gridBagLayoutFrame.getContentPane().add(
                new BorderLayoutComparison().buildPanelWithGridBagLayout(), BorderLayout.CENTER);

        gridBagLayoutFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                gridBagLayoutFrame.pack();
                gridBagLayoutFrame.setVisible(true);
            }
        });

        final JFrame zoneLayoutFrame = new JFrame("ZoneLayout");
        zoneLayoutFrame.getContentPane().add(
                new BorderLayoutComparison().buildPanelWithZoneLayout(), BorderLayout.CENTER);

        zoneLayoutFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                zoneLayoutFrame.pack();
                zoneLayoutFrame.setVisible(true);
            }
        });
    }
}
