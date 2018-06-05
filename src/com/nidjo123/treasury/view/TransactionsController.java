package com.nidjo123.treasury.view;

import com.nidjo123.treasury.Main;
import com.nidjo123.treasury.model.Item;
import com.nidjo123.treasury.model.Transaction;
import com.nidjo123.treasury.model.dao.TransactionDAO;
import com.nidjo123.treasury.model.dao.TransactionsDAOImpl;
import com.nidjo123.treasury.util.CurrencyUtil;
import com.nidjo123.treasury.util.DateUtil;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class TransactionsController {

    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableColumn<Transaction, String> itemColumn;

    @FXML
    private TableColumn<Transaction, String> descriptionColumn;

    @FXML
    private TableColumn<Transaction, String> incomeColumn;

    @FXML
    private TableColumn<Transaction, String> expenseColumn;

    @FXML
    private TableColumn<Transaction, String> remarkColumn;

    @FXML
    private TextField descriptionFilterField;

    private ObservableList<Transaction> transactions;
    private TransactionDAO transactionDAO;

    private Main main;

    public TransactionsController() {
        transactionDAO = new TransactionsDAOImpl();
    }

    @FXML
    private void initialize() {
        // transactions table
        descriptionColumn.setCellValueFactory(transaction -> transaction.getValue().descriptionProperty());
        incomeColumn.setCellValueFactory(transaction -> new SimpleStringProperty(CurrencyUtil.format(transaction.getValue().getIncome())));
        expenseColumn.setCellValueFactory(transaction -> new SimpleStringProperty(CurrencyUtil.format(transaction.getValue().getExpense())));
        itemColumn.setCellValueFactory(transaction -> transaction.getValue().itemProperty().getValue().nameProperty());
        remarkColumn.setCellValueFactory(transaction -> transaction.getValue().remarkProperty());

        dateColumn.setCellValueFactory(transaction -> new SimpleStringProperty(transaction.getValue().dateProperty().getValue().format(DateUtil.DATE_FORMATTER)));

        updateTransactions();
    }

    @FXML
    private void showEditTransactionDialog() {
        Transaction transaction = transactionsTable.getSelectionModel().getSelectedItem();

        if (transaction == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nije odabrana transakcija!");
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("Upozorenje");
            alert.setHeaderText("");

            alert.showAndWait();

            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/EditTransactionDialog.fxml"));

            BorderPane pane = loader.load();
            EditTransactionController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("Uređivanje transakcije");
            stage.initModality(Modality.WINDOW_MODAL);

            Scene scene = new Scene(pane);
            stage.setScene(scene);

            controller.setTransactionProperty(transaction);
            controller.setStage(stage);

            stage.showAndWait();

            updateTransactions();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteTransaction() {
        Transaction transaction = transactionsTable.getSelectionModel().getSelectedItem();

        if (transaction == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nije odabrana transakcija!");
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("Upozorenje");
            alert.setHeaderText("");

            alert.showAndWait();

            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Jeste li sigurni da želite obrisati transakciju?");
        alert.initOwner(main.getPrimaryStage());
        alert.setTitle("Potvrda");
        alert.setHeaderText("Potrebno je potvrditi brisanje transakcije.");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                transactionDAO.deleteTransaction(transaction);
            }
        });

        updateTransactions();
    }

    public void updateTransactions() {
        transactions = transactionDAO.getAllTransactions();

        FilteredList<Transaction> filteredTransactions = new FilteredList<Transaction>(transactions, p -> true);

        descriptionFilterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTransactions.setPredicate(transaction -> {
                if (newValue == null || newValue.isEmpty()) return true;

                if (transaction.getDescription().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Transaction> sortedTransactions = new SortedList<>(filteredTransactions);

        transactionsTable.setItems(sortedTransactions);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
