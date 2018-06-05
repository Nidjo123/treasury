package com.nidjo123.treasury.db;

import com.nidjo123.treasury.Main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {
    private static Connection connection;
    private static String url;
    private static Properties properties;

    private Database() {
    }

    static {
        try {
            properties = new Properties();

            properties.load(Main.class.getResourceAsStream("config.properties"));

            String dbms = properties.getProperty("dbms");
            String host = properties.getProperty("host");
            String database = properties.getProperty("db");

            url = "jdbc:" + dbms + "://" + host + "/" + database;

            connection = DriverManager.getConnection(url, properties);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed() || !connection.isValid(0)) {
                connection = DriverManager.getConnection(url, properties);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connection;
    }
}
