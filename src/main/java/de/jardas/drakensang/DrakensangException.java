/*
 * DrakensangException.java
 *
 * Version $Revision$ $Date$
 *
 * This file is part of the Abu Dhabi eGovernment Portal.
 *
 * Copyright 2007-2008 ]init[ AG, Berlin, Germany.
 */
package de.jardas.drakensang;

public class DrakensangException extends RuntimeException {
	public DrakensangException() {
		super();
	}

	public DrakensangException(String message, Throwable cause) {
		super(message, cause);
	}

	public DrakensangException(String message) {
		super(message);
	}

	public DrakensangException(Throwable cause) {
		super(cause);
	}
}
