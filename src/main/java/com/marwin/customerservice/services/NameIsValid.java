package com.marwin.customerservice.services;

import java.util.regex.Pattern;

public class NameIsValid {
    private static final String NAME_REGEX = "^[\\p{L}]+$";

    public static boolean isValidName(String name) {
        // Check if the name is not empty and contains only letters
        return name != null && !name.trim().isEmpty() && Pattern.matches(NAME_REGEX, name);
    }
}
