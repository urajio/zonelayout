package com.atticlabs.zonelayout.swing;


import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.IdentityHashMap;

final class ZoneLayoutImpl implements ZoneLayout {
    static final Map DEFAULT_PRESETS;

    static {
        Map temp = new HashMap();
        temp.put("0", Preset.createSpacer(0, 0));
        temp.put("1", Preset.createSpacer(3, 0));
        temp.put("2", Preset.createSpacer(6, 0));
        temp.put("3", Preset.createSpacer(12, 0));
        temp.put("4", Preset.createSpacer(18, 0));
        temp.put("5", Preset.createSpacer(0, 3));
        temp.put("6", Preset.createSpacer(0, 6));
        temp.put("7", Preset.createSpacer(0, 12));
        temp.put("8", Preset.createSpacer(0, 18));
        DEFAULT_PRESETS = temp;
    }

    static final String PREFIX_COMPONENT_PRESET = "ComponentPreset";

    Map componentPresets = new HashMap(DEFAULT_PRESETS);
    List rowDefinitions = new ArrayList();
    int rowDefinitionLength = -1;
    List rows;
    List columns;
    Map zones = new HashMap();
    Map templateNameMap = new HashMap();
    List templates = new ArrayList();
    Map bindings = new HashMap();
    IdentityHashMap allPoints = new IdentityHashMap();
    boolean compiled;
    boolean sectionSizesComputed;


    public ZoneLayoutImpl() {
    }

    // User Methods

    public Map getZones() {
        return zones;
    }

    public ZoneLayout addRow(String rowDefinition) {
        if (compiled) {
            throw new RuntimeException("You cannot change the definition of the layout after adding components.");
        }

        if ("".equals(rowDefinition)) {
            throw new RuntimeException("Row definition cannot be an empty string.");
        }

        if (rowDefinitionLength < 0) {
            rowDefinitionLength = rowDefinition.length();
        }
        else {
            if (rowDefinition.length() != rowDefinitionLength) {
                throw new RuntimeException("Row #" + (rowDefinitions.size() + 1) + " in layout definition does " +
                        "not match the length of previous rows.");
            }
        }

        rowDefinitions.add(rowDefinition);

        preProcessRow(rowDefinition);

        return this;
    }

    public ZoneLayout addRow(String rowDefinition, String templateName) {
        Template template = (Template) templateNameMap.get(templateName);

        if (template == null) {
            template = new Template(templateName, rowDefinitions.size());
            templateNameMap.put(template.getName(), template);
            templates.add(template);
        }
        else {
            template.incrementEndPoint();
        }

        addRow(rowDefinition);

        return this;
    }

    public void insertTemplate(String templateName) {
        compile();

        Template template = (Template) templateNameMap.get(templateName);

        if (template == null) {
            throw new RuntimeException("Unknown template name '" + templateName + "'");
        }

        AxisView view = template.getView();
        ZoneContext context = template.getNewZoneContext();
        zones.putAll(context.getZones());
        if (template.getLastContext() != null) {
            template.setLastContextZoneIds();
            zones.putAll(template.getLastContext().getZones());
        }

        template.setLastContext(context);
        int start = view.getValue(template.getInsertionPoint());
        int amount = view.getValue(template.getEndPoint()) - start + 1;
        adjustAllPoints(view, start, amount);
    }

    public void setPresetComponent(int presetId, Preset preset) {
        if (presetId < 1 || presetId >9) {
            throw new IllegalArgumentException("presetId must be from 1 - 9");
        }
        componentPresets.put(String.valueOf(presetId), preset);
    }

    public Preset getPreset(int presetId) {
        return (Preset) componentPresets.get(String.valueOf(presetId));
    }

    public Zone getZone(String zoneId) {
        return (Zone) zones.get(zoneId);
    }

    // Compilation Methods

    void preProcessRow(String row) {
        for (int i = 0; i < row.length(); i++) {
            char c = row.charAt(i);
            if (Character.isLetter(c)) {
                String zoneId = String.valueOf(c);
                if (! zones.containsKey(zoneId)) {
                    zones.put(zoneId, new Zone(zoneId));
                }
            }
        }
    }

    public void compile() {
        compile(true);
    }

    void compile(boolean optimize) {
        if (compiled) return;

        List modifierCharacters = new ArrayList();
        int x = 0;
        int y = 0;

        for (Iterator iterator = rowDefinitions.iterator(); iterator.hasNext();) {
            String s = (String) iterator.next();
            for (x = 0; x < s.length(); x++) {
                char c = s.charAt(x);
                if ('.' == c) {
                    continue;
                }
                else if (Character.isLetter(c)) {
                    Zone zone = (Zone) zones.get(String.valueOf(c));
                    if (! zone.isRootSet()) {
                        zone.setRoot(x, y);
                    }
                    else {
                        if (x < zone.getRoot().x) {
                            throw new RuntimeException("The second identifier '" + c + "' must mark the bottom right corner.");
                        }
                        zone.setSecondRoot(x, y);
                    }
                }
                else if (Character.isDigit(c)) {
                    String preset = Character.toString(c);
                    if (componentPresets.containsKey(preset)) {
                        Zone zone = new Zone(PREFIX_COMPONENT_PRESET + "-" + c + "-" + zones.keySet().size());
                        getPreset(Integer.parseInt(preset)).setUpZone(zone);
                        zone.setRoot(x,y);
                        zones.put(zone.getId(), zone);
                    }
                    else {
                        throw new RuntimeException("Unknown component preset '" + c + "' at " + x + "," + y);
                    }
                }
                else if (Modifier.SPECIAL_CHARACTERS.indexOf(c) != -1) {
                    modifierCharacters.add(new Modifier(x, y, c));
                }
                else {
                    throw new RuntimeException("Unknown character '" + c + "' at " + x + "," + y);
                }
            }
            y++;
        }

        for (int i = 0; i < modifierCharacters.size(); i++) {
            Modifier modifier = (Modifier) modifierCharacters.get(i);
            boolean used = false;
            for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
                Zone zone = (Zone) iterator.next();
                if (zone.contains(modifier.getX(), modifier.getY())) {
                    modifier.modifyZone(zone);
                    used = true;
                }
            }
            if (! used) {
                if (Preset.SPECIAL_CHARACTERS.indexOf(modifier.getCharacter()) != -1) {
                    Zone zone = new Zone(PREFIX_COMPONENT_PRESET + "-" + modifier.getCharacter() + "-" + zones.keySet().size());
                    Preset.getSpecialPreset(modifier.getCharacter()).setUpZone(zone);
                    zone.setRoot(modifier.getX(), modifier.getY());
                    zones.put(zone.getId(), zone);
                }
                else {
                    throw new RuntimeException("Unused modifier " + modifier);
                }
            }
        }

        checkZoneIntersection();

        collectAllPoints();

        if (optimize) {
            optimizeZones(AxisView.X, getRowDefinitionWidth());
            optimizeZones(AxisView.Y, getRowDefinitionHeight());
        }

        removeTemplates();
        setDefaultZoneGives();

        compiled = true;
    }

    void checkZoneIntersection() {
        List otherZones = new ArrayList(zones.values());
        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            otherZones.remove(zone);
            for (Iterator iterator1 = otherZones.iterator(); iterator1.hasNext();) {
                Zone otherZone = (Zone) iterator1.next();
                if (zone.intersects(otherZone)) {
                    throw new RuntimeException("Zone '" +  zone.getId() + "' intersects zone '" + otherZone.getId() + "'.");
                }
            }
            otherZones.add(zone);
        }
    }

    void optimizeZones(AxisView view, int count) {
        Point p = new Point();
        Set last = null;
        Set current = null;
        for (int i = 0; i < count; i++) {
            view.setValue(p, i);
            current = getZonesThatIntersectAxisLine(view, p);

            if (current.isEmpty()) {
                adjustAllPoints(view, i, -1);
                i--;
                count--;
            }
            else if (last != null) {
                if (current.equals(last)) {
                    adjustAllPoints(view, i, -1);
                    i--;
                    count--;
                }
            }
            last = current;
        }
    }

    Set getZonesThatIntersectAxisLine(AxisView view, Point line) {
        Set intersectingZones = new HashSet();

        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            if (zone.intersectsAxisLine(view, line)) {
                intersectingZones.add(zone);
            }
        }

        return intersectingZones;
    }

    void collectAllPoints() {
        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            allPoints.put(zone.getRoot(), null);
            allPoints.put(zone.getSecondRoot(), null);
        }

        for (Iterator iterator = templateNameMap.values().iterator(); iterator.hasNext();) {
            Template template = (Template) iterator.next();
            allPoints.put(template.getInsertionPoint(), null);
            allPoints.put(template.getEndPoint(), null);
        }
    }

    void adjustAllPoints(AxisView view, int startingCoordinate, int amount) {
        for (Iterator pointIter = allPoints.keySet().iterator(); pointIter.hasNext();) {
            Point point = (Point) pointIter.next();
            if (view.getValue(point) >= startingCoordinate) {
                view.adjust(point, amount);
            }
        }
    }

    Set getZonesContainedWithinAxisLines(AxisView view, Point start, Point finish) {
        Set containedZones = new HashSet();

        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            if (view.getValue(zone.getRoot()) >= view.getValue(start) &&
                    view.getValue(zone.getSecondRoot()) <= view.getValue(finish)) {
                containedZones.add(zone);
            }
        }

        return containedZones;
    }

    int getRowDefinitionWidth() {
        return ((String) rowDefinitions.get(0)).length();
    }

    int getRowDefinitionHeight() {
        return rowDefinitions.size();
    }

    void removeTemplates() {
        for (Iterator iterator = templates.iterator(); iterator.hasNext();) {
            Template template = (Template) iterator.next();
            AxisView view = template.getView();
            Set containedZones = getZonesContainedWithinAxisLines(view, template.getInsertionPoint(), template.getEndPoint());
            for (Iterator zoneIter = containedZones.iterator(); zoneIter.hasNext();) {
                Zone zone = (Zone) zoneIter.next();
                zones.remove(zone.getId());
                allPoints.remove(zone.getRoot());
                allPoints.remove(zone.getSecondRoot());
                template.addZone(zone);
            }

            int start = view.getValue(template.getEndPoint()) + 1;
            int amount = view.getValue(template.getEndPoint()) - view.getValue(template.getInsertionPoint()) + 1;
            adjustAllPoints(view, start, -1 * amount);

            allPoints.remove(template.getInsertionPoint());
            allPoints.remove(template.getEndPoint());
        }

        for (Iterator iterator = templates.iterator(); iterator.hasNext();) {
            Template template = (Template) iterator.next();
            allPoints.put(template.getInsertionPoint(), null);
            allPoints.put(template.getEndPoint(), null);
        }
    }

    void setDefaultZoneGives() {
        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            if (! zone.isGiveUserSet()) {
                zone.setGive(zone.getGive().horizontal * zone.getWidth(), zone.getGive().vertical);
                zone.setGive(zone.getGive().horizontal, zone.getGive().vertical * zone.getHeight());
            }
        }
    }

    //  Layout Manager 2 Methods

    public void addLayoutComponent(Component comp, Object constraints) {
        compile();

        if (! (constraints instanceof String)) {
            throw new RuntimeException("Illegal constraint (must be a String identifying a zone).");
        }

        String zoneId = (String) constraints;
        Zone zone = (Zone) zones.get(zoneId);

        if (zone == null) {
            throw new RuntimeException("Unknown zone '" + constraints + "'");
        }

        zone.setComponent(comp);
        bindings.put(comp, zone);
    }

    public Dimension maximumLayoutSize(Container target) {
        return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
    }

    public float getLayoutAlignmentX(Container target) {
        return 0.5f;
    }

    public float getLayoutAlignmentY(Container target) {
        return 0.5f;
    }

    public void invalidateLayout(Container target) {
        synchronized(target.getTreeLock()) {
            rows = null;
            columns = null;
            sectionSizesComputed = false;
            for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
                Zone zone = (Zone) iterator.next();
                zone.clearCache();
            }
        }
    }

    public void addLayoutComponent(String name, Component comp) {
        throw new UnsupportedOperationException("use addLayoutComponent(Component comp, Object constraints).");
    }

    public void removeLayoutComponent(Component comp) {
        Zone zone = (Zone) bindings.get(comp);

        if (zone == null) {
            throw new RuntimeException("The component is not contained in this layout (perhaps replaced by another?).");
        }

        zone.setComponent(null);
        bindings.remove(comp);
    }

    public Dimension preferredLayoutSize(Container parent) {
        return getLayoutSize(new PreferredSizeView(), parent);
    }

    public Dimension minimumLayoutSize(Container parent) {
        return getLayoutSize(new MinimumSizeView(), parent);
    }

    public void layoutContainer(Container parent) {

        synchronized(parent.getTreeLock()) {
            createSections();
            computeSectionSizes();
            Dimension size = parent.getSize();
            layoutSections(parent, rows, AxisView.Y, AxisView.Y.getSize(size));
            layoutSections(parent, columns, AxisView.X, AxisView.X.getSize(size));
            layoutZones();
            layoutComponents(parent);
        }

    }

    // Internal Layout Methods

    Dimension getLayoutSize(SizeView view, Container parent) {
        synchronized(parent.getTreeLock()) {
            createSections();
            computeSectionSizes();

            int w = getCombinedSize(columns, view);
            w += parent.getInsets().left + parent.getInsets().right;

            int h = getCombinedSize(rows, view);
            h += parent.getInsets().top + parent.getInsets().bottom;

            return new Dimension(w, h);
        }
    }

    boolean sectionsCreated() {
        return rows != null && columns != null;
    }

    void createSections() {
        if (! sectionsCreated()) {
            compile();
            rows = new ArrayList();
            columns = new ArrayList();
            createColumnsAndRows();
            setSectionWeights(rows, AxisView.Y);
            setSectionWeights(columns, AxisView.X);
        }
    }

    void createColumnsAndRows() {
        int max = getMaxZoneAxisValue(AxisView.X);
        for (int i = 0; i <= max; i++) {
            columns.add(new Column(this, i));
        }

        max = getMaxZoneAxisValue(AxisView.Y);

        for (int i = 0; i <= max; i++) {
            rows.add(new Row(this, i));
        }
    }

    int getMaxZoneAxisValue(AxisView view) {
        int value = -1;
        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            value = Math.max(value, view.getValue(zone.getSecondRoot()));
        }
        return value;
    }

    void computeSectionSizes() {
//        checkZoneSizes(zones.values());
        if (! sectionSizesComputed) {
            clearSizes(rows);
            clearSizes(columns);
            setSectionSizes(rows, AxisView.Y, new MinimumSizeView());
            copyMinSizeToPrefSize(rows);
            setSectionSizes(rows, AxisView.Y, new PreferredSizeView());
            setSectionSizes(columns, AxisView.X, new MinimumSizeView());
            copyMinSizeToPrefSize(columns);
            setSectionSizes(columns, AxisView.X, new PreferredSizeView());
            sectionSizesComputed = true;
        }
    }

    void clearSizes(List sections) {
        for (int i = 0; i < sections.size(); i++) {
            Section section = (Section) sections.get(i);
            section.setMinimumSize(0);
            section.setPreferredSize(0);
        }
    }

    void copyMinSizeToPrefSize(List sections) {
        for (Iterator iterator = sections.iterator(); iterator.hasNext();) {
            Section section = (Section) iterator.next();
            section.setPreferredSize(section.getMinimumSize());
        }
    }

    void setSectionWeights(List sections, final AxisView axisView) {
        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            if (axisView.getSize(zone) == 1 && axisView.getTake(zone) > 0) {
                Section section = (Section) sections.get(axisView.getStart(zone));
                section.setTake(Math.max(section.getTake(),  axisView.getTake(zone)));
            }

        }

        for (Iterator iterator = sections.iterator(); iterator.hasNext();) {
            Section section = (Section) iterator.next();
            boolean isSpannedSection = true;
            double spannedTake = axisView.getValue(Zone.MAX_GIVE_OR_TAKE);
            for (Iterator iterator1 = section.getZones().iterator(); iterator1.hasNext();) {
                Zone zone = (Zone) iterator1.next();
                if (axisView.getSize(zone) == 1) {
                    isSpannedSection = false;
                    break;
                }
                else {
                    spannedTake = Math.min(spannedTake, (double) axisView.getTake(zone) / (double) axisView.getSize(zone));
                }
            }
            if (isSpannedSection) {
                section.setTake(spannedTake);
            }
        }

        initSectionGives(sections, axisView);

        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            if (axisView.getSize(zone) > 1 && axisView.getTake(zone) > 0) {
                double take = axisView.getTake(zone);
                double totalTake = 0;

                for (int i = axisView.getStart(zone); i <= axisView.getFinish(zone); i++) {
                    Section section = (Section) sections.get(i);

                    totalTake += section.getTake();
                }

                if (totalTake < take) {
                    adjustTakes(sections, zone, axisView, totalTake, take);
                }
            }

            if (axisView.getSize(zone) > 1) {
                int totalGiveAcrossZone = 0;

                for (int i = axisView.getStart(zone); i <= axisView.getFinish(zone); i++) {
                    Section section = (Section) sections.get(i);
                    totalGiveAcrossZone += section.getGive();
                }

                if (totalGiveAcrossZone > axisView.getGive(zone)) {
                    adjustGives(sections, zone, axisView, totalGiveAcrossZone, axisView.getGive(zone));
                }
            }
        }
    }

    void initSectionGives(List sections, AxisView axisView) {
        for (Iterator iterator = sections.iterator(); iterator.hasNext();) {
            Section section = (Section) iterator.next();
            section.setGive(Integer.MAX_VALUE);
        }

        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();

            if (axisView.getSize(zone) == 1) {
                Section section = (Section) sections.get(axisView.getStart(zone));
                section.setGive(Math.min(section.getGive(), axisView.getGive(zone)));
            }
        }

        for (Iterator iterator = sections.iterator(); iterator.hasNext();) {
            Section section = (Section) iterator.next();

            if (section.getGive() == Integer.MAX_VALUE) {
                Set sectionZones = section.getZones();
                double newGive = section.getGive();

                for (Iterator iterator1 = sectionZones.iterator(); iterator1.hasNext();) {
                    Zone zone = (Zone) iterator1.next();
                    int giveRemaining = getGiveRemainingToBeDistributed(sections, zone, axisView);

                    if (giveRemaining == 0) {
                        newGive = 0;
                        break;
                    }

                    int openSections = getUnsetGiveSectionCount(sections, zone, axisView);
                    int sectionShare = giveRemaining / openSections + (giveRemaining % openSections > 0 ? 1 : 0);

                    if (sectionShare < newGive) {
                        newGive = sectionShare;
                    }
                }

                section.setGive(newGive);
            }
        }
    }

    int getGiveRemainingToBeDistributed(List sections, Zone zone, AxisView axisView) {
        int give = axisView.getGive(zone);

        for (int i = axisView.getStart(zone); i <= axisView.getFinish(zone); i++) {
            Section section = (Section) sections.get(i);
            if (section.getGive() != Integer.MAX_VALUE) {
                give -= section.getGive();
            }
        }

        return Math.max(0, give);
    }

    int getUnsetGiveSectionCount(List sections, Zone zone, AxisView axisView) {
        int count = 0;

        for (int i = axisView.getStart(zone); i <= axisView.getFinish(zone); i++) {
            Section section = (Section) sections.get(i);
            if (section.getGive() == Integer.MAX_VALUE) {
                count++;
            }
        }

        return count;
    }

    void adjustGives(List sections, Zone zone, AxisView axisView, int currentGiveTotal, int newGiveTotal) {
        if (newGiveTotal == 0) {
            for (int i = axisView.getStart(zone); i <= axisView.getFinish(zone); i++) {
                Section section = (Section) sections.get(i);
                section.setGive(0);
            }
        }
        else {

            double[] values = new double[axisView.getFinish(zone) - axisView.getStart(zone) + 1];

            for (int i = axisView.getStart(zone); i <= axisView.getFinish(zone); i++) {
                values[i - axisView.getStart(zone)] = ((Section) sections.get(i)).getGive();
            }

            MathRoutines.proportionallyRedistributeValues(values, currentGiveTotal, newGiveTotal);

            for (int i = axisView.getStart(zone); i <= axisView.getFinish(zone); i++) {
                ((Section) sections.get(i)).setGive(values[i - axisView.getStart(zone)]);
            }
        }
    }

    void adjustTakes(List sections, Zone zone, AxisView axisView, double currentTakeTotal, double newTakeTotal) {
        double[] values = new double[axisView.getFinish(zone) - axisView.getStart(zone) + 1];

        if (currentTakeTotal == 0) {
            Arrays.fill(values, 1);
            currentTakeTotal = values.length;
        }
        else {
            for (int i = axisView.getStart(zone); i <= axisView.getFinish(zone); i++) {
                values[i - axisView.getStart(zone)] = ((Section) sections.get(i)).getTake();
            }
        }

        MathRoutines.proportionallyRedistributeValues(values, currentTakeTotal, newTakeTotal);

        for (int i = axisView.getStart(zone); i <= axisView.getFinish(zone); i++) {
            ((Section) sections.get(i)).setTake(values[i - axisView.getStart(zone)]);
        }
    }

    void setSectionSizes(List sections, AxisView axisView, SizeView sizeView) {
        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            if (axisView.getSize(zone) == 1 && zone.hasVisibleComponent()) {
                Section section = (Section) sections.get(axisView.getStart(zone));
                int current = sizeView.getSize(section);
                sizeView.setSize(section, Math.max(current, axisView.getSize(sizeView.getSize(zone.getComponentSizeCache()))));
            }
        }

        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            if (axisView.getSize(zone) > 1  && zone.hasVisibleComponent()) {
                int start = axisView.getStart(zone);
                int finish = axisView.getFinish(zone);
                int current = getCombinedSize(sections.subList(start, finish + 1), sizeView);
                int desired =  axisView.getSize(sizeView.getSize(zone.getComponentSizeCache()));
                if (current < desired) {
                    allocateAdditionalSpace(sections.subList(start, finish + 1), desired - current, sizeView, true);
                }
            }
        }
    }

    int getCombinedSize(List sections, SizeView sizeView) {
        int total = 0;

        for (int i = 0; i < sections.size(); i++) {
            Section section = (Section) sections.get(i);
            total += sizeView.getSize(section);
        }

        return total;
    }

    void layoutSections(Container parent, List sections, AxisView view, int size) {
        int prefSize = view.getSize(preferredLayoutSize(parent));

        for (int i = 0; i < sections.size(); i++) {
            Section section = (Section) sections.get(i);
            section.setSize(section.getPreferredSize());
        }

        if (prefSize < size) {
            allocateAdditionalSpace(sections, size - prefSize, new DisplaySizeView(), false);
        }
        else if (prefSize > size) {
            revokeAdditionalSpace(sections, prefSize - size, new DisplaySizeView(),
                    new MinimumSizeView()); // , false);
        }

        int location = view.getAdjustment(parent.getInsets());
        for (int i = 0; i < sections.size(); i++) {
            Section section = (Section) sections.get(i);
            section.setDisplayCoordinate(location);
            location += section.getSize();
        }
    }

    void layoutZones() {
        for (Iterator iterator = zones.values().iterator(); iterator.hasNext();) {
            Zone zone = (Zone) iterator.next();
            setZoneAxisSize(zone, rows, AxisView.Y);
            setZoneAxisSize(zone, columns, AxisView.X);
        }
    }

    void setZoneAxisSize(Zone zone, List sections, AxisView view) {
        Section startSection = (Section) sections.get(view.getValue(zone.getRoot()));
        view.setValue(zone.getDisplayRoot(), startSection.getDisplayCoordinate());
        Section endSection = (Section) sections.get(view.getValue(zone.getSecondRoot()));
        view.setValue(zone.getDisplaySecondRoot(), endSection.getDisplayCoordinate() + endSection.getSize() - 1);
    }

    void layoutComponents(Container parent) {
        Component[] components = parent.getComponents();
        for (int i = 0; i < components.length; i++) {
            Zone zone = (Zone) bindings.get(components[i]);

            if (zone == null) {
                throw new RuntimeException("Component " + components[i] + " is not bound to a zone in this layout.");
            }



            if (zone.hasVisibleComponent()) {
                Component component = zone.getComponent();

                int availableWidth = zone.getDisplayWidth() - zone.getInsets().left - zone.getInsets().right;
                int availableHeight = zone.getDisplayHeight() - zone.getInsets().top - zone.getInsets().bottom;

                int componentWidth = 0;
                int componentHeight = 0;

                if (availableWidth < component.getPreferredSize().width) {
                        componentWidth = availableWidth;
                }
                else if (zone.getHorizontalAlignment() == Zone.ALIGN_FILL) {
                    componentWidth = availableWidth;
                }
                else {
                    componentWidth = component.getPreferredSize().width;
                }

                if (availableHeight < component.getPreferredSize().height) {
                        componentHeight = availableHeight;
                }
                else if (zone.getVerticalAlignment() == Zone.ALIGN_FILL) {
                    componentHeight = availableHeight;
                }
                else {
                    componentHeight = component.getPreferredSize().height;
                }

                Rectangle zoneRectangle = new Rectangle(zone.getDisplayRoot().x + zone.getInsets().left,
                        zone.getDisplayRoot().y + zone.getInsets().top,
                        availableWidth,
                        availableHeight);


                Rectangle componentRectangle = new Rectangle(zoneRectangle.x, zoneRectangle.y,
                        componentWidth,
                        componentHeight);

                alignComponentWithinZone(zoneRectangle, componentRectangle, zone.getHorizontalAlignment(),
                        zone.getVerticalAlignment());

                component.setBounds(componentRectangle.x,
                        componentRectangle.y,
                        componentRectangle.width,
                        componentRectangle.height);
            }
        }
    }

    void alignComponentWithinZone(Rectangle zone, Rectangle component, int horizontalAlignment,
                                  int verticalAlignment) {
        int adjustDown = 0;
        int adjustRight = 0;
        switch (horizontalAlignment) {
            case Zone.ALIGN_MIDDLE:
                adjustRight = (zone.width - component.width) / 2;
                component.x = zone.x + adjustRight;
                break;
            case Zone.ALIGN_LEFT:
                break;
            case Zone.ALIGN_RIGHT:
                adjustRight = zone.width - component.width;
                component.x = zone.x + adjustRight;
                break;
        }
        switch (verticalAlignment) {
            case Zone.ALIGN_MIDDLE:
                adjustDown = (zone.height - component.height) / 2;
                component.y = zone.y + adjustDown;
                break;
            case Zone.ALIGN_TOP:
                break;
            case Zone.ALIGN_BOTTOM:
                adjustDown = zone.height - component.height;
                component.y = zone.y + adjustDown;
                break;
        }
    }

    void allocateAdditionalSpace(List sections, int space,
                                 SizeView sizeView, boolean allocatedWithZeroTake) {
        double[] takes = new double[sections.size()];

        for (int i = 0; i < sections.size(); i++) {
            Section section = (Section) sections.get(i);
            takes[i] = section.getTake();
        }

        if (allocatedWithZeroTake && MathRoutines.sum(takes) == 0.0) {
            Arrays.fill(takes, 1);
        }

        int[] toTake = MathRoutines.getSpaceToTake(takes, space);

        int i = 0;

        for (int j = 0; j < sections.size(); j++) {
            Section section = (Section) sections.get(j);
            sizeView.setSize(section, sizeView.getSize(section) + toTake[j]);
        }
    }

    void revokeAdditionalSpace(List sections, int space,
                               SizeView sizeView, SizeView limitingView) { //, boolean revokeFromZeroGiveIfNecessary) {
        double[] gives = new double[sections.size()];
        int[] spaceAvailable = new int[sections.size()];

        for (int i = 0; i < sections.size(); i++) {
            Section section = (Section) sections.get(i);
            gives[i] = section.getGive();
            spaceAvailable[i] = sizeView.getDifference(limitingView, section);
        }

        int[] toGive = MathRoutines.getSpaceToGive(gives, spaceAvailable, space);

        for (int i = 0; i < sections.size(); i++) {
            Section section = (Section) sections.get(i);
            sizeView.setSize(section, sizeView.getSize(section) - toGive[i]);
        }
    }

}
