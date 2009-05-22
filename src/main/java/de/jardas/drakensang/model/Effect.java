package de.jardas.drakensang.model;

import org.apache.commons.lang.builder.ToStringBuilder;

import de.jardas.drakensang.dao.Static;


public class Effect {
    private final EffectTarget targetType;
    private final String targetName;
    private final int modifier;

    public Effect(final EffectTarget targetType, final String targetName,
        final int modifier) {
        super();
        this.targetType = targetType;
        this.targetName = targetName;
        this.modifier = modifier;
    }

    public int getModifier() {
        return this.modifier;
    }

    public String getTargetName() {
        return this.targetName;
    }

    public EffectTarget getTargetType() {
        return this.targetType;
    }

    public String getTargetNameKey() {
        switch (getTargetType()) {
        case Talent:
            return Talente.getNameKey(getTargetName());

        case MagicResistance:
            return "Magieresistenz";

        case Life:
            return "LE";

        case Mana:
            return "AE";

        case Endurance:
            return "AU";
            
        case Dodge:
        	return "Ausweichen";
            
        case Spell:
        	return Static.get("Name", getTargetName(), "ZaAttr", "_Template_Zauber");

        default:
            return getTargetName();
        }
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
