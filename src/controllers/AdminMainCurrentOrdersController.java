package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import objects.Order;
import objects.Util;

import java.io.*;
import java.net.URL;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class AdminMainCurrentOrdersController implements Initializable {

    private ObservableList<Order> currentOrders = FXCollections.observableArrayList();

    @FXML
    private TableView<Order> currentOrdersTable;

    @FXML
    private TableColumn<Order, String> roomNumberCol;

    @FXML
    private TableColumn<Order, String> guestSurnameCol;

    @FXML
    private TableColumn<Order, String> guestNameCol;

    @FXML
    private AnchorPane currentOrderPane;

    @FXML
    private Label currentNameLabel;

    @FXML
    private Label currentEmailLabel;

    @FXML
    private Label currentPhoneLabel;

    @FXML
    private Label currentPaymentMethodLabel;

    @FXML
    private Label currentRoomNumberLabel;

    @FXML
    private Label currentDatesLabel;

    @FXML
    private Label currentCostLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        currentOrderPane.setOpacity(0);
        initData();

        roomNumberCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        guestSurnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        guestNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        currentOrdersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showCurrentOrderInfo(newValue));
        currentOrdersTable.setItems(currentOrders);
    }

    void initData() {
        currentOrders.clear();
        String line;
        File file = new File("src/res/roomsBroned.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                currentOrders.add(new Order(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showCurrentOrderInfo(Order order) {
        if (order != null) {
            currentNameLabel.setText(order.getSurname() + " " + order.getName());
            currentEmailLabel.setText(order.getEmail());
            currentPhoneLabel.setText(order.getPhone());
            currentPaymentMethodLabel.setText(order.getPaymentMethod());
            currentRoomNumberLabel.setText(order.getRoomNumber());
            currentDatesLabel.setText(Util.resDays(order.getEntryDate()) + " - " + Util.resDays(order.getExitDate()));
            currentCostLabel.setText(ChronoUnit.DAYS.between(order.getEntryDate(), order.getExitDate()) * order.getRoomCost() + " руб.");
            currentOrderPane.setOpacity(1);
        } else {
            currentOrderPane.setOpacity(0);
        }
    }

    @FXML
    void removeOrderAction(ActionEvent event) {
        currentOrders.remove(currentOrdersTable.getSelectionModel().getSelectedItem());
        File file = new File("src/res/roomsBroned.txt");
        Util.rewriteData(currentOrders, file);
    }
}
