package de.jardas.drakensang.game;

import java.util.Random;


public final class Dice {
    public static final Dice W6 = new Dice(6);
    public static final Dice W20 = new Dice(20);
    private static final Random RANDOM = new Random();
    private final int sides;

    private Dice(final int sides) {
        super();
        this.sides = sides;
    }

    public int roll() {
        return RANDOM.nextInt(sides) + 1;
    }

    @Override
    public String toString() {
        return "W" + sides;
    }
}
