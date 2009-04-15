package de.jardas.drakensang.model;

import de.jardas.drakensang.dao.Messages;


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

        if (getDiceMultiplier() > 1) {
            buffer.append(getDiceMultiplier());
        }

        buffer.append(Messages.get("D6"));

        if (getAddition() != 0) {
            if (getAddition() > 0) {
                buffer.append("+");
            }

            buffer.append(getAddition());
        }

        return buffer.toString();
    }
}
