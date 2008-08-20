package de.jardas.drakensang.model;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class Character extends Persistable {
    private int abenteuerpunkte;
    private int steigerungspunkte;
    private final Attribute attribute = new Attribute();
    private final Talente talente = new Talente();
    private final Sonderfertigkeiten sonderfertigkeiten = new Sonderfertigkeiten();
    private final Zauberfertigkeiten zauberfertigkeiten = new Zauberfertigkeiten();
    private final Inventory inventory = new Inventory();
    private Race race;
    private Culture culture;
    private Profession profession;
    private Sex sex;
    private boolean magician;
    private String lookAtText;
    private boolean localizeLookAtText;
    private CharacterSet characterSet;
    private CasterType casterType;
    private CasterRace casterRace;
    private int lebensenergieBonus;
    private int astralenergieBonus;
    private final List<Advantage> advantages = new ArrayList<Advantage>();
    private int level;

    public boolean isPlayerCharacter() {
        return "CharWizardPC".equals(getId());
    }

    public int getMoneyAmount() {
        Set<Money> money = getInventory().getItems(Money.class);

        return money.isEmpty() ? 0 : money.iterator().next().getCount();
    }

    public void setMoneyAmount(int amount) {
        Set<Money> money = getInventory().getItems(Money.class);

        if (money.size() != 1) {
            throw new IllegalArgumentException("The character " + getName()
                + " can not carry money.");
        }

        money.iterator().next().setCount(amount);
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public Talente getTalente() {
        return talente;
    }

    public Sonderfertigkeiten getSonderfertigkeiten() {
        return sonderfertigkeiten;
    }

    public Zauberfertigkeiten getZauberfertigkeiten() {
        return zauberfertigkeiten;
    }

    public int getAbenteuerpunkte() {
        return abenteuerpunkte;
    }

    public void setAbenteuerpunkte(int abenteuerpunkte) {
        this.abenteuerpunkte = abenteuerpunkte;
    }

    public int getSteigerungspunkte() {
        return steigerungspunkte;
    }

    public void setSteigerungspunkte(int steigerungspunkte) {
        this.steigerungspunkte = steigerungspunkte;
    }

    public Culture getCulture() {
        return this.culture;
    }

    public void setCulture(Culture culture) {
        this.culture = culture;
    }

    public Profession getProfession() {
        return this.profession;
    }

    public void setProfession(Profession profession) {
        this.profession = profession;
    }

    public Race getRace() {
        return this.race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public Sex getSex() {
        return this.sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public boolean isMagician() {
        return this.magician;
    }

    public void setMagician(boolean magician) {
        this.magician = magician;
    }

    public String getLookAtText() {
        return lookAtText;
    }

    public void setLookAtText(String lookAtText) {
        this.lookAtText = lookAtText;
    }

    public boolean isLocalizeLookAtText() {
        return localizeLookAtText;
    }

    public void setLocalizeLookAtText(boolean localizeLookAtText) {
        this.localizeLookAtText = localizeLookAtText;
    }

    public CharacterSet getCharacterSet() {
        return characterSet;
    }

    public void setCharacterSet(CharacterSet characterSet) {
        this.characterSet = characterSet;
    }

    public CasterType getCasterType() {
        return casterType;
    }

    public void setCasterType(CasterType casterType) {
        this.casterType = casterType;
    }

    public CasterRace getCasterRace() {
        return casterRace;
    }

    public void setCasterRace(CasterRace casterRace) {
        this.casterRace = casterRace;
    }

    public int getAstralenergieBonus() {
        return this.astralenergieBonus;
    }

    public void setAstralenergieBonus(int astralenergieBonus) {
        this.astralenergieBonus = astralenergieBonus;
    }

    public int getLebensenergieBonus() {
        return this.lebensenergieBonus;
    }

    public void setLebensenergieBonus(int lebensenergieBonus) {
        this.lebensenergieBonus = lebensenergieBonus;
    }

    public int getAttackeBasis() {
        return (int) Math.round((double) (getAttribute().get("MU")
            + getAttribute().get("GE") + getAttribute().get("KK")) / 5);
    }

    public int getParadeBasis() {
        return (int) Math.round((double) (getAttribute().get("IN")
            + getAttribute().get("GE") + getAttribute().get("KK")) / 5);
    }

    public int getFernkampfBasis() {
        return (int) Math.round((double) (getAttribute().get("IN")
            + getAttribute().get("FF") + getAttribute().get("KK")) / 5);
    }

    public int getLebensenergie() {
        int basis = (int) Math.round((double) (getAttribute().get("KO")
                + getAttribute().get("KO") + getAttribute().get("KK")) / 2);

        return basis + getLebensenergieBonus()
        + getRace().getLebensenergieModifikator()
        + getCulture().getLebensenergieModifikator()
        + getProfession().getLebensenergieModifikator();
    }

    public int getAusdauer() {
        int basis = (int) Math.round((double) (getAttribute().get("MU")
                + getAttribute().get("KO") + getAttribute().get("GE")) / 2);

        return basis + getRace().getAusdauerModifikator()
        + getCulture().getAusdauerModifikator()
        + getProfession().getAusdauerModifikator();
    }

    public int getAstralenergie() {
        int basis = (int) Math.round((double) (getAttribute().get("MU")
                + getAttribute().get("IN") + getAttribute().get("CH")) / 2);

        return basis + getAstralenergieBonus()
        + getRace().getAstralenergieModifikator()
        + getCulture().getAstralenergieModifikator()
        + getProfession().getAstralenergieModifikator();
    }

    public int getMagieresistenz() {
        return (int) Math.round((double) (getAttribute().get("MU")
            + getAttribute().get("KL") + getAttribute().get("KO")) / 5)
        + getRace().getMagieresistenzModifikator()
        + getCulture().getMagieresistenzModifikator()
        + getProfession().getMagieresistenzModifikator();
    }

    public List<Advantage> getAdvantages() {
        return this.advantages;
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this,
            ToStringStyle.MULTI_LINE_STYLE).toString();
    }
}
