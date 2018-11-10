package hotelDAO;

import java.sql.SQLException;

public interface DataDAO {
    boolean checkData(String login, String password) throws SQLException;
}
