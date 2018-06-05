package com.nidjo123.treasury.reports;

public class ReportException extends Exception {
    public ReportException() {
        super("Report exception.");
    }

    public ReportException(String message) {
        super(message);
    }
}
