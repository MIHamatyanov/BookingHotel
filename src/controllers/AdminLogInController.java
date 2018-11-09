package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.*;

public class AdminLogInController {

    @FXML
    private TextField loginField;

    @FXML
    private PasswordField passwordField;

    @FXML
    void cancelAction(ActionEvent event) {
        loginField.setText("");
        passwordField.setText("");

    }

    @FXML
    void enterAction(ActionEvent event) throws IOException{
        String login = loginField.getText();
        String password = passwordField.getText();

        File file = new File("src/res/admins.txt");
        String line;
        boolean flag = false;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(" ");
                if (login.equals(data[0]) && password.equals(data[1])) {
                    flag = true;
                }
            }
        } catch (IOException e) {
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
