package controllers;

import hotelDAO.OrderDAO;
import hotelDAO.OrdersRepository;
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
import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class AdminMainNewOrdersController implements Initializable {

    private ObservableList<Order> newOrders = FXCollections.observableArrayList();

    @FXML
    private TableView<Order> ordersTable;

    @FXML
    private TableColumn<Order, String> roomNumberCol;

    @FXML
    private TableColumn<Order, String> guestSurnameCol;

    @FXML
    private TableColumn<Order, String> guestNameCol;

    @FXML
    private AnchorPane orderInfoPane;

    @FXML
    private Label nameLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Label phoneLabel;

    @FXML
    private Label paymentMethodLabel;

    @FXML
    private Label roomNumberLabel;

    @FXML
    private Label datesLabel;

    @FXML
    private Label costLabel;

    private OrderDAO DBorders = new OrdersRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderInfoPane.setOpacity(0);
        initData();
        roomNumberCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        guestSurnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        guestNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ordersTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showOrderInfo(newValue));
        ordersTable.setItems(newOrders);
    }

    void initData() {
        newOrders.clear();

        try {
            newOrders.addAll(DBorders.getByStatus("0"));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void showOrderInfo(Order order) {
        if (order != null) {
            nameLabel.setText(order.getSurname() + " " + order.getName());
            emailLabel.setText(order.getEmail());
            phoneLabel.setText(order.getPhone());
            paymentMethodLabel.setText(order.getPaymentMethod());
            roomNumberLabel.setText(order.getRoomNumber());
            datesLabel.setText(Util.resDays(order.getEntryDate()) + " - " + Util.resDays(order.getExitDate()));
            costLabel.setText(order.getTotalSum() + " руб.");
            orderInfoPane.setOpacity(1);
        } else {
            orderInfoPane.setOpacity(0);
        }
    }

    @FXML
    void acceptAction(ActionEvent event) {

        try {
            DBorders.updateStatus(ordersTable.getSelectionModel().getSelectedItem().getRoomNumber(), "1");
            newOrders.remove(ordersTable.getSelectionModel().getSelectedItem());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void cancelAction(ActionEvent event) {

        try {
            DBorders.delete(ordersTable.getSelectionModel().getSelectedItem().getRoomNumber());
            newOrders.remove(ordersTable.getSelectionModel().getSelectedItem());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
