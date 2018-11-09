package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import objects.Order;
import objects.Util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class UserConfirmController implements Initializable {

    @FXML
    private ImageView roomImage;

    @FXML
    private Label roomNumber;

    @FXML
    private Label entryDate;

    @FXML
    private Label exitDate;

    @FXML
    private Label guestNumber;

    @FXML
    private Label totalSum;

    @FXML
    private Label residenceDays;

    @FXML
    private Label residenceTotalSum;


    private Order orderInfo;


    void setInfo(Order order){
        this.orderInfo = order;
        roomNumber.setText(orderInfo.getRoomNumber());
        entryDate.setText(dateToText(orderInfo.getEntryDate()));
        exitDate.setText(dateToText(orderInfo.getExitDate()));
        guestNumber.setText(orderInfo.getGuestsNumber());
        totalSum.setText(ChronoUnit.DAYS.between(orderInfo.getEntryDate(), orderInfo.getExitDate())* orderInfo.getRoomCost() + " руб.");
        residenceDays.setText(Util.resDays(orderInfo.getEntryDate()) + " - " + Util.resDays(orderInfo.getExitDate()));
        residenceTotalSum.setText(ChronoUnit.DAYS.between(orderInfo.getEntryDate(), orderInfo.getExitDate())* orderInfo.getRoomCost() + " руб (" + orderInfo.getRoomCost() + " руб/сутки)");
        File file = new File("src/res/"+ orderInfo.getRoomNumber()+".jpg");
        Image image = new Image(file.toURI().toString());
        roomImage.setImage(image);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void changeAction(ActionEvent event) throws IOException {
        Stage stage = ((Stage)((Node) event.getSource()).getScene().getWindow());
        stage.setTitle("Выбор номера гостиницы");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(UserConfirmController.class.getResource("../view/UserMain.fxml"));
        Parent root = loader.load();

        UserMainController controller = loader.getController();
        controller.setOrderInfo(orderInfo);
        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
        stage.show();
    }

    public void acceptAction(ActionEvent event) throws IOException {
        Stage stage = ((Stage)((Node) event.getSource()).getScene().getWindow());
        stage.setTitle("Информация о госте");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../view/UserInformation.fxml"));
        Parent root = loader.load();
        UserInformationController controller = loader.getController();
        controller.setOrderInfo(orderInfo);
        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
        stage.show();
    }

    private String dateToText(LocalDate date) {
        int day = date.getDayOfMonth();
        int month = date.getMonthValue();
        int year = date.getYear();

        return day + " " + Util.convertMonthToString(month) + " " + year + "г.";
    }
}
