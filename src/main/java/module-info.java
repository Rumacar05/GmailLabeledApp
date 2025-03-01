module com.ruma.gmaillabeledapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ruma.gmaillabeledapp to javafx.fxml;
    exports com.ruma.gmaillabeledapp;
    exports com.ruma.gmaillabeledapp.controller;
    opens com.ruma.gmaillabeledapp.controller to javafx.fxml;
}