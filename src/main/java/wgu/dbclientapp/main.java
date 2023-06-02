package wgu.dbclientapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import wgu.dbclientapp.sqlQuery.CustomerQuery;
import wgu.dbclientapp.sqlQuery.FirstLevelDivisionQuery;
import wgu.dbclientapp.sqlQuery.JDBC;

import java.io.IOException;
import java.sql.SQLException;


/**
 * Main class where the program starts.
 */
public class main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 434);
        stage.setTitle("Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) throws SQLException {
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}