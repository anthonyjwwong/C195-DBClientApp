package wgu.dbclientapp.sqlQuery;

import wgu.dbclientapp.model.Customer;
import wgu.dbclientapp.model.CustomerList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * SQL query for the customer table
 */
public abstract class CustomerQuery {

    /**
     * Populates the customer tableview with customer table in database.
     * @throws SQLException sqlException
     */
    public static void populateTable() throws SQLException {
        String sql = "SELECT * FROM CUSTOMERS";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();


        while (rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String number = rs.getString("Phone");
            String createdDate= rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");
            String firstLevelDiv = FirstLevelDivisionQuery.getDivision(divisionId);
            int country_code = FirstLevelDivisionQuery.getCountryCode(divisionId);
            String country = CountryQuery.getCountry(country_code);



            Customer addNewCustomer = new Customer(customerId, name,address, postalCode, number, firstLevelDiv, country, createdDate, createdBy, lastUpdate, lastUpdatedBy);
            CustomerList.addCustomer(addNewCustomer);
        }
    }

    /**
     * Inserts a row of customer information into table in database
     * @param id id
     * @param name name
     * @param address address
     * @param postal postal
     * @param phone phone
     * @param createDate createDate
     * @param createBy createBy
     * @param updateDate updateDate
     * @param updateBy updateBy
     * @param divisionId divisionid
     * @return Insertion of the row into the customer table
     * @throws SQLException sql exception
     */
    public static int insert(int id, String name, String address, String postal, String phone,
                             String createDate, String createBy, String updateDate, String updateBy, int divisionId) throws SQLException {
        String sql = "INSERT INTO CUSTOMERS VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setString(2, name);
        ps.setString(3, address);
        ps.setString(4, postal);
        ps.setString(5, phone);
        ps.setString(6, createDate);
        ps.setString(7, createBy);
        ps.setString(8, updateDate);
        ps.setString(9, updateBy);
        ps.setInt(10, divisionId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Updates a certain row in the customer table
     * @param id id
     * @param name name
     * @param address address
     * @param postal postal
     * @param phone phone
     * @param updateDate update date
     * @param updateBy update by
     * @param divisionId division id
     * @return Updated Row
     * @throws SQLException sqlexception
     */
    public static int update(int id,String name, String address, String postal, String phone,String updateDate, String updateBy, int divisionId) throws SQLException {
        String sql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?, Last_Update =? ,Last_Updated_By=?, Division_ID=? WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ps.setString(2, address);
        ps.setString(3, postal);
        ps.setString(4, phone);
        ps.setString(5, updateDate);
        ps.setString(6, updateBy);
        ps.setInt(7, divisionId);
        ps.setInt(8, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Getting the creatDate of a specific row in the table
     * @param id id of row
     * @return the createDate
     * @throws SQLException sqlException
     */
    public static String getCreateDate(int id) throws SQLException {
        String sql = "SELECT Create_Date FROM CUSTOMERS Where Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        String createdDate = null;
        while (rs.next()) {
            createdDate = rs.getString("Create_Date");
        }
        return createdDate;
    }

    /**
     * Getting the createdBy in the customer id
     * @param id id of the row to get the createdBy
     * @return the createdBy
     * @throws SQLException sqlException
     */
    public static String getCreatedBy(int id) throws SQLException {
        String sql = "SELECT Created_By FROM CUSTOMERS Where Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        String createdBy = null;
        while (rs.next()) {
            createdBy = rs.getString("Created_By");
        }
        return createdBy;
    }

    /**
     * Deletes a specific row from the table
     * @param id id of the rows to be deleted
     * @return the deletion of the row
     * @throws SQLException sqlException
     */
    public static int delete(int id) throws SQLException {
        String sql = "DELETE FROM customers WHERE Customer_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }
}
