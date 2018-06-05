package com.nidjo123.treasury.reports;

public class NoTransactionsException extends ReportException {
    public NoTransactionsException() {
        super("There are no transactions.");
    }
}
