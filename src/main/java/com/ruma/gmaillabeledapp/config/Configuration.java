package com.ruma.gmaillabeledapp.config;

import java.io.FileInputStream;
import java.util.Properties;

public class Configuration {
    private static final String FILE_PROPERTIES_PATH = "/config.properties";

    public static String GMAIL_USERNAME;
    public static String GMAIL_PASSWORD;

    private static Properties properties;

    public static void start() throws Exception {
        if (properties == null) {
            properties = new Properties();
            properties.load(new FileInputStream(Configuration.class.getResource(FILE_PROPERTIES_PATH).toURI().getPath()));
            setProperties();
        }
    }

    private static void setProperties() {
        GMAIL_USERNAME = properties.getProperty("gmail.user");
        GMAIL_PASSWORD = properties.getProperty("gmail.password");
    }
}
