package com.nidjo123.treasury.view;

import com.nidjo123.treasury.reports.NoTransactionsException;
import com.nidjo123.treasury.reports.Report;
import com.nidjo123.treasury.reports.ReportException;
import com.nidjo123.treasury.reports.TransactionsReport;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.time.LocalDate;

public class PrintTransactionsReportController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private void initialize() {
    }

    private Stage parentStage;

    @FXML
    private void generateTransactionsReport() {
        if (!isInputValid()) {
            return;
        }

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        Report report = new TransactionsReport(startDate, endDate);

        try {
            report.generate();
        } catch (NoTransactionsException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING, "Nema transakcija u odabranom razdoblju.");
            alert.initOwner(parentStage);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("");

            alert.showAndWait();
        } catch (ReportException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
            alert.initOwner(parentStage);
            alert.setTitle("Pogreška");
            alert.setHeaderText("Dogodila se pogreška :(");

            alert.showAndWait();
        }
    }



    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (startDatePicker.getValue() == null) {
            errorMessage += "Početni datum nije odabran!\n";
        }

        if (endDatePicker.getValue() == null) {
            errorMessage += "Završni datum nije odabran!\n";
        }

        if (!errorMessage.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING, errorMessage);
            alert.initOwner(parentStage);
            alert.setTitle("Upozorenje");
            alert.setHeaderText("Niste upisali sve podatke.");

            alert.showAndWait();

            return false;
        }

        return true;
    }
}
