package com.marwin.customerservice.services;

import java.util.regex.Pattern;

public class PhoneNumberIsValid {
    private static final String PHONE_NUMBER_REGEX = "^\\+380\\d{9}$";

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && Pattern.matches(PHONE_NUMBER_REGEX, phoneNumber);
    }
}
