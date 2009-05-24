package de.jardas.drakensang.gui.util;

import org.apache.commons.lang.WordUtils;


public class WordWrap {
    public static String addNewlines(String in) {
        return addNewlines(in, 80, "\n");
    }

    public static String addHtmlNewlines(String in) {
        return "<html>" + addNewlines(in, 80, "<br>") + "</html>";
    }

    private static String addNewlines(String in, int length, String nl) {
        return WordUtils.wrap(in, length, nl, false);
    }
}
