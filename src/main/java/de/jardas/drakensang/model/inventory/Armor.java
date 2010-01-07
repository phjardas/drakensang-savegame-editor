package de.jardas.drakensang.model.inventory;

public class Armor extends InventoryItem {
    private int ruestungKopf;
    private int ruestungBrust;
    private int ruestungRuecken;
    private int ruestungBauch;
    private int ruestungArmLinks;
    private int ruestungArmRechts;
    private int ruestungBeinLinks;
    private int ruestungBeinRechts;

    public Armor() {
        super(false);
    }

    public int getRuestungKopf() {
        return ruestungKopf;
    }

    public void setRuestungKopf(int ruestungKopf) {
        this.ruestungKopf = ruestungKopf;
    }

    public int getRuestungBrust() {
        return ruestungBrust;
    }

    public void setRuestungBrust(int ruestungBrust) {
        this.ruestungBrust = ruestungBrust;
    }

    public int getRuestungRuecken() {
        return ruestungRuecken;
    }

    public void setRuestungRuecken(int ruestungRuecken) {
        this.ruestungRuecken = ruestungRuecken;
    }

    public int getRuestungBauch() {
        return ruestungBauch;
    }

    public void setRuestungBauch(int ruestungBauch) {
        this.ruestungBauch = ruestungBauch;
    }

    public int getRuestungArmLinks() {
        return ruestungArmLinks;
    }

    public void setRuestungArmLinks(int ruestungArmLinks) {
        this.ruestungArmLinks = ruestungArmLinks;
    }

    public int getRuestungArmRechts() {
        return ruestungArmRechts;
    }

    public void setRuestungArmRechts(int ruestungArmRechts) {
        this.ruestungArmRechts = ruestungArmRechts;
    }

    public int getRuestungBeinLinks() {
        return ruestungBeinLinks;
    }

    public void setRuestungBeinLinks(int ruestungBeinLinks) {
        this.ruestungBeinLinks = ruestungBeinLinks;
    }

    public int getRuestungBeinRechts() {
        return ruestungBeinRechts;
    }

    public void setRuestungBeinRechts(int ruestungBeinRechts) {
        this.ruestungBeinRechts = ruestungBeinRechts;
    }
}
