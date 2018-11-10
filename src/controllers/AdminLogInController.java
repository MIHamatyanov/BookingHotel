package controllers;

import hotelDAO.DataDAO;
import hotelDAO.DataRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;
import java.sql.SQLException;

public class AdminLogInController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    private DataDAO DBdata = new DataRepository();

    @FXML
    void cancelAction(ActionEvent event) {
        loginField.setText("");
        passwordField.setText("");

    }

    @FXML
    void enterAction(ActionEvent event) throws IOException{
        String login = loginField.getText();
        String password = passwordField.getText();

        boolean flag = false;
        try {
            flag = DBdata.checkData(login, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (flag) {
            Stage stage = ((Stage) ((Node) event.getSource()).getScene().getWindow());
            stage.setTitle("Окно администратора");
            Parent root = FXMLLoader.load(getClass().getResource("../view/AdminMain.fxml"));
            stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
            stage.show();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Неверный логин/пароль");
            alert.setContentText("Проверьте правильность логина/пароля");
            alert.showAndWait();
        }
    }

}
