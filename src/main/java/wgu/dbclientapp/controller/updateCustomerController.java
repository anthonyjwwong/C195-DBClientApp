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
import wgu.dbclientapp.model.Customer;
import wgu.dbclientapp.model.CustomerList;
import wgu.dbclientapp.sqlQuery.CustomerQuery;
import wgu.dbclientapp.sqlQuery.FirstLevelDivisionQuery;

/**
 * Controller for the update customer page.
 */
public class updateCustomerController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button updateCustomerButton;

    @FXML
    private TextField cAddText;

    @FXML
    private ComboBox<String> cCountryDrop;

    @FXML
    private ComboBox<String> cDivisionDrop;

    @FXML
    private TextField cNameText;

    @FXML
    private TextField cPhoneText;

    @FXML
    private TextField cPostalText;

    @FXML
    private TextField cId;

    private Customer selectedCustomer;

    ObservableList<String> countryList = FXCollections.observableArrayList("U.S", "UK", "Canada");
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime now = LocalDateTime.now();

    /**
     * Event Handler for the country drop box.
     * When the country is selected, set the firstleveldivision dropbox with the corresponding countries or states.
     * @param event event
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
     * Event Handler for the update customer button.
     * Update the customer class in the index provided.
     * @param event click event
     * @throws SQLException exception for SQL handling.
     */
    @FXML
    void updateCustomer(ActionEvent event) throws SQLException {
        int id = Integer.parseInt(cId.getText());
        String name = cNameText.getText();
        String address = cAddText.getText();
        String division = cDivisionDrop.getSelectionModel().getSelectedItem().toString();
        String country = cCountryDrop.getSelectionModel().getSelectedItem().toString();
        String phone = cPhoneText.getText();
        String postal = cPostalText.getText();
        int divisionId = FirstLevelDivisionQuery.getDivisionId(division);
        String currentTime = dtf.format(now);
        String currentUser = String.valueOf(loginController.getCurrentUser().getUsername());
        String createDate = CustomerQuery.getCreateDate(id);
        String createdBy = CustomerQuery.getCreatedBy(id);

        CustomerQuery.update(id,name,address,postal,phone,currentTime,currentUser,divisionId);
        Customer newCustomer = new Customer(id,name, address, postal,phone,division, country, createDate, createdBy, currentTime, currentUser );
        CustomerList.updateList(id-1, newCustomer);
        updateCustomerButton.getScene().getWindow().hide();
    }


    /**
     * Initialize method for when page loads.
     * Populate all the text field with the selectedcustomer's information.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        selectedCustomer = mainCustomerController.selectedCustomer();


        cId.setText(String.valueOf(selectedCustomer.getId()));
        cNameText.setText(String.valueOf(selectedCustomer.getName()));
        cAddText.setText(String.valueOf(selectedCustomer.getAddress()));
        cPostalText.setText(String.valueOf(selectedCustomer.getPostalCode()));
        cPhoneText.setText(String.valueOf(selectedCustomer.getPhone()));
        cDivisionDrop.setValue(selectedCustomer.getDivision());
        cCountryDrop.setValue(selectedCustomer.getCountry());
        cCountryDrop.setItems(countryList);
        String country = cCountryDrop.getSelectionModel().getSelectedItem().toString();
        try {
            cDivisionDrop.setItems(FirstLevelDivisionQuery.getDivision(country));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
