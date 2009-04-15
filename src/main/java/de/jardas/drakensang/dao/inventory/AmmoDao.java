package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.model.inventory.Ammo;


public class AmmoDao extends InventoryItemDao<Ammo> {
    private static final String[] FIELDS = {
            "Guid", "_ID", "_Level", "_Layers", "Transform", "Id", "Graphics",
            "AmmoGraphicsShort", "AmmoGraphicsMedium", "AmmoGraphicsLong",
            "Placeholder", "Physics", "Name", "LookAtText", "EquipmentType",
            "MaxStackCount", "StackCount", "Gew", "PickingRange", "Value",
            "AmmoCategory", "ScriptPreset", "ScriptOverride", "LimitedScript",
            "CanUse", "CanDestroy", "IconBrush", "LookAtUnidentified",
            "IdentificationTalent", "InfoUnIdentified", "InfoIdentified",
            "IdentificationDifficulty", "IsMagical", "IsIdentified", "Robable",
            "PickingHeight", "UseAnim", "SoundUI", "StorageGUID", "Lootable",
            "VelocityVector", "PhysicCategory", "StorageSlotId", "IsTradeItem",
            "InventoryType", "IsEquiped", "EquipmentSlotId", "SpellTargetOffset",
            "LocalizeLookAtText", "IsSplitting", "InfiniteStack", "UseTalent",
        };

    public AmmoDao() {
        super(Ammo.class, "_Instance_Ammo");
    }

    @Override
    protected String[] getFields() {
        return FIELDS;
    }
}
