package com.nidjo123.treasury.test;

import com.nidjo123.treasury.model.Transaction;
import com.nidjo123.treasury.model.dao.ItemDAO;
import com.nidjo123.treasury.model.dao.ItemDAOImpl;
import com.nidjo123.treasury.model.dao.TransactionDAO;
import com.nidjo123.treasury.model.dao.TransactionsDAOImpl;
import javafx.collections.ObservableList;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.Assert.*;


public class TestTransactionsDAO {
    private static TransactionDAO transactionDAO;
    private static ItemDAO itemDAO;

    @BeforeClass
    public static void setupDatabase() {
        transactionDAO = new TransactionsDAOImpl();
        itemDAO = new ItemDAOImpl();
    }

    @Test
    public void testAllTransactions() {
        ObservableList<Transaction> transactions = transactionDAO.getAllTransactions();

        assertTrue(transactions.size() > 0);
    }

    @Test
    public void testInsertion() {
        Transaction t1 = new Transaction();

        t1.setDate(LocalDate.now());
        t1.setDescription("Opis");
        t1.setIncome(BigDecimal.valueOf(95.99));
        t1.setItem(itemDAO.getItem("Milostinja"));
        t1.setExpense(BigDecimal.valueOf(10.99));
        t1.setRemark("Napomena");

        boolean saved = transactionDAO.saveTransaction(t1);

        assertTrue(saved);
        assertTrue(t1.getId() > 0);

        Transaction t2 = transactionDAO.getTransaction(t1.getId());

        assertNotNull(t2);

        assertTrue(t1.getId() > 0);
        assertTrue(t2.getId() > 0);
        assertEquals(t1.getId(), t2.getId());
        assertEquals(t1.getDate(), t2.getDate());
        assertEquals(t1.getDescription(), t2.getDescription());
        assertEquals(t1.getIncome(), t2.getIncome());
        assertEquals(t1.getExpense(), t2.getExpense());
        assertEquals(t1.getRemark(), t2.getRemark());
    }

    @Test
    public void testDelete() {
        Transaction transaction = new Transaction();

        transaction.setDate(LocalDate.now());
        transaction.setDescription("Opis");
        transaction.setIncome(BigDecimal.valueOf(95.99));
        transaction.setItem(itemDAO.getItem("Milostinja"));
        transaction.setExpense(BigDecimal.valueOf(10.99));
        transaction.setRemark("Napomena");

        boolean saved = transactionDAO.saveTransaction(transaction);

        assertTrue(saved);

        boolean deleted = transactionDAO.deleteTransaction(transaction);

        assertTrue(deleted);
        assertFalse(transactionDAO.transactionExists(transaction.getId()));
    }

    @Test
    public void testUpdate() {
        Transaction transaction = new Transaction();

        transaction.setDate(LocalDate.now());
        transaction.setDescription("Opis");
        transaction.setIncome(BigDecimal.valueOf(95.99));
        transaction.setItem(itemDAO.getItem("Milostinja"));
        transaction.setExpense(BigDecimal.valueOf(10.99));
        transaction.setRemark("Napomena");

        boolean saved = transactionDAO.saveTransaction(transaction);

        assertTrue(saved);

        LocalDate date = LocalDate.of(2017, 9, 22);
        transaction.setDate(date);
        transaction.setRemark("Nova napomena");

        boolean updated = transactionDAO.updateTransaction(transaction);

        assertTrue(updated);

        // check that it is updated
        Transaction updatedTransaction = transactionDAO.getTransaction(transaction.getId());

        assertTrue(updatedTransaction.getDate().equals(date));

        boolean deleted = transactionDAO.deleteTransaction(updatedTransaction);

        assertTrue(deleted);
    }
}
