package de.jardas.drakensang.dao.inventory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.jardas.drakensang.dao.SavegameDao;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.shared.DrakensangException;
import de.jardas.drakensang.shared.model.inventory.Inventory;
import de.jardas.drakensang.shared.model.inventory.InventoryItem;


public class InventoryDao {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(InventoryDao.class);
    private static final Set<InventoryItemDao<?extends InventoryItem>> ITEM_DAOS =
        new HashSet<InventoryItemDao<?extends InventoryItem>>();

    static {
        ITEM_DAOS.add(new WeaponDao());
        ITEM_DAOS.add(new ShieldDao());
        ITEM_DAOS.add(new ArmorDao());
        ITEM_DAOS.add(new AmmoDao());
        ITEM_DAOS.add(new MoneyDao());
        ITEM_DAOS.add(new ItemDao());
        ITEM_DAOS.add(new JewelryDao());
        ITEM_DAOS.add(new KeyDao());
        ITEM_DAOS.add(new RecipeDao());
        ITEM_DAOS.add(new TorchDao());
    }

    public static List<InventoryItem> loadItems(byte[] storageGuid) {
        List<InventoryItem> items = new ArrayList<InventoryItem>();

        for (InventoryItemDao<?extends InventoryItem> dao : ITEM_DAOS) {
            items.addAll(loadItems(storageGuid, dao.getItemClass()));
        }

        return items;
    }

    private static Collection<InventoryItem> loadItems(byte[] storageGuid,
        Class<?extends InventoryItem> itemClass) {
        InventoryItemDao<?extends InventoryItem> dao = getInventoryItemDao(itemClass);
        final String sql = "select * from " + dao.getTable()
            + " where StorageGuid = ?";
        LOG.debug("Loading inventory for storage Guid "
            + Arrays.toString(storageGuid) + ": " + sql);

        try {
            PreparedStatement stmt = SavegameDao.getConnection()
                                                .prepareStatement(sql);
            stmt.setBytes(1, storageGuid);

            ResultSet results = stmt.executeQuery();
            List<InventoryItem> items = new ArrayList<InventoryItem>();

            while (results.next()) {
                try {
                    final InventoryItem item = dao.load(results);
                    items.add(item);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            return items;
        } catch (SQLException e) {
            throw new DrakensangException("Error loading items of type "
                + itemClass.getName() + " in storage Guid "
                + Arrays.toString(storageGuid) + ": " + e, e);
        }
    }

    public static void loadInventory(Character character)
        throws SQLException {
        final List<InventoryItem> items = loadItems(character.getGuid());

        for (InventoryItem item : items) {
            character.getInventory().getItems().add(item);
            item.setInventory(character.getInventory());
        }
    }

    public static List<Class<?extends InventoryItem>> getInventoryItemTypes() {
        final List<Class<?extends InventoryItem>> ret = new ArrayList<Class<?extends InventoryItem>>();

        for (InventoryItemDao<?extends InventoryItem> dao : ITEM_DAOS) {
            ret.add(dao.getItemClass());
        }

        return ret;
    }

    public static <I extends InventoryItem> List<I> loadInventory(
        Class<I> itemClass) {
        return getInventoryItemDao(itemClass).loadInventory();
    }

    private static <I extends InventoryItem> InventoryItemDao<I> getInventoryItemDao(
        Class<I> itemClass) {
        for (InventoryItemDao<?extends InventoryItem> dao : ITEM_DAOS) {
            if (dao.isApplicable(itemClass)) {
                @SuppressWarnings("unchecked")
                final InventoryItemDao<I> theDao = (InventoryItemDao<I>) dao;

                return theDao;
            }
        }

        throw new IllegalArgumentException("No DAO known for "
            + itemClass.getName() + ".");
    }

    public static void save(Inventory inventory) throws SQLException {
        for (InventoryItem item : inventory.getItems()) {
            getInventoryItemDao(item.getClass()).save(item);
        }
    }
}
