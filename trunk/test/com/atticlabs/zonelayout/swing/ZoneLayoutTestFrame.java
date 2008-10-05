package com.atticlabs.zonelayout.swing;

import com.atticlabs.zonelayout.swing.ZoneLayoutImpl;

import javax.swing.*;
import java.awt.*;

public class ZoneLayoutTestFrame {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("ZoneLayout Test");
        frame.getContentPane().setLayout(new BorderLayout());

        final ZoneLayout layout = new ZoneLayoutImpl();

        layout.addRow("i_ia-ah..");
        layout.addRow("j^jb-b...");
        layout.addRow("k_kc-c...");
        layout.addRow("l^ld-d.+.");
        layout.addRow("m_me-e.*.");
        layout.addRow("n^nf-f...");
        layout.addRow("o_og-g...");
        layout.addRow("p.|..p..h");
        layout.getZone("p").setTake(0, 1);

        final JLabel seven = createLabel("seven:");
        JPanel basePanel = new JPanel(layout);
        basePanel.add(createLabel("one:"), "i");
        basePanel.add(createLabel("two:"), "j");
        basePanel.add(createLabel("three:"), "k");
        basePanel.add(createLabel("four:"), "l");
        basePanel.add(createLabel("five:"), "m");
        basePanel.add(createLabel("six:"), "n");
        basePanel.add(seven, "o");
        basePanel.add(new JTextField(10), "a");
        basePanel.add(new JTextField(10), "b");
        basePanel.add(new JTextField(10), "c");
        basePanel.add(new JTextField(10), "d");
        basePanel.add(new JTextField(10), "e");
        basePanel.add(new JTextField(10), "f");
        basePanel.add(new JTextField(10), "g");
        basePanel.add(new JTextArea(), "h");
        basePanel.add(Box.createVerticalGlue(), "p");

        frame.getContentPane().add(basePanel, BorderLayout.CENTER);

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
}
