package com.nidjo123.treasury.model.dao;

import com.nidjo123.treasury.model.Item;
import com.nidjo123.treasury.model.Transaction;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * Defines interface that every Transaction Data Access Object should implement.
 * This includes functions for loading all transactions, fetching single transaction based on certain attributes,
 * saving transactions, updating transactions, and deleting transactions.
 */
public interface TransactionDAO {

    /**
     * Returns transaction object with provided id. Every transaction has a unique id.
     *
     * @param id id of the transaction.
     * @return Instance of Transaction with a given id loaded from database.
     */
    Transaction getTransaction(int id);

    /**
     * Returns all transactions from the database.
     *
     * @return Returns {@link ObservableList} containing all transactions currently present in database.
     */
    ObservableList<Transaction> getAllTransactions();

    /**
     * Returns all transactions associated with provided item.
     *
     * @param item item associated with transactions to be returned.
     * @return Returns {@link ObservableList} containing all transactions associated with provided item.
     */
    ObservableList<Transaction> getTransactionsByItem(Item item);

    /**
     * Returns all transactions whose execution date is in range [startDate, endDate].
     *
     * @param startDate start date (inclusive).
     * @param endDate   end date (inclusive).
     * @return Returns {@link ObservableList} containing all transactions whose execution date is in provided range [startDate, endDate].
     */
    ObservableList<Transaction> getTransactionsByDate(LocalDate startDate, LocalDate endDate);

    /**
     * Saves transaction to database.
     *
     * @param transaction transaction object to save.
     * @return Returns true if transaction was successfully saved, false otherwise.
     */
    boolean saveTransaction(Transaction transaction);

    /**
     * Updates transaction with associated id with data provided in the transaction object.
     *
     * @param transaction transaction object containing new data. Transaction with same id in database will be updated.
     * @return Return true if transaction was successfully updated, false otherwise.
     */
    boolean updateTransaction(Transaction transaction);

    /**
     * Deletes transaction from database.
     *
     * @param transaction transaction object to delete.
     * @return Returns true if transaction was successfully deleted from database, false otherwise.
     */
    default boolean deleteTransaction(Transaction transaction) {
        return deleteTransaction(transaction.getId());
    }

    /**
     * Deletes transaction with provided id from database, if it exists.
     *
     * @param id integer id of the transaction to remove.
     * @return Returns true if transaction with provided id was successfully removed from database, false otherwise. Also returns false if id does not exist in database.
     */
    boolean deleteTransaction(int id);

    /**
     * Returns true if transaction with provided integer id exists.
     *
     * @param id id to check.
     * @return Returns true if transaction with provided id exists in database.
     */
    default boolean transactionExists(int id) {
        return getTransaction(id) != null;
    }
}
