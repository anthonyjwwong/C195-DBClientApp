package wgu.dbclientapp.controller;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import wgu.dbclientapp.sqlQuery.CustomerQuery;
import wgu.dbclientapp.model.Customer;
import wgu.dbclientapp.model.CustomerList;
import wgu.dbclientapp.sqlQuery.FirstLevelDivisionQuery;

/**
 * Add Customer Controller - Controller for Add Customer page.
 */
public class addCustomerController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button addCustomerButton;

    @FXML
    private TextField cAddText;

    @FXML
    private ComboBox<String> cCountryDrop;

    @FXML
    private ComboBox<?> cDivisionDrop;

    @FXML
    private TextField cNameText;

    @FXML
    private TextField cPhoneText;

    @FXML
    private TextField cPostalText;

    ObservableList<String> countryList = FXCollections.observableArrayList("U.S", "UK", "Canada");

    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    /**
     * Action Event on Add Customer button.
     * Adds the User input into a Customer Class, and the table view.
     * @param event
     * @throws SQLException
     */
    @FXML
    void addCustomer(ActionEvent event) throws SQLException {
        int id = CustomerList.getAllCustomer().size() + 1;
        String name = cNameText.getText();
        String address = cAddText.getText();
        String division = cDivisionDrop.getSelectionModel().getSelectedItem().toString();
        String country = cCountryDrop.getSelectionModel().getSelectedItem().toString();
        String phone = cPhoneText.getText();
        String postal = cPostalText.getText();
        int divisionId = FirstLevelDivisionQuery.getDivisionId(division);
        String currentTime = dtf.format(now);
        String currentUser = String.valueOf(loginController.getCurrentUser().getUsername());

        CustomerQuery.insert(id, name, address, postal, phone, currentTime, currentUser, currentTime, currentUser, divisionId);
        Customer newCustomer = new Customer(id, name, address, postal, phone, division, country, currentTime, currentUser , currentTime, currentUser);
        CustomerList.addCustomer(newCustomer);
        addCustomerButton.getScene().getWindow().hide();
    }

    /**
     * Event Handler for the drop box
     * When the Country for the drop box is selected, populates the other dropbox with the connecting first level divisions.
     * @param event
     */
    @FXML
    void onCountryDropDown(ActionEvent event) {
        String country = cCountryDrop.getSelectionModel().getSelectedItem().toString();
        try {
            cDivisionDrop.setItems(FirstLevelDivisionQuery.getDivision(country));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Initialize method for when page loads.
     * Set the items in the dropboxes.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cCountryDrop.setItems(countryList);
        cCountryDrop.setValue("U.S");
        try {
            cDivisionDrop.setItems(FirstLevelDivisionQuery.getDivision("U.S"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}