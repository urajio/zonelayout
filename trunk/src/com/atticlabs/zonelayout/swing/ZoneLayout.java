package com.atticlabs.zonelayout.swing;

import java.awt.LayoutManager2;
import java.util.Map;

public interface ZoneLayout extends LayoutManager2 {
    Map getZones();

    ZoneLayout addRow(String rowDefinition);

    ZoneLayout addRow(String rowDefinition, String templateName);

    void insertTemplate(String templateName);

    void setPresetComponent(int presetId, Preset preset);

    Preset getPreset(int presetId);

    Zone getZone(String zoneId);

    void compile();
}
