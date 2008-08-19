/*
 * Challenge.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.game;

import java.util.ArrayList;
import java.util.List;


public class Challenge {
    private final List<Integer> talents = new ArrayList<Integer>();
    private int bonus;
    private int handicap;

    public Challenge addBonus(int bonus) {
        this.bonus += bonus;

        return this;
    }

    public Challenge addHandicap(int handicap) {
        this.handicap += handicap;

        return this;
    }

    public Challenge addTalent(int talent) {
        return addTalents(talent);
    }

    public Challenge addTalents(int... talents) {
        for (int talent : talents) {
            this.talents.add(talent);
        }

        return this;
    }

    @Override
    public String toString() {
        return "Challenge on " + talents + " with talent bonus " + bonus
        + ", handicap " + handicap;
    }

    public boolean execute() {
        int remaining = bonus;

        for (int talent : talents) {
            final int roll = Dice.W20.roll();
            int diff = roll - (talent - handicap);

            if (bonus < 0) {
                diff -= bonus;
            }

            if (diff > 0) {
                remaining -= diff;

                if (remaining < 0) {
                    return false;
                }
            }
        }

        return true;
    }

    public double estimateChances(int count) {
        int success = 0;

        for (int i = 0; i < count; i++) {
            if (execute()) {
                success++;
            }
        }

        return (double) success / count;
    }
}
