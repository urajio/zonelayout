package com.atticlabs.zonelayout.swing;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;

import javax.swing.*;
import java.awt.*;

public class ZoneLayoutAPITest extends TestCase {
    public ZoneLayoutAPITest(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(ZoneLayoutAPITest.class);
    }

    public void testAlignment() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();

        layout.addRow("ab<bc<_cd_de_>ef>fg^>gh^hi<^i");
        layout.compile();

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
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("a*a"); // aa
        layout.compile();

        JComponent a = createComponent(10, 10, 10, 10);

        JPanel p = new JPanel(layout);
        p.add(a, "a");
        Dimension d = layout.preferredLayoutSize(p);
        d.setSize(d.getWidth() + 10, d.getHeight() + 10);
        p.setSize(d);
        layout.layoutContainer(p);
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
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("aab-bc|cd+d");
        layout.compile();

        assertEquals(layout.getZone("a").getHorizontalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("a").getVerticalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("b").getHorizontalAlignment(), Zone.ALIGN_FILL);
        assertEquals(layout.getZone("b").getVerticalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("c").getHorizontalAlignment(), Zone.ALIGN_MIDDLE);
        assertEquals(layout.getZone("c").getVerticalAlignment(), Zone.ALIGN_FILL);
        assertEquals(layout.getZone("d").getVerticalAlignment(), Zone.ALIGN_FILL);
        assertEquals(layout.getZone("d").getHorizontalAlignment(), Zone.ALIGN_FILL);
    }

    public void testCoordinates() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("ab-bc|cd+d"); // abccd
        layout.addRow("x........."); // x   x
        layout.addRow(".........x"); // y yzz
        layout.addRow("y....yz..z");
        layout.compile();

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
    }

    public void testZoneSizes() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
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
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
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
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
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
//        assertEquals(14, ((Section) layout.rows.get(0)).getSize());
//        assertEquals(15, ((Section) layout.rows.get(1)).getSize());
//        assertEquals(14, ((Section) layout.columns.get(0)).getSize());
//        assertEquals(15, ((Section) layout.columns.get(1)).getSize());
//        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
//        assertEquals(14, ((Section) layout.rows.get(1)).getDisplayCoordinate());
//        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
//        assertEquals(14, ((Section) layout.columns.get(1)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("a").getDisplayRoot().x);
        assertEquals(0, layout.getZone("a").getDisplayRoot().y);
        assertEquals(28, layout.getZone("a").getDisplaySecondRoot().x);
        assertEquals(13, layout.getZone("a").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("b").getDisplayRoot().x);
        assertEquals(14, layout.getZone("b").getDisplayRoot().y);
        assertEquals(13, layout.getZone("b").getDisplaySecondRoot().x);
        assertEquals(28, layout.getZone("b").getDisplaySecondRoot().y);
        assertEquals(14, layout.getZone("c").getDisplayRoot().x);
        assertEquals(14, layout.getZone("c").getDisplayRoot().y);
        assertEquals(28, layout.getZone("c").getDisplaySecondRoot().x);
        assertEquals(28, layout.getZone("c").getDisplaySecondRoot().y);

        layout = ZoneLayoutFactory.newZoneLayout();
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
//        assertEquals(10, ((Section) layout.rows.get(0)).getSize());
//        assertEquals(20, ((Section) layout.rows.get(1)).getSize());
//        assertEquals(20, ((Section) layout.columns.get(0)).getSize());
//        assertEquals(10, ((Section) layout.columns.get(1)).getSize());
//        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
//        assertEquals(10, ((Section) layout.rows.get(1)).getDisplayCoordinate());
//        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
//        assertEquals(20, ((Section) layout.columns.get(1)).getDisplayCoordinate());
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

        layout = ZoneLayoutFactory.newZoneLayout();;
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
//        assertEquals(10, ((Section) layout.rows.get(0)).getSize());
//        assertEquals(20, ((Section) layout.rows.get(1)).getSize());
//        assertEquals(20, ((Section) layout.columns.get(0)).getSize());
//        assertEquals(10, ((Section) layout.columns.get(1)).getSize());
//        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
//        assertEquals(10, ((Section) layout.rows.get(1)).getDisplayCoordinate());
//        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
//        assertEquals(20, ((Section) layout.columns.get(1)).getDisplayCoordinate());
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

    public void testZoneSizesWithLessThanPreferredSpaceAndGive() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
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
//        assertEquals(0, ((Section)layout.columns.get(0)).getGive());
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 1, ((Section)layout.columns.get(1)).getGive());
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 1, ((Section)layout.rows.get(0)).getGive());
//        assertEquals(0, ((Section)layout.rows.get(1)).getGive());
//        assertEquals(10, ((Section) layout.rows.get(0)).getSize());
//        assertEquals(20, ((Section) layout.rows.get(1)).getSize());
//        assertEquals(20, ((Section) layout.columns.get(0)).getSize());
//        assertEquals(10, ((Section) layout.columns.get(1)).getSize());
//        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
//        assertEquals(10, ((Section) layout.rows.get(1)).getDisplayCoordinate());
//        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
//        assertEquals(20, ((Section) layout.columns.get(1)).getDisplayCoordinate());
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

        layout = ZoneLayoutFactory.newZoneLayout();;
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
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 1, ((Section)layout.columns.get(0)).getGive());
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 5, ((Section)layout.columns.get(1)).getGive());
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 1, ((Section)layout.columns.get(2)).getGive());
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 1, ((Section)layout.columns.get(3)).getGive());
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 1, ((Section)layout.rows.get(0)).getGive());
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 1, ((Section)layout.rows.get(1)).getGive());
//        assertEquals(15, ((Section) layout.rows.get(0)).getSize());
//        assertEquals(15, ((Section) layout.rows.get(1)).getSize());
//        assertEquals(18, ((Section) layout.columns.get(0)).getSize());
//        assertEquals(10, ((Section) layout.columns.get(1)).getSize());
//        assertEquals(18, ((Section) layout.columns.get(2)).getSize());
//        assertEquals(18, ((Section) layout.columns.get(3)).getSize());
//        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
//        assertEquals(15, ((Section) layout.rows.get(1)).getDisplayCoordinate());
//        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
//        assertEquals(18, ((Section) layout.columns.get(1)).getDisplayCoordinate());
//        assertEquals(28, ((Section) layout.columns.get(2)).getDisplayCoordinate());
//        assertEquals(46, ((Section) layout.columns.get(3)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("a").getDisplayRoot().x);
        assertEquals(0, layout.getZone("a").getDisplayRoot().y);
        assertEquals(17, layout.getZone("a").getDisplaySecondRoot().x);
        assertEquals(14, layout.getZone("a").getDisplaySecondRoot().y);
        assertEquals(18, layout.getZone("b").getDisplayRoot().x);
        assertEquals(0, layout.getZone("b").getDisplayRoot().y);
        assertEquals(27, layout.getZone("b").getDisplaySecondRoot().x);
        assertEquals(14, layout.getZone("b").getDisplaySecondRoot().y);
        assertEquals(28, layout.getZone("c").getDisplayRoot().x);
        assertEquals(0, layout.getZone("c").getDisplayRoot().y);
        assertEquals(45, layout.getZone("c").getDisplaySecondRoot().x);
        assertEquals(14, layout.getZone("c").getDisplaySecondRoot().y);
        assertEquals(46, layout.getZone("d").getDisplayRoot().x);
        assertEquals(0, layout.getZone("d").getDisplayRoot().y);
        assertEquals(63, layout.getZone("d").getDisplaySecondRoot().x);
        assertEquals(14, layout.getZone("d").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("e").getDisplayRoot().x);
        assertEquals(15, layout.getZone("e").getDisplayRoot().y);
        assertEquals(63, layout.getZone("e").getDisplaySecondRoot().x);
        assertEquals(29, layout.getZone("e").getDisplaySecondRoot().y);

        layout = ZoneLayoutFactory.newZoneLayout();;
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
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 1, ((Section)layout.columns.get(0)).getGive());
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 5, ((Section)layout.columns.get(1)).getGive());
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 1, ((Section)layout.columns.get(2)).getGive());
//        assertEquals(ZoneLayout.WEIGHT_INCREASE_FACTOR * 1, ((Section)layout.columns.get(3)).getGive());
//        assertEquals(0, ((Section)layout.rows.get(0)).getGive());
//        assertEquals(0, ((Section)layout.rows.get(1)).getGive());
//        assertEquals(20, ((Section) layout.rows.get(0)).getSize());
//        assertEquals(20, ((Section) layout.rows.get(1)).getSize());
//        assertEquals(19, ((Section) layout.columns.get(0)).getSize());
//        assertEquals(14, ((Section) layout.columns.get(1)).getSize());
//        assertEquals(19, ((Section) layout.columns.get(2)).getSize());
//        assertEquals(19, ((Section) layout.columns.get(3)).getSize());
//        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
//        assertEquals(20, ((Section) layout.rows.get(1)).getDisplayCoordinate());
//        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
//        assertEquals(19, ((Section) layout.columns.get(1)).getDisplayCoordinate());
//        assertEquals(33, ((Section) layout.columns.get(2)).getDisplayCoordinate());
//        assertEquals(52, ((Section) layout.columns.get(3)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("a").getDisplayRoot().x);
        assertEquals(0, layout.getZone("a").getDisplayRoot().y);
        assertEquals(18, layout.getZone("a").getDisplaySecondRoot().x);
        assertEquals(19, layout.getZone("a").getDisplaySecondRoot().y);
        assertEquals(19, layout.getZone("b").getDisplayRoot().x);
        assertEquals(0, layout.getZone("b").getDisplayRoot().y);
        assertEquals(32, layout.getZone("b").getDisplaySecondRoot().x);
        assertEquals(19, layout.getZone("b").getDisplaySecondRoot().y);
        assertEquals(33, layout.getZone("c").getDisplayRoot().x);
        assertEquals(0, layout.getZone("c").getDisplayRoot().y);
        assertEquals(51, layout.getZone("c").getDisplaySecondRoot().x);
        assertEquals(19, layout.getZone("c").getDisplaySecondRoot().y);
        assertEquals(52, layout.getZone("d").getDisplayRoot().x);
        assertEquals(0, layout.getZone("d").getDisplayRoot().y);
        assertEquals(70, layout.getZone("d").getDisplaySecondRoot().x);
        assertEquals(19, layout.getZone("d").getDisplaySecondRoot().y);
        assertEquals(0, layout.getZone("e").getDisplayRoot().x);
        assertEquals(20, layout.getZone("e").getDisplayRoot().y);
        assertEquals(70, layout.getZone("e").getDisplaySecondRoot().x);
        assertEquals(39, layout.getZone("e").getDisplaySecondRoot().y);
    }

    public void testTemplateSizes() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("a.....");          // aa
        layout.addRow(".....a");          // bb
        layout.addRow("b....b", "temp");  // cd
        layout.addRow("c..*cd", "temp");  // ee
        layout.addRow("e....e", "temp2"); // fg
        layout.addRow("f..*fg", "temp2"); // hh
        layout.addRow("h....h");          //
        layout.compile();
//        layout.createSections();

//        assertEquals(2, layout.templateNameMap.size());
//        assertEquals(0, ((Template )layout.templateNameMap.get("temp")).insertionPoint.x);
//        assertEquals(1, ((Template )layout.templateNameMap.get("temp")).insertionPoint.y);
//        assertEquals(0, ((Template )layout.templateNameMap.get("temp")).endPoint.x);
//        assertEquals(2, ((Template )layout.templateNameMap.get("temp")).endPoint.y);
//        assertEquals(0, ((Template )layout.templateNameMap.get("temp2")).insertionPoint.x);
//        assertEquals(1, ((Template )layout.templateNameMap.get("temp2")).insertionPoint.y);
//        assertEquals(0, ((Template )layout.templateNameMap.get("temp2")).endPoint.x);
//        assertEquals(2, ((Template )layout.templateNameMap.get("temp2")).endPoint.y);
        assertEquals(0, layout.getZone("h").getRoot().x);
        assertEquals(1, layout.getZone("h").getRoot().y);
        assertEquals(1, layout.getZone("h").getSecondRoot().x);
        assertEquals(1, layout.getZone("h").getSecondRoot().y);
//        assertEquals(2, layout.rows.size());

    }

    public void testTemplateZones() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("a...ae");          // ae
        layout.addRow("b...b+", "temp");  // be
        layout.addRow("c..*c.", "temp");  // ce
        layout.addRow("d...de");          // de
        layout.compile();

        assertNotNull(layout.getZone("e"));
    }

    public void testTemplateInsertion() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
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

//        assertEquals(5, ((Template)layout.templateNameMap.get("temp")).insertionPoint.y);
//        assertEquals(5, ((Template)layout.templateNameMap.get("temp2")).insertionPoint.y);
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

//        assertEquals(15, ((Template)layout.templateNameMap.get("temp")).insertionPoint.y);
//        assertEquals(15, ((Template)layout.templateNameMap.get("temp2")).insertionPoint.y);
    }

    public void testTemplateLayout() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
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
//        assertEquals(10, ((Section)layout.rows.get(0)).getSize());
//        assertEquals(10, ((Section)layout.rows.get(1)).getSize());
//        assertEquals(0, ((Section)layout.rows.get(0)).getDisplayCoordinate());
//        assertEquals(10, ((Section)layout.rows.get(1)).getDisplayCoordinate());
        assertEquals(1, layout.getZone("b-0").getHeight());
        assertEquals(1, layout.getZone("b-0").getRoot().y);
        assertEquals(1, layout.getZone("b").getHeight());
        assertEquals(3, layout.getZone("b").getRoot().y);
//        assertEquals(1, ((Section)layout.rows.get(1)).getZones().size());

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
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
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
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
        layout.addRow("~");
        layout.compile();

        JPanel p = new JPanel();
        Dimension d = layout.preferredLayoutSize(p);
        assertEquals(0, d.width);
        assertEquals(0, d.height);
        d.setSize(100, 100);
        p.setSize(d);
        layout.layoutContainer(p);
//        assertEquals(0, ((Section) layout.rows.get(0)).getSize());
//        assertEquals(100, ((Section) layout.columns.get(0)).getSize());
//        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
//        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("ComponentPreset-~-0").getDisplayRoot().x);
        assertEquals(0, layout.getZone("ComponentPreset-~-0").getDisplayRoot().y);
        assertEquals(99, layout.getZone("ComponentPreset-~-0").getDisplaySecondRoot().x);
        assertEquals(-1, layout.getZone("ComponentPreset-~-0").getDisplaySecondRoot().y);

        layout = ZoneLayoutFactory.newZoneLayout();;
        layout.addRow("!");
        layout.compile();

        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        assertEquals(0, d.width);
        assertEquals(0, d.height);
        d.setSize(100, 100);
        p.setSize(d);
        layout.layoutContainer(p);
//        assertEquals(100, ((Section) layout.rows.get(0)).getSize());
//        assertEquals(0, ((Section) layout.columns.get(0)).getSize());
//        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
//        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("ComponentPreset-!-0").getDisplayRoot().x);
        assertEquals(0, layout.getZone("ComponentPreset-!-0").getDisplayRoot().y);
        assertEquals(-1, layout.getZone("ComponentPreset-!-0").getDisplaySecondRoot().x);
        assertEquals(99, layout.getZone("ComponentPreset-!-0").getDisplaySecondRoot().y);

        layout = ZoneLayoutFactory.newZoneLayout();;
        layout.addRow("*");
        layout.compile();

        p = new JPanel();
        d = layout.preferredLayoutSize(p);
        assertEquals(0, d.width);
        assertEquals(0, d.height);
        d.setSize(100, 100);
        p.setSize(d);
        layout.layoutContainer(p);
//        assertEquals(100, ((Section) layout.rows.get(0)).getSize());
//        assertEquals(100, ((Section) layout.columns.get(0)).getSize());
//        assertEquals(0, ((Section) layout.rows.get(0)).getDisplayCoordinate());
//        assertEquals(0, ((Section) layout.columns.get(0)).getDisplayCoordinate());
        assertEquals(0, layout.getZone("ComponentPreset-*-0").getDisplayRoot().x);
        assertEquals(0, layout.getZone("ComponentPreset-*-0").getDisplayRoot().y);
        assertEquals(99, layout.getZone("ComponentPreset-*-0").getDisplaySecondRoot().x);
        assertEquals(99, layout.getZone("ComponentPreset-*-0").getDisplaySecondRoot().y);
    }

    public void testSpanningAcrossTemplates() {
        ZoneLayout layout = ZoneLayoutFactory.newZoneLayout();
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

    protected static JComponent createComponent(int minWidth, int minHeight, int width, int height) {
        JComponent comp = new JComponent() {

        };
        comp.setMinimumSize(new Dimension(minWidth, minHeight));
        comp.setPreferredSize(new Dimension(width, height));
        return comp;
    }
}

