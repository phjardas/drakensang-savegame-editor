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

        try {
            Statement stmt = SavegameDao.getConnection().createStatement();
            ResultSet result = stmt.executeQuery(
                    "select name from sqlite_master where type = 'table'");

            while (result.next()) {
                String table = result.getString("name");

                try {
                    ids.addAll(collectIds(table));
                } catch (SQLException e) {
                    LOG.debug("Error loading IDs from table '" + table + "': "
                        + e);
                }
            }

            LOG.debug("Got a total of " + ids.size() + " IDs.");
        } catch (SQLException e) {
            throw new DrakensangException("Error collecting IDs: " + e, e);
        }

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
        LOG.debug("Loading IDs from table '" + table + "'.");

        Statement stmt = SavegameDao.getConnection().createStatement();
        ResultSet result = stmt.executeQuery("select Guid from " + table);
        List<byte[]> ids = new ArrayList<byte[]>();

        while (result.next()) {
            ids.add(result.getBytes("Guid"));
        }

        LOG.debug("Loaded " + ids.size() + " IDs from table '" + table + "'.");

        return ids;
    }

    public static void reset() {
        guids = null;
    }
}
