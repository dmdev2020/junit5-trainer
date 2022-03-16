package com.dmdev.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.util.Properties;

@UtilityClass
public final class PropertiesUtil {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    @SneakyThrows
    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
