package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.JComponent;
import java.awt.BorderLayout;
import java.awt.Dimension;

public class TonsOfComponentsTest {

    public JPanel buildPanel(JComponent[] components) {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("a+*ab+*bc+*c", "row");
        JPanel panel = new JPanel(layout);

        layout.insertTemplate("row");
        panel.add(components[0], "a");
        panel.add(components[1], "b");
        panel.add(components[2], "c");
        layout.insertTemplate("row");
        panel.add(components[3], "a");
        panel.add(components[4], "b");
        panel.add(components[5], "c");
        layout.insertTemplate("row");
        panel.add(components[6], "a");
        panel.add(components[7], "b");
        panel.add(components[8], "c");

        return panel;
    }

    public JPanel buildButtonPanel() {
        return buildPanel(createSecondLevelPanels());
    }

    public JComponent[] createSecondLevelPanels() {
        JComponent[] panels = new JComponent[9];
        for (int i = 0; i < 9; i++) {
            panels[i] = buildPanel(createPanels());
        }
        return panels;
    }

    public JComponent[] createPanels() {
        JComponent[] panels = new JComponent[9];
        for (int i = 0; i < 9; i++) {
            panels[i] = buildPanel(createButtons());
        }
        return panels;
    }

    public JComponent[] createButtons() {
        JComponent[] buttons = new JComponent[9];
        for (int i = 0; i < 9; i++) {
            buttons[i] = createButton(String.valueOf(i));
        }
        return buttons;
    }

    public JButton createButton(String label) {
        JButton button = new JButton(label) {
            int calls = 0;

            public Dimension getPreferredSize() {
                System.out.println(calls++);
                return super.getPreferredSize();
            }
        };
//        button.setPreferredSize(new Dimension(10, 10));
        return button;
    }


    public static void main(String[] args) {
        final JFrame frame = new JFrame("TonsOfComponents");
        frame.getContentPane().setLayout(new BorderLayout());

        frame.getContentPane().add(new TonsOfComponentsTest().buildButtonPanel(), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
