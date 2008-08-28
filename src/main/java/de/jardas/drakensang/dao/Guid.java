/*
 * Guid.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.dao;

import de.jardas.drakensang.DrakensangException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
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
    private static List<byte[]> guids;

    private static List<byte[]> getGuids() {
        if (guids == null) {
            guids = collectIds();
        }

        return guids;
    }

    public static byte[] generateGuid() {
        while (true) {
            byte[] id = new byte[16];
            RANDOM.nextBytes(id);

            if (!getGuids().contains(id)) {
                LOG.debug("Generated Guid: " + Arrays.toString(id));
                getGuids().add(id);

                return id;
            }

            LOG.debug("Guid already exists: " + Arrays.toString(id));
        }
    }

    private static List<byte[]> collectIds() {
        List<byte[]> ids = new ArrayList<byte[]>();

        for (String table : TABLES) {
            try {
                ids.addAll(collectIds(table));
            } catch (SQLException e) {
                throw new DrakensangException(
                    "Error loading Guids from table '" + table + "': " + e, e);
            }
        }

        LOG.debug("Got a total of " + ids.size() + " Guids.");

        Collections.sort(ids,
            new Comparator<byte[]>() {
                public int compare(byte[] o1, byte[] o2) {
                    if (o1.length > o2.length) {
                        return 1;
                    }

                    if (o1.length < o2.length) {
                        return -1;
                    }

                    for (int i = 0; i < o1.length; i++) {
                        if (o1[i] > o2[i]) {
                            return 1;
                        }

                        if (o1[i] < o2[i]) {
                            return -1;
                        }
                    }

                    return 0;
                }
            });

        byte[] previous = null;

        for (byte[] id : ids) {
            if ((previous != null) && previous.equals(id)) {
                throw new DrakensangException("Duplicate Guid found: "
                    + Arrays.toString(id));
            }

            previous = id;
        }

        return ids;
    }

    private static List<byte[]> collectIds(String table)
        throws SQLException {
        Statement stmt = SavegameDao.getConnection().createStatement();
        ResultSet result = stmt.executeQuery("select Guid from " + table);
        List<byte[]> ids = new ArrayList<byte[]>();

        while (result.next()) {
            ids.add(result.getBytes("Guid"));
        }

        LOG.debug("Loaded " + ids.size() + " Guids from table '" + table + "'.");

        return ids;
    }

    public static void reset() {
        guids = null;
    }
}
