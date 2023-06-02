module wgu.dbclientapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens wgu.dbclientapp to javafx.fxml;
    opens  wgu.dbclientapp.model to javafx.fxml;


    exports wgu.dbclientapp;
    exports wgu.dbclientapp.controller;
    exports wgu.dbclientapp.model;
    opens wgu.dbclientapp.controller to javafx.fxml;
}