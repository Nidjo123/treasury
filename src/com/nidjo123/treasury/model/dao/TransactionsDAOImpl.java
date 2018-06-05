package com.nidjo123.treasury.model.dao;

import com.nidjo123.treasury.db.Database;
import com.nidjo123.treasury.model.Item;
import com.nidjo123.treasury.model.Transaction;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;

/**
 * Class that implements Transaction Data Access Object interface.
 * It contains methods for getting, deleting, saving, and updating transactions.
 */
public class TransactionsDAOImpl implements TransactionDAO {
    private ItemDAO itemDAO;

    public TransactionsDAOImpl() {
        this.itemDAO = new ItemDAOImpl();
    }

    /**
     * Returns transaction object with provided id. Every transaction has a unique id.
     *
     * @param id id of the transaction.
     * @return Instance of Transaction with a given id loaded from database.
     */
    @Override
    public Transaction getTransaction(int id) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            return resultSetToTransaction(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Returns all transactions from the database.
     *
     * @return Returns {@link ObservableList} containing all transactions currently present in database.
     */
    @Override
    public ObservableList<Transaction> getAllTransactions() {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions ORDER BY date ASC, item_id ASC", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = statement.executeQuery();

            return resultSetToMultiTransactions(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.emptyObservableList();
    }

    /**
     * Returns all transactions associated with provided item.
     *
     * @param item item associated with transactions to be returned.
     * @return Returns {@link ObservableList} containing all transactions associated with provided item.
     */
    @Override
    public ObservableList<Transaction> getTransactionsByItem(Item item) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions WHERE item_id = ? ORDER BY date ASC", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            ResultSet rs = statement.executeQuery();

            return resultSetToMultiTransactions(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.emptyObservableList();
    }

    /**
     * Returns all transactions whose execution date is in range [startDate, endDate].
     *
     * @param startDate start date (inclusive).
     * @param endDate   end date (inclusive).
     * @return Returns {@link ObservableList} containing all transactions whose execution date is in provided range [startDate, endDate].
     */
    @Override
    public ObservableList<Transaction> getTransactionsByDate(LocalDate startDate, LocalDate endDate) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM transactions WHERE date >= ? AND date <= ? ORDER BY date ASC", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setDate(1, Date.valueOf(startDate));
            statement.setDate(2, Date.valueOf(endDate));

            ResultSet rs = statement.executeQuery();

            return resultSetToMultiTransactions(rs);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return FXCollections.emptyObservableList();
    }

    /**
     * Saves transaction to database.
     *
     * @param transaction transaction object to save.
     * @return Returns true if transaction was successfully saved, false otherwise.
     */
    @Override
    public boolean saveTransaction(Transaction transaction) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("INSERT INTO transactions(date, item_id, description, income, expense, remark) VALUES (?, ?, ?, ?, ?, ?) RETURNING id", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setDate(1, Date.valueOf(transaction.getDate()));
            statement.setInt(2, transaction.getItem_id());
            statement.setString(3, transaction.getDescription());
            statement.setBigDecimal(4, transaction.getIncome());
            statement.setBigDecimal(5, transaction.getExpense());
            statement.setString(6, transaction.getRemark());

            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                transaction.setId(rs.getInt("id"));

                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Updates transaction with associated id with data provided in the transaction object.
     *
     * @param transaction transaction object containing new data. Transaction with same id in database will be updated.
     * @return Return true if transaction was successfully updated, false otherwise.
     */
    @Override
    public boolean updateTransaction(Transaction transaction) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("UPDATE transactions SET (date, item_id, description, income, expense, remark)=(?, ?, ?, ?, ?, ?) WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setDate(1, Date.valueOf(transaction.getDate()));
            statement.setInt(2, transaction.getItem_id());
            statement.setString(3, transaction.getDescription());
            statement.setBigDecimal(4, transaction.getIncome());
            statement.setBigDecimal(5, transaction.getExpense());
            statement.setString(6, transaction.getRemark());

            statement.setInt(7, transaction.getId());

            int updated = statement.executeUpdate();

            return updated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Deletes transaction with provided id from database, if it exists.
     *
     * @param id integer id of the transaction to remove.
     * @return Returns true if transaction with provided id was successfully removed from database, false otherwise. Also returns false if id does not exist in database.
     */
    @Override
    public boolean deleteTransaction(int id) {
        Connection connection = Database.getConnection();

        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM transactions WHERE id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE)) {
            statement.setInt(1, id);

            int deleted = statement.executeUpdate();

            return deleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Converts {@link ResultSet} to multiple transactions and returns an {@link ObservableList}.
     *
     * @param rs ResultSet containing rows from transactions table.
     * @return Returns {@link ObservableList} containing transactions from the {@link ResultSet} rs.
     * @throws SQLException when there is a problem with fetching data from database.
     */
    private ObservableList<Transaction> resultSetToMultiTransactions(ResultSet rs) throws SQLException {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();

        int rowCount = getRowCount(rs);

        for (int row = 0; row < rowCount; row++) {
            transactions.add(resultSetToTransaction(rs));
        }

        return transactions;
    }

    /**
     * Converts {@link ResultSet} object to {@link Transaction} objects, i.e. fills {@link Transaction} instance with data from database.
     *
     * @param rs ResultSet containing rows from transactions table.
     * @return Returns {@link Transaction} object filled with data from {@link ResultSet}.
     * @throws SQLException when there is a problem with fetching data from database.
     */
    private Transaction resultSetToTransaction(ResultSet rs) throws SQLException {
        if (rs.next()) {
            Transaction transaction = new Transaction();
            transaction.setId(rs.getInt("id"));
            transaction.setDate(rs.getDate("date").toLocalDate());

            int itemId = rs.getInt("item_id");
            Item item = itemDAO.getItem(itemId);
            transaction.setItem_id(itemId);
            transaction.setItem(item);

            transaction.setDescription(rs.getString("description"));
            transaction.setIncome(rs.getBigDecimal("income"));
            transaction.setExpense(rs.getBigDecimal("expense"));
            transaction.setRemark(rs.getString("remark"));

            return transaction;
        }

        return null;
    }

    /**
     * Returns number of rows in the result set.
     *
     * @param rs instance of {@link ResultSet} to get count of rows for.
     * @return Returns integer representing number of rows in provided {@link ResultSet}.
     */
    private int getRowCount(ResultSet rs) {
        int rowCount = -1;
        try {
            rs.last();
            rowCount = rs.getRow();
            rs.beforeFirst();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rowCount;
    }
}
