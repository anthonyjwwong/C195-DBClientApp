package wgu.dbclientapp.controller;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import wgu.dbclientapp.model.User;
import wgu.dbclientapp.sqlQuery.AppointmentQuery;
import wgu.dbclientapp.sqlQuery.CustomerQuery;
import wgu.dbclientapp.sqlQuery.UserQuery;
import wgu.dbclientapp.util.LogActivity;

public class loginController implements Initializable {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Label labelName;

    @FXML
    private Label labelPassword;

    @FXML
    private Button loginButton;

    @FXML
    private Label warningLabel;

    @FXML
    private TextField textName;

    @FXML
    private TextField textPassword;

    @FXML
    private Label currentLocation;

    static User currentUser;
    private String warningTitle = "Login Error";
    private String warningHeader = "Login Failed";
    private String warningContent = "Login has failed, please try again";

    /**
     *  Gets the current user that just loggedin
     * @return The user that just logged in.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    Alert warningAlert= new Alert(Alert.AlertType.ERROR);

    /**
     * Warning Error Alert
     * @param title title - Title for the error alert
     * @param header header - Header for the error alert
     * @param content Content - Main Body of the error alert
     */
    void errorAlert(String title, String header, String content) {
        warningAlert.setTitle(title);
        warningAlert.setHeaderText(header);
        warningAlert.setContentText(content);
        warningAlert.showAndWait();
    }

    /**
     * Event Handler for log in button.
     * If log in fails, error alert and logs it to a text file.
     * If passes, logs and goes on to next page.
     * @param event event
     * @throws SQLException exception for SQL handling.
     */
    @FXML
    void onLoginClicked(ActionEvent event) throws SQLException {
        String tUserName = textName.getText();
        String tPassword = textPassword.getText();
        Boolean loginSuccess = UserQuery.loginCheck(tUserName, tPassword);

        // If log in fails, send waring and logs.
       if (!loginSuccess) {
           errorAlert(warningTitle, warningHeader, warningContent);
           LogActivity.logon(tUserName, false);
        } else {
           // if passes, change to next page
           currentUser = new User(tUserName);
           LogActivity.logon(tUserName, true);
           CustomerQuery.populateTable();
           AppointmentQuery.populateTable();
           FXMLLoader loader = new FXMLLoader();
           loader.setLocation(getClass().getResource("/wgu/dbclientapp/mainCustomer.fxml"));
           try {
               loader.load();
           } catch (IOException e) {
               throw new RuntimeException(e);
           }

           Parent root = loader.getRoot();
           Stage stage = new Stage();
           stage.setScene(new Scene(root));
           stage.show();
           loginButton.getScene().getWindow().hide();

       }

    }


    /**
     * On initialize, depending on the locale, switches the language from FR to ENG or ENG to FR.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ZoneId zone = ZoneId.systemDefault();
        currentLocation.setText(zone.toString());

        ResourceBundle rb = ResourceBundle.getBundle("wgu/dbclientapp/utility/lang", Locale.getDefault());
        if (Locale.getDefault().getLanguage().equals("fr")) {
            labelName.setText(rb.getString("username"));
            labelPassword.setText(rb.getString("password"));
            loginButton.setText(rb.getString("login"));
            warningTitle = rb.getString("loginWarningTitle");
            warningHeader = rb.getString("loginWarningHeader");
            warningContent = rb.getString("loginWarningMessage");
        } else {
            labelName.setText(rb.getString("username"));
            labelPassword.setText(rb.getString("password"));
            loginButton.setText(rb.getString("login"));
            warningTitle = rb.getString("loginWarningTitle");
            warningHeader = rb.getString("loginWarningHeader");
            warningContent = rb.getString("loginWarningMessage");
        }

    }
}