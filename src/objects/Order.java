package objects;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Order {
    private String roomNumber;
    private LocalDate entryDate;
    private LocalDate exitDate;
    private String guestsNumber;
    private Integer totalSum;
    private String surname;
    private String name;
    private String email;
    private String phone;
    private String paymentMethod;
    private String cardInfo;

    public Order(String roomNumber, LocalDate entryDate, LocalDate exitDate, String guestsNumber, Integer totalSum) {
        this.roomNumber = roomNumber;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.guestsNumber = guestsNumber;
        this.totalSum = totalSum;
        this.surname = "";
        this.name = "";
        this.email = "";
        this.phone = "";
        this.paymentMethod = "";
        this.cardInfo = "-";
    }

    public Order(String roomNumber, LocalDate entryDate, LocalDate exitDate, Integer totalSum, String surname, String name, String email, String phone, String paymentMethod) {
        this.roomNumber = roomNumber;
        this.entryDate = entryDate;
        this.exitDate = exitDate;
        this.totalSum = totalSum;
        this.surname = surname;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.paymentMethod = paymentMethod;
        this.cardInfo = "";
        this.guestsNumber = "";
    }

    public Order(String line) {
        String[] data = line.split("\\|");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.roomNumber = data[0];
        this.entryDate = LocalDate.parse(data[1], formatter);
        this.exitDate = LocalDate.parse(data[2], formatter);
        this.totalSum = Integer.parseInt(data[3]);
        this.surname = data[4];
        this.name = data[5];
        this.email = data[6];
        this.phone = data[7];
        this.paymentMethod = data[8];
        this.cardInfo = data[9];
    }



    @Override
    public String toString() {
        String separator = "|";
        return roomNumber + separator + entryDate + separator + exitDate + separator + totalSum + separator + surname + separator + name + separator + email + separator + phone + separator + paymentMethod + separator + cardInfo;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public LocalDate getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(LocalDate entryDate) {
        this.entryDate = entryDate;
    }

    public LocalDate getExitDate() {
        return exitDate;
    }

    public void setExitDate(LocalDate exitDate) {
        this.exitDate = exitDate;
    }

    public String getGuestsNumber() {
        return guestsNumber;
    }

    public void setGuestsNumber(String guestsNumber) {
        this.guestsNumber = guestsNumber;
    }

    public Integer getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(Integer totalSum) {
        this.totalSum = totalSum;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getCardInfo() {
        return cardInfo;
    }

    public void setCardInfo(String cardInfo) {
        this.cardInfo = cardInfo;
    }
}
