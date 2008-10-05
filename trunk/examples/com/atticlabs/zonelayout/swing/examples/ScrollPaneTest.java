package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class ScrollPaneTest {

    public JPanel buildPanel() {
        JLabel firstLabel = new JLabel("First Label");
        firstLabel.setMinimumSize(new Dimension(0,0));
        JTextField firstField = new JTextField("First Field");
        firstField.setMinimumSize(new Dimension(0,0));

        JLabel secondLabel = new JLabel("Second Label");
        secondLabel.setMinimumSize(new Dimension(0,0));
        JTextField secondField = new JTextField("Second Field");
        secondField.setMinimumSize(new Dimension(0,0));

        JLabel thirdLabel = new JLabel("Third Label");
        thirdLabel.setMinimumSize(new Dimension(0,0));
        JTextField thirdField = new JTextField("Third Field");
        thirdField.setMinimumSize(new Dimension(0,0));

        JTextArea textArea = new JTextArea("Hey this is a text area");
        textArea.setMinimumSize(new Dimension(0,0));
        JScrollPane scroller = new JScrollPane(textArea);
        scroller.setMinimumSize(textArea.getMinimumSize());
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();

        layout.addRow(".5..............");
        layout.addRow("............r...");
        layout.addRow("2a>a2b..~-b2....", "pairRow");
        layout.addRow(".5..............");
        layout.addRow("2c.+*.....c2....");
        layout.addRow(".5..............", "lastPairRow");
        layout.addRow("2A<A2B..~-B2....", "lastPairRow");
        layout.addRow(".....!.......+*r");

        JPanel panel = new JPanel(layout);

        JButton r = new JButton("Button");
        r.setMinimumSize(new Dimension(0,0));
        panel.add(r, "r");

        layout.insertTemplate("pairRow");
        panel.add(firstLabel, "a");
        panel.add(firstField, "b");

        layout.insertTemplate("pairRow");
        panel.add(secondLabel, "a");
        panel.add(secondField, "b");

        layout.insertTemplate("lastPairRow");
        panel.add(thirdLabel, "A");
        panel.add(thirdField, "B");

        panel.add(scroller, "c");
        return panel;
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("ScrollPaneTest");

        frame.getContentPane().add(
                new ScrollPaneTest().buildPanel(), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
