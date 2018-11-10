package hotelDAO;

import objects.Order;
import objects.Room;

import java.sql.SQLException;
import java.util.List;

public interface OrderDAO{

    void add(Order model) throws SQLException;

    void delete(String roomNumber) throws SQLException;

    void updateStatus(String roomNumber, String newStatus) throws SQLException;

    List<Order> getByStatus(String status) throws SQLException;
}
