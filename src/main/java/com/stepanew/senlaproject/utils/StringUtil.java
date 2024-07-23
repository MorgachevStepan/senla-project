package com.stepanew.senlaproject.utils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtil {

    public static String trimToFirstDot(String input) {
        if (input == null) {
            return null;
        }

        int index = input.indexOf('.');

        if (index == -1) {
            return input;
        }

        return input.substring(index + 1, input.length() - 1);
    }

}
