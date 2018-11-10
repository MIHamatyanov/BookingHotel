package hotelDAO;

import objects.Order;
import objects.Room;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RoomsRepository implements RoomDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private static final String URL = "jdbc:postgresql://localhost:5432/DBtest";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "12345";

    private static final String SQL_GET_ROOM_BY_CAPACITY =
            "SELECT * FROM rooms WHERE roomcapacity = ? ORDER BY roomnumber ASC";

    private static final String SQL_GET_ROOM_BY_NUMBER =
            "SELECT * FROM rooms WHERE roomnumber = ?";

    private static final String SQL_INSERT_ROOM =
            "INSERT INTO rooms VALUES(?,?,?,?)";

    private static final String SQL_DELETE_BY_NUMBER =
            "DELETE FROM rooms WHERE roomnumber = ?";

    private static final String SQL_SELECT_ALL =
            "SELECT * FROM rooms ORDER BY roomnumber ASC";


    public RoomsRepository() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public Room getByRoomNumber(String roomNumber) throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        preparedStatement = connection.prepareStatement(SQL_GET_ROOM_BY_NUMBER);
        preparedStatement.setString(1, roomNumber);
        resultSet = preparedStatement.executeQuery();
        resultSet.next();

        connection.close();
        return new Room(resultSet.getString("roomnumber"),
                resultSet.getString("roomrate"),
                resultSet.getString("roomcost"),
                resultSet.getString("roomcapacity"));
    }

    @Override
    public void add(Room model) throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        preparedStatement = connection.prepareStatement(SQL_INSERT_ROOM);
        preparedStatement.setString(1, model.getRoomNumber());
        preparedStatement.setString(2, model.getRoomRate().split(" ")[1]);
        preparedStatement.setString(3, model.getRoomCost().split(" ")[1]);
        preparedStatement.setString(4, model.getGuestsNumber());
        preparedStatement.execute();
        connection.close();
    }

    @Override
    public void delete(String roomNumber) throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        preparedStatement = connection.prepareStatement(SQL_DELETE_BY_NUMBER);
        preparedStatement.setString(1, roomNumber);
        preparedStatement.executeUpdate();
        connection.close();
    }

    @Override
    public List<Room> getAll() throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        Statement statement = connection.createStatement();
        resultSet = statement.executeQuery(SQL_SELECT_ALL);
        List<Room> rooms = new ArrayList<>();
        while (resultSet.next()) {
            rooms.add(new Room(resultSet.getString("roomnumber"),
                    resultSet.getString("roomrate"),
                    resultSet.getString("roomcost"),
                    resultSet.getString("roomcapacity")));
        }
        connection.close();
        return rooms;
    }

    @Override
    public List<Room> getByCapacity(String capacity, LocalDate entryDate, LocalDate exitDate) throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        PreparedStatement statement = connection.prepareStatement("SELECT roomnumber, entrydate, exitdate FROM orders", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
        ResultSet set = statement.executeQuery();

        preparedStatement = connection.prepareStatement(SQL_GET_ROOM_BY_CAPACITY);
        preparedStatement.setString(1,capacity);
        resultSet = preparedStatement.executeQuery();

        List<Room> rooms = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while (resultSet.next()) {
            boolean flag = true;
            set.beforeFirst();
            while (set.next()) {
                if (resultSet.getString("roomnumber").equals(set.getString("roomnumber"))) {
                    LocalDate orderEntry = LocalDate.parse(set.getString("entrydate"), formatter);
                    LocalDate orderExit = LocalDate.parse(set.getString("exitdate"), formatter);

                    if ((!(entryDate.isBefore(orderEntry) && exitDate.isBefore(orderEntry))) && !(entryDate.isAfter(orderExit) && exitDate.isAfter(orderExit))) {
                        flag = false;
                    }
                }
            }
            if (flag) {
                rooms.add(new Room(resultSet.getString("roomnumber"),
                        resultSet.getString("roomrate"),
                        resultSet.getString("roomcost"),
                        resultSet.getString("roomcapacity")));
            }
        }
        connection.close();
        return rooms;
    }
}
