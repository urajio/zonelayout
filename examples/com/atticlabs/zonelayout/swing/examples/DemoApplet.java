package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;
import com.atticlabs.zonelayout.swing.Zone;

import javax.swing.JApplet;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.SwingUtilities;
import javax.swing.border.TitledBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;
import java.io.StringWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;
import java.util.Iterator;

public class DemoApplet extends JApplet {
    public void init() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("a+!a2b...");
        layout.addRow("6........");
        layout.addRow("c..c..+*b");
        layout.addRow("6........");
        layout.addRow("d+......d");
        getContentPane().setLayout(layout);

        final JTextArea layoutCode = new JTextArea();
        layoutCode.setColumns(20);
        layoutCode.setFont(new Font("Courier New", Font.BOLD, 12));
        final JTextArea errors = new JTextArea();
        errors.setColumns(15);
        errors.setRows(8);
        JButton runCode = new JButton("Compile");
        final JPanel resultPanel = new JPanel();
        final JScrollPane resultScrollPane = new JScrollPane(resultPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Results"));

        JScrollPane layoutCodeScrollPane = new JScrollPane(layoutCode, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        layoutCodeScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Layout Code"));
        final JScrollPane errorsScrollPane = new JScrollPane(errors, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        errorsScrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Errors"));

        getContentPane().add(layoutCodeScrollPane, "a");
        getContentPane().add(errorsScrollPane, "d");
        getContentPane().add(resultScrollPane, "b");
        getContentPane().add(runCode, "c");
        getContentPane().setBackground(Color.white);
        resultPanel.setBackground(Color.white);
        resultScrollPane.setBackground(Color.white);
        errorsScrollPane.setBackground(Color.white);
        layoutCodeScrollPane.setBackground(Color.white);
        runCode.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                errors.setText("");
                ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();

                try {
                    BufferedReader reader = new BufferedReader(new StringReader(layoutCode.getText()));

                    String line = reader.readLine();
                    while (line != null) {
                        layout.addRow(line);
                        line = reader.readLine();
                    }

                    layout.compile();

                    resultPanel.removeAll();
                    resultPanel.setLayout(layout);

                    for (Iterator iterator = layout.getZones().values().iterator(); iterator.hasNext();) {
                        Zone zone = (Zone) iterator.next();

                        if (! zone.isPresetZone()) {
                            JButton button = new JButton(zone.getId());
                            button.setPreferredSize(new Dimension(50, 20));
                            resultPanel.add(button, zone.getId());
                        }
                    }
                    System.out.println(resultPanel.getPreferredSize());
                    resultPanel.revalidate();
                    resultPanel.repaint();
                }
                catch (Exception ex) {
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(new BufferedWriter(sw));
                    pw.println(ex.getMessage());
                    ex.printStackTrace(pw);
                    pw.flush();
                    errors.setText(sw.toString());
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            errorsScrollPane.getVerticalScrollBar().setValue(0);
                        }
                    });
                }
            }
        });
    }
}
