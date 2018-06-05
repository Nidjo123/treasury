package com.nidjo123.treasury.model;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class models the Transaction entity. Transactions are atoms in the treasury. They have date on which they are executed, item they are referencing,
 * description text, income and expense amounts, optional remark and transfered amount of money. Every transaction has a unique id. which is also the primary key.
 */
public class Transaction {
    /**
     * Unique integer of the transaction, also the primary key.
     */
    private final IntegerProperty id;

    /**
     * Date on which transaction executes.
     */
    private final ObjectProperty<LocalDate> date;

    /**
     * Id of the item this transaction references.
     */
    private final IntegerProperty item_id;

    /**
     * Item instance associated with this transaction.
     */
    private final ObjectProperty<Item> item;

    /**
     * Description of the transaction.
     */
    private final StringProperty description;

    /**
     * Income amount of the transaction.
     */
    private final ObjectProperty<BigDecimal> income;

    /**
     * Expense amount of the transaction.
     */
    private final ObjectProperty<BigDecimal> expense;

    /**
     * Optional remark about the transaction.
     */
    private final StringProperty remark;

    /**
     * Constructor of the class. Sets every member to null.
     */
    public Transaction() {
        this.id = new SimpleIntegerProperty();
        this.date = new SimpleObjectProperty<>();
        this.item_id = new SimpleIntegerProperty();
        this.item = new SimpleObjectProperty<>();
        this.description = new SimpleStringProperty();
        this.income = new SimpleObjectProperty<>();
        this.expense = new SimpleObjectProperty<>();
        this.remark = new SimpleStringProperty();

        item.addListener(new ChangeListener<Item>() {
            @Override
            public void changed(ObservableValue<? extends Item> observable, Item oldValue, Item newValue) {
                item_id.set(newValue.getId());
            }
        });
    }

    /**
     * Returns integer id of the transaction.
     *
     * @return Returns integer id of the transaction.
     */
    public int getId() {
        return id.get();
    }

    /**
     * Returns Item instance associated with this transaction.
     *
     * @return Returns Item instance associated with this transaction.
     */
    public Item getItem() {
        return item.get();
    }

    /**
     * Returns item property of this transaction.
     *
     * @return Returns item property of this transaction.
     */
    public ObjectProperty<Item> itemProperty() {
        return item;
    }

    /**
     * Sets item object associated with this transaction.
     *
     * @param item Item instance to associate this transaction with.
     */
    public void setItem(Item item) {
        this.item.set(item);
    }

    /**
     * Returns id property of the transaction.
     *
     * @return Returns id property of the transaction.
     */
    public IntegerProperty idProperty() {
        return id;
    }

    /**
     * Sets id of the transaction. Id must be unique for every transaction.
     *
     * @param id integer id to set for the transaction. Must be unique.
     */
    public void setId(int id) {
        this.id.set(id);
    }

    /**
     * Returns date on which transactions executes as instance of LocalDate.
     *
     * @return Returns date on which transactions executes as instance of LocalDate.
     */
    public LocalDate getDate() {
        return date.get();
    }

    /**
     * Returns date property of the transaction.
     *
     * @return Returns date property of the transaction.
     */
    public ObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    /**
     * Sets the date on which transaction executes.
     *
     * @param date instance of LocalDate which sets the date on which transaction executes.
     */
    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    /**
     * Returns integer item id associated with this transaction.
     *
     * @return Returns integer item id associated with this transaction.
     */
    public int getItem_id() {
        return item_id.get();
    }

    /**
     * Returns item id property of the transaction.
     *
     * @return Returns item id property of the transaction.
     */
    public IntegerProperty item_idProperty() {
        return item_id;
    }

    /**
     * Sets item id associated with this transaction. Item with given id must be present in the database.
     *
     * @param item_id integer id of the item to associated the transaction with. Item with given id must be present in the database.
     */
    public void setItem_id(int item_id) {
        this.item_id.set(item_id);
    }

    /**
     * Returns the description of the transaction.
     *
     * @return Returns string description of the transaction.
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * Returns description property of the transaction.
     *
     * @return Returns description property of the transaction.
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * Sets the description of the transaction.
     *
     * @param description new description of the transaction.
     */
    public void setDescription(String description) {
        this.description.set(description);
    }

    public BigDecimal getIncome() {
        return income.get();
    }

    public ObjectProperty<BigDecimal> incomeProperty() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income.set(income);
    }

    public BigDecimal getExpense() {
        return expense.get();
    }

    public ObjectProperty<BigDecimal> expenseProperty() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense.set(expense);
    }

    public String getRemark() {
        return remark.get();
    }

    public StringProperty remarkProperty() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark.set(remark);
    }
}
