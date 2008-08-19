/*
 * AttributesValidation.java
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


public class AttributesValidation implements Validation {
    private static final int MAX = 40;

    public ValidationResult validate(Character character) {
        ValidationResult result = new ValidationResult();

        for (String key : character.getAttribute().getKeys()) {
            final int value = character.getAttribute().get(key);

            if (value > MAX) {
                result.addWarning(character, "validation.attributes.max",
                    Messages.get(key), MAX, value);
            }
        }

        return result;
    }
}
