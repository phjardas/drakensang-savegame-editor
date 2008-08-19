/*
 * ValidationResult.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.model.validation;

import de.jardas.drakensang.dao.Messages;
import de.jardas.drakensang.model.Character;

import java.text.MessageFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ValidationResult {
    private final Map<Character, List<Message>> messages = new HashMap<Character, List<Message>>();

    public Map<Character, List<Message>> getMessages() {
        return this.messages;
    }

    public List<Message> getMessage(Character character) {
        final List<Message> items = getMessages().get(character);

        return (items != null) ? items : new ArrayList<Message>(0);
    }

    public boolean containsErrors() {
        for (List<Message> messages : getMessages().values()) {
            for (Message message : messages) {
                if (message.getPriority().ordinal() >= Priority.ERROR.ordinal()) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean containsWarnings() {
        for (List<Message> messages : getMessages().values()) {
            for (Message message : messages) {
                if (message.getPriority().ordinal() >= Priority.WARNING.ordinal()) {
                    return true;
                }
            }
        }

        return false;
    }

    public void addInfo(Character character, String message,
        Object... parameters) {
        addMessage(character, Priority.INFO, message, parameters);
    }

    public void addWarning(Character character, String message,
        Object... parameters) {
        addMessage(character, Priority.WARNING, message, parameters);
    }

    public void addError(Character character, String message,
        Object... parameters) {
        addMessage(character, Priority.ERROR, message, parameters);
    }

    private void addMessage(Character character, Priority priority,
        String message, Object... parameters) {
        final Message msg = new Message(character, priority, message, parameters);
        addMessage(msg);
    }

    private void addMessage(final Message msg) {
        List<Message> list = this.messages.get(msg.getCharacter());

        if (list == null) {
            list = new ArrayList<Message>();
            this.messages.put(msg.getCharacter(), list);
        }

        list.add(msg);
    }

    public void merge(ValidationResult result) {
        for (List<Message> messages : result.getMessages().values()) {
            for (Message message : messages) {
                addMessage(message);
            }
        }
    }

    @Override
    public String toString() {
        return getMessages().toString();
    }

    public static class Message {
        private final Character character;
        private final String key;
        private final Priority priority;
        private final Object[] parameters;

        public Message(Character character, Priority priority, String key,
            Object... parameters) {
            super();
            this.character = character;
            this.key = key;
            this.priority = priority;
            this.parameters = parameters;
        }

        public String getKey() {
            return this.key;
        }

        public Priority getPriority() {
            return this.priority;
        }

        public Object[] getParameters() {
            return this.parameters;
        }

        public Character getCharacter() {
            return this.character;
        }

        @Override
        public String toString() {
            return MessageFormat.format(Messages.get(key), parameters);
        }
    }
    public static enum Priority {INFO, WARNING, ERROR;
    }
}
