package wgu.dbclientapp.sqlQuery;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * All the Queries done with the contact table
 */
public abstract class ContactQuery {
    /**
     * Gets contact name from table by id.
     * @param id id of the contact
     * @return the contact name
     * @throws SQLException sqlException
     */
    public static String getContact(int id) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE Contact_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        String contactName = null;
        while (rs.next()) {
            contactName = rs.getString("Contact_Name");
        }

        return contactName;
    }

    /**
     * Get contactId by the name
     * @param name name of the id to get
     * @return the id of the name
     * @throws SQLException sqlException
     */
    public static int getContactId(String name) throws SQLException {
        String sql = "SELECT * FROM contacts WHERE Contact_Name = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();
        int contactId = 0;
        while (rs.next()) {
            contactId = rs.getInt("Contact_ID");
        }

        return contactId;
    }

    /**
     * Get all contact from the table
     * @return an observablelist of the contacts.
     * @throws SQLException sqlException
     */
    public static ObservableList getAllContact() throws SQLException {
        ObservableList<String> contactList = FXCollections.observableArrayList();

        String sql = "SELECT * FROM contacts";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            String contact = rs.getString("Contact_Name");
            contactList.add(contact);
        }
        return contactList;
    }
}
