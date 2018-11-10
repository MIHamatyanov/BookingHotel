package controllers;

import hotelDAO.OrderDAO;
import hotelDAO.OrdersRepository;
import hotelDAO.RoomDAO;
import hotelDAO.RoomsRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import objects.Order;
import objects.Room;

import java.io.*;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.ResourceBundle;

public class UserMainController implements Initializable {
    private ObservableList<TableRow> rooms = FXCollections.observableArrayList();

    @FXML
    private TableView<TableRow> roomsTable;

    @FXML
    private TableColumn<TableRow, String> roomNumberCol;

    @FXML
    private TableColumn<TableRow, String> roomRateCol;

    @FXML
    private TableColumn<TableRow, Integer> roomCostCol;

    @FXML
    private TableColumn<TableRow, AcceptButton> roomChooseCol;

    @FXML
    private DatePicker entryDate;

    @FXML
    private DatePicker exitDate;

    @FXML
    private ComboBox<String> guestNumber;

    private Order orderInfo;
    private RoomDAO DBrooms = new RoomsRepository();
    private OrderDAO DBorders = new OrdersRepository();

    void setOrderInfo(Order orderInfo) {
        this.orderInfo = orderInfo;
        entryDate.setValue(orderInfo.getEntryDate());
        exitDate.setValue(orderInfo.getExitDate());
        guestNumber.setValue(orderInfo.getGuestsNumber());
        rooms.clear();
        initData();
        roomsTable.setItems(rooms);
        roomsTable.setOpacity(1);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        /*
            Инициализация DatePicker
         */
        

        entryDate.setValue(LocalDate.now());
        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item,empty);
                        if (item.isBefore(entryDate.getValue()) && (item.getYear() <= exitDate.getValue().getYear()) && (item.getMonthValue() <= exitDate.getValue().getMonthValue())) {
                            setDisable(true);
                            setStyle("-fx-background-color: #b3b3b3");
                        }

                        if (exitDate.getValue().isBefore(entryDate.getValue())) {
                            exitDate.setValue(entryDate.getValue().plusDays(1));
                        }

                        long p = ChronoUnit.DAYS.between(entryDate.getValue(), item);
                        setTooltip(new Tooltip("Количество дней: " + p));

                        if (item.isAfter(entryDate.getValue().minusDays(1)) && item.isBefore(exitDate.getValue().plusDays(1))) {
                            setStyle("-fx-background-color: #0075d6; -fx-text-fill: #ffffff");
                        }
                    }
                };
            }
        };
        final Callback<DatePicker, DateCell> dayCellFactory2 = new Callback<>() {
            @Override
            public DateCell call(DatePicker param) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item,empty);

                        if (exitDate.getValue().isBefore(entryDate.getValue())) {
                            exitDate.setValue(entryDate.getValue().plusDays(1));
                        }

                        LocalDate today = LocalDate.now();
                        if (item.isBefore(today)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #b3b3b3");
                        }
                        if (item.isAfter(entryDate.getValue().minusDays(1)) && item.isBefore(exitDate.getValue().plusDays(1))) {
                            setStyle("-fx-background-color: #0075d6; -fx-text-fill: #ffffff");
                        }
                    }
                };
            }
        };
        entryDate.setDayCellFactory(dayCellFactory2);
        exitDate.setDayCellFactory(dayCellFactory);
        exitDate.setValue(entryDate.getValue().plusDays(1));



        /*
            Инициализация ComboBox
         */
        ObservableList<String> options = FXCollections.observableArrayList("1 взрослый", "2 взрослых", "3 взрослых", "4 взрослых");
        guestNumber.setItems(options);
        guestNumber.getSelectionModel().selectFirst();



        /*
            Инициализация TableView
         */
        roomsTable.setOpacity(0);

        roomNumberCol.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        roomRateCol.setCellValueFactory(new PropertyValueFactory<>("roomRate"));
        roomCostCol.setCellValueFactory(new PropertyValueFactory<>("roomCost"));
        roomChooseCol.setCellValueFactory(new PropertyValueFactory<>("roomChoose"));
    }

    private void initData() {
        try {
            List<Room> arrayListRooms = DBrooms.getByCapacity(guestNumber.getValue().split(" ")[0], entryDate.getValue(), exitDate.getValue());
            for (Room room : arrayListRooms) {
                rooms.add(new TableRow(room.getRoomNumber(), room.getRoomRate(), room.getRoomCost()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



    /*
        Заполнение таблицы
     */
    public void acceptAction(ActionEvent event) {
        rooms.clear();
        initData();
        roomsTable.setItems(rooms);
        roomsTable.setOpacity(1);
    }


    public class AcceptButton extends Button {
        public AcceptButton(TableRow info) {
            super("Выбрать");
            setOnAction((event -> {
                try {
                    Stage stage = ((Stage)((Node) event.getSource()).getScene().getWindow());
                    stage.setTitle("Подтверждение бронирования");
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(UserConfirmController.class.getResource("../view/UserConfirm.fxml"));
                    Parent root = loader.load();
                    UserConfirmController controller = loader.getController();

                    String roomNumber = info.getRoomNumber();
                    LocalDate roomEntryDate = entryDate.getValue();
                    LocalDate roomExitDate = exitDate.getValue();
                    String roomGuestsNumber = guestNumber.getValue();
                    Integer totalSum = Integer.parseInt(info.getRoomCost().split(" ")[1]) * (int)ChronoUnit.DAYS.between(roomEntryDate, roomExitDate);
                    orderInfo = new Order(roomNumber, roomEntryDate, roomExitDate, roomGuestsNumber, totalSum);
                    controller.setInfo(orderInfo);
                    stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }));
        }
    }

    public class TableRow {
        private final String roomNumber;
        private final String roomRate;
        private final String roomCost;
        private final AcceptButton roomChoose;

        public TableRow(String line) {
            String[] data = line.split("\\|");
            this.roomNumber = data[0];
            this.roomRate = "Рейтинг: " + data[1];
            this.roomCost = "Цена: " + data[2];
            roomChoose = new AcceptButton(this);
        }

        public TableRow(String roomNumber, String roomRate, String roomCost) {
            this.roomNumber = roomNumber;
            this.roomRate = roomRate;
            this.roomCost = roomCost;
            roomChoose = new AcceptButton(this);
        }


        public String getRoomNumber() {
            return roomNumber;
        }

        public String getRoomRate() {
            return roomRate;
        }

        public String getRoomCost() {
            return roomCost;
        }

        public AcceptButton getRoomChoose() {
            return roomChoose;
        }
    }
}
