package com.devandre.mediumclone.utils;

/**
 * @author Andre on 19/02/2024
 * @project medium-clone
 */
public class Constants {

    public static final String API_BASE_PATH = "/api/v1/";

    public static final String TEST = "The %s field should not be blank";

    private Constants() {
        throw new AssertionError("Constants class should not be instantiated.");
    }

    public static final class ErrorMessages {

        public static final String MALFORMED_JSON_REQUEST = "Malformed JSON request";
        public static final String WRITABLE_ERROR = "Error writing JSON output";
        public static final String METHOD_NOT_FOUND = "Could not find the %s method for URL %s";
        public static final String DATABASE_ERROR = "Database error";
        public static final String VALIDATION_MESSAGE = "Validation error. Check 'errors' field for details.";

        private ErrorMessages() {
            throw new AssertionError("ErrorMessages class should not be instantiated.");
        }
    }

    public static final class DB {
        public static final String SCHEMA = "bo_events";
        private DB() {
            throw new AssertionError("DB class should not be instantiated.");
        }
    }

    public static final class ValidationMessages {
        public static final String NOT_BLANK_MESSAGE = "The name field should not be blank";

        private ValidationMessages() {
            throw new AssertionError("ValidationMessages class should not be instantiated.");
        }
    }
}
