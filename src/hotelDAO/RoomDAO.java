package hotelDAO;

import objects.Room;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface RoomDAO{
    Room getByRoomNumber(String roomNumber) throws SQLException;

    void add(Room model) throws SQLException;

    void delete(String roomNumber) throws SQLException;

    List<Room> getAll() throws SQLException;

    List<Room> getByCapacity(String capacity, LocalDate entryDate, LocalDate exitDate) throws SQLException;
}
