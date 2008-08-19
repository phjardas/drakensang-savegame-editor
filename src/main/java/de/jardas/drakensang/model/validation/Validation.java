/*
 * Validation.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang.model.validation;

import de.jardas.drakensang.model.Character;


public interface Validation {
    ValidationResult validate(Character character);
}
