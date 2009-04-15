package de.jardas.drakensang.dao;

import de.jardas.drakensang.DrakensangException;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.Arrays;
import java.util.Random;


public class Guid {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
        .getLogger(Guid.class);
    private static final String[] TABLES = {
            "_Instance_ActionDummy", "_Instance_AmbienceBubble",
            "_Instance_Ammo", "_Instance_Annotation",
            "_Instance_AreaSpellTrigger", "_Instance_Armor", "_Instance_Backup",
            "_Instance_Blockade", "_Instance_Book", "_Instance_BookStand",
            "_Instance_Camera", "_Instance_Chest", "_Instance_CutsceneCamera",
            "_Instance_DestroyableEntity", "_Instance_Door",
            "_Instance_EffectBox", "_Instance_EncounterTrigger",
            "_Instance_Entry", "_Instance_Exit", "_Instance_ExitObject",
            "_Instance_FlimFlamFunkelLight", "_Instance_GardianumReflector",
            "_Instance_GroundTypeBox", "_Instance_Herb", "_Instance_Item",
            "_Instance_Jewelry", "_Instance_Key", "_Instance_MonsterLarge",
            "_Instance_Lights", "_Instance_LocationButton",
            "_Instance_MapSegment", "_Instance_MapSegmentArea",
            "_Instance_MarkerPoint", "_Instance_Money", "_Instance_Monster",
            "_Instance_MusicTrigger", "_Instance_NPC", "_Instance_Object",
            "_Instance_PathWaypoint", "_Instance_PC", "_Instance_PC_CharWizard",
            "_Instance_PostEffects", "_Instance_Recipe",
            "_Instance_RemoteCamera", "_Instance_Shield",
            "_Instance_SoundObject", "_Instance_SpawnPoint",
            "_Instance_StateObject", "_Instance_Switch", "_Instance_TalkPoint",
            "_Instance_Torch", "_Instance_Trap", "_Instance_Tree",
            "_Instance_Trigger", "_Instance_VisibilityBox", "_Instance_Weapon",
            "_Instance_WorkBench", "_Instance_WorldMapCamera",
            "_Instance_WorldMapCameraBox", "_Instance_WorldMapPath",
            "_Instance_WorldMapPlayerParty", "_Instance_Light",
            "_Instance__Environment", "TrapTable",
        };
    private static final Random RANDOM = new Random();

    public static byte[] generateGuid() {
        while (true) {
            byte[] id = new byte[16];
            RANDOM.nextBytes(id);

            if (isAvailable(id)) {
                LOG.debug("Generated Guid: " + Arrays.toString(id));

                return id;
            }

            LOG.debug("Guid already exists: " + Arrays.toString(id));
        }
    }

    private static boolean isAvailable(byte[] id) {
        LOG.debug("Validating availability of Guid " + Arrays.toString(id)
            + ".");

        try {
            StringBuffer sql = new StringBuffer();

            for (String table : TABLES) {
                if (sql.length() > 0) {
                    sql.append(" union ");
                }

                sql.append("select Guid from ").append(table);
            }

            String query = "select count(*) from (" + sql + ") where Guid = ?";
            PreparedStatement stmt = SavegameDao.getConnection()
                                                .prepareStatement(query);
            stmt.setBytes(1, id);

            ResultSet result = stmt.executeQuery();

            if (!result.next()) {
                return true;
            }

            return result.getInt(1) == 0;
        } catch (SQLException e) {
            throw new DrakensangException("Error validating Guid "
                + Arrays.toString(id) + ": " + e, e);
        }
    }
}
