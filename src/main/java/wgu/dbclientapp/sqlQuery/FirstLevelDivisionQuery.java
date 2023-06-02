package wgu.dbclientapp.sqlQuery;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import wgu.dbclientapp.controller.addCustomerController;

/**
 * SQL Queries to the firstLevelDivision table in database
 */
public abstract class FirstLevelDivisionQuery {
    /**
     * Gets the division by the id
     * @param id id of division to get
     * @return the division
     * @throws SQLException sqlException
     */
    public static String getDivision(int id) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        String division = null;

        while (rs.next()) {
            division = rs.getString("Division");
            System.out.print(division+ "\n");

        }
        return division;
    }

    /**
     * Gets the country code from division by id
     * @param id id of row to get
     * @return the country code
     * @throws SQLException sqlexception
     */
    public static int getCountryCode(int id) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        int countryCode = 0;
        while (rs.next()) {
            countryCode = rs.getInt("Country_ID");
            System.out.print(countryCode+ "\n");

        }
        return countryCode;
    }

    /**
     * Gets the division id with the division
     * @param division division of the division id
     * @return the division id
     * @throws SQLException sqlException
     */
    public static int getDivisionId(String division) throws SQLException {
        String sql = "SELECT * FROM first_level_divisions WHERE Division = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, division);
        ResultSet rs = ps.executeQuery();
        int divisionId = 0;
        while (rs.next()) {
            divisionId = rs.getInt("Division_ID");

        }
        return divisionId;
    }

    /**
     * Updates the combo box of the add/update customer page.
     * @param country country of the divisons
     * @return the divisions depending on which country is chosen
     * @throws SQLException sqlException
     */
    public static ObservableList getDivision(String country) throws SQLException {
        ObservableList<String> divisionList = FXCollections.observableArrayList();

        int countryCode = 0;
        if (country == "U.S") {
            countryCode =1 ;
        } else if (country == "UK") {
            countryCode =2;
        } else {
            countryCode =3;
    }

        String sql = "SELECT * FROM first_level_divisions WHERE Country_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, countryCode);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String division = rs.getString("Division");
            divisionList.add(division);
        }
        return divisionList;
    }


}
