package de.jardas.drakensang.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
public enum LocaleOption {
	ENGLISH(Locale.UK, "Farewell."),
    GERMAN(Locale.GERMANY, "Gehabt Euch wohl."),
    POLISH(new Locale("pl"), "Bywaj.");

    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(LocaleOption.class);
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
        final String msg = Messages.getRequired(
                "0482f6e4-2ca5-4d3b-a236-97cb5eb38526");

        for (LocaleOption option : values()) {
            if (option.isMyLanguage(msg)) {
                LOG.info("Test message '" + msg + "' resolved to locale '" +
                    option.getLocale() + "'.");

                return option.getLocale();
            }
        }

        throw new LocaleNotFoundException(msg);
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
        private final String testMessage;

        public LocaleNotFoundException(String testMessage) {
            super("Unable to determine locale from test message '" +
                testMessage + "'.");
            this.testMessage = testMessage;
        }

        public String getTestMessage() {
            return testMessage;
        }
    }
}