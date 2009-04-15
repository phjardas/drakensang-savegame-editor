package de.jardas.drakensang.dao.inventory;

import de.jardas.drakensang.model.inventory.Recipe;


public class RecipeDao extends InventoryItemDao<Recipe> {
    private static final String[] FIELDS = {
            "Guid", "_ID", "_Level", "_Layers", "Transform", "Id",
            "InfoIdentified", "Name", "RecipeIngredients", "Difficulty",
            "RecipeResult", "TaAttr", "Graphics", "Placeholder", "Physics",
            "LookAtText", "Value", "Gew", "PickingRange", "CanUse", "CanDestroy",
            "IconBrush", "Robable", "PickingHeight", "UseAnim", "ScriptPreset",
            "ScriptOverride", "StorageGUID", "Lootable", "VelocityVector",
            "PhysicCategory", "StorageSlotId", "SoundUI", "IsTradeItem",
            "UseTalent", "LocalizeLookAtText", "LookAtUnidentified",
            "InfoUnIdentified", "IdentificationTalent",
            "IdentificationDifficulty", "IsIdentified", "IsMagical",
            "LimitedScript",
        };

    public RecipeDao() {
        super(Recipe.class, "_Instance_Recipe");
    }

    @Override
    protected String[] getFields() {
        return FIELDS;
    }
}
