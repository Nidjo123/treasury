package com.nidjo123.treasury.model.dao;

import com.nidjo123.treasury.model.Item;
import javafx.collections.ObservableList;

/**
 * Defines interface that every Item Data Access Object should implement.
 * Contains methods for fetching items by id and name, fetching all items, checking for item
 * existence and deleting items.
 */
public interface ItemDAO {
    /**
     * Returns Item instance associated with provided integer id.
     *
     * @param id Integer id of item.
     * @return Returns instance of Item which has provided id.
     */
    Item getItem(Integer id);

    /**
     * Returns Item instance associated with provided String name.
     *
     * @param name String name of item.
     * @return Returns instance of Item which has provided name.
     */
    Item getItem(String name);

    /**
     * Returns all items from database in ObservableList.
     *
     * @return Returns all items currently in the database packed in ObservableList.
     */
    ObservableList<Item> getAllItems();

    /**
     * Saves provided item in the database and returns the status. If returned value is
     * true, everything was ok, and if false was returned, there was an error.
     *
     * @param item Instance of Item to save in the database.
     * @return Returns true of everything was ok, false if there was a problem.
     */
    boolean saveItem(Item item);

    /**
     * Deletes item from the database. Returns true if operation was successful, and false otherwise.
     *
     * @param item instance of Item to remove from database. Item with provided id and name should exist in the database.
     * @return Returns true if operation was successfully executed, and false if there was a problem.
     */
    boolean deleteItem(Item item);

    /**
     * Updates item that already exists in the database which has the same id.
     *
     * @param item Item to update. Item with rovided id must already exist in the database.
     * @return Returns true if item was successfully updated, and false if there was a problem.
     */
    boolean updateItem(Item item);

    /**
     * Checks if item with provided integer id exists and returns true if it does, and false otherwise.
     *
     * @param id id of item to check.
     * @return Returns true if item with provided id exists, and false otherwise.
     */
    boolean itemExists(Integer id);

    /**
     * Checks if item with provided name exists and returns true if it does, and false otherwise.
     *
     * @param name name of the item in form of string.
     * @return Returns true if item with provided name exists, and false otherwise.
     */
    boolean itemExists(String name);
}
