package com.nidjo123.treasury.model.dao;

import com.nidjo123.treasury.db.Database;
import com.nidjo123.treasury.model.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.postgresql.util.PSQLException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ItemDAOImpl implements ItemDAO {
    /**
     * Returns Item instance associated with provided integer id.
     *
     * @param id Integer id of item.
     * @return Returns instance of Item which has provided id.
     */
    @Override
    public Item getItem(Integer id) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM items WHERE id = ?");) {
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Item item = new Item();

                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));

                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns Item instance associated with provided String name.
     *
     * @param name String name of item.
     * @return Returns instance of Item which has provided name.
     */
    @Override
    public Item getItem(String name) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM items WHERE name = ?");) {
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                Item item = new Item();

                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));

                return item;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns all items from database in ObservableList.
     *
     * @return Returns all items currently in the database packed in ObservableList.
     */
    @Override
    public ObservableList<Item> getAllItems() {
        ObservableList<Item> items = FXCollections.observableArrayList();

        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM items ORDER BY name ASC, id ASC");) {
            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                Item item = new Item();
                item.setId(rs.getInt("id"));
                item.setName(rs.getString("name"));

                items.add(item);
            }

            return items;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Saves provided item in the database and returns the status. If returned value is
     * true, everything was ok, and if false was returned, there was an error.
     *
     * @param item Instance of Item to save in the database.
     * @return Returns true of everything was ok, false if there was a problem.
     */
    @Override
    public boolean saveItem(Item item) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO items(name) VALUES (?) RETURNING id");) {
            statement.setString(1, item.getName());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                item.setId(id);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getSQLState());
        }

        return false;
    }

    /**
     * Deletes item from the database. Returns true if operation was successful, and false otherwise.
     *
     * @param item instance of Item to remove from database. Item with provided id and name should exist in the database.
     * @return Returns true if operation was successfully executed, and false if there was a problem.
     */
    @Override
    public boolean deleteItem(Item item) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM items WHERE id = ? AND name = ?");) {
            int id = item.getId();
            String name = item.getName();

            statement.setInt(1, id);
            statement.setString(2, name);

            int deleted = statement.executeUpdate();

            if (deleted == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates item that already exists in the database which has the same id.
     *
     * @param item Item to update. Item with rovided id must already exist in the database.
     * @return Returns true if item was successfuly updated, and false if there was a problem.
     */
    @Override
    public boolean updateItem(Item item) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("UPDATE items SET name = ? WHERE id = ?");) {
            statement.setString(1, item.getName());
            statement.setInt(2, item.getId());

            int updated = statement.executeUpdate();

            if (updated == 1) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Checks if item with provided integer id exists and returns true if it does, and false otherwise.
     *
     * @param id id of item to check.
     * @return Returns true if item with provided id exists, and false otherwise.
     */
    @Override
    public boolean itemExists(Integer id) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM items WHERE id = ?");) {
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Checks if item with provided name exists and returns true if it does, and false otherwise.
     *
     * @param name name of the item in form of string.
     * @return Returns true if item with provided name exists, and false otherwise.
     */
    @Override
    public boolean itemExists(String name) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM items WHERE name = ?");) {
            statement.setString(1, name);

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return false;
    }
}
