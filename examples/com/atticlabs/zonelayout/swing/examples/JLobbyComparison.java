package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;
import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.Dimension;

//import org.wonderly.awt.Packer;

/**
 * User: Brian Nahas
 * Date: Feb 2, 2006
 * Time: 2:26:09 PM
 */
public class JLobbyComparison {
    static JPanel buildZoneLayoutPanel() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("l.a>a2b-~b3c>c2d-~d");
        layout.addRow("+36................");
        layout.addRow("l.e*+.............e");

        JPanel p = new JPanel(layout);
        p.add(new JLabel("First Name:"), "a");
        p.add(new JLabel("Last Name:"), "c");
        p.add(new JTextField(), "b");
        p.add(new JTextField(), "d");
        p.add(new JTextArea(), "e");
        p.add(new JList(new Object[] { "my", "name", "is", "brian"}), "l");
        p.setPreferredSize(new Dimension(640, 480));
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return p;
    }

    static JPanel buildFormLayoutPanel() {
        FormLayout layout = new FormLayout("r:d, 3dlu, d:g, 5dlu, r:d, 3dlu, d:g",
                "p, 3dlu, f:d:g");

        CellConstraints cc = new CellConstraints();
        JPanel p = new JPanel(layout);
        p.add(new JLabel("First Name:"), cc.xy(1, 1));
        p.add(new JLabel("Last Name:"), cc.xy(5, 1));
        p.add(new JTextField(), cc.xy(3, 1));
        p.add(new JTextField(), cc.xy(7, 1));
        p.add(new JTextArea(), cc.xyw(1, 3, 7));

        p.setPreferredSize(new Dimension(640, 480));
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return p;
    }

    static JPanel buildGridBagLayoutPanel() {
        JPanel p = new JPanel();
//        Packer packer = new Packer(p);
//        packer.pack(new JLabel("First Name:")).gridx(0).east();
//        packer.pack(Box.createHorizontalStrut(6)).gridx(1);
//        packer.pack(new JTextField("")).gridx(2).fillx();
//        packer.pack(Box.createHorizontalStrut(12)).gridx(3);
//        packer.pack(new JLabel("Last Name:")).gridx(4).east();
//        packer.pack(Box.createHorizontalStrut(6)).gridx(5);
//        packer.pack(new JTextField("")).gridx(6).fillx();
//        packer.pack(Box.createVerticalStrut(12)).gridx(0).gridy(1).gridw(7);
//        packer.pack(new JTextArea()).gridx(0).gridy(2).gridw(7).fillboth();

        p.setPreferredSize(new Dimension(640, 480));
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        return p;
    }

    static void showFrame(String title, JPanel panel) {
        final JFrame frame = new JFrame(title);

        frame.getContentPane().add(panel, BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }

    static void createFrames() {
        showFrame("ZoneLayout", buildZoneLayoutPanel());
        showFrame("FormLayout", buildFormLayoutPanel());
        showFrame("GridBagLayout", buildGridBagLayoutPanel());
    }

    public static void main(String[] args) {
        createFrames();
    }
}
