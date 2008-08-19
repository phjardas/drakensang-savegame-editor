/*
 * ValidationFrame.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.gui;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.Character;
import de.jardas.drakensang.model.validation.ValidationResult;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import java.text.Collator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class ValidationFrame extends JFrame {
    private final ValidationResult results;
    private final JTabbedPane tabs = new JTabbedPane();

    public ValidationFrame(final ValidationResult results)
        throws HeadlessException {
        super();
        this.results = results;

        init();
        pack();
    }

    private void init() {
        setLayout(new BorderLayout());
        add(new JLabel("Test"), BorderLayout.NORTH);
        add(tabs, BorderLayout.CENTER);

        List<Character> characters = new ArrayList<Character>(results.getMessages()
                                                                     .keySet());
        Collections.sort(characters,
            new Comparator<Character>() {
                private final Collator collator = Collator.getInstance();

                public int compare(Character o1, Character o2) {
                    return collator.compare(getCharacterName(o1),
                        getCharacterName(o2));
                }
            });

        for (Character character : characters) {
            JPanel tab = new JPanel();
            tabs.addTab(getCharacterName(character), tab);
        }
    }

    private String getCharacterName(Character character) {
        return character.isLocalizeLookAtText()
        ? Messages.get(character.getLookAtText()) : character.getLookAtText();
    }
}
