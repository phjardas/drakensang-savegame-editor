/*
 * Validator.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.model.validation;

import de.jardas.drakensang.model.Character;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class Validator implements Validation {
    private final List<Validation> validations = new ArrayList<Validation>();

    public Validator() {
        validations.add(new AttributesValidation());
    }

    public ValidationResult validate(Character character) {
        ValidationResult result = new ValidationResult();

        for (Validation validation : validations) {
            result.merge(validation.validate(character));
        }

        return result;
    }

    public ValidationResult validate(Collection<Character> characters) {
        ValidationResult result = new ValidationResult();

        for (Character character : characters) {
            result.merge(validate(character));
        }

        return result;
    }
}
