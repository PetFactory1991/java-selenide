// src/main/java/com/google/automation/config/EnvironmentConfig.java
package com.google.automation.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvironmentConfig {
    private static final Logger log = LoggerFactory.getLogger(EnvironmentConfig.class);
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        String env = System.getProperty("env", "dev").toLowerCase();
        String propertiesFile = "environments/" + env + ".properties";

        log.info("Загрузка конфигурации для окружения: {}", env);

        try (InputStream is = EnvironmentConfig.class.getClassLoader().getResourceAsStream(propertiesFile)) {
            if (is != null) {
                properties.load(is);
                log.info("Конфигурация загружена успешно");
            } else {
                log.warn("Файл конфигурации не найден: {}", propertiesFile);
                loadDefaultProperties();
            }
        } catch (IOException e) {
            log.error("Ошибка при загрузке конфигурации: {}", e.getMessage(), e);
            loadDefaultProperties();
        }
    }

    private static void loadDefaultProperties() {
        log.info("Загрузка конфигурации по умолчанию");

        try (InputStream is = EnvironmentConfig.class.getClassLoader().getResourceAsStream("environments/default.properties")) {
            if (is != null) {
                properties.load(is);
                log.info("Конфигурация по умолчанию загружена успешно");
            } else {
                log.error("Файл конфигурации по умолчанию не найден");
            }
        } catch (IOException e) {
            log.error("Ошибка при загрузке конфигурации по умолчанию: {}", e.getMessage(), e);
        }
    }

    public static String getBaseUrl() {
        return getProperty("base.url", "https://www.google.com/?hl=en");
    }

    public static String getBucketName() {
        return getProperty("aws.s3.bucket", "utyfdfrthby08bucket");
    }

    public static String getAWSRegion() {
        return getProperty("aws.region", "eu-central-1");
    }

    public static boolean isHeadless() {
        return Boolean.parseBoolean(getProperty("browser.headless", "false"));
    }

    public static int getTimeout() {
        return Integer.parseInt(getProperty("browser.timeout", "10000"));
    }

    public static String getBrowserSize() {
        return getProperty("browser.size", "1920x1080");
    }

    public static String getProperty(String key, String defaultValue) {
        // Сначала проверяем системные свойства (переданные через -D)
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }

        // Затем проверяем свойства из файла
        return properties.getProperty(key, defaultValue);
    }
}