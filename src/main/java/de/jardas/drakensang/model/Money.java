package de.jardas.drakensang.model;

public class Money extends InventoryItem {
    @Override
    public String toString() {
        int dukaten = getCount() / 1000;
        int silber = (getCount() - (dukaten * 1000)) / 100;
        int heller = getCount() - (dukaten * 1000) - (silber * 100);

        return dukaten + "D " + silber + "S " + heller + "H";
    }
}
