package com.nidjo123.treasury.reports;

import com.nidjo123.treasury.model.Transaction;
import com.nidjo123.treasury.model.dao.TransactionDAO;
import com.nidjo123.treasury.model.dao.TransactionsDAOImpl;
import com.nidjo123.treasury.util.CurrencyUtil;
import com.nidjo123.treasury.util.DateUtil;
import de.nixosoft.jlr.JLRConverter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TransactionsReport extends TreasuryReport {
    private LocalDate startDate;
    private LocalDate endDate;
    private TransactionDAO transactionDAO;
    private List<Transaction> transactions;

    public TransactionsReport(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.transactionDAO = new TransactionsDAOImpl();
        this.transactions = transactionDAO.getTransactionsByDate(startDate, endDate);

        // setup file names
        templateName = "transactionsReport.tex";
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyyyy_HHmmss");
        outputName = "blagajnicki_dnevnik_" + formatter.format(now) + ".tex";
    }

    @Override
    protected void fillData(JLRConverter converter) {
        BigDecimal totalIncome = transactions.stream().map(t -> t.getIncome()).reduce(BigDecimal.ZERO, (x, y) -> x.add(y));
        BigDecimal totalExpense = transactions.stream().map(t -> t.getExpense()).reduce(BigDecimal.ZERO, (x, y) -> x.add(y));

        BigDecimal total = totalIncome.add(totalExpense.negate());

        ArrayList<Map<String, String>> transactionsList = new ArrayList<>();

        for (Transaction transaction : transactions) {
            Map<String, String> map = new HashMap<>();

            map.put("date", DateUtil.format(transaction.getDate()));
            map.put("description", transaction.getDescription());
            map.put("income", CurrencyUtil.format(transaction.getIncome()));
            map.put("expense", CurrencyUtil.format(transaction.getExpense()));

            transactionsList.add(map);
        }

        converter.replace("transactions", transactionsList);
        converter.replace("startDate", DateUtil.formatAbbrevMonth(startDate));
        converter.replace("endDate", DateUtil.formatAbbrevMonth(endDate));
        converter.replace("totalIncome", CurrencyUtil.format(totalIncome));
        converter.replace("totalExpense", CurrencyUtil.format(totalExpense));
        converter.replace("total", CurrencyUtil.format(total));
        converter.replace("numberOfTransactions", transactionsList.size());
    }
}
