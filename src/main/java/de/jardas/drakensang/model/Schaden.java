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
    private int diceMultiplier;
    private int addition;

    public Schaden(final int diceMultiplier, final int addition) {
        super();
        this.diceMultiplier = diceMultiplier;
        this.addition = addition;
    }

    public int getAddition() {
        return this.addition;
    }

    public void setAddition(int addition) {
        this.addition = addition;
    }

    public int getDiceMultiplier() {
        return this.diceMultiplier;
    }

    public void setDiceMultiplier(int diceMultiplier) {
        this.diceMultiplier = diceMultiplier;
    }

    public int getMinimum() {
        return getDiceMultiplier() + getAddition();
    }

    public int getMaximum() {
        return (getDiceMultiplier() * 6) + getAddition();
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

        buffer.append(" (").append(getMinimum()).append("-").append(getMaximum())
              .append(")");

        return buffer.toString();
    }
}
