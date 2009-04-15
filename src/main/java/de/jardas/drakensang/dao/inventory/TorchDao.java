package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.model.inventory.Torch;


public class TorchDao extends InventoryItemDao<Torch> {
    private static final String[] FIELDS = {
            "Guid", "_ID", "_Level", "_Layers", "Transform", "Id", "Graphics",
            "Physics", "EntityTriggerRadius", "SoundSet", "Name", "State",
            "LookAtText", "EquipmentType", "WeaponAnimGroup",
            "AlternativeWeaponAnimGroup", "TaAttr", "WpW6", "WpW6plus",
            "WpTPKK1", "WpTPKK2", "Gew", "WpLg", "WpBF", "WpINI", "Value",
            "WpATmod", "WpPAmod", "WpDK", "WpATRange", "WpReload", "WpD1",
            "WpD2", "WpD3", "WpD4", "WpD5", "WpTP1", "WpTP2", "WpTP3", "WpTP4",
            "WpTP5", "QuestId", "PickingRange", "WpAmmoCategory", "ScriptPreset",
            "ScriptOverride", "CanUse", "CanDestroy", "IconBrush",
            "LookAtUnidentified", "IdentificationTalent", "InfoUnIdentified",
            "InfoIdentified", "IdentificationDifficulty", "IsIdentified",
            "Robable", "PickingHeight", "UseAnim", "LightColor",
            "LightIntensity", "LightRange", "LightCastShadows",
            "LightShadowIntensity", "LightFlickerEnable",
            "LightFlickerFrequency", "LightFlickerIntensity",
            "LightFlickerPosition", "SoundUI", "StorageGUID", "Lootable",
            "StateBaseTransform", "StateBaseTransformSet", "VelocityVector",
            "PhysicCategory", "StorageSlotId", "IsTradeItem", "InventoryType",
            "IsEquiped", "EquipmentSlotId", "LocalizeLookAtText", "IsMagical",
            "IsCharGraphics", "AnimSet", "SpellTargetOffset", "LimitedScript",
            "UseTalent", "EntityDiscovered",
        };

    public TorchDao() {
        super(Torch.class, "_Instance_Torch");
    }

    @Override
    protected String[] getFields() {
        return FIELDS;
    }
}
