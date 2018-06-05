package com.nidjo123.treasury;

import com.nidjo123.treasury.view.ItemsController;
import com.nidjo123.treasury.view.RootController;
import com.nidjo123.treasury.view.TransactionsController;
import com.nidjo123.treasury.view.TreasuryController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main class of the program. Extends JavaFX application and therefore implements start method. Main method just launches the application.
 */
public class Main extends Application {

    /**
     * Name of the program.
     */
    private static final String APP_NAME = "Župna blagajna";

    /**
     * Primary stage of the program, owner of all other stages.
     */
    private Stage primaryStage;

    /**
     * Root layout of the program, containing all other layouts.
     */
    private BorderPane rootLayout;

    /**
     * Tab pane which is in the center of root layout (BorderPane).
     */
    private TabPane tabPane;

    /**
     * Called by JavaFX, sets up the program. Initializes the stage, layouts and scenes.
     *
     * @param primaryStage
     * @throws Exception
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        primaryStage.setTitle(APP_NAME);

        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));

        initRootLayout();

        initTreasuryTab();

        initItemsTab();

        initTransactionsTab();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/RootLayout.fxml"));

            rootLayout = loader.load();

            RootController controller = loader.getController();

            controller.setMain(this);

            tabPane = (TabPane) rootLayout.getCenter();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTreasuryTab() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TreasuryView.fxml"));

            AnchorPane transactionsView = loader.load();

            TreasuryController controller = loader.getController();
            controller.setMain(this);

            Tab tab = tabPane.getTabs().get(0);

            tab.setContent(transactionsView);

            tab.setOnSelectionChanged(event -> {
                if (tab.isSelected()) {
                    controller.updateData();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initItemsTab() {
        Tab tab = tabPane.getTabs().get(1);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ItemsView.fxml"));

            AnchorPane itemView = loader.load();

            tab.setContent(itemView);

            ItemsController controller = loader.getController();

            tab.setOnSelectionChanged(event -> {
                if (tab.isSelected()) {
                    controller.fillItems();
                }
            });

            controller.setMain(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initTransactionsTab() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/TransactionsView.fxml"));

            AnchorPane pane = loader.load();

            TransactionsController controller = loader.getController();
            controller.setMain(this);

            Tab tab = tabPane.getTabs().get(2);

            tab.setContent(pane);

            tab.setOnSelectionChanged(event -> {
                if (tab.isSelected()) {
                    controller.updateTransactions();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns primary stage of the program.
     *
     * @return Returns primary stage of the program (owner of all other stages).
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    /**
     * Main method of the program. Just launches the JavaFX application with passed args. Actual entry point is the start method.
     *
     * @param args Arguments of the program passed to JavaFX.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
