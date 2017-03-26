package com.intive.patronage.toz.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringFormatter {
    public String trimToLengthPreserveWord(String string, Integer length) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(".{1,").append(length).append("}(?:\\s|$)");
        Pattern regex = Pattern.compile(stringBuilder.toString(), Pattern.DOTALL);
        Matcher regexMatcher = regex.matcher(string);
        regexMatcher.find();
        return regexMatcher.group();
    }
}
