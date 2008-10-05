package com.atticlabs.zonelayout.swing;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Insets;
import java.awt.Polygon;

public final class Zone {
    private static final Magnitude DEFAULT_GIVE = new Magnitude(1, 1);
    private static final Magnitude DEFAULT_TAKE = new Magnitude(0, 0);

    public static final Magnitude MAX_GIVE_OR_TAKE = new Magnitude(100, 100);

    public static final Insets DEFAULT_INSETS = new Insets(0,0,0,0);

    public static final int ALIGN_MIDDLE = 0;
    public static final int ALIGN_BOTTOM = 1;
    public static final int ALIGN_TOP = 2;
    public static final int ALIGN_LEFT = 3;
    public static final int ALIGN_RIGHT = 4;
    public static final int ALIGN_FILL = 5;

    Point root;
    Point secondRoot;
    int horizontalAlignment;
    int verticalAlignment;
    Insets insets = DEFAULT_INSETS;
    String id;
    Magnitude give = DEFAULT_GIVE;
    Magnitude take = DEFAULT_TAKE;
    Component component;
    ComponentSizeCache componentSizeCache = new ComponentSizeCache();
    Point displayRoot = new Point();
    Point displaySecondRoot = new Point();
    Preset preset;

    Zone(String id) {
        this.id = id;
    }

    Zone(Zone zone) {
        id = zone.id;
        root = zone.root == null ? null : new Point(zone.root);
        secondRoot = zone.secondRoot == null ? null : new Point(zone.secondRoot);
        horizontalAlignment = zone.horizontalAlignment;
        verticalAlignment = zone.verticalAlignment;
        insets = (Insets) zone.insets.clone();
        give = new Magnitude(zone.give);
        take = new Magnitude(zone.take);
        displayRoot = new Point(zone.displayRoot);
        displaySecondRoot = new Point(zone.displaySecondRoot);

        if (zone.preset != null) {
            zone.preset.setUpZone(this);
        }
    }

    /**
     * The max 'take' in either direction is 100.
     * @param horizontal
     * @param vertical
     */
    public void setTake(int horizontal, int vertical) {
        if (horizontal > MAX_GIVE_OR_TAKE.horizontal || vertical > MAX_GIVE_OR_TAKE.vertical) {
            throw new RuntimeException("Maximum 'take' in either direction is 100.");
        }
        take = new Magnitude(horizontal,  vertical);
    }

    /**
     * The max 'take' in either direction is 100.
     * @param take
     */
    public void setTake(Magnitude take) {
        if (take.horizontal > MAX_GIVE_OR_TAKE.horizontal || take.vertical > MAX_GIVE_OR_TAKE.vertical) {
            throw new RuntimeException("Maximum 'take' in either direction is 100.");
        }
        this.take = take;
    }

    public Magnitude getTake() {
        return take;
    }

    /**
     * The max 'give' in either direction is 100.
     * @param horizontal
     * @param vertical
     */
    public void setGive(int horizontal, int vertical) {
        if (horizontal > MAX_GIVE_OR_TAKE.horizontal || vertical > MAX_GIVE_OR_TAKE.vertical) {
            throw new RuntimeException("Maximum 'give' in either direction is 100.");
        }
        give = new Magnitude(horizontal, vertical);
    }

    /**
     * The max 'give' in either direction is 100.
     * @param give
     */
    public void setGive(Magnitude give) {
        if (give.horizontal > MAX_GIVE_OR_TAKE.horizontal || give.vertical > MAX_GIVE_OR_TAKE.vertical) {
            throw new RuntimeException("Maximum 'give' in either direction is 100.");
        }
        this.give = give;
    }

    boolean isGiveUserSet() {
        return give != DEFAULT_GIVE;
    }

    public Magnitude getGive() {
        return give;
    }

    public Component getComponent() {
        return component;
    }

    void setComponent(Component component) {
        this.component = component;
        componentSizeCache.setComponent(component);
    }

    ComponentSizeCache getComponentSizeCache() {
        return componentSizeCache;
    }

    void clearCache() {
        componentSizeCache.clearCache();
    }

    boolean hasVisibleComponent() {
        return component != null && component.isVisible();
    }

    Dimension getSize() {
        return new Dimension(getWidth(), getHeight());
    }

    public Point getDisplayRoot() {
        return displayRoot;
    }

    void setDisplayRoot(Point displayRoot) {
        this.displayRoot = displayRoot;
    }

    boolean intersectsAxisLine(AxisView view, Point p) {
        return (view.getValue(root) <= view.getValue(p) && view.getValue(secondRoot) >= view.getValue(p));
    }

    public Point getDisplaySecondRoot() {
        return displaySecondRoot;
    }

    void setDisplaySecondRoot(Point displaySecondRoot) {
        this.displaySecondRoot = displaySecondRoot;
    }

    public int getDisplayWidth() {
        return getDisplaySecondRoot().x - getDisplayRoot().x + 1;
    }

    public int getDisplayHeight() {
        return getDisplaySecondRoot().y - getDisplayRoot().y + 1;
    }

    public Dimension getDisplaySize() {
        return new Dimension(getDisplayWidth(), getDisplayHeight());
    }

    public int getX() {
        return getRoot().x;
    }

    public int getY() {
        return getRoot().y;
    }

    public int getWidth() {
        return secondRoot.x - root.x + 1;
    }

    public int getHeight() {
        return secondRoot.y - root.y + 1;
    }

    public Point getRoot() {
        return root;
    }

    public Point getSecondRoot() {
        return secondRoot;
    }

    public void setRoot(int x, int y) {
        root = new Point(x, y);

        if (! isSecondRootSet()) {
            setSecondRoot(x, y);
        }
    }

    public boolean isRootSet() {
        return root != null;
    }

    public void setSecondRoot(int x, int y) {
        if (isSecondRootSet() && (! secondRoot.equals(root))) {
            throw new RuntimeException("The second root on zone '" + id + "' has already been set.");
        }
        secondRoot = new Point(x, y);
    }

    public boolean isSecondRootSet() {
        return secondRoot != null;
    }

    public boolean contains(int x, int y) {
        if (x >= root.x && x <= (secondRoot.x) && y >= root.y && y <= (secondRoot.y)) return true;
        else return false;
    }

    public String getId() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(int horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(int verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public Insets getInsets() {
        return insets;
    }

    public void setInsets(Insets insets) {
        this.insets = insets;
    }

    public String toString() {
        return "Zone " + getId();
    }

    public boolean isPresetZone() {
        return preset != null;
    }

    public boolean intersects(Zone z) {
        return contains(z.getRoot().x, z.getRoot().y) || contains(z.getSecondRoot().x, z.getSecondRoot().y);
    }
}
