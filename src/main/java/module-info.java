module com.ruma.gmaillabeledapp {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.ruma.gmaillabeledapp to javafx.fxml;
    exports com.ruma.gmaillabeledapp;
}