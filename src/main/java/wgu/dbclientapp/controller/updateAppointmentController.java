package wgu.dbclientapp.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import wgu.dbclientapp.model.*;
import wgu.dbclientapp.sqlQuery.AppointmentQuery;
import wgu.dbclientapp.sqlQuery.ContactQuery;
import wgu.dbclientapp.sqlQuery.CustomerQuery;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

/**
 * Controller for the update appointment page class
 */
public class updateAppointmentController implements Initializable {

    @FXML
    private ComboBox<String> aptContactDropBox;

    @FXML
    private TextField aptCustIdTextField;

    @FXML
    private TextArea aptDescriptionTextArea;

    @FXML
    private DatePicker aptEndDate;

    @FXML
    private ComboBox<String> aptEndHour;

    @FXML
    private ComboBox<String> aptEndMinute;

    @FXML
    private TextField aptIDtextfield;

    @FXML
    private TextField aptLocaTextField;

    @FXML
    private DatePicker aptStartDate;

    @FXML
    private ComboBox<String> aptStartHour;

    @FXML
    private ComboBox<String> aptStartMinute;

    @FXML
    private TextField aptTitleTextField;

    @FXML
    private TextField aptTypeTextField;

    @FXML
    private TextField aptUserIdTextField;

    @FXML
    private Button updateAppointmentButton;

    private Appointment selectedAppointment;
    ObservableList<String> hours = FXCollections.observableArrayList(
            "01","02","03","04","05","06","07","08","09","10","11","12",
            "13","14","15","16","17","18","19","20","21","22","23"
    );
    ObservableList<String> timeIncrement = FXCollections.observableArrayList(
            "00","10","15","20","25","30","35","40","45","50","55"
    );

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();
    Alert alert= new Alert(Alert.AlertType.ERROR);

    /**
     * Warning alert
     * @param title title of warning
     * @param header header of warning
     * @param content main body of warning
     */
    void warning( String title, String header, String content) {
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Input validation to make sure there are no empty text field.
     */
    void emptyTxtfield() {
        if (aptTitleTextField.getText().isBlank() || aptLocaTextField.getText().isBlank() || aptContactDropBox.getSelectionModel().isEmpty()||
                aptTypeTextField.getText().isBlank() || aptDescriptionTextArea.getText().isBlank() || aptUserIdTextField.getText().isBlank() ||
                aptCustIdTextField.getText().isBlank() || aptStartDate.getValue() == null || aptEndDate.getValue() == null ||
                aptStartHour.getSelectionModel().isEmpty() || aptStartMinute.getSelectionModel().isEmpty() ||
                aptEndHour.getSelectionModel().isEmpty() || aptEndMinute.getSelectionModel().isEmpty()) {
            warning("Empty Field", "Empty Field", "Please do not leave any of the fields empty");
        }
    }

    /**
     * Update Appointment button on click events.
     * Updates the currently selected appointment class.
     * @param event event
     * @throws SQLException exception for SQL handling.
     */
    @FXML
    void updateAppointment(ActionEvent event) throws SQLException {
        emptyTxtfield();
        Appointment selectedAppointment = mainAppointmentController.getSelectedAppointment();
        int currentindex = AppointmentList.indexOf(selectedAppointment);
        /* This gets the values of all the variables to be converted to UTC */
        LocalDate startApptDate = aptStartDate.getValue();
        int startYear = startApptDate.getYear();
        int startMonth = startApptDate.getMonthValue();
        int startDay = startApptDate.getDayOfMonth();
        int startHour = Integer.parseInt(aptStartHour.getSelectionModel().getSelectedItem());
        int startMin = Integer.parseInt(aptStartMinute.getSelectionModel().getSelectedItem());

        LocalDate endApptDate = aptEndDate.getValue();
        int endYear = endApptDate.getYear();
        int endMonth = endApptDate.getMonthValue();
        int endDay = endApptDate.getDayOfMonth();
        int endHour = Integer.parseInt(aptEndHour.getSelectionModel().getSelectedItem());
        int endMin = Integer.parseInt(aptEndMinute.getSelectionModel().getSelectedItem());

        /* Variables to be added into the classes */
        int appointmentId = Integer.parseInt(aptIDtextfield.getText());
        String title = aptTitleTextField.getText();
        String location = aptLocaTextField.getText();
        String contact = aptContactDropBox.getSelectionModel().getSelectedItem().toString();
        String type = aptTypeTextField.getText();
        String description = aptDescriptionTextArea.getText();
        int userId = Integer.parseInt(aptUserIdTextField.getText());
        int customerId = Integer.parseInt(aptCustIdTextField.getText());
        String startDate = aptStartDate.getValue().toString();
        String endDate = aptEndDate.getValue().toString();

        String startTime = aptStartHour.getSelectionModel().getSelectedItem().toString() + ":" + aptStartMinute.getSelectionModel().getSelectedItem().toString()
                + ":00";
        String endTime = aptEndHour.getSelectionModel().getSelectedItem().toString() + ":" + aptEndMinute.getSelectionModel().getSelectedItem().toString()
                + ":00";
        String start = startDate + " " + startTime;
        String end = endDate + " " + endTime;

        String currentTime = dtf.format(now);
        String currentUser = String.valueOf(loginController.getCurrentUser().getUsername());
        int contactId = ContactQuery.getContactId(contact);
        String createDate = AppointmentQuery.getCreateDate(appointmentId);
        String createdBy = AppointmentQuery.getCreatedBy(appointmentId);
        String contactName = ContactQuery.getContact(contactId);
        Customer newCustomer = CustomerList.getCustomer(customerId);

        /* Converts from localtime to UTC to be inserted into database */
        String utcStart = TimeConversion.localToUTC(startYear, startMonth, startDay, startHour, startMin);
        String utcEnd = TimeConversion.localToUTC(endYear, endMonth, endDay,endHour,endMin);

        /* Converts from LocalTime to EST for the input validation office hour */
        int estStartHour = TimeConversion.localToEST(startYear, startMonth, startDay, startHour, startMin);
        int estEndHour = TimeConversion.localToEST(endYear, endMonth, endDay,endHour,endMin);

        if (estStartHour < 8 || estEndHour >= 22) {
            warning("Cannot Schedule Appointment", "Exceeded office hours", "Please choose a time within normal office hours");
            return;
        }

        if (startHour > endHour) {
            warning("Cannot Schedule Appointment", "The end hour cannot be earlier than the start hour", "Please change the time.");
            return;
        }
        if (LocalDate.parse(startDate).compareTo(LocalDate.parse(endDate)) > 0 ) {
            warning("Cannot Schedule Appointment", "The start date cannot exceed the end date", "Please change the date.");
            return;
        }


        for (Appointment appointment: AppointmentList.getAllAppointment()) {

            if (appointment.getCustomerId() == customerId) {
                if (appointment.getAppointmentId() != appointmentId) {
                    if (LocalDate.parse(appointment.getStartDate().split(" ")[0]).compareTo(LocalDate.parse(startDate.split(" ")[0])) == 0) {
                        int existStartHour = Integer.parseInt(appointment.getStartDate().substring(11,13));
                        int existStartMin = Integer.parseInt(appointment.getStartDate().substring(14,16));
                        int existEndHour = Integer.parseInt(appointment.getEndDate().substring(11,13));
                        int existEndMin = Integer.parseInt(appointment.getEndDate().substring(14,16));
                        LocalTime startTime1 = LocalTime.of( existStartHour, existStartMin );
                        LocalTime stopTime1= LocalTime.of( existEndHour , existEndMin );
                        LocalTime startTime2 = LocalTime.of(startHour  , startMin);
                        LocalTime stopTime2 = LocalTime.of( endHour , endMin );

                        Boolean isOverLap = ((startTime1.isBefore( stopTime2 )) && ( stopTime1.isAfter( startTime2 )));
                        if (isOverLap) {
                            warning("Cannot Schedule appointment", "Customer has overlapping appointments", "Please schedule another time for this customer.");
                            return;

                        }
                        }
                    }
                }

            }


            /* Adding all the information into the database, appointment class and appointment list class */

            Appointment newAppointment = new Appointment(appointmentId, title,description,location, type,
                    start,end,createDate,createdBy,currentTime,currentUser,customerId,userId,contactId);
            newAppointment.setContactName(contactName);
            newCustomer.addAssociatedAppointment(newAppointment);
            AppointmentList.updateList(currentindex,newAppointment);
            AppointmentQuery.update(title,description,location, type,utcStart, utcEnd, currentTime,currentUser,customerId,userId,contactId,appointmentId);




            updateAppointmentButton.getScene().getWindow().hide();
    }

    /**
     * On initialize, all the text fields are populated with the selected appointment's information
     * @param url url
     * @param resourceBundle resourcebundle.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selectedAppointment = mainAppointmentController.getSelectedAppointment();
        String hour = selectedAppointment.getStartDate().substring(10,13);
        System.out.println(hour);

        aptIDtextfield.setText(String.valueOf(selectedAppointment.getAppointmentId()));
        aptTitleTextField.setText(selectedAppointment.getTitle());
        aptLocaTextField.setText(selectedAppointment.getLocation());
        aptTypeTextField.setText(selectedAppointment.getType());
        aptDescriptionTextArea.setText(selectedAppointment.getDescription());
        aptUserIdTextField.setText(String.valueOf(selectedAppointment.getUserId()));
        aptCustIdTextField.setText(String.valueOf(selectedAppointment.getCustomerId()));
        aptStartDate.setValue(LocalDate.parse(selectedAppointment.getStartDate().split(" ")[0]));
        aptStartHour.setValue(selectedAppointment.getStartDate().substring(11,13));
        aptStartMinute.setValue(selectedAppointment.getStartDate().substring(14,16));
        aptEndDate.setValue(LocalDate.parse(selectedAppointment.getEndDate().split(" ")[0]));
        aptEndHour.setValue(selectedAppointment.getEndDate().substring(11,13));
        aptEndMinute.setValue(selectedAppointment.getEndDate().substring(14,16));
        try {
            aptContactDropBox.setValue(ContactQuery.getContact(selectedAppointment.getContactId()));
            aptContactDropBox.setItems(ContactQuery.getAllContact());
            aptStartHour.setItems(hours);
            aptEndHour.setItems(hours);
            aptStartMinute.setItems(timeIncrement);
            aptEndMinute.setItems(timeIncrement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
