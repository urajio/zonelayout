package com.atticlabs.zonelayout.swing;

import com.atticlabs.zonelayout.swing.ZoneLayoutImpl;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.BorderFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

public class ZoneLayoutTemplateTestFrame {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("ZoneLayout Test");
        frame.getContentPane().setLayout(new BorderLayout());

        final ZoneLayout layout = new ZoneLayoutImpl();
        layout.addRow("a......ae");
        layout.addRow("b>b1c~-c*", "label-value");
        layout.addRow("...7....+", "label-value");
        layout.addRow("d......de");
        JPanel basePanel = new JPanel(layout);
        basePanel.add(createLabel("Values"), "a");
        basePanel.add(createLabel("Copyright 2005"), "d");
        basePanel.add(new JTextArea(20, 20), "e");

        for (int i = 0; i < 10; i++) {
            layout.insertTemplate("label-value");
            basePanel.add(createLabel(String.valueOf(i)), "b");
            basePanel.add(createTextField(100, 200), "c");
        }

        frame.getContentPane().add(basePanel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });

//        final JFrame frame = new JFrame("ZoneLayout Test");
//        frame.getContentPane().setLayout(new BorderLayout());
//
//        JPanel basePanel = new JPanel(new GridBagLayout());
//        GridBagConstraints gc = new GridBagConstraints();
//        gc.gridx = 0; gc.gridy = 0; gc.gridwidth = 2; gc.anchor = GridBagConstraints.CENTER;
//        basePanel.add(createLabel("Values"), gc);
//
//        gc.gridwidth = 1;
//        gc.gridy++;
//        gc.anchor = GridBagConstraints.EAST;
//        for (int i = 0; i < 10; i++) {
//            gc.gridx = 0;
//            gc.fill = GridBagConstraints.NONE;
//            basePanel.add(createLabel(String.valueOf(i)), gc);
//            gc.gridx = 1;
//            gc.fill = GridBagConstraints.HORIZONTAL;
//            basePanel.add(new JTextField(), gc);
//            gc.gridy++;
//        }
//
//        gc.gridx = 0; gc.gridwidth = 2; gc.anchor = GridBagConstraints.CENTER; gc.fill = GridBagConstraints.NONE;
//        basePanel.add(createLabel("Copyright 2005"), gc);
//
//        frame.getContentPane().add(basePanel, BorderLayout.CENTER);
//
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                frame.pack();
//                frame.setVisible(true);
//            }
//        });

    }

    public static JLabel createLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setBorder(BorderFactory.createLineBorder(Color.red));
        return lbl;
    }

    public static JTextField createTextField(int min, int pref) {
        JTextField txt = new JTextField();
        txt.setMinimumSize(new Dimension(min, txt.getMinimumSize().height));
        txt.setPreferredSize(new Dimension(pref, txt.getPreferredSize().height));
        return txt;
    }
}
