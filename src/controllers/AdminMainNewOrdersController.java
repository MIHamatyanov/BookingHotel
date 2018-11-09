package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
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
        String line;
        File file = new File("src/res/newOrders.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                Order order = new Order(line);
                newOrders.add(order);
            }
        } catch (IOException e) {
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
            costLabel.setText(ChronoUnit.DAYS.between(order.getEntryDate(), order.getExitDate()) * order.getRoomCost() + " руб.");
            orderInfoPane.setOpacity(1);
        } else {
            orderInfoPane.setOpacity(0);
        }
    }

    @FXML
    void acceptAction(ActionEvent event) {
        File file = new File("src/res/roomsBroned.txt");
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.append(ordersTable.getSelectionModel().getSelectedItem().toString()).append("\n");
            newOrders.remove(ordersTable.getSelectionModel().getSelectedItem());
        } catch (IOException e) {
            e.printStackTrace();
        }

        file = new File("src/res/newOrders.txt");
        Util.rewriteData(newOrders, file);
    }

    @FXML
    void cancelAction(ActionEvent event) {
        newOrders.remove(ordersTable.getSelectionModel().getSelectedItem());
        File file = new File("src/res/newOrders.txt");
        Util.rewriteData(newOrders, file);
    }

}
