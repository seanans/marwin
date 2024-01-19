package com.marwin.customerservice.services;

import java.util.regex.Pattern;

public class EmailValid {
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

    public static boolean isValidEmail(String email) {
        // Check if the name is not empty and contains only letters
        return email != null && !email.trim().isEmpty() && Pattern.matches(EMAIL_REGEX, email);
    }

}
