package de.jardas.drakensang.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;

public enum LocaleOption {
	GERMAN(Locale.GERMANY, "Folge dem Meister"),
	ENGLISH(Locale.UK, "");

	private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger
			.getLogger(LocaleOption.class);
	private final Locale locale;
	private final String translation;

	private LocaleOption(Locale locale, String translation) {
		this.locale = locale;
		this.translation = translation;
	}

	public boolean isMyLanguage(String test) {
		return translation.equalsIgnoreCase(test);
	}

	public Locale getLocale() {
		return locale;
	}

	public static Locale guessLocale() throws LocaleNotFoundException {
		try {
			final String msg = Messages
					.getRequired("00005f1c-2f82-4789-ac28-1b4b571576b3");

			for (LocaleOption option : values()) {
				if (option.isMyLanguage(msg)) {
					LOG.info("Test message '" + msg + "' resolved to locale '"
							+ option.getLocale() + "'.");

					return option.getLocale();
				}
			}
		} catch (MissingResourceException e) {
			// ignore
		}

		throw new LocaleNotFoundException();
	}

	public static Locale[] getAvailableLocales() {
		final List<Locale> locales = new ArrayList<Locale>();

		for (LocaleOption option : values()) {
			if (!locales.contains(option.getLocale())) {
				locales.add(option.getLocale());
			}
		}

		return locales.toArray(new Locale[locales.size()]);
	}

	public static class LocaleNotFoundException extends Exception {
		public LocaleNotFoundException() {
			super("Unable to determine locale");
		}
	}
}
