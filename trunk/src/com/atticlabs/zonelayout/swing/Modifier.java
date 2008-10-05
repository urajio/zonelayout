package com.atticlabs.zonelayout.swing;

class Modifier {
    static final String SPECIAL_CHARACTERS = "<>_^-|+~!*";
    static final int DEFAULT_TAKE = 1;
    int x, y;
    char c;

    Modifier(int x, int y, char c) {
        this.x = x;
        this.y = y;
        this.c = c;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    char getCharacter() {
        return c;
    }

    void modifyZone(Zone zone) {
        if (c == '<') {
            zone.setHorizontalAlignment(Zone.ALIGN_LEFT);
        }
        else if (c == '>') {
            zone.setHorizontalAlignment(Zone.ALIGN_RIGHT);
        }
        else if (c == '_') {
            zone.setVerticalAlignment(Zone.ALIGN_BOTTOM);
        }
        else if (c == '^') {
            zone.setVerticalAlignment(Zone.ALIGN_TOP);
        }
        else if (c == '-') {
            zone.setHorizontalAlignment(Zone.ALIGN_FILL);
        }
        else if (c == '|') {
            zone.setVerticalAlignment(Zone.ALIGN_FILL);
        }
        else if (c == '+') {
            zone.setVerticalAlignment(Zone.ALIGN_FILL);
            zone.setHorizontalAlignment(Zone.ALIGN_FILL);
        }
        else if (c == '!') {
            zone.setTake(0, DEFAULT_TAKE);
        }
        else if (c == '~') {
            zone.setTake(DEFAULT_TAKE, 0);
        }
        else if (c == '*') {
            zone.setTake(DEFAULT_TAKE, DEFAULT_TAKE);
        }
        else {
            throw new RuntimeException("Unknown character '" + c + "' at " + x + "," + y);
        }
    }

    public String toString() {
        return "'" + c + "' at " + x + "," + y;
    }
}
