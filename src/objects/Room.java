package objects;

public class Room {
    private String roomNumber;
    private String roomRate;
    private String roomCost;
    private String guestsNumber;

    public Room(String line) {
        String[] data = line.split("\\|");
        this.roomNumber = data[0];
        this.roomRate = "Рейтинг: " + data[1];
        this.roomCost = "Цена: " + data[2];
        this.guestsNumber = data[3];
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(String roomRate) {
        this.roomRate = roomRate;
    }

    public String getRoomCost() {
        return roomCost;
    }

    public void setRoomCost(String roomCost) {
        this.roomCost = roomCost;
    }

    public String getGuestsNumber() {
        return guestsNumber;
    }

    public void setGuestsNumber(String guestsNumber) {
        this.guestsNumber = guestsNumber;
    }
}
