package com.atticlabs.zonelayout.swing;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import javax.swing.JTree;
import javax.swing.JFrame;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Component;
import java.util.HashSet;
import java.util.Set;


public class ZoneLayoutTest extends TestCase {
    public ZoneLayoutTest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(ZoneLayoutTest.class);
    }

    public void testCoordinatesWithoutOptimization() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();

        layout.addRow("ab.b");
        layout.addRow("c..c");
        layout.addRow("d...");
        layout.addRow("...d");
        layout.compile(false);

        assertEquals(layout.getZone("a").getX(), 0);
        assertEquals(layout.getZone("a").getY(), 0);
        assertEquals(layout.getZone("a").getWidth(), 1);
        assertEquals(layout.getZone("a").getHeight(), 1);

        assertEquals(layout.getZone("b").getX(), 1);
        assertEquals(layout.getZone("b").getY(), 0);
        assertEquals(layout.getZone("b").getWidth(), 3);
        assertEquals(layout.getZone("b").getHeight(), 1);

        assertEquals(layout.getZone("c").getX(), 0);
        assertEquals(layout.getZone("c").getY(), 1);
        assertEquals(layout.getZone("c").getWidth(), 4);
        assertEquals(layout.getZone("c").getHeight(), 1);

        assertEquals(layout.getZone("d").getX(), 0);
        assertEquals(layout.getZone("d").getY(), 2);
        assertEquals(layout.getZone("d").getWidth(), 4);
        assertEquals(layout.getZone("d").getHeight(), 2);
    }

    public void testAlignment() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();

        layout.addRow("ab<bc<_cd_de_>ef>fg^>gh^hi<^i");
        layout.compile(false);

        assertEquals(layout.getZone("a").getHorizontalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("a").getVerticalAlignment(), Zone.ALIGN_MIDDLE);

        assertEquals(layout.getZone("b").getHorizontalAlignment(), Zone.ALIGN_LEFT);
        assertEquals(layout.getZone("b").getVerticalAlignment(), Zone.ALIGN_MIDDLE);

        assertEquals(layout.getZone("c").getHorizontalAlignment(), Zone.ALIGN_LEFT);
        assertEquals(layout.getZone("c").getVerticalAlignment(), Zone.ALIGN_BOTTOM);

        assertEquals(layout.getZone("d").getHorizontalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("d").getVerticalAlignment(), Zone.ALIGN_BOTTOM);

        assertEquals(layout.getZone("e").getHorizontalAlignment(), Zone.ALIGN_RIGHT);
        assertEquals(layout.getZone("e").getVerticalAlignment(), Zone.ALIGN_BOTTOM);

        assertEquals(layout.getZone("f").getHorizontalAlignment(), Zone.ALIGN_RIGHT);
        assertEquals(layout.getZone("f").getVerticalAlignment(), Zone.ALIGN_MIDDLE);

        assertEquals(layout.getZone("g").getHorizontalAlignment(), Zone.ALIGN_RIGHT);
        assertEquals(layout.getZone("g").getVerticalAlignment(), Zone.ALIGN_TOP);

        assertEquals(layout.getZone("h").getHorizontalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("h").getVerticalAlignment(), Zone.ALIGN_TOP);

        assertEquals(layout.getZone("i").getHorizontalAlignment(), Zone.ALIGN_LEFT);
        assertEquals(layout.getZone("i").getVerticalAlignment(), Zone.ALIGN_TOP);

    }

    public void testComponentPositioning() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("a*a"); // aa
        layout.compile();

        JComponent a = createComponent(10, 10, 10, 10);
//        layout.addLayoutComponent(a, "a");
        JPanel p = new JPanel(layout);
        p.add(a, "a");
        Dimension d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() + 10, d.getHeight() + 10);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(20, ((Section) layout.rows.get(0)).getSize());
        assertEquals(20, ((Section) layout.columns.get(0)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("a").getDisplayRoot().x);
        assertEquals(0, layout.getZone("a").getDisplayRoot().y);
        assertEquals(19, layout.getZone("a").getDisplaySecondRoot().x);
        assertEquals(19, layout.getZone("a").getDisplaySecondRoot().y);

        assertEquals(10, a.getBounds().width);
        assertEquals(10, a.getBounds().height);

        assertEquals(5, a.getBounds().x);
        assertEquals(5, a.getBounds().y);

        layout.invalidateLayout(p);
        layout.getZone("a").setHorizontalAlignment(Zone.ALIGN_LEFT);
        layout.getZone("a").setVerticalAlignment(Zone.ALIGN_MIDDLE);
        layout.layoutContainer(p);
        assertEquals(0, a.getBounds().x);
        assertEquals(5, a.getBounds().y);

        layout.invalidateLayout(p);
        layout.getZone("a").setHorizontalAlignment(Zone.ALIGN_LEFT);
        layout.getZone("a").setVerticalAlignment(Zone.ALIGN_TOP);
        layout.layoutContainer(p);
        assertEquals(0, a.getBounds().x);
        assertEquals(0, a.getBounds().y);

        layout.invalidateLayout(p);
        layout.getZone("a").setHorizontalAlignment(Zone.ALIGN_MIDDLE);
        layout.getZone("a").setVerticalAlignment(Zone.ALIGN_TOP);
        layout.layoutContainer(p);
        assertEquals(5, a.getBounds().x);
        assertEquals(0, a.getBounds().y);

        layout.invalidateLayout(p);
        layout.getZone("a").setHorizontalAlignment(Zone.ALIGN_RIGHT);
        layout.getZone("a").setVerticalAlignment(Zone.ALIGN_TOP);
        layout.layoutContainer(p);
        assertEquals(10, a.getBounds().x);
        assertEquals(0, a.getBounds().y);

        layout.invalidateLayout(p);
        layout.getZone("a").setHorizontalAlignment(Zone.ALIGN_RIGHT);
        layout.getZone("a").setVerticalAlignment(Zone.ALIGN_MIDDLE);
        layout.layoutContainer(p);
        assertEquals(10, a.getBounds().x);
        assertEquals(5, a.getBounds().y);

        layout.invalidateLayout(p);
        layout.getZone("a").setHorizontalAlignment(Zone.ALIGN_RIGHT);
        layout.getZone("a").setVerticalAlignment(Zone.ALIGN_BOTTOM);
        layout.layoutContainer(p);
        assertEquals(10, a.getBounds().x);
        assertEquals(10, a.getBounds().y);

        layout.invalidateLayout(p);
        layout.getZone("a").setHorizontalAlignment(Zone.ALIGN_MIDDLE);
        layout.getZone("a").setVerticalAlignment(Zone.ALIGN_BOTTOM);
        layout.layoutContainer(p);
        assertEquals(5, a.getBounds().x);
        assertEquals(10, a.getBounds().y);

        layout.invalidateLayout(p);
        layout.getZone("a").setHorizontalAlignment(Zone.ALIGN_LEFT);
        layout.getZone("a").setVerticalAlignment(Zone.ALIGN_BOTTOM);
        layout.layoutContainer(p);
        assertEquals(0, a.getBounds().x);
        assertEquals(10, a.getBounds().y);
    }

    public void testFill() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("aab-bc|cd+d");
        layout.compile(false);

        assertEquals(layout.getZone("a").getHorizontalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("a").getVerticalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("b").getHorizontalAlignment(), Zone.ALIGN_FILL);
        assertEquals(layout.getZone("b").getVerticalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("c").getHorizontalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("c").getVerticalAlignment(), Zone.ALIGN_FILL);
        assertEquals(layout.getZone("d").getVerticalAlignment(), Zone.ALIGN_FILL);
        assertEquals(layout.getZone("d").getHorizontalAlignment(), Zone.ALIGN_FILL);
    }

    public void testIntersection() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("ab-bc|cd+d"); // abccd
        layout.addRow("x........."); // x   x
        layout.addRow(".........x"); // y yzz
        layout.addRow("y....yz..z");
        layout.compile();

        assertEquals(getZones(layout, new String[] { "a", "b", "c", "d" }),
                layout.getZonesThatIntersectAxisLine(AxisView.Y, new Point(0,0)));
        assertEquals(getZones(layout, new String[] { "x" }),
                layout.getZonesThatIntersectAxisLine(AxisView.Y, new Point(0,1)));
        assertEquals(getZones(layout, new String[] { "y", "z" }),
                layout.getZonesThatIntersectAxisLine(AxisView.Y, new Point(0,2)));

        assertEquals(getZones(layout, new String[] { "a", "x", "y" }),
                layout.getZonesThatIntersectAxisLine(AxisView.X, new Point(0,0)));
        assertEquals(getZones(layout, new String[] { "b", "x", "y" }),
                layout.getZonesThatIntersectAxisLine(AxisView.X, new Point(1,0)));
        assertEquals(getZones(layout, new String[] { "c", "x", "y" }),
                layout.getZonesThatIntersectAxisLine(AxisView.X, new Point(2,0)));
        assertEquals(getZones(layout, new String[] { "c", "x", "z" }),
                layout.getZonesThatIntersectAxisLine(AxisView.X, new Point(3,0)));
        assertEquals(getZones(layout, new String[] { "d", "x", "z" }),
                layout.getZonesThatIntersectAxisLine(AxisView.X, new Point(4,0)));
    }

    public void testContainment() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("ab-bc|cd+d"); // abccd
        layout.addRow("x........."); // x   x
        layout.addRow(".........x"); // y yzz
        layout.addRow("y....yz..z");
        layout.compile();

        assertEquals(getZones(layout, new String[] { "a", "b", "c", "d", "x" }),
                layout.getZonesContainedWithinAxisLines(AxisView.Y, new Point(0,0), new Point(0,1)));
        assertEquals(getZones(layout, new String[] { "y", "z", "x" }),
                layout.getZonesContainedWithinAxisLines(AxisView.Y, new Point(0,1), new Point(0,2)));
        assertEquals(getZones(layout, new String[] { "a", "b" }),
                layout.getZonesContainedWithinAxisLines(AxisView.X, new Point(0,0), new Point(1,0)));
        assertEquals(getZones(layout, new String[] { "c", "d", "z" }),
                layout.getZonesContainedWithinAxisLines(AxisView.X, new Point(2,0), new Point(4,0)));
    }

    protected Set getZones(ZoneLayout layout, String[] zoneIds) {
        Set zones = new HashSet();
        for (int i = 0; i < zoneIds.length; i++) {
            String zone = zoneIds[i];
            zones.add(layout.getZone(zone));
        }
        return zones;
    }

    public void testCoordinates() {
        ZoneLayout layout = new ZoneLayoutImpl();
        layout.addRow("ab-bc|cd+d"); // abccd
        layout.addRow("x........."); // x   x
        layout.addRow(".........x"); // y yzz
        layout.addRow("y....yz..z");
        layout.compile();

//        assertEquals(3, layout.rows.size());
//        assertEquals(5, layout.columns.size());
//        assertEquals(0, ((Row) layout.rows.get(0)).getCoordinate());
//        assertEquals(1, ((Row) layout.rows.get(1)).getCoordinate());
//        assertEquals(2, ((Row) layout.rows.get(2)).getCoordinate());
//        assertEquals(0, ((Column) layout.columns.get(0)).getCoordinate());
//        assertEquals(1, ((Column) layout.columns.get(1)).getCoordinate());
//        assertEquals(2, ((Column) layout.columns.get(2)).getCoordinate());
//        assertEquals(3, ((Column) layout.columns.get(3)).getCoordinate());
//        assertEquals(4, ((Column) layout.columns.get(4)).getCoordinate());
        assertEquals(new Point(0,0), layout.getZone("a").getRoot());
        assertEquals(new Point(0,0), layout.getZone("a").getSecondRoot());
        assertEquals(new Point(1,0), layout.getZone("b").getRoot());
        assertEquals(new Point(1,0), layout.getZone("b").getSecondRoot());
        assertEquals(new Point(2,0), layout.getZone("c").getRoot());
        assertEquals(new Point(3,0), layout.getZone("c").getSecondRoot());
        assertEquals(new Point(4,0), layout.getZone("d").getRoot());
        assertEquals(new Point(4,0), layout.getZone("d").getSecondRoot());
        assertEquals(new Point(0,1), layout.getZone("x").getRoot());
        assertEquals(new Point(4,1), layout.getZone("x").getSecondRoot());
        assertEquals(new Point(0,2), layout.getZone("y").getRoot());
        assertEquals(new Point(2,2), layout.getZone("y").getSecondRoot());
        assertEquals(new Point(3,2), layout.getZone("z").getRoot());
        assertEquals(new Point(4,2), layout.getZone("z").getSecondRoot());

        layout = new ZoneLayoutImpl();
        layout.addRow("ab-b.c"); // abc
        layout.addRow("x....."); // x x
        layout.addRow("...x.."); // y y
        layout.addRow("......");
        layout.addRow("y..y..");
        layout.compile();

//        assertEquals(3, layout.rows.size());
//        assertEquals(5, layout.columns.size());
//        assertEquals(0, ((Row) layout.rows.get(0)).getCoordinate());
//        assertEquals(1, ((Row) layout.rows.get(1)).getCoordinate());
//        assertEquals(2, ((Row) layout.rows.get(2)).getCoordinate());
//        assertEquals(0, ((Column) layout.columns.get(0)).getCoordinate());
//        assertEquals(1, ((Column) layout.columns.get(1)).getCoordinate());
//        assertEquals(2, ((Column) layout.columns.get(2)).getCoordinate());
//        assertEquals(3, ((Column) layout.columns.get(3)).getCoordinate());
//        assertEquals(4, ((Column) layout.columns.get(4)).getCoordinate());
        assertEquals(new Point(0,0), layout.getZone("a").getRoot());
        assertEquals(new Point(0,0), layout.getZone("a").getSecondRoot());
        assertEquals(new Point(1,0), layout.getZone("b").getRoot());
        assertEquals(new Point(1,0), layout.getZone("b").getSecondRoot());
        assertEquals(new Point(2,0), layout.getZone("c").getRoot());
        assertEquals(new Point(2,0), layout.getZone("c").getSecondRoot());
        assertEquals(new Point(0,1), layout.getZone("x").getRoot());
        assertEquals(new Point(1,1), layout.getZone("x").getSecondRoot());
        assertEquals(new Point(0,2), layout.getZone("y").getRoot());
        assertEquals(new Point(1,2), layout.getZone("y").getSecondRoot());
    }

    public void testSectionSizes() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("ab-bc*|cd+d"); // abccd
        layout.addRow("x.........."); // x   x
        layout.addRow("..........x"); // y yzz
        layout.addRow("y....yz...z");
        layout.compile();
        layout.createSections();

        layout.addLayoutComponent(createComponent(1, 1, 2, 2), "a");
        layout.addLayoutComponent(createComponent(2, 2, 3, 3), "b");
        layout.addLayoutComponent(createComponent(3, 3, 4, 4), "c");
        layout.addLayoutComponent(createComponent(4, 4, 5, 5), "d");
        layout.addLayoutComponent(createComponent(5, 5, 6, 6), "x");
        layout.addLayoutComponent(createComponent(6, 6, 7, 7), "y");
        layout.addLayoutComponent(createComponent(7, 7, 8, 8), "z");
        layout.computeSectionSizes();
        assertEquals(4, ((Section)layout.rows.get(0)).getMinimumSize());
        assertEquals(5, ((Section)layout.rows.get(1)).getMinimumSize());
        assertEquals(7, ((Section)layout.rows.get(2)).getMinimumSize());
        assertEquals(1, ((Section)layout.columns.get(0)).getMinimumSize());
        assertEquals(2, ((Section)layout.columns.get(1)).getMinimumSize());
        assertEquals(3, ((Section)layout.columns.get(2)).getMinimumSize());
        assertEquals(3, ((Section)layout.columns.get(3)).getMinimumSize());
        assertEquals(4, ((Section)layout.columns.get(4)).getMinimumSize());
        assertEquals(5, ((Section)layout.rows.get(0)).getPreferredSize());
        assertEquals(6, ((Section)layout.rows.get(1)).getPreferredSize());
        assertEquals(8, ((Section)layout.rows.get(2)).getPreferredSize());
        assertEquals(2, ((Section)layout.columns.get(0)).getPreferredSize());
        assertEquals(3, ((Section)layout.columns.get(1)).getPreferredSize());
        assertEquals(3, ((Section)layout.columns.get(2)).getPreferredSize());
        assertEquals(3, ((Section)layout.columns.get(3)).getPreferredSize());
        assertEquals(5, ((Section)layout.columns.get(4)).getPreferredSize());

    }

    public void testAdjustGives() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("abcdef"); // abccd
        layout.addRow("g....g"); // x   x
        layout.getZone("a").setGive(5, 0);
        layout.getZone("b").setGive(6, 0);
        layout.getZone("c").setGive(7, 0);
        layout.getZone("d").setGive(8, 0);
        layout.getZone("e").setGive(9, 0);
        layout.getZone("f").setGive(10, 0);
        layout.getZone("g").setGive(24, 0);
        layout.compile();
        layout.createSections();

        assertEquals(0d, ((Row)layout.rows.get(0)).getGive(), 0.01);
        assertEquals(0d, ((Row)layout.rows.get(1)).getGive(), 0.01);
        assertEquals(2.67, ((Column)layout.columns.get(0)).getGive(), 0.01);
        assertEquals(3.20, ((Column)layout.columns.get(1)).getGive(), 0.01);
        assertEquals(3.73, ((Column)layout.columns.get(2)).getGive(), 0.01);
        assertEquals(4.27, ((Column)layout.columns.get(3)).getGive(), 0.01);
        assertEquals(4.80, ((Column)layout.columns.get(4)).getGive(), 0.01);
        assertEquals(5.33, ((Column)layout.columns.get(5)).getGive(), 0.01);

        layout = new ZoneLayoutImpl();
        layout.addRow("ag");
        layout.addRow("b.");
        layout.addRow("c.");
        layout.addRow("d.");
        layout.addRow("e.");
        layout.addRow("fg");
        layout.getZone("a").setGive(0, 11);
        layout.getZone("b").setGive(0, 12);
        layout.getZone("c").setGive(0, 13);
        layout.getZone("d").setGive(0, 14);
        layout.getZone("e").setGive(0, 15);
        layout.getZone("f").setGive(0, 30);
        layout.getZone("g").setGive(0, 60);
        layout.compile();
        layout.createSections();

        assertEquals(6.95, ((Row)layout.rows.get(0)).getGive(), 0.01);
        assertEquals(7.58, ((Row)layout.rows.get(1)).getGive(), 0.01);
        assertEquals(8.21, ((Row)layout.rows.get(2)).getGive(), 0.01);
        assertEquals(8.84, ((Row)layout.rows.get(3)).getGive(), 0.01);
        assertEquals(9.47, ((Row)layout.rows.get(4)).getGive(), 0.01);
        assertEquals(18.95, ((Row)layout.rows.get(5)).getGive(), 0.01);
        assertEquals(0d, ((Column)layout.columns.get(0)).getGive(), 0.01);
        assertEquals(0d, ((Column)layout.columns.get(1)).getGive(), 0.01);
    }

    public void testSectionWeights() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("ab-bc*|cd+d"); // abccd
        layout.addRow("x.........."); // x   x
        layout.addRow("..........x"); // y yzz
        layout.addRow("y....yz...z");
        layout.compile();
        layout.createSections();

        assertEquals(0d, ((Section)layout.columns.get(0)).getTake(), 0.01);
        assertEquals(0d, ((Section)layout.columns.get(1)).getTake(), 0.01);
        assertEquals(.5, ((Section)layout.columns.get(2)).getTake(), 0.01);
        assertEquals(.5, ((Section)layout.columns.get(3)).getTake(), 0.01);
        assertEquals(0d, ((Section)layout.columns.get(4)).getTake(), 0.01);
        assertEquals(1d, ((Section)layout.rows.get(0)).getTake(), 0.01);
        assertEquals(0d, ((Section)layout.rows.get(1)).getTake(), 0.01);
        assertEquals(0d, ((Section)layout.rows.get(2)).getTake(), 0.01);

        layout = new ZoneLayoutImpl();
        layout.addRow("a.ae."); // ae
        layout.addRow("b.b*."); // b
        layout.addRow("c.c.."); // c
        layout.addRow("d*d.e"); // de
        layout.compile();
        layout.createSections();

        assertEquals(1, ((Section)layout.columns.get(0)).getTake(), 0.01);
        assertEquals(1, ((Section)layout.columns.get(1)).getTake(), 0.01);
        assertEquals(0, ((Section)layout.rows.get(0)).getTake(), 0.01);
        assertEquals(0, ((Section)layout.rows.get(1)).getTake(), 0.01);
        assertEquals(0, ((Section)layout.rows.get(2)).getTake(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(3)).getTake(), 0.01);

        layout = new ZoneLayoutImpl();
        layout.addRow("a.ae."); // ae
        layout.addRow("b.b.."); // b
        layout.addRow("c.c.."); // c
        layout.addRow("d.d.e"); // de
        layout.getZone("d").setTake(0, 1);
        layout.getZone("e").setTake(2, 2);
        layout.compile();
        layout.createSections();

        assertEquals(0, ((Section)layout.columns.get(0)).getTake(), 0.01);
        assertEquals(2, ((Section)layout.columns.get(1)).getTake(), 0.01);
        assertEquals(0, ((Section)layout.rows.get(0)).getTake(), 0.01);
        assertEquals(0, ((Section)layout.rows.get(1)).getTake(), 0.01);
        assertEquals(0, ((Section)layout.rows.get(2)).getTake(), 0.01);
        assertEquals(2, ((Section)layout.rows.get(3)).getTake(), 0.01);

        layout = new ZoneLayoutImpl();
        layout.addRow("a.ae."); // ae
        layout.addRow("b.b.."); // b
        layout.addRow("c.c.."); // c
        layout.addRow("d.d.e"); // de
        layout.compile();
        layout.createSections();

        assertEquals(4, layout.getZone("e").getGive().vertical);
        assertEquals(1, layout.getZone("e").getGive().horizontal);
        assertEquals(1, ((Section)layout.columns.get(0)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.columns.get(1)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(0)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(1)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(2)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(3)).getGive(), 0.01);

        layout = new ZoneLayoutImpl();
        layout.addRow("a.ae."); // ae
        layout.addRow("b.b.."); // b
        layout.addRow("c.c.."); // c
        layout.addRow("d.d.e"); // de
        layout.getZone("a").setGive(2, 0);
        layout.getZone("d").setGive(2, 2);
        layout.getZone("e").setGive(10, 40);
        layout.getZone("b").setGive(20, 20);
        layout.getZone("c").setGive(50, 50);
        layout.compile();
        layout.createSections();

        assertEquals(2, ((Section)layout.columns.get(0)).getGive(), 0.01);
        assertEquals(10, ((Section)layout.columns.get(1)).getGive(), 0.01);
        assertEquals(0, ((Section)layout.rows.get(0)).getGive(), 0.01);
        assertEquals(11.11, ((Section)layout.rows.get(1)).getGive(), 0.01);
        assertEquals(27.78, ((Section)layout.rows.get(2)).getGive(), 0.01);
        assertEquals(1.11, ((Section)layout.rows.get(3)).getGive(), 0.01);
    }

    public void testZoneSizes() {
        ZoneLayout layout = new ZoneLayoutImpl();
        layout.addRow("ab-bc*|cd+d"); // abccd
        layout.addRow("x.........."); // x   x
        layout.addRow("..........x"); // y yzz
        layout.addRow("y....yz...z");
        layout.compile();

        layout.addLayoutComponent(createComponent(1, 1, 2, 2), "a");
        layout.addLayoutComponent(createComponent(2, 2, 3, 3), "b");
        layout.addLayoutComponent(createComponent(3, 3, 4, 4), "c");
        layout.addLayoutComponent(createComponent(4, 4, 5, 5), "d");
        layout.addLayoutComponent(createComponent(5, 5, 6, 6), "x");
        layout.addLayoutComponent(createComponent(6, 6, 7, 7), "y");
        layout.addLayoutComponent(createComponent(7, 7, 8, 8), "z");
        JPanel p = new JPanel();
        p.setSize(layout.preferredLayoutSize(p));
        layout.layoutContainer(p);
        assertEquals(0, layout.getZone("a").getDisplayRoot().x);
        assertEquals(0, layout.getZone("a").getDisplayRoot().y);
        assertEquals(1, layout.getZone("a").getDisplaySecondRoot().x);
        assertEquals(4, layout.getZone("a").getDisplaySecondRoot().y);
        assertEquals(2, layout.getZone("b").getDisplayRoot().x);
        assertEquals(0, layout.getZone("b").getDisplayRoot().y);
        assertEquals(4, layout.getZone("b").getDisplaySecondRoot().x);
        assertEquals(4, layout.getZone("b").getDisplaySecondRoot().y);
        assertEquals(5, layout.getZone("c").getDisplayRoot().x);
        assertEquals(0, layout.getZone("c").getDisplayRoot().y);
        assertEquals(10, layout.getZone("c").getDisplaySecondRoot().x);
        assertEquals(4, layout.getZone("c").getDisplaySecondRoot().y);
        assertEquals(11, layout.getZone("d").getDisplayRoot().x);
        assertEquals(0, layout.getZone("d").getDisplayRoot().y);
        assertEquals(15, layout.getZone("d").getDisplaySecondRoot().x);
        assertEquals(4, layout.getZone("d").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("x").getDisplayRoot().x);
        assertEquals(5, layout.getZone("x").getDisplayRoot().y);
        assertEquals(15, layout.getZone("x").getDisplaySecondRoot().x);
        assertEquals(10, layout.getZone("x").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("y").getDisplayRoot().x);
        assertEquals(11, layout.getZone("y").getDisplayRoot().y);
        assertEquals(7, layout.getZone("y").getDisplaySecondRoot().x);
        assertEquals(18, layout.getZone("y").getDisplaySecondRoot().y);
        assertEquals(8, layout.getZone("z").getDisplayRoot().x);
        assertEquals(11, layout.getZone("z").getDisplayRoot().y);
        assertEquals(15, layout.getZone("z").getDisplaySecondRoot().x);
        assertEquals(18, layout.getZone("z").getDisplaySecondRoot().y);
    }

    public void testZoneSizesWithExtraSpace() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("a....a"); // aa
        layout.addRow("b..*bc"); // bc
        layout.compile();

        layout.addLayoutComponent(createComponent(10, 10, 10, 10), "a");
        layout.addLayoutComponent(createComponent(10, 10, 10, 10), "b");
        layout.addLayoutComponent(createComponent(10, 10, 10, 10), "c");
        JPanel p = new JPanel();
        Dimension d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() + 10, d.getHeight() + 10);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(10, ((Section) layout.rows.get(0)).getSize());
        assertEquals(20, ((Section) layout.rows.get(1)).getSize());
        assertEquals(20, ((Section) layout.columns.get(0)).getSize());
        assertEquals(10, ((Section) layout.columns.get(1)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(10, ((Section) layout.rows.get(1)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(20, ((Section) layout.columns.get(1)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("a").getDisplayRoot().x);
        assertEquals(0, layout.getZone("a").getDisplayRoot().y);
        assertEquals(29, layout.getZone("a").getDisplaySecondRoot().x);
        assertEquals(9, layout.getZone("a").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("b").getDisplayRoot().x);
        assertEquals(10, layout.getZone("b").getDisplayRoot().y);
        assertEquals(19, layout.getZone("b").getDisplaySecondRoot().x);
        assertEquals(29, layout.getZone("b").getDisplaySecondRoot().y);
        assertEquals(20, layout.getZone("c").getDisplayRoot().x);
        assertEquals(10, layout.getZone("c").getDisplayRoot().y);
        assertEquals(29, layout.getZone("c").getDisplaySecondRoot().x);
        assertEquals(29, layout.getZone("c").getDisplaySecondRoot().y);
    }

    public void testZoneSizesWithLessThanPreferredSpace() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("a....a"); // aa
        layout.addRow("b..*bc"); // bc
        layout.compile();

        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "a");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "b");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "c");
        JPanel p = new JPanel();
        Dimension d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() - 11, d.getHeight() - 11);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(14, ((Section) layout.rows.get(0)).getSize());
        assertEquals(15, ((Section) layout.rows.get(1)).getSize());
        assertEquals(14, ((Section) layout.columns.get(0)).getSize());
        assertEquals(15, ((Section) layout.columns.get(1)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(14, ((Section) layout.rows.get(1)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(14, ((Section) layout.columns.get(1)).getDisplayCoordinate());

        layout = new ZoneLayoutImpl();
        layout.addRow("a....a"); // aa
        layout.addRow("b..*bc"); // bc
        layout.compile();

        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "a");
        layout.addLayoutComponent(createComponent(20, 20, 20, 20), "b");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "c");
        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() - 10, d.getHeight() - 10);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(10, ((Section) layout.rows.get(0)).getSize());
        assertEquals(20, ((Section) layout.rows.get(1)).getSize());
        assertEquals(20, ((Section) layout.columns.get(0)).getSize());
        assertEquals(10, ((Section) layout.columns.get(1)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(10, ((Section) layout.rows.get(1)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(20, ((Section) layout.columns.get(1)).getDisplayCoordinate());

        layout = new ZoneLayoutImpl();
        layout.addRow("a....a"); // aa
        layout.addRow("b..*bc"); // bc
        layout.compile();

        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "a");
        layout.addLayoutComponent(createComponent(20, 20, 20, 20), "b");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "c");
        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() - 20, d.getHeight() - 20);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(10, ((Section) layout.rows.get(0)).getSize());
        assertEquals(20, ((Section) layout.rows.get(1)).getSize());
        assertEquals(20, ((Section) layout.columns.get(0)).getSize());
        assertEquals(10, ((Section) layout.columns.get(1)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(10, ((Section) layout.rows.get(1)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(20, ((Section) layout.columns.get(1)).getDisplayCoordinate());
    }

    public void testZoneSizesWithLessThanPreferredSpaceAndGive() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("a....a"); // aa
        layout.addRow("b..*bc"); // bc
        layout.compile();

        layout.getZone("a").setGive(1, 1);
        layout.getZone("b").setGive(0, 0);
        layout.getZone("c").setGive(1, 0);

        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "a");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "b");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "c");
        JPanel p = new JPanel();
        Dimension d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() - 11, d.getHeight() - 11);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(0, ((Section)layout.columns.get(0)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.columns.get(1)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(0)).getGive(), 0.01);
        assertEquals(0, ((Section)layout.rows.get(1)).getGive(), 0.01);
        assertEquals(10, ((Section) layout.rows.get(0)).getSize());
        assertEquals(20, ((Section) layout.rows.get(1)).getSize());
        assertEquals(20, ((Section) layout.columns.get(0)).getSize());
        assertEquals(10, ((Section) layout.columns.get(1)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(10, ((Section) layout.rows.get(1)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(20, ((Section) layout.columns.get(1)).getDisplayCoordinate());

        layout = new ZoneLayoutImpl();
        layout.addRow("abcd"); // aa
        layout.addRow("e..e"); // bc
        layout.compile();

        layout.getZone("b").setGive(5, 1);
        layout.getZone("e").setGive(10, 1);

        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "a");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "b");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "c");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "d");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "e");
        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() - 16, d.getHeight() - 10);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(1, ((Section)layout.columns.get(0)).getGive(), 0.01);
        assertEquals(5, ((Section)layout.columns.get(1)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.columns.get(2)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.columns.get(3)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(0)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(1)).getGive(), 0.01);
        assertEquals(15, ((Section) layout.rows.get(0)).getSize());
        assertEquals(15, ((Section) layout.rows.get(1)).getSize());
        assertEquals(18, ((Section) layout.columns.get(0)).getSize());
        assertEquals(10, ((Section) layout.columns.get(1)).getSize());
        assertEquals(18, ((Section) layout.columns.get(2)).getSize());
        assertEquals(18, ((Section) layout.columns.get(3)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(15, ((Section) layout.rows.get(1)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(18, ((Section) layout.columns.get(1)).getDisplayCoordinate());
        assertEquals(28, ((Section) layout.columns.get(2)).getDisplayCoordinate());
        assertEquals(46, ((Section) layout.columns.get(3)).getDisplayCoordinate());

        layout = new ZoneLayoutImpl();
        layout.addRow("abcd"); // aa
        layout.addRow("e..e"); // bc
        layout.compile();

        layout.getZone("b").setGive(5, 0);
        layout.getZone("e").setGive(10, 0);

        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "a");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "b");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "c");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "d");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "e");
        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() - 9, d.getHeight() - 5);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(1, ((Section)layout.columns.get(0)).getGive(), 0.01);
        assertEquals(5, ((Section)layout.columns.get(1)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.columns.get(2)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.columns.get(3)).getGive(), 0.01);
        assertEquals(0, ((Section)layout.rows.get(0)).getGive(), 0.01);
        assertEquals(0, ((Section)layout.rows.get(1)).getGive(), 0.01);
        assertEquals(20, ((Section) layout.rows.get(0)).getSize());
        assertEquals(20, ((Section) layout.rows.get(1)).getSize());
        assertEquals(19, ((Section) layout.columns.get(0)).getSize());
        assertEquals(14, ((Section) layout.columns.get(1)).getSize());
        assertEquals(19, ((Section) layout.columns.get(2)).getSize());
        assertEquals(19, ((Section) layout.columns.get(3)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(20, ((Section) layout.rows.get(1)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(19, ((Section) layout.columns.get(1)).getDisplayCoordinate());
        assertEquals(33, ((Section) layout.columns.get(2)).getDisplayCoordinate());
        assertEquals(52, ((Section) layout.columns.get(3)).getDisplayCoordinate());

        layout = new ZoneLayoutImpl();
        layout.addRow("a+~a"); // aa
        layout.addRow("6..."); // 6
        layout.addRow("b+*b"); // bb
        layout.addRow("c..c"); // cc
        layout.compile();

        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "a");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "b");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "c");
        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() - 9, d.getHeight() - 5);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(1, ((Section)layout.columns.get(0)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.columns.get(1)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(0)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(1)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(2)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(3)).getGive(), 0.01);

        layout = new ZoneLayoutImpl();
        layout.addRow("a+~a"); // aa
        layout.addRow("6..."); // 6
        layout.addRow("b+*b"); // bb
        layout.addRow("c..c"); // cc
        layout.compile();

        layout.getZone("a").setGive(new Magnitude(10, 10));
        layout.getZone("b").setGive(new Magnitude(20, 20));
        layout.getZone("c").setGive(new Magnitude(30, 30));

        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "a");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "b");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "c");
        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() - 9, d.getHeight() - 5);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(1, ((Section)layout.columns.get(0)).getGive(), 0.01);
        assertEquals(9, ((Section)layout.columns.get(1)).getGive(), 0.01);
        assertEquals(10, ((Section)layout.rows.get(0)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(1)).getGive(), 0.01);
        assertEquals(20, ((Section)layout.rows.get(2)).getGive(), 0.01);
        assertEquals(30, ((Section)layout.rows.get(3)).getGive(), 0.01);

        layout = new ZoneLayoutImpl();
        layout.addRow("a..a"); // a  a
        layout.addRow(".23."); //  23
        layout.addRow("b+*b"); // b  b
        layout.addRow("c..c"); // c  c
        layout.compile();

        layout.getZone("a").setGive(new Magnitude(10, 10));
        layout.getZone("b").setGive(new Magnitude(20, 20));
        layout.getZone("c").setGive(new Magnitude(30, 30));

        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "a");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "b");
        layout.addLayoutComponent(createComponent(10, 10, 20, 20), "c");
        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() - 9, d.getHeight() - 5);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(4, ((Section)layout.columns.get(0)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.columns.get(1)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.columns.get(2)).getGive(), 0.01);
        assertEquals(4, ((Section)layout.columns.get(3)).getGive(), 0.01);
        assertEquals(10, ((Section)layout.rows.get(0)).getGive(), 0.01);
        assertEquals(1, ((Section)layout.rows.get(1)).getGive(), 0.01);
        assertEquals(20, ((Section)layout.rows.get(2)).getGive(), 0.01);
        assertEquals(30, ((Section)layout.rows.get(3)).getGive(), 0.01);
    }

    public void testTemplateSizes() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("a.....");          // aa
        layout.addRow(".....a");          // bb
        layout.addRow("b....b", "temp");  // cd
        layout.addRow("c..*cd", "temp");  // ee
        layout.addRow("e....e", "temp2"); // fg
        layout.addRow("f..*fg", "temp2"); // hh
        layout.addRow("h....h");          //
        layout.compile();
        layout.createSections();

        assertEquals(2, layout.templateNameMap.size());
        assertEquals(0, ((Template )layout.templateNameMap.get("temp")).insertionPoint.x);
        assertEquals(1, ((Template )layout.templateNameMap.get("temp")).insertionPoint.y);
        assertEquals(0, ((Template )layout.templateNameMap.get("temp")).endPoint.x);
        assertEquals(2, ((Template )layout.templateNameMap.get("temp")).endPoint.y);
        assertEquals(0, ((Template )layout.templateNameMap.get("temp2")).insertionPoint.x);
        assertEquals(1, ((Template )layout.templateNameMap.get("temp2")).insertionPoint.y);
        assertEquals(0, ((Template )layout.templateNameMap.get("temp2")).endPoint.x);
        assertEquals(2, ((Template )layout.templateNameMap.get("temp2")).endPoint.y);
        assertEquals(0, layout.getZone("h").getRoot().x);
        assertEquals(1, layout.getZone("h").getRoot().y);
        assertEquals(1, layout.getZone("h").getSecondRoot().x);
        assertEquals(1, layout.getZone("h").getSecondRoot().y);
        assertEquals(2, layout.rows.size());

    }

    public void testTemplateCoordinates() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("a+*3.................");
        layout.addRow("....b>b2c-~c2d>d2e-~e", "doubleRow");
        layout.addRow(".....6...............", "doubleRow");
        layout.addRow("....f>f2g-~.........g", "singleRow");
        layout.addRow(".....6...............", "singleRow");
        layout.addRow("....h...............h", "buttonBar");
        layout.addRow("..a............!.....");
        layout.compile();
        layout.createSections();

        assertEquals(3, layout.templateNameMap.size());
        assertEquals(0, ((Template )layout.templateNameMap.get("doubleRow")).insertionPoint.x);
        assertEquals(1, ((Template )layout.templateNameMap.get("doubleRow")).insertionPoint.y);
        assertEquals(0, ((Template )layout.templateNameMap.get("doubleRow")).endPoint.x);
        assertEquals(2, ((Template )layout.templateNameMap.get("doubleRow")).endPoint.y);
        assertEquals(0, ((Template )layout.templateNameMap.get("singleRow")).insertionPoint.x);
        assertEquals(1, ((Template )layout.templateNameMap.get("singleRow")).insertionPoint.y);
        assertEquals(0, ((Template )layout.templateNameMap.get("singleRow")).endPoint.x);
        assertEquals(2, ((Template )layout.templateNameMap.get("singleRow")).endPoint.y);
        assertEquals(0, ((Template )layout.templateNameMap.get("buttonBar")).insertionPoint.x);
        assertEquals(1, ((Template )layout.templateNameMap.get("buttonBar")).insertionPoint.y);
        assertEquals(0, ((Template )layout.templateNameMap.get("buttonBar")).endPoint.x);
        assertEquals(1, ((Template )layout.templateNameMap.get("buttonBar")).endPoint.y);
    }

    public void testTemplateZones() {
        ZoneLayout layout = new ZoneLayoutImpl();
        layout.addRow("a...ae");          // ae
        layout.addRow("b...b+", "temp");  // be
        layout.addRow("c..*c.", "temp");  // ce
        layout.addRow("d...de");          // de
        layout.compile();

        assertNotNull(layout.getZone("e"));
    }

    public void testTemplateInsertion() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("a.....");          // aa
        layout.addRow(".....a");          // bb
        layout.addRow("b....b", "temp");  // cd
        layout.addRow("c..*cd", "temp");  // ee
        layout.addRow("e....e", "temp2"); // fg
        layout.addRow("f..*fg", "temp2"); // hh
        layout.addRow("h....h");          //
        layout.compile();

        assertNull(layout.getZone("b"));
        assertNull(layout.getZone("c"));
        assertNull(layout.getZone("d"));
        assertNull(layout.getZone("e"));
        assertNull(layout.getZone("f"));
        assertNull(layout.getZone("g"));

        layout.insertTemplate("temp");

        assertNotNull(layout.getZone("b"));
        assertNotNull(layout.getZone("c"));
        assertNotNull(layout.getZone("d"));
        assertNull(layout.getZone("e"));
        assertNull(layout.getZone("f"));
        assertNull(layout.getZone("g"));
        assertNull(layout.getZone("b-0"));
        assertNull(layout.getZone("c-0"));
        assertNull(layout.getZone("d-0"));

        layout.insertTemplate("temp");

        assertNotNull(layout.getZone("b"));
        assertNotNull(layout.getZone("c"));
        assertNotNull(layout.getZone("d"));
        assertNull(layout.getZone("e"));
        assertNull(layout.getZone("f"));
        assertNull(layout.getZone("g"));
        assertNotNull(layout.getZone("b-0"));
        assertNotNull(layout.getZone("c-0"));
        assertNotNull(layout.getZone("d-0"));

        assertEquals(5, ((Template)layout.templateNameMap.get("temp")).insertionPoint.y);
        assertEquals(5, ((Template)layout.templateNameMap.get("temp2")).insertionPoint.y);
        assertEquals(new Point(0, 0), layout.getZone("a").getRoot());
        assertEquals(new Point(1, 0), layout.getZone("a").getSecondRoot());
        assertEquals(new Point(0, 3), layout.getZone("b").getRoot());
        assertEquals(new Point(1, 3), layout.getZone("b").getSecondRoot());
        assertEquals(new Point(0, 4), layout.getZone("c").getRoot());
        assertEquals(new Point(0, 4), layout.getZone("c").getSecondRoot());
        assertEquals(new Point(1, 4), layout.getZone("d").getRoot());
        assertEquals(new Point(1, 4), layout.getZone("d").getSecondRoot());
        assertEquals(new Point(0, 5), layout.getZone("h").getRoot());
        assertEquals(new Point(1, 5), layout.getZone("h").getSecondRoot());
        assertEquals(new Point(0, 1), layout.getZone("b-0").getRoot());
        assertEquals(new Point(1, 1), layout.getZone("b-0").getSecondRoot());
        assertEquals(new Point(0, 2), layout.getZone("c-0").getRoot());
        assertEquals(new Point(0, 2), layout.getZone("c-0").getSecondRoot());
        assertEquals(new Point(1, 2), layout.getZone("d-0").getRoot());
        assertEquals(new Point(1, 2), layout.getZone("d-0").getSecondRoot());

        for (int i = 0; i < 5; i++) {
            layout.insertTemplate("temp2");
            layout.addLayoutComponent(new JLabel(String.valueOf(i)), "e");
            layout.addLayoutComponent(new JLabel(String.valueOf(i + 10)), "f");
            layout.addLayoutComponent(new JLabel(String.valueOf(i + 20)), "g");
        }

        assertEquals("4", ((JLabel)layout.getZone("e").getComponent()).getText());
        assertEquals("14", ((JLabel)layout.getZone("f").getComponent()).getText());
        assertEquals("24", ((JLabel)layout.getZone("g").getComponent()).getText());

        for (int i = 0; i < 3; i++) {
            assertEquals("" + (i), ((JLabel)layout.getZone("e-" + i).getComponent()).getText());
            assertEquals("" + (i + 10), ((JLabel)layout.getZone("f-" + i).getComponent()).getText());
            assertEquals("" + (i + 20), ((JLabel)layout.getZone("g-" + i).getComponent()).getText());
        }

        assertEquals(15, ((Template)layout.templateNameMap.get("temp")).insertionPoint.y);
        assertEquals(15, ((Template)layout.templateNameMap.get("temp2")).insertionPoint.y);
    }

    public void testTemplateLayout() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("a..+....");          // aa
        layout.addRow(".......a");          // bb
        layout.addRow("b..+...b", "temp");  // cd
        layout.addRow("c.+.cd+d", "temp");  // hh
        layout.addRow("h...+..h");          //
        layout.compile();

        layout.addLayoutComponent(createComponent(10, 10, 10, 10), "a");
        layout.addLayoutComponent(createComponent(10, 10, 10, 10), "h");

        for (int i = 0; i < 2; i++) {
            layout.insertTemplate("temp");
            layout.addLayoutComponent(createComponent(10, 10, 10, 10), "b");
            layout.addLayoutComponent(createComponent(10, 10, 10, 10), "c");
            layout.addLayoutComponent(createComponent(10, 10, 10, 10), "d");
        }

        JPanel p = new JPanel();
        p.setSize(layout.preferredLayoutSize(p));
        layout.layoutContainer(p);

        assertEquals(0, layout.getZone("a").getDisplayRoot().x);
        assertEquals(0, layout.getZone("a").getDisplayRoot().y);
        assertEquals(19, layout.getZone("a").getDisplaySecondRoot().x);
        assertEquals(9, layout.getZone("a").getDisplaySecondRoot().y);
        assertEquals(10, ((Section)layout.rows.get(0)).getSize());
        assertEquals(10, ((Section)layout.rows.get(1)).getSize());
        assertEquals(0, ((Section)layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(10, ((Section)layout.rows.get(1)).getDisplayCoordinate());
        assertEquals(1, layout.getZone("b-0").getHeight());
        assertEquals(1, layout.getZone("b-0").getRoot().y);
        assertEquals(1, layout.getZone("b").getHeight());
        assertEquals(3, layout.getZone("b").getRoot().y);
        assertEquals(1, ((Section)layout.rows.get(1)).getZones().size());

        assertEquals(0, layout.getZone("c").getDisplayRoot().x);
        assertEquals(40, layout.getZone("c").getDisplayRoot().y);
        assertEquals(9, layout.getZone("c").getDisplaySecondRoot().x);
        assertEquals(49, layout.getZone("c").getDisplaySecondRoot().y);
        assertEquals(10, layout.getZone("d").getDisplayRoot().x);
        assertEquals(40, layout.getZone("d").getDisplayRoot().y);
        assertEquals(19, layout.getZone("d").getDisplaySecondRoot().x);
        assertEquals(49, layout.getZone("d").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("b").getDisplayRoot().x);
        assertEquals(30, layout.getZone("b").getDisplayRoot().y);
        assertEquals(19, layout.getZone("b").getDisplaySecondRoot().x);
        assertEquals(39, layout.getZone("b").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("c-0").getDisplayRoot().x);
        assertEquals(20, layout.getZone("c-0").getDisplayRoot().y);
        assertEquals(9, layout.getZone("c-0").getDisplaySecondRoot().x);
        assertEquals(29, layout.getZone("c-0").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("b-0").getDisplayRoot().x);
        assertEquals(10, layout.getZone("b-0").getDisplayRoot().y);
        assertEquals(19, layout.getZone("b-0").getDisplaySecondRoot().x);
        assertEquals(19, layout.getZone("b-0").getDisplaySecondRoot().y);
        assertEquals(10, layout.getZone("d-0").getDisplayRoot().x);
        assertEquals(20, layout.getZone("d-0").getDisplayRoot().y);
        assertEquals(19, layout.getZone("d-0").getDisplaySecondRoot().x);
        assertEquals(29, layout.getZone("d-0").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("c").getDisplayRoot().x);
        assertEquals(40, layout.getZone("c").getDisplayRoot().y);
        assertEquals(9, layout.getZone("c").getDisplaySecondRoot().x);
        assertEquals(49, layout.getZone("c").getDisplaySecondRoot().y);
        assertEquals(10, layout.getZone("d").getDisplayRoot().x);
        assertEquals(40, layout.getZone("d").getDisplayRoot().y);
        assertEquals(19, layout.getZone("d").getDisplaySecondRoot().x);
        assertEquals(49, layout.getZone("d").getDisplaySecondRoot().y);

    }

    public void testPresets() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("a.a1b.b"); // a1b
        layout.addRow("...7..."); //  7
        layout.addRow("c.....c"); // ccc
        layout.compile();

        layout.addLayoutComponent(createComponent(10, 10, 10, 10), "a");
        layout.addLayoutComponent(createComponent(10, 10, 10, 10), "b");
        layout.addLayoutComponent(createComponent(10, 10, 10, 10), "c");
        JPanel p = new JPanel();
        Dimension d = layout.preferredLayoutSize(p);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(10, ((Section) layout.rows.get(0)).getSize());
        assertEquals(12, ((Section) layout.rows.get(1)).getSize());
        assertEquals(10, ((Section) layout.rows.get(2)).getSize());
        assertEquals(10, ((Section) layout.columns.get(0)).getSize());
        assertEquals(3, ((Section) layout.columns.get(1)).getSize());
        assertEquals(10, ((Section) layout.columns.get(2)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(10, ((Section) layout.rows.get(1)).getDisplayCoordinate());
        assertEquals(22, ((Section) layout.rows.get(2)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(10, ((Section) layout.columns.get(1)).getDisplayCoordinate());
        assertEquals(13, ((Section) layout.columns.get(2)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("a").getDisplayRoot().x);
        assertEquals(0, layout.getZone("a").getDisplayRoot().y);
        assertEquals(9, layout.getZone("a").getDisplaySecondRoot().x);
        assertEquals(9, layout.getZone("a").getDisplaySecondRoot().y);
        assertEquals(10, layout.getZone("ComponentPreset-1-3").getDisplayRoot().x);
        assertEquals(0, layout.getZone("ComponentPreset-1-3").getDisplayRoot().y);
        assertEquals(12, layout.getZone("ComponentPreset-1-3").getDisplaySecondRoot().x);
        assertEquals(9, layout.getZone("ComponentPreset-1-3").getDisplaySecondRoot().y);
        assertEquals(13, layout.getZone("b").getDisplayRoot().x);
        assertEquals(0, layout.getZone("b").getDisplayRoot().y);
        assertEquals(22, layout.getZone("b").getDisplaySecondRoot().x);
        assertEquals(9, layout.getZone("b").getDisplaySecondRoot().y);
        assertEquals(10, layout.getZone("ComponentPreset-7-4").getDisplayRoot().x);
        assertEquals(10, layout.getZone("ComponentPreset-7-4").getDisplayRoot().y);
        assertEquals(12, layout.getZone("ComponentPreset-7-4").getDisplaySecondRoot().x);
        assertEquals(21, layout.getZone("ComponentPreset-7-4").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("c").getDisplayRoot().x);
        assertEquals(22, layout.getZone("c").getDisplayRoot().y);
        assertEquals(22, layout.getZone("c").getDisplaySecondRoot().x);
        assertEquals(31, layout.getZone("c").getDisplaySecondRoot().y);
    }

    public void testSpecialPresets() {
        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("~");
        layout.compile();

        JPanel p = new JPanel();
        Dimension d = layout.preferredLayoutSize(p);
        assertEquals(0, d.width);
        assertEquals(0, d.height);
        d.setSize(100, 100);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(0, ((Section) layout.rows.get(0)).getSize());
        assertEquals(100, ((Section) layout.columns.get(0)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("ComponentPreset-~-0").getDisplayRoot().x);
        assertEquals(0, layout.getZone("ComponentPreset-~-0").getDisplayRoot().y);
        assertEquals(99, layout.getZone("ComponentPreset-~-0").getDisplaySecondRoot().x);
        assertEquals(-1, layout.getZone("ComponentPreset-~-0").getDisplaySecondRoot().y);

        layout = new ZoneLayoutImpl();
        layout.addRow("!");
        layout.compile();

        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        assertEquals(0, d.width);
        assertEquals(0, d.height);
        d.setSize(100, 100);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(100, ((Section) layout.rows.get(0)).getSize());
        assertEquals(0, ((Section) layout.columns.get(0)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("ComponentPreset-!-0").getDisplayRoot().x);
        assertEquals(0, layout.getZone("ComponentPreset-!-0").getDisplayRoot().y);
        assertEquals(-1, layout.getZone("ComponentPreset-!-0").getDisplaySecondRoot().x);
        assertEquals(99, layout.getZone("ComponentPreset-!-0").getDisplaySecondRoot().y);

        layout = new ZoneLayoutImpl();
        layout.addRow("*");
        layout.compile();

        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        assertEquals(0, d.width);
        assertEquals(0, d.height);
        d.setSize(100, 100);
        p.setSize(d);
        layout.layoutContainer(p);
        assertEquals(100, ((Section) layout.rows.get(0)).getSize());
        assertEquals(100, ((Section) layout.columns.get(0)).getSize());
        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("ComponentPreset-*-0").getDisplayRoot().x);
        assertEquals(0, layout.getZone("ComponentPreset-*-0").getDisplayRoot().y);
        assertEquals(99, layout.getZone("ComponentPreset-*-0").getDisplaySecondRoot().x);
        assertEquals(99, layout.getZone("ComponentPreset-*-0").getDisplaySecondRoot().y);
    }

    public void testSpanningAcrossTemplates() {
        ZoneLayout layout = new ZoneLayoutImpl();
        layout.addRow("a......e");          // ae
        layout.addRow("......a.");          // b
        layout.addRow("b.....b.", "temp");  // c
        layout.addRow("c.....c.", "temp");  // de
        layout.addRow("d.....de");          //
        layout.compile();

        layout.insertTemplate("temp");
        layout.insertTemplate("temp");
        assertEquals(new Point(1,0), layout.getZone("e").getRoot());
        assertEquals(new Point(1,5), layout.getZone("e").getSecondRoot());
    }

    public void testMultipleTemplates() {
        JTextField firstNameTxt = new JTextField(10);
        JTextField lastNameTxt = new JTextField(10);
        JTextField phoneTxt = new JTextField(10);
        JTextField emailTxt = new JTextField(10);
        JTextField addr1Txt = new JTextField(10);
        JTextField addr2Txt = new JTextField(10);
        JTextField cityTxt = new JTextField(10);
        JTextField stateTxt = new JTextField(10);
        JTextField postalTxt = new JTextField(10);
        JTextField countryTxt = new JTextField();
        JList nameList = new JList(new Object[] {
                "Bunny, Bugs",
                "Cat, Sylvester",
                "Coyote, Wile E.",
                "Devil, Tasmanian",
                "Duck, Daffy",
                "Fudd, Elmer",
                "Le Pew, Pepe",
                "Martian, Marvin"
        });


        ZoneLayoutImpl layout = new ZoneLayoutImpl();
        layout.addRow("a..3...................");
        layout.addRow("....b>b2c-~c2d>..d2e-~e", "doubleRow");
        layout.addRow("....6..................", "doubleRow");
        layout.addRow("....i>i2j-~j2k<k2l-~..l", "emailRow");
        layout.addRow("....6..................", "emailRow");
        layout.addRow("+*..f>f2g-~...........g", "singleRow");
        layout.addRow("....6..................", "singleRow");
        layout.addRow("....h.................h", "buttonBar");
        layout.addRow("..a.!..................");

        ZoneLayout btnBarLayout = ZoneLayoutFactory.newZoneLayout();
        btnBarLayout.addRow("a2b2c2d2e");
        JPanel buttonPanel = new JPanel(btnBarLayout);
        buttonPanel.add(new JButton("New"), "a");
        buttonPanel.add(new JButton("Delete"), "b");
        buttonPanel.add(new JButton("Edit"), "c");
        buttonPanel.add(new JButton("Save"), "d");
        buttonPanel.add(new JButton("Cancel"), "e");

        JPanel basePanel = new JPanel((layout));
        basePanel.add(nameList, "a");
        layout.insertTemplate("doubleRow");
        basePanel.add(new JLabel("Last Name"), "b");
        basePanel.add(lastNameTxt, "c");
        basePanel.add(new JLabel("First Name"), "d");
        basePanel.add(firstNameTxt, "e");
        layout.insertTemplate("emailRow");
        basePanel.add(new JLabel("Phone"), "i");
        basePanel.add(phoneTxt, "j");
        basePanel.add(new JLabel("Email"), "k");
        basePanel.add(emailTxt, "l");
        layout.insertTemplate("singleRow");
        basePanel.add(new JLabel("Address 1"), "f");
        basePanel.add(addr1Txt, "g");
        layout.insertTemplate("singleRow");
        basePanel.add(new JLabel("Address 2"), "f");
        basePanel.add(addr2Txt, "g");
        layout.insertTemplate("doubleRow");
        basePanel.add(new JLabel("City"), "b");
        basePanel.add(cityTxt, "c");
        layout.insertTemplate("doubleRow");
        basePanel.add(new JLabel("State"), "b");
        basePanel.add(stateTxt, "c");
        basePanel.add(new JLabel("Postal Code"), "d");
        basePanel.add(postalTxt, "e");
        layout.insertTemplate("doubleRow");
        basePanel.add(new JLabel("Country"), "b");
        basePanel.add(countryTxt, "c");
        layout.insertTemplate("buttonBar");
        basePanel.add(buttonPanel, "h");

        basePanel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        layout.layoutContainer(basePanel);

        assertEquals(17, layout.rows.size());
        assertEquals(12, layout.columns.size());

//        assertEquals(100, ((Column)layout.columns.get(9)).getTake());
//        assertEquals(0, ((Column)layout.columns.get(8)).getTake());
//        assertEquals(6, ((Column)layout.columns.get(8)).getPreferredSize());
//        assertEquals(6, ((Column)layout.columns.get(8)).getSize());
//        assertEquals(6, ((Column)layout.columns.get(10)).getSize());
    }

    public void testComponentSizes() {
        ZoneLayout layout = new ZoneLayoutImpl();
        JPanel p = new JPanel(layout);
        p.setSize(80, 80);
        layout.addRow("a..ab..b");
        JComponent a = createComponent(50, 50, 30, 40);
        JComponent b = createComponent(50, 50, 40, 30);
        p.add(a, "a");
        p.add(b, "b");
        layout.layoutContainer(p);

        assertEquals(a.getBounds().width, 30);
        assertEquals(a.getBounds().height, 40);
        assertEquals(b.getBounds().width, 40);
        assertEquals(b.getBounds().height, 30);
    }

    public void testEmptyContainer() throws Exception {

        ZoneLayout layout = new ZoneLayoutImpl();
        layout.addRow("l>ls-~s");
        layout.addRow("..5....");
        layout.addRow("c+*...c");
        JPanel p = new JPanel(layout);
        layout.layoutContainer(p);

    }

    public void testEmptyFrame() throws Exception {

        ZoneLayout layout = new ZoneLayoutImpl();
        layout.addRow("l>ls-~s");
        layout.addRow("..5....");
        layout.addRow("c+*...c");
        JFrame f = new JFrame("Empty Frame");

        JPanel p = new JPanel(layout);
        f.add(p);
        f.pack();

    }

    public void testSpannedTakeLayout() throws Exception {
        ZoneLayoutImpl layout =  new ZoneLayoutImpl();
        layout.addRow("lt-~t");
        layout.addRow(".5...");
        layout.addRow("c+*.c");

        JPanel panel = new JPanel(layout);

        panel.add(new JLabel("Label: "), "l");
        panel.add(new JTextField(), "t");
        panel.add(new JTree(), "c");

        layout.layoutContainer(panel);
        assertEquals(3, layout.columns.size());
        assertEquals(3, layout.rows.size());
        assertEquals(0, ((Column)layout.columns.get(0)).getTake(), 0.01);
        assertEquals(0, ((Column)layout.columns.get(1)).getTake(), 0.01);
        assertEquals(1, ((Column)layout.columns.get(2)).getTake(), 0.01);
    }

    public void testSecondRootPlacement() throws Exception {
        ZoneLayoutImpl layout =  new ZoneLayoutImpl();
        layout.addRow("...a");
        layout.addRow("a...");

        try {
            layout.compile();
            fail("Compile did not throw an exception for an illegal layout.");
        } catch (Exception e) {
        }
    }

    public void testZoneIntersection() throws Exception {
        ZoneLayoutImpl layout =  new ZoneLayoutImpl();
        layout.addRow("abab");

        try {
            layout.compile();
            fail("Compile did not throw an exception for zone intersection.");
        } catch (Exception e) {
        }
    }

    protected static JComponent createComponent(int minWidth, int minHeight, int width, int height) {
        JComponent comp = new JComponent() {

        };
        comp.setMinimumSize(new Dimension(minWidth, minHeight));
        comp.setPreferredSize(new Dimension(width, height));
        comp.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        return comp;
    }
}
