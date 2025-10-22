package com.example.Atai_Ibragimov.config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ServerConfig {
    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = ServerConfig.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                System.out.println("application.properties не найден. Использую значения по умолчанию.");
                setDefaultProperties();
            } else {

                properties.load(new java.io.InputStreamReader(input, StandardCharsets.UTF_8));
                System.out.println("application.properties успешно загружен");
            }
        } catch (IOException ex) {
            System.err.println("Ошибка загрузки application.properties: " + ex.getMessage());
            setDefaultProperties();
        } catch (Exception ex) {
            System.err.println("Неожиданная ошибка: " + ex.getMessage());
            setDefaultProperties();
        }
    }

    private static void setDefaultProperties() {
        properties.setProperty("server.port", "9090");
        properties.setProperty("server.host", "localhost");
        properties.setProperty("lalafo.base-url", "https://lalafo.kg");
        properties.setProperty("lalafo.max-adverts", "100");
        properties.setProperty("lalafo.timeout", "10000");

    }

    public static int getServerPort() {
        try {
            return Integer.parseInt(properties.getProperty("server.port", "9090"));
        } catch (NumberFormatException e) {
            System.err.println("Ошибка парсинга порта, использую 9090");
            return 9090;
        }
    }

    public static String getServerHost() {
        return properties.getProperty("server.host", "localhost");
    }

    public static String getLalafoBaseUrl() {
        return properties.getProperty("lalafo.base-url", "https://lalafo.kg");
    }

    public static int getMaxAdverts() {
        try {
            return Integer.parseInt(properties.getProperty("lalafo.max-adverts", "100"));
        } catch (NumberFormatException e) {
            System.err.println("Ошибка парсинга max-adverts, использую 100");
            return 100;
        }
    }

    public static int getTimeout() {
        try {
            return Integer.parseInt(properties.getProperty("lalafo.timeout", "10000"));
        } catch (NumberFormatException e) {
            System.err.println("Ошибка парсинга timeout, использую 10000");
            return 10000;
        }
    }

    public static String getAppName() {
        return properties.getProperty("app.name", "Atai Ibragimov Lalafo Parser");
    }
}