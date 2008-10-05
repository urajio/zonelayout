package com.atticlabs.zonelayout.swing.examples;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.debug.FormDebugPanel;

import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import java.awt.Dimension;

/**
 * User: Brian Nahas
 * Date: Jan 25, 2006
 * Time: 1:35:33 PM
 */
public class SimpleFormLayout {
    public JPanel buildPanel() {
        FormLayout layout = new FormLayout(
                "d:g",
                "f:d:g"

        );
        FormDebugPanel panel = new FormDebugPanel(layout);
        PanelBuilder builder = new PanelBuilder(layout, panel);
        CellConstraints cc = new CellConstraints();
        JTextArea txtArea = new JTextArea(20, 100);
        txtArea.setMinimumSize(new Dimension(txtArea.getPreferredSize().width / 2, txtArea.getPreferredSize().height / 2));
        builder.add(txtArea, cc.xy(1, 1));
        JPanel p = builder.getPanel();
        p.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        System.out.println(layout.getRowSpec(1).getResizeWeight());
        System.out.println(layout.getRowSpec(1).getDefaultAlignment() == RowSpec.DEFAULT);
        return p;
    }

    public static void main(String[] args) {
        final JFrame frame = new JFrame("FormLayout");

        frame.getContentPane().add(
                new SimpleFormLayout().buildPanel(), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
