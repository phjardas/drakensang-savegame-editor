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
