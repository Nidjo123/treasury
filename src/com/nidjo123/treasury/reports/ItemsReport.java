package com.nidjo123.treasury.reports;

import com.nidjo123.treasury.model.Item;
import com.nidjo123.treasury.model.Transaction;
import com.nidjo123.treasury.model.dao.ItemDAO;
import com.nidjo123.treasury.model.dao.ItemDAOImpl;
import com.nidjo123.treasury.model.dao.TransactionDAO;
import com.nidjo123.treasury.model.dao.TransactionsDAOImpl;
import com.nidjo123.treasury.util.CurrencyUtil;
import com.nidjo123.treasury.util.DateUtil;
import de.nixosoft.jlr.JLRConverter;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ItemsReport extends TreasuryReport {
    private LocalDate startDate;
    private LocalDate endDate;
    private TransactionDAO transactionDAO;
    private ItemDAO itemDAO;

    public ItemsReport(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        transactionDAO = new TransactionsDAOImpl();
        itemDAO = new ItemDAOImpl();

        // setup file names
        templateName = "itemsReport.tex";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        outputName = "ispis_po_stavkama_" + formatter.format(now) + ".tex";
    }

    @Override
    protected void fillData(JLRConverter converter) {
        ObservableList<Item> items = itemDAO.getAllItems();
        ObservableList<Transaction> transactions = transactionDAO.getTransactionsByDate(startDate, endDate);

        converter.replace("numberOfItems", items.size());
        converter.replace("startDate", DateUtil.formatAbbrevMonth(startDate));
        converter.replace("endDate", DateUtil.formatAbbrevMonth(endDate));

        List<Map<String, String>> itemsList = new ArrayList<>();

        for (Item item : items) {
            ObservableList<Transaction> itemTransactions = transactions.filtered(t -> t.getItem_id() == item.getId());

            BigDecimal itemIncome = itemTransactions.stream().map(t -> t.getIncome()).reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
            BigDecimal itemExpense = itemTransactions.stream().map(t -> t.getExpense()).reduce(BigDecimal.ZERO, (x, y) -> x.add(y));

            BigDecimal itemTotal = itemIncome.add(itemExpense.negate());

            Map<String, String> map = new HashMap<>();

            map.put("name", item.getName());
            map.put("income", CurrencyUtil.format(itemIncome));
            map.put("expense", CurrencyUtil.format(itemExpense));
            map.put("total", CurrencyUtil.format(itemTotal));

            itemsList.add(map);
        }

        itemsList.sort((x, y) -> x.get("name").compareToIgnoreCase(y.get("name")));

        converter.replace("items", itemsList);

        BigDecimal totalIncome = transactions.stream().map(t -> t.getIncome()).reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
        BigDecimal totalExpense = transactions.stream().map(t -> t.getExpense()).reduce(BigDecimal.ZERO, (x, y) -> x.add(y));

        BigDecimal total = totalIncome.add(totalExpense.negate());

        converter.replace("totalIncome", CurrencyUtil.format(totalIncome));
        converter.replace("totalExpense", CurrencyUtil.format(totalExpense));
        converter.replace("total", CurrencyUtil.format(total));
    }
}
