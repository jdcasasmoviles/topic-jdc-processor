package com.example.infrastructure.util;

public final class Constants {

    private Constants() {}

    public static final String API_VERSION = "v1";
    public static final String BASE_PATH = "/api/" + API_VERSION;

    public static class Kafka {
        public static final String TOPIC_JDC = "topic-jdc";
        public static final String GROUP_ID = "jdc-processor-group";
        public static final int RETRIES = 10;
        public static final int MAX_IN_FLIGHT = 5;
        public static final int BUFFER_SIZE = 1000;

        private Kafka() {}
    }

    public static class Errors {
        public static final String VALIDATION_FAILED = "VALIDATION_FAILED";
        public static final String PUBLISH_FAILED = "PUBLISH_FAILED";
        public static final String INTERNAL_ERROR = "INTERNAL_ERROR";
        public static final String NOT_FOUND = "NOT_FOUND";
        public static final String RATE_LIMIT_EXCEEDED = "RATE_LIMIT_EXCEEDED";
        public static final String TIMEOUT = "TIMEOUT";

        private Errors() {}
    }

    public static class Headers {
        public static final String CORRELATION_ID = "X-Correlation-ID";
        public static final String REQUEST_ID = "X-Request-ID";
        public static final String SOURCE = "X-Source";
        public static final String PRIORITY = "X-Priority";

        private Headers() {}
    }
}