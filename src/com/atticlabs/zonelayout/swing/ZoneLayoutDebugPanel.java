package com.atticlabs.zonelayout.swing;

import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Line2D;
import java.util.Iterator;

/**
 * You can use this class to show you the zones and the rows and columns
 * of a ZoneLayout.  Zones are outlined in a red dashed lines, and the
 * rows and columns are distinguished with blue dashed lines.  The behavior
 * of this class is a little flaky right now due to how it renders the panel
 * so it causes slow redraws and occasionally doesn't refresh propertly.
 *
 * USE AT YOUR OWN RISK!!!!!!
 */
public class ZoneLayoutDebugPanel extends JPanel {
    ZoneLayoutImpl layout;
    JPanel targetPanel;
    JLayeredPane layeredPane;
    ZoneLayoutDebugRenderPanel renderPanel;

    /**
     * Wrap a JPanel with a Debug Panel that shows the zones and rows/columns of a ZoneLayout.
     * The newTargetPanel must be using ZoneLayout as it's layout manager!
     * @param newTargetPanel
     */
    public ZoneLayoutDebugPanel(JPanel newTargetPanel) {
        super(new BorderLayout());
        this.targetPanel = newTargetPanel;
        this.layout = (ZoneLayoutImpl) targetPanel.getLayout();
        renderPanel = new ZoneLayoutDebugRenderPanel(this.layout);
        layeredPane = new JLayeredPane();

        layeredPane.addComponentListener(new ComponentAdapter() {
            public void componentHidden(ComponentEvent e) {
                setBounds();
            }

            public void componentMoved(ComponentEvent e) {
                setBounds();
            }

            public void componentResized(ComponentEvent e) {
                setBounds();
            }

            public void componentShown(ComponentEvent e) {
                setBounds();
            }

            void setBounds() {
                targetPanel.setBounds(layeredPane.getBounds());
                renderPanel.setBounds(layeredPane.getBounds());
                targetPanel.revalidate();
                renderPanel.revalidate();
            }
        });

        super.add(layeredPane, BorderLayout.CENTER);

        layeredPane.add(targetPanel, new Integer(0));
        layeredPane.add(renderPanel, new Integer(1));
    }

    public Dimension getPreferredSize() {
        return targetPanel.getPreferredSize();
    }

    public Dimension getMinimumSize() {
        return targetPanel.getMinimumSize();
    }

    public Dimension getMaximumSize() {
        return targetPanel.getMaximumSize();
    }

    private class ZoneLayoutDebugRenderPanel extends JComponent {
        ZoneLayoutImpl layout;

        ZoneLayoutDebugRenderPanel(ZoneLayoutImpl layout) {
            setOpaque(false);
            this.layout = layout;
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, new float[] { 1.0f }, 0.0f));
            g2.setColor(Color.red);
            for (Iterator iterator = layout.getZones().values().iterator(); iterator.hasNext();) {
                Zone zone =  (Zone) iterator.next();
                g2.drawRect(zone.getDisplayRoot().x,  zone.getDisplayRoot().y, zone.getDisplayWidth() - 1, zone.getDisplayHeight() - 1);
//                int w = zone.getDisplaySize().width;
//                int h = zone.getDisplaySize().height;
//                g2.drawString("" + w + "," + h, zone.getDisplayRoot().x + 2, zone.getDisplayRoot().y + g2.getFontMetrics().getAscent() + 2);
            }

            g2.setColor(Color.blue);
            for (Iterator iterator = layout.rows.iterator(); iterator.hasNext();) {
                Row row = (Row) iterator.next();
                int y = row.getDisplayCoordinate();
                Line2D line = new Line2D.Double(0, y, getParent().getWidth(), y);
                g2.draw(line);
            }
            for (Iterator iterator = layout.columns.iterator(); iterator.hasNext();) {
                Column column = (Column) iterator.next();
                int x = column.getDisplayCoordinate();
                Line2D line = new Line2D.Double(x, 0, x, getParent().getHeight());
                g2.draw(line);
            }
            g2.dispose();
        }
    }
}

