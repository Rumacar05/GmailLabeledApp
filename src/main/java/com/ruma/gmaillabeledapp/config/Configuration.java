package com.ruma.gmaillabeledapp.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Configuration {
    private static final String FILE_PROPERTIES_PATH = "/config.properties";

    public static String GMAIL_USERNAME;
    public static String GMAIL_PASSWORD;

    private static Properties properties;

    public static void start() throws Exception {
        if (properties == null) {
            properties = new Properties();
            try (InputStream inputStream = Configuration.class.getResourceAsStream(FILE_PROPERTIES_PATH)) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Cannot find properties file: " + FILE_PROPERTIES_PATH);
                }
                properties.load(inputStream);
                setProperties();
            }
        }
    }

    private static void setProperties() {
        GMAIL_USERNAME = properties.getProperty("gmail.user");
        GMAIL_PASSWORD = properties.getProperty("gmail.password");
    }
}
