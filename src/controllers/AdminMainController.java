package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.*;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMainController implements Initializable {

    @FXML
    private TabPane tabPane;

    @FXML
    private Tab newOrdersTab;

    @FXML
    private Tab currentOrdersTab;

    @FXML
    private Tab allRoomsTab;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            FXMLLoader newOrderTabLoader = new FXMLLoader();
            newOrderTabLoader.setLocation(getClass().getResource("../view/AdminMainNewOrders.fxml"));
            newOrdersTab.setOnSelectionChanged(event -> ((AdminMainNewOrdersController) newOrderTabLoader.getController()).initData());
            newOrdersTab.setContent(newOrderTabLoader.load());

            FXMLLoader currentOrderLoader = new FXMLLoader();
            currentOrderLoader.setLocation(getClass().getResource("../view/AdminMainCurrentOrders.fxml"));
            currentOrdersTab.setOnSelectionChanged(event -> ((AdminMainCurrentOrdersController) currentOrderLoader.getController()).initData());
            currentOrdersTab.setContent(currentOrderLoader.load());

            FXMLLoader allRoomsLoader = new FXMLLoader();
            allRoomsLoader.setLocation(getClass().getResource("../view/AdminMainAllRooms.fxml"));
            allRoomsTab.setOnSelectionChanged(event -> ((AdminMainAllRoomsController) allRoomsLoader.getController()).initData());
            allRoomsTab.setContent(allRoomsLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
