package com.nidjo123.treasury.test;

import com.nidjo123.treasury.model.Item;
import com.nidjo123.treasury.model.dao.ItemDAO;
import com.nidjo123.treasury.model.dao.ItemDAOImpl;
import javafx.collections.ObservableList;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestItemsDAO {

    private static ItemDAO itemDAO;

    @BeforeClass
    public static void setupDatabase() {
        itemDAO = new ItemDAOImpl();
    }

    @Test
    public void testAllItems() {
        ObservableList<Item> items = itemDAO.getAllItems();

        assertTrue(items.size() > 0);
    }

    @Test
    public void testExistence() {
        boolean shouldExist = itemDAO.itemExists("Milostinja");
        boolean shouldNotExist = itemDAO.itemExists("Pizza");

        assertTrue(shouldExist);
        assertFalse(shouldNotExist);

        shouldExist = itemDAO.itemExists(6);
        shouldNotExist = itemDAO.itemExists(-1);

        assertTrue(shouldExist);
        assertFalse(shouldNotExist);

        shouldNotExist = itemDAO.itemExists(314);

        assertFalse(shouldNotExist);
    }

    @Test
    public void testSavingAndDeletingItems() {
        Item item = new Item(0, "Jamnica");

        boolean saved = itemDAO.saveItem(item);

        assertTrue(saved);

        boolean deleted = itemDAO.deleteItem(item);

        assertTrue(deleted);
    }

    @Test
    public void testUpdatingItem() {
        Item item = itemDAO.getItem("Milostinja");

        assertEquals("Milostinja", item.getName());

        item.setName("Milostinja2");

        boolean updated = itemDAO.updateItem(item);

        assertTrue(updated);

        boolean shouldNotExist = itemDAO.itemExists("Milostinja");
        boolean shouldExist = itemDAO.itemExists(item.getId());

        assertFalse(shouldNotExist);
        assertTrue(shouldExist);

        shouldExist = itemDAO.itemExists("Milostinja2");

        assertTrue(shouldExist);

        item.setName("Milostinja");

        updated = itemDAO.updateItem(item);

        assertTrue(updated);
    }
}
