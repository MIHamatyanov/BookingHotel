package controllers;

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
            allRoomsRateLabel.setText(room.getRoomRate());
            allRoomsCostLabel.setText(room.getRoomCost());
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
        String line;
        File file = new File("src/res/roomsBroned.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                ordersCurrent.add(new Order(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void initData() {
        allRooms.clear();
        initDataCurrent();
        String line;
        File file = new File("src/res/roomsInfo.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                allRooms.add(new Room(line));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
