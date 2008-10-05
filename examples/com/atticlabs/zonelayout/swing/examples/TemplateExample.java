package com.atticlabs.zonelayout.swing.examples;

import com.atticlabs.zonelayout.swing.ZoneLayout;
import com.atticlabs.zonelayout.swing.ZoneLayoutFactory;
import com.atticlabs.zonelayout.swing.ZoneLayoutDebugPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

public class TemplateExample {
    public static void main(String[] args) {
        final JFrame frame = new JFrame("Template Example");
        frame.getContentPane().setLayout(new BorderLayout());

        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("h......h");
        layout.addRow("...6....");
        layout.addRow("k>k2v-~v", "mapEntry");
        layout.addRow("...6....", "mapEntry");

        Map myMap = new HashMap();
        myMap.put("Product", "ZoneLayout");
        myMap.put("Version", "1.0");
        myMap.put("Vendor", "Attic Labs LLC");

        JPanel basePanel = new JPanel(layout);
        basePanel.add(new JLabel("Map Values"), "h");

        for (Iterator iterator = myMap.keySet().iterator(); iterator.hasNext();) {
            String key = (String) iterator.next();

            layout.insertTemplate("mapEntry");

            basePanel.add(new JLabel(key + ":"), "k");
            basePanel.add(new JTextField((String) myMap.get(key)), "v");
        }

        frame.getContentPane().add(new ZoneLayoutDebugPanel(basePanel), BorderLayout.CENTER);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
