package com.atticlabs.zonelayout.swing;

import javax.swing.*;
import java.awt.*;

public class GiveTakeTestFrame {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("GiveTake Test");
        frame.getContentPane().setLayout(new BorderLayout());

        final ZoneLayout layout = new ZoneLayoutImpl();
        layout.addRow("a+ab+bc+cd+de+eg");
        layout.addRow("f..*+..........+");
        layout.addRow("..............fg");
        layout.compile();
        layout.getZone("f").setTake(10, 10);
        layout.getZone("g").setTake(10, 10);
        layout.getZone("a").setTake(5, 0);
        layout.getZone("e").setTake(5, 0);
//        layout.getZone("a").setGive(20, 0);
//        layout.getZone("b").setGive(20, 0);
//        layout.getZone("c").setGive(20, 0);
//        layout.getZone("d").setGive(20, 0);
//        layout.getZone("e").setGive(20, 0);
//        layout.getZone("f").setGive(4, 0);
//        layout.getZone("g").setGive(20, 0);

        JPanel basePanel = new JPanel(layout);
        basePanel.add(createButton("a"), "a");
        basePanel.add(createButton("b"), "b");
        basePanel.add(createButton("c"), "c");
        basePanel.add(createButton("d"), "d");
        basePanel.add(createButton("e"), "e");
        basePanel.add(createButton("f"), "f");
        basePanel.add(createButton("g"), "g");
        layout.getZone("a").getComponent().setVisible(false);
        frame.getContentPane().add(new ZoneLayoutDebugPanel(basePanel), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });

    }

    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setBorder(BorderFactory.createLineBorder(Color.red));
        return lbl;
    }

    public static JButton createButton(String label) {
        JButton button = new JButton(label);
        button.setPreferredSize(new Dimension(100, 100));
        button.setMinimumSize(new Dimension(50, 50));
        return button;
    }
}

