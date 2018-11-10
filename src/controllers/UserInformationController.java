package controllers;

import hotelDAO.OrderDAO;
import hotelDAO.OrdersRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import objects.Order;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserInformationController implements Initializable {

    @FXML
    private TextField userNameField;

    @FXML
    private TextField userSurnameField;

    @FXML
    private TextField userEmailField;

    @FXML
    private TextField userPhoneField;

    @FXML
    private AnchorPane cardInfoPane;

    @FXML
    private TextField cardNumber;

    @FXML
    private TextField cardOwner;

    @FXML
    private TextField cardEndDate;

    @FXML
    private PasswordField cardCVV;

    @FXML
    private Label errorLabel;

    @FXML
    private RadioButton bankCard;

    private boolean isBankCard = true;
    private Order orderInfo;
    private OrderDAO DBorders = new OrdersRepository();

    void setOrderInfo(Order orderInfo) {
        this.orderInfo = orderInfo;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setOpacity(0);

        userPhoneField.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue.replaceAll("\\+", "").length() > 11) {
                userPhoneField.setText(oldValue);
            }
        }));

        cardNumber.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (((newValue.length() == 4) || (newValue.length() == 9) || (newValue.length() == 14)) && (oldValue.length() < newValue.length()) && !(newValue.equals(""))) {
                cardNumber.appendText(" ");
            }
            if (newValue.length() > 19) {
                cardNumber.setText(oldValue);
            }
        }));

        cardEndDate.textProperty().addListener(((observable, oldValue, newValue) -> {
            if ((newValue.length() == 2) && (oldValue.length() < newValue.length())) {
                cardEndDate.appendText("/");
            }
            if (newValue.length() > 5) {
                cardEndDate.setText(oldValue);
            }
        }));

        cardCVV.textProperty().addListener(((observable, oldValue, newValue) -> {
            if ((newValue.length() > 3) || !(newValue.matches("[0-9]*"))) {
                cardCVV.setText(oldValue);
            }
        }));
    }

    @FXML
    void acceptAction(ActionEvent event) throws IOException {
        if (isInputValid()) {
            errorLabel.setOpacity(0);

            orderInfo.setName(userNameField.getText());
            orderInfo.setSurname(userSurnameField.getText());
            orderInfo.setEmail(userEmailField.getText());
            orderInfo.setPhone(userPhoneField.getText());
            if (bankCard.isSelected()) {
                orderInfo.setPaymentMethod("банковской картой");
                orderInfo.setCardInfo(cardNumber.getText() + "-" + cardOwner.getText() + "-" + cardEndDate.getText() + "-" + cardCVV.getText());
            } else {
                orderInfo.setPaymentMethod("оплата при заселении");
            }

            try {
                DBorders.add(orderInfo);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Заказ отправлен");
            alert.setHeaderText("Ваше бронирование ожидает подтверждения");
            alert.setContentText("Вам позвонит администратор гостиницы");
            alert.showAndWait();
            cancelAction(event);
        } else {
            errorLabel.setOpacity(1);
        }
    }

    @FXML
    void cancelAction(ActionEvent event) throws IOException {
        Stage stage = ((Stage)((Node) event.getSource()).getScene().getWindow());
        stage.setTitle("Выбор номера гостиницы");
        Parent root = FXMLLoader.load(getClass().getResource("../view/UserMain.fxml"));
        stage.setScene(new Scene(root, stage.getScene().getWidth(), stage.getScene().getHeight()));
        stage.show();
    }

    public void bankCardSelected(ActionEvent event) {
        cardNumber.setStyle(null);
        cardOwner.setStyle(null);
        cardEndDate.setStyle(null);
        cardCVV.setStyle(null);
        isBankCard = true;
        cardInfoPane.setVisible(true);
    }


    public void cashSelected(ActionEvent event) {
        errorLabel.setOpacity(0);
        isBankCard = false;
        cardInfoPane.setVisible(false);
        cardNumber.setText("");
        cardOwner.setText("");
        cardEndDate.setText("");
        cardCVV.setText("");
    }

    private boolean isInputValid() {
        boolean flag = true;
        if (!(userNameField.getText().matches("[а-яА-Я]+")) && !(userNameField.getText().matches("[a-zA-Z]+"))) {
            flag = false;
            userNameField.setStyle("-fx-border-color: #ff232d");
        }

        if (!(userSurnameField.getText().matches("[а-яА-Я]+")) && !(userSurnameField.getText().matches("[a-zA-Z]+"))) {
            flag = false;
            userSurnameField.setStyle("-fx-border-color: #ff232d");
        }

        if (!(userEmailField.getText().matches("([a-zA-z0-9.\\-]+)(@)([a-zA-Z]+)(\\.)(ru|com)"))) {
            flag = false;
            userEmailField.setStyle("-fx-border-color: #ff232d");
        }

        if (!(userPhoneField.getText().matches("[+]?[0-9]{11}"))) {
            flag = false;
            userPhoneField.setStyle("-fx-border-color: #ff232d");
        }

        if (isBankCard) {
            if (!(cardNumber.getText().matches("([0-9]{4})(\\s)([0-9]{4})(\\s)([0-9]{4})(\\s)([0-9]{4})"))) {
                flag = false;
                cardNumber.setStyle("-fx-border-color: #ff232d");
            }

            if (!(cardOwner.getText().matches("([a-zA-Z]+)(\\s)([a-zA-Z]+)"))) {
                flag = false;
                cardOwner.setStyle("-fx-border-color: #ff232d");
            }

            if (!(cardEndDate.getText().matches("([0-9]{2})(/)[0-9]{2}"))) {
                flag = false;
                cardEndDate.setStyle("-fx-border-color: #ff232d");
            }

            if (!(cardCVV.getText().matches("[0-9]{3}"))) {
                flag = false;
                cardCVV.setStyle("-fx-border-color: #ff232d");
            }
        }
        return flag;
    }

    public void updateFields(KeyEvent keyEvent) {
        TextField textField = (TextField) keyEvent.getSource();
        textField.setStyle(null);
    }
}
