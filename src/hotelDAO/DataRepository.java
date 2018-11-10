package hotelDAO;

import java.sql.*;

public class DataRepository implements DataDAO {

    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/DBtest";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "12345";

    public DataRepository() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public boolean checkData(String login, String password) throws SQLException {
        Connection connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
        PreparedStatement statement = connection.prepareStatement("SELECT password FROM privatedata WHERE login = ?");
        statement.setString(1,login);
        ResultSet set = statement.executeQuery();
        if (!set.next()) {
            return false;
        }
        connection.close();
        return set.getString("password").equals(password);

    }
}
