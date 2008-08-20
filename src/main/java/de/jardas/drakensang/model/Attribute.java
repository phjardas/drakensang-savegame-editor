package de.jardas.drakensang.model;

public class Attribute extends IntegerMap {
    public static final String[] KEYS = new String[] {
            "CH", "GE", "FF", "IN", "KL", "KK", "KO", "MU",
        };

    @Override
    public String[] getKeys() {
        return Attribute.KEYS;
    }
}
