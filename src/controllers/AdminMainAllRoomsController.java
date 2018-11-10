package controllers;

import hotelDAO.OrderDAO;
import hotelDAO.OrdersRepository;
import hotelDAO.RoomDAO;
import hotelDAO.RoomsRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import objects.Order;
import objects.Room;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AdminMainAllRoomsController implements Initializable {

    private ObservableList<Order> ordersCurrent = FXCollections.observableArrayList();
    private ObservableList<Room> allRooms = FXCollections.observableArrayList();

    @FXML
    private TableView<Room> allRoomsTable;

    @FXML
    private TableColumn<Room, String> roomNumberCol;

    @FXML
    private TableColumn<Room, String> roomRateCol;

    @FXML
    private TableColumn<Room, String> roomCostCol;

    @FXML
    private AnchorPane roomInfoPane;

    @FXML
    private Label allRoomsNumberLabel;

    @FXML
    private Label allRoomsRateLabel;

    @FXML
    private Label allRoomsCostLabel;

    @FXML
    private Label guestsNumberLabel;

    @FXML
    private Label allRoomsStatus;

    private RoomDAO DBrooms = new RoomsRepository();
    private OrderDAO DBorders = new OrdersRepository();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        roomInfoPane.setOpacity(0);
        initData();

        roomNumberCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomRateCol.setCellValueFactory(new PropertyValueFactory<>("roomRate"));
        roomCostCol.setCellValueFactory(new PropertyValueFactory<>("roomCost"));
        allRoomsTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> showRoomsInfo(newValue));
        allRoomsTable.setItems(allRooms);
    }

    private void showRoomsInfo(Room room) {
        if (room != null) {
            allRoomsNumberLabel.setText(room.getRoomNumber());
            allRoomsRateLabel.setText(room.getRoomRate().split(" ")[1]);
            allRoomsCostLabel.setText(room.getRoomCost().split(" ")[1]);
            guestsNumberLabel.setText(room.getGuestsNumber());
            allRoomsStatus.setText("Свободен");
            LocalDate today = LocalDate.now();
            for (Order order : ordersCurrent) {
                if (room.getRoomNumber().equals(order.getRoomNumber())) {
                    if (order.getEntryDate().minusDays(1).isBefore(today) && order.getExitDate().plusDays(1).isAfter(today)) {
                        allRoomsStatus.setText("Занят");
                    }
                }
            }

            roomInfoPane.setOpacity(1);
        }
    }

    private void initDataCurrent() {
        ordersCurrent.clear();

        try {
            ordersCurrent.addAll(DBorders.getByStatus("1"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void initData() {
        allRooms.clear();
        initDataCurrent();

        try {
            allRooms.addAll(DBrooms.getAll());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
