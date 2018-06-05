package com.nidjo123.treasury.util;

import com.nidjo123.treasury.Main;

import java.io.IOException;
import java.util.Properties;

public class Config {
    private static Config instance = null;

    private Properties properties;

    private Config() {
        properties = new Properties();
        try {
            properties.load(Main.class.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            instance = new Config();
        }

        return instance;
    }

    public String get(String property) {
        return properties.getProperty(property);
    }

    public Properties getProperties() {
        return properties;
    }
}
