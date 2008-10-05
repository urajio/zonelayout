package com.atticlabs.zonelayout.swing;

class Row extends Section {
    static final AxisView VIEW = new YAxisView();

    Row(ZoneLayoutImpl layout, int id) {
        super(layout, id);
    }

    Row(Row row, ZoneContext context) {
        super(row, context);
    }

    AxisView getView() {
        return VIEW;
    }

    Section duplicate(ZoneContext context) {
        return new Row(this, context);
    }

    public String toString() {
        return "Row " + getCoordinate();
    }
}
