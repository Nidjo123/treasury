package com.nidjo123.treasury.view;

import com.nidjo123.treasury.Main;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RootController {
    @FXML
    private MenuItem printTreasuryReportItem;

    @FXML
    private MenuItem printItemReportItem;

    private Main main;

    @FXML
    private void initialize() {

    }

    @FXML
    private void showTransactionsReportDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/PrintTransactionsReportDialog.fxml"));

            AnchorPane pane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ispis blagajničkog dnevnika");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(main.getPrimaryStage());

            Scene scene = new Scene(pane);
            stage.setScene(scene);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showAbout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/AboutDialog.fxml"));

            VBox pane = loader.load();

            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("O programu");

            Scene scene = new Scene(pane);
            stage.setScene(scene);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void showItemsReportDialog() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/PrintItemsReportDialog.fxml"));

            AnchorPane pane = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Ispis po stavkama");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(main.getPrimaryStage());

            Scene scene = new Scene(pane);
            stage.setScene(scene);

            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleClose() {
        main.getPrimaryStage().close();
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
