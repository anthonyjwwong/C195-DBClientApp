package wgu.dbclientapp.sqlQuery;

import wgu.dbclientapp.model.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * All the query to the appointment Table of the SQL DATABASe
 */
public abstract class AppointmentQuery {
    /**
     * Populates the tableview of the main appointment page with the appointment table information
     * @throws SQLException sql exception
     */
    public static void populateTable() throws SQLException {
        String sql = "SELECT * FROM appointments";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            int aptId = rs.getInt("Appointment_ID");
            String title=rs.getString("Title");
            String description = rs.getString("Description");
            String location = rs.getString("Location");
            String type = rs.getString("Type");
            String startDate = rs.getString("Start");
            String endDate = rs.getString("End");
            String createDate = rs.getString("Create_Date");
            String createdBy = rs.getString("Created_By");
            String lastUpdate = rs.getString("Last_Update");
            String lastUpdateBy =rs.getString("Last_Updated_By");
            int custId = rs.getInt("Customer_ID");
            int userId = rs.getInt("User_ID");
            int contactId = rs.getInt("Contact_ID");
            String contactName = ContactQuery.getContact(contactId);
            Customer newCustomer = CustomerList.getCustomer(custId);


            /* Getting the Start date information to convert to local time */
            String date = startDate.split(" ")[0];
            String time = startDate.split(" ")[1];
            int year =Integer.parseInt(date.split("-")[0]);
            int month =Integer.parseInt(date.split("-")[1]);
            int day =Integer.parseInt(date.split("-")[2]);
            int hour = Integer.parseInt(time.split(":")[0]);
            int min = Integer.parseInt(time.split(":")[1]);
            String localTimeStart = TimeConversion.utcToLocal(year, month, day, hour ,min);


            /* Getting the end date information to convert to local time */
            String eDate = endDate.split(" ")[0];
            String eTime = endDate.split(" ")[1];
            int eYear =Integer.parseInt(eDate.split("-")[0]);
            int eMonth =Integer.parseInt(eDate.split("-")[1]);
            int eDay =Integer.parseInt(eDate.split("-")[2]);
            int eHour = Integer.parseInt(eTime.split(":")[0]);
            int eMin = Integer.parseInt(eTime.split(":")[1]);
            String localTimeEnd = TimeConversion.utcToLocal(eYear, eMonth, eDay, eHour ,eMin);


            Appointment newAppointment = new Appointment(aptId, title, description, location, type, localTimeStart,
                    localTimeEnd, createDate,createdBy, lastUpdate, lastUpdateBy, custId, userId, contactId);


            newCustomer.addAssociatedAppointment(newAppointment);

            newAppointment.setContactName(contactName);

            AppointmentList.addAppointment(newAppointment);

        }


    }

    /**
     * Inserts into the database appointment table
     * @param id id
     * @param title title
     * @param description description
     * @param location location
     * @param type type
     * @param start start
     * @param end end
     * @param createDate createDate
     * @param createBy createBy
     * @param updateDate updateDate
     * @param updateBy updateBy
     * @param customerId customerId
     * @param userId userId
     * @param contactId contactId
     * @return insert into table
     * @throws SQLException sql execption
     */
    public static int insert(int id, String title, String description, String location, String type, String start, String end,
                             String createDate, String createBy, String updateDate, String updateBy, int customerId, int userId, int contactId) throws SQLException {
        String sql = "INSERT INTO appointments VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ps.setString(2, title);
        ps.setString(3, description);
        ps.setString(4, location);
        ps.setString(5, type);
        ps.setString(6, start);
        ps.setString(7, end);
        ps.setString(8, createDate);
        ps.setString(9, createBy);
        ps.setString(10, updateDate);
        ps.setString(11, updateBy);
        ps.setInt(12,customerId);
        ps.setInt(13, userId);
        ps.setInt(14,contactId);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Updates a certain row in the appointment table
     * @param title title
     * @param description description
     * @param location location
     * @param type type
     * @param start start
     * @param end end
     * @param updateDate updateDate
     * @param updateBy updateBy
     * @param customerId customerId
     * @param userId userId
     * @param contactId contactId
     * @param id id
     * @return Updates a certain row in the table
     * @throws SQLException sql exception
     */
    public static int update(String title, String description, String location, String type, String start, String end,
                              String updateDate, String updateBy, int customerId, int userId, int contactId, int id) throws SQLException {
        String sql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type =? ,Start=?, End=?" +
                ",Last_Update=?,Last_Updated_By=?, Customer_ID=?, User_ID=?, Contact_ID=? WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, title);
        ps.setString(2, description);
        ps.setString(3, location);
        ps.setString(4, type);
        ps.setString(5, start);
        ps.setString(6, end);
        ps.setString(7, updateDate);
        ps.setString(8, updateBy);
        ps.setInt(9,customerId);
        ps.setInt(10, userId);
        ps.setInt(11,contactId);
        ps.setInt(12, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Getting the create date from the appointment table by id.
     * @param id id to get createdate from
     * @return the date
     * @throws SQLException sql exception
     */
    public static String getCreateDate(int id) throws SQLException {
        String sql = "SELECT Create_Date FROM appointments Where Appointment_ID = ?";
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
     * Gets the createdBy information from the appointment table by id.
     * @param id id to get the createdBy information from
     * @return the createdBy information
     * @throws SQLException sql exception
     */
    public static String getCreatedBy(int id) throws SQLException {
        String sql = "SELECT Created_By FROM appointments Where Appointment_ID = ?";
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
     * Deletes from the appointment table by id.
     * @param id id of row to be deleted
     * @return deletion of a row
     * @throws SQLException sql exception
     */
    public static int delete(int id) throws SQLException {
        String sql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        int rowsAffected = ps.executeUpdate();
        return rowsAffected;
    }

    /**
     * Count the number of same type in the appointment table.
     * @param type the type to count
     * @return the number of same type in the table
     * @throws SQLException sql exception.
     */
    public static int countType(String type) throws SQLException {
        String sql = "SELECT COUNT(Type) FROM appointments WHERE Type = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, type);
        ResultSet rs = ps.executeQuery();
        int typecounter = 0;
        while(rs.next()) {
            typecounter = rs.getInt("COUNT(Type)");
        }
        return typecounter;
    }


}
