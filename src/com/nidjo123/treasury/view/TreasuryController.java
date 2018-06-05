package com.nidjo123.treasury.view;

import com.nidjo123.treasury.Main;
import com.nidjo123.treasury.model.Item;
import com.nidjo123.treasury.model.Transaction;
import com.nidjo123.treasury.model.dao.ItemDAO;
import com.nidjo123.treasury.model.dao.ItemDAOImpl;
import com.nidjo123.treasury.model.dao.TransactionDAO;
import com.nidjo123.treasury.model.dao.TransactionsDAOImpl;
import com.nidjo123.treasury.util.CurrencyUtil;
import com.nidjo123.treasury.util.DateUtil;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.StringConverter;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Optional;

public class TreasuryController {
    @FXML
    private TableView<Transaction> transactionsTable;

    @FXML
    private TableColumn<Transaction, String> dateColumn;

    @FXML
    private TableView<Item> itemsTable;

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
    private TableColumn<Item, String> itemSelectColumn;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField incomeField;

    @FXML
    private TextField expenseField;

    @FXML
    private TextField remarkField;

    @FXML
    private TextField incomeSumField;

    @FXML
    private TextField expenseSumField;

    @FXML
    private TextField totalField;

    private Main main;

    private ItemDAO itemDAO;
    private TransactionDAO transactionDAO;

    private ObservableList<Item> items;
    private ObservableList<Transaction> transactions;

    public TreasuryController() {
        this.itemDAO = new ItemDAOImpl();
        this.transactionDAO = new TransactionsDAOImpl();
    }

    @FXML
    private void initialize() {
        // items table
        itemSelectColumn.setCellValueFactory(item -> item.getValue().nameProperty());

        incomeField.setTextFormatter(CurrencyUtil.currencyFormatter());
        expenseField.setTextFormatter(CurrencyUtil.currencyFormatter());

        // transactions table
        descriptionColumn.setCellValueFactory(transaction -> transaction.getValue().descriptionProperty());
        incomeColumn.setCellValueFactory(transaction -> new SimpleStringProperty(CurrencyUtil.format(transaction.getValue().getIncome())));
        expenseColumn.setCellValueFactory(transaction -> new SimpleStringProperty(CurrencyUtil.format(transaction.getValue().getExpense())));
        itemColumn.setCellValueFactory(transaction -> transaction.getValue().itemProperty().getValue().nameProperty());
        remarkColumn.setCellValueFactory(transaction -> transaction.getValue().remarkProperty());

        dateColumn.setCellValueFactory(transaction -> new SimpleStringProperty(transaction.getValue().dateProperty().getValue().format(DateUtil.DATE_FORMATTER)));

        updateData();

        // fix scrollbar alignment workaround
        Platform.runLater(() -> itemsTable.refresh());
        Platform.runLater(() -> transactionsTable.refresh());
    }

    /**
     * Fills tables with fresh data from database and calculates current state of treasury.
     */
    public void updateData() {
        // fill the tables
        updateItems();
        updateTransactions();
        updateTotals();
    }

    @FXML
    private void addNewTransaction() {
        if (isInputValid()) {
            LocalDate date = datePicker.getValue();
            String description = descriptionField.getText();
            Item item = itemsTable.getSelectionModel().getSelectedItem();

            String remark = remarkField.getText();

            BigDecimal income = BigDecimal.ZERO;
            BigDecimal expense = BigDecimal.ZERO;

            String incomeText = incomeField.getText();
            String expenseText = expenseField.getText();

            NumberFormat formatter = CurrencyUtil.getHRKFormatter();

            try {
                if (!incomeText.isEmpty()) {
                    income = new BigDecimal(formatter.parse(incomeText).doubleValue());
                }
                if (!expenseText.isEmpty()) {
                    expense = new BigDecimal(formatter.parse(expenseText).doubleValue());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Transaction transaction = new Transaction();
            transaction.setDate(date);
            transaction.setDescription(description);
            transaction.setIncome(income);
            transaction.setExpense(expense);
            transaction.setRemark(remark);
            transaction.setItem(item);

            boolean saved = transactionDAO.saveTransaction(transaction);

            if (saved) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(main.getPrimaryStage());
                alert.setTitle("Uspjeh");
                alert.setHeaderText("");
                alert.setContentText("Transakcije je uspješno spremljena!");
                alert.showAndWait();

                updateData();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(main.getPrimaryStage());
                alert.setTitle("Pogreška");
                alert.setContentText("Došlo je do pogreške prilikom pohranjivanja transakcije.");
                alert.showAndWait();
            }
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (datePicker.getValue() == null) {
            errorMessage += "Datum nije odabran!\n";
        }
        if (descriptionField.getText() == null || descriptionField.getText().isEmpty()) {
            errorMessage += "Opis je prazan!\n";
        }
        if (itemsTable.getSelectionModel().getSelectedIndex() < 0) {
            errorMessage += "Stavka nije odabrana!\n";
        }

        NumberFormat formatter = CurrencyUtil.getHRKFormatter();

        BigDecimal income = null;
        BigDecimal expense = null;

        if (!incomeField.getText().isEmpty()) {
            try {
                income = BigDecimal.valueOf(formatter.parse(incomeField.getText()).doubleValue());
            } catch (ParseException e) {
                errorMessage += "Unesite ispravan iznos primitka!\n";
            }
        }

        if (!expenseField.getText().isEmpty()) {
            try {
                expense = BigDecimal.valueOf(formatter.parse(expenseField.getText()).doubleValue());
            } catch (ParseException e) {
                errorMessage += "Unesite ispravan iznos izdatka!\n";
            }
        }

        // check that not both income and expense are different from zero
        if (income != null && expense != null) {
            if (income.compareTo(BigDecimal.ZERO) != 0 && expense.compareTo(BigDecimal.ZERO) != 0) {
                errorMessage += "Nije moguće unijeti primitak i izdatak istovremeno!";
            }
        }

        if (!errorMessage.isEmpty()) {
            // show alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("Pogreška");
            alert.setHeaderText("Molimo provjerite unesene podatke.");
            alert.setContentText(errorMessage);
            alert.showAndWait();

            return false;
        }

        return true;
    }

    private void updateTotals() {
        NumberFormat formatter = CurrencyUtil.getHRKFormatter();

        if (transactions.isEmpty()) {
            String zero = formatter.format(BigDecimal.ZERO);
            incomeSumField.setText(zero);
            expenseSumField.setText(zero);
            totalField.setText(zero);
            return;
        }

        Optional<BigDecimal> totalIncome = transactions.stream().map(transaction -> transaction.getIncome()).reduce((x, y) -> x.add(y));
        Optional<BigDecimal> totalExpense = transactions.stream().map(transaction -> transaction.getExpense()).reduce((x, y) -> x.add(y));

        if (totalIncome.isPresent() && totalExpense.isPresent()) {
            BigDecimal income = totalIncome.get();
            BigDecimal expense = totalExpense.get();
            incomeSumField.setText(formatter.format(income));
            expenseSumField.setText(formatter.format(expense));
            totalField.setText(formatter.format(income.add(expense.negate())));
        }
    }

    private void updateTransactions() {
        transactions = transactionDAO.getAllTransactions();

        transactionsTable.setItems(transactions);
    }

    private void updateItems() {
        items = itemDAO.getAllItems();

        itemsTable.setItems(items);
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
