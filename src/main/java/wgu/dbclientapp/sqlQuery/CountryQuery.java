package wgu.dbclientapp.sqlQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Query for the country table
 */
public abstract class CountryQuery {
    /**
     * Get country name by id
     * @param id the id of the country
     * @return the country
     * @throws SQLException sqlException
     */
    public static String getCountry(int id) throws SQLException {
        String sql = "SELECT * FROM countries WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        String country = null;
        while (rs.next()) {
            country = rs.getString("Country");
        }
        return country;
    }

}
