package wgu.dbclientapp.sqlQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL Query with the user table
 */
public abstract class UserQuery {
    /**
     * For individual testing. See all the users in the user table on the system log.
     * @throws SQLException sqlException
     */
    public static void select() throws SQLException {
        String sql = "SELECT * FROM USERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String userName = rs.getString("User_Name");
            String password = rs.getString("Password");
            System.out.print(userId + "|");
            System.out.print(userName + "|");
            System.out.print(password + "\n");
        }
    }


    /**
     * Checks to see if the entered username and password exist in the table
     * @param username username to validate
     * @param password password to validate
     * @return true or false depending on whether the username and password is found in the table
     * @throws SQLException sqlexception.
     */
    public static boolean loginCheck(String username, String password) throws SQLException {
        String sql = "SELECT * FROM USERS WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            return false;
        }

        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            String dbUserName = rs.getString("User_Name");
            String dbPassword = rs.getString("Password");
            System.out.print(userId + "|");
            System.out.print(dbUserName + "|");
            System.out.print(dbPassword + "\n");
            return true;
        }
        return true;
    }
}

