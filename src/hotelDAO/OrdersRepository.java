package hotelDAO;

import objects.Order;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrdersRepository implements OrderDAO {

    private Connection connection;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;
    private static final String URL = "jdbc:postgresql://localhost:5432/DBtest";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "12345";

    private static final String SQL_INSERT_ORDER =
            "INSERT INTO orders VALUES(?,?,?,?,?,?,?,?,?)";

    private static final String SQL_DELETE_BY_NUMBER =
            "DELETE FROM orders WHERE roomnumber = ?";

    private static final String SQL_UPDATE_ORDER_STATUS =
            "UPDATE orders SET status = ? WHERE roomnumber = ?";

    private static final String SQL_GET_BY_STATUS =
            "SELECT * FROM orders WHERE status = ?";


    public OrdersRepository() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void add(Order model) throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        preparedStatement = connection.prepareStatement(SQL_INSERT_ORDER);
        preparedStatement.setString(1,model.getRoomNumber());
        preparedStatement.setString(2, model.getEntryDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        preparedStatement.setString(3, model.getExitDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        preparedStatement.setString(4, model.getSurname());
        preparedStatement.setString(5, model.getName());
        preparedStatement.setString(6, model.getEmail());
        preparedStatement.setString(7, model.getPhone());
        preparedStatement.setString(8, model.getPaymentMethod());
        preparedStatement.setInt(9, model.getTotalSum());

        preparedStatement.execute();
        connection.close();
    }

    @Override
    public void delete(String roomNumber) throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        preparedStatement = connection.prepareStatement(SQL_DELETE_BY_NUMBER);
        preparedStatement.setString(1, roomNumber);
        preparedStatement.execute();
        connection.close();
    }

    @Override
    public void updateStatus(String roomNumber, String newStatus) throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        preparedStatement = connection.prepareStatement(SQL_UPDATE_ORDER_STATUS);
        preparedStatement.setString(1, newStatus);
        preparedStatement.setString(2, roomNumber);

        preparedStatement.executeUpdate();
        connection.close();
    }

    @Override
    public List<Order> getByStatus(String status) throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        preparedStatement = connection.prepareStatement(SQL_GET_BY_STATUS);
        preparedStatement.setString(1, status);
        resultSet = preparedStatement.executeQuery();

        List<Order> orders = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        while (resultSet.next()) {
            orders.add(new Order(resultSet.getString("roomnumber"),
                    LocalDate.parse(resultSet.getString("entrydate"), formatter),
                    LocalDate.parse(resultSet.getString("exitdate"), formatter),
                    resultSet.getInt("totalsum"),
                    resultSet.getString("surname"),
                    resultSet.getString("name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone"),
                    resultSet.getString("paymentmethod")));
        }
        connection.close();
        return orders;
    }
}
