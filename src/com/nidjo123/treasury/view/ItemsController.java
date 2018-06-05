package com.nidjo123.treasury.view;

import com.nidjo123.treasury.Main;
import com.nidjo123.treasury.model.Item;
import com.nidjo123.treasury.model.dao.ItemDAO;
import com.nidjo123.treasury.model.dao.ItemDAOImpl;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

/**
 * This class implements methods for controlling items view.
 */
public class ItemsController {

    /**
     * Reference to items table.
     */
    @FXML
    private TableView<Item> itemsTable;

    /**
     * Reference to item name column in items table.
     */
    @FXML
    private TableColumn<Item, String> itemNameColumn;

    /**
     * Reference to text field for adding new items.
     */
    @FXML
    private TextField itemTextField;

    /**
     * Reference to text field for filtering items in the table.
     */
    @FXML
    private TextField filterItemField;

    /**
     * Reference to button for saving new items.
     */
    @FXML
    private Button saveItemButton;

    /**
     * List of items in the table.
     */
    private ObservableList<Item> items;

    /**
     * Instance of the Item Data Access Object.
     */
    private ItemDAO itemDAO;

    /**
     * Instance of the program.
     */
    private Main main;

    /**
     * Constructor for items controller.
     */
    public ItemsController() {
        itemDAO = new ItemDAOImpl();
    }

    /**
     * This method is called AFTER the constructor, and when all @FXML fields are already populated and initialized.
     * Does some initialization of @FXML fields and loads items into the items table.
     */
    @FXML
    private void initialize() {
        itemNameColumn.setCellValueFactory(item -> item.getValue().nameProperty());

        itemTextField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                saveItemButton.fire();
            }
        });

        itemNameColumn.setOnEditCommit(event -> {
            Item selectedItem = itemsTable.getSelectionModel().getSelectedItem();

            System.out.println(selectedItem.getName());
        });

        fillItems();
    }

    /**
     * Loads all items from the database to items list and fills the appropriate table.
     */
    public void fillItems() {
        items = itemDAO.getAllItems();

        FilteredList<Item> filteredItems = new FilteredList<>(items, p -> true);

        filterItemField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                if (item.getName().toLowerCase().contains(newValue.toLowerCase())) {
                    return true;
                }

                return false;
            });
        });

        SortedList<Item> sortedItems = new SortedList<>(filteredItems);

        sortedItems.comparatorProperty().bind(itemsTable.comparatorProperty());

        itemsTable.setItems(sortedItems);
    }

    @FXML
    private void handleNewItem() {
        String name = itemTextField.getText();

        if (!name.isEmpty()) {
            Item item = new Item();
            item.setName(name);

            if (!itemDAO.saveItem(item)) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(main.getPrimaryStage());
                alert.setTitle("Pogreška");
                alert.setHeaderText("Dogodila se pogreška.");
                alert.setContentText("Stavku nije bilo moguće dodati. \nNapomena: Stavke moraju imati jedinstveni naziv.");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.initOwner(main.getPrimaryStage());
                alert.setContentText("Stavka je uspješno dodana!");
                alert.setHeaderText("Uspjeh!");
                alert.setTitle("Obavijest");

                alert.showAndWait();

                itemTextField.clear();

                fillItems();
            }
        }
    }

    @FXML
    private void handleDeleteItem() {
        Item item = itemsTable.getSelectionModel().getSelectedItem();

        if (item != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Jeste li sigurni da želite obrisati označenu stavku?");
            alert.initOwner(main.getPrimaryStage());
            alert.setTitle("Potvrda");
            alert.setHeaderText("Potvrda o brisanju stavke");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    itemDAO.deleteItem(item);
                    fillItems();
                }
            });
        }
    }

    /**
     * Sets instance of the program (owner).
     *
     * @param main instance of the program which is owner of this view-controller.
     */
    public void setMain(Main main) {
        this.main = main;
    }
}
