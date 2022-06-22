import java.sql.*;
import java.util.LinkedList;

public class RunJDBC {
    static final String DB_URL = "jdbc:mysql://localhost/jdbc_user";
    static final String USER = "anton";
    static final String PASS = "qwerty123";

    public static void main(String[] args) {

        LinkedList<User> userList = new LinkedList<>();
        userList.add(new User(25, "Petya"));
        userList.add(new User(18, "Vasya"));
        userList.add(new User(20, "Katya"));

        // Open a connection
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement();
        ) {

            insertIntoUsers(stmt, userList);
            LinkedList<User> findUserList = selectFromUsers(stmt, 18, 20);
            for (User user :
                    findUserList) {
                System.out.println(user.toString());
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertIntoUsers(Statement stmt, LinkedList<User> userList) throws SQLException {
        for (User user :
                userList) {
            String sql = "INSERT INTO users VALUES (" + user.getId() + ",'" + user.getName() + "'," + user.getAge() + ")";

            if (checkValuesInBase(stmt, user.getId())) {
                stmt.executeUpdate(sql);
            }
        }
    }

    public static LinkedList<User> selectFromUsers(Statement stmt, int fromAge, int toAge) throws SQLException {

        LinkedList<User> userList = new LinkedList<>();

        String sql = "SELECT * FROM users WHERE age BETWEEN " + fromAge + " AND " + toAge;
        ResultSet jdbc_user = stmt.executeQuery(sql);
        while (jdbc_user.next()) {
            userList.add(new User(jdbc_user.getInt("age"), jdbc_user.getString("name")));
        }

        return userList;
    }

    public static boolean checkValuesInBase(Statement stmt, int id) {

        String sql = "SELECT * FROM users WHERE id = " + id;
        try {
            ResultSet user = stmt.executeQuery(sql);
            while (user.next()) {
                return false;
            }
        } catch (SQLException e) {
            return true;
        }
        return true;
    }

}
