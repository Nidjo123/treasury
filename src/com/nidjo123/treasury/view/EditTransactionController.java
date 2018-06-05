package com.nidjo123.treasury.view;

import com.nidjo123.treasury.model.Item;
import com.nidjo123.treasury.model.Transaction;
import com.nidjo123.treasury.model.dao.ItemDAO;
import com.nidjo123.treasury.model.dao.ItemDAOImpl;
import com.nidjo123.treasury.model.dao.TransactionDAO;
import com.nidjo123.treasury.model.dao.TransactionsDAOImpl;
import com.nidjo123.treasury.util.CurrencyUtil;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;

public class EditTransactionController {

    @FXML
    private TableView<Item> itemsTable;

    @FXML
    private TableColumn<Item, String> itemsColumn;

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

    private ObservableList<Item> items;

    private SimpleObjectProperty<Transaction> transactionProperty;

    private ItemDAO itemDAO;
    private TransactionDAO transactionDAO;

    private Stage stage;

    public EditTransactionController() {
        itemDAO = new ItemDAOImpl();
        transactionDAO = new TransactionsDAOImpl();
        transactionProperty = new SimpleObjectProperty<>();
    }

    public void setTransactionProperty(Transaction transaction) {
        this.transactionProperty.set(transaction);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void initialize() {
        items = itemDAO.getAllItems();

        itemsColumn.setCellValueFactory(item -> item.getValue().nameProperty());

        // fill items table and select appropriate item
        itemsTable.setItems(items);

        transactionProperty.addListener((observable, oldValue, transaction) -> {
            if (transaction == null) return;

            Item item = transaction.getItem();

            itemsTable.requestFocus();
            itemsTable.getSelectionModel().select(item);
            itemsTable.scrollTo(item);

            // fill text fields
            datePicker.setValue(transaction.getDate());
            descriptionField.setText(transaction.getDescription());

            incomeField.setTextFormatter(CurrencyUtil.currencyFormatter(transaction.getIncome()));

            expenseField.setTextFormatter(CurrencyUtil.currencyFormatter(transaction.getExpense()));

            remarkField.setText(transaction.getRemark());
        });
    }

    @FXML
    private void handleUpdateTransaction() {
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

            Transaction transaction = transactionProperty.get();

            transaction.setDate(date);
            transaction.setItem(item);
            transaction.setDescription(description);
            transaction.setIncome(income);
            transaction.setExpense(expense);
            transaction.setRemark(remark);

            TransactionDAO transactionDAO = new TransactionsDAOImpl();

            if (transactionDAO.updateTransaction(transaction)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Promjena je uspješno spremljena!");
                alert.initOwner(stage);
                alert.setTitle("Uspjeh");
                alert.setHeaderText("Uspjeh!");

                alert.showAndWait();

                stage.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Nažalost, promjena nije spremljena.");
                alert.initOwner(stage);
                alert.setTitle("Pogreška");
                alert.setHeaderText("Došlo je do pogreške.");

                alert.showAndWait();
            }
        }
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (datePicker.getValue() == null) {
            errorMessage += "Datum nije odabran!\n";
        }
        if (descriptionField.getText() == null) {
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
            Alert alert = new Alert(Alert.AlertType.WARNING, errorMessage);
            alert.initOwner(stage);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Molimo provjerite unesene podatke.");
            alert.showAndWait();

            return false;
        }

        return true;
    }

    @FXML
    private void closeDialog() {
        stage.close();
    }
}
