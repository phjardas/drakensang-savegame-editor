/*
 * Schaden.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.model;

public class Schaden {
    private final int diceMultiplier;
    private final int addition;

    public Schaden(final int diceMultiplier, final int addition) {
        super();
        this.diceMultiplier = diceMultiplier;
        this.addition = addition;
    }

    public int getAddition() {
        return this.addition;
    }

    public int getDiceMultiplier() {
        return this.diceMultiplier;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getDiceMultiplier()).append("W6");

        if (getAddition() != 0) {
            if (getAddition() > 0) {
                buffer.append("+");
            }

            buffer.append(getAddition());
        }

        return buffer.toString();
    }
}
