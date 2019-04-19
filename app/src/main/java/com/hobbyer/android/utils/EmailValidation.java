package com.hobbyer.android.utils;

public class EmailValidation {



        private static String EMAIL_PATTERN = "[a-zA-Z0-9_-]+@[a-z]{2,50}+\\.+[a-z]{2,3}+";
        private static String EMAIL_PATTERN_With_LIMIT = "[a-zA-Z0-9_-]+@[a-z]{2,50}+\\.+[a-z]{2,3}+\\.+[a-z]{2,3}+";

        public static boolean validEmail(final String email) {
            return email.matches(EMAIL_PATTERN) || email.matches(EMAIL_PATTERN_With_LIMIT) ? true : false;
        }
    }



