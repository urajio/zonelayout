package com.atticlabs.zonelayout.swing;

final class Column extends Section {
    static final AxisView VIEW = new XAxisView();

    Column(ZoneLayoutImpl layout, int id) {
        super(layout, id);
    }

    Column(Column column, ZoneContext context) {
        super(column, context);
    }

    AxisView getView() {
        return VIEW;
    }

    Section duplicate(ZoneContext context) {
        return new Column(this, context);
    }

    public String toString() {
        return "Column " + getCoordinate();
    }
}
