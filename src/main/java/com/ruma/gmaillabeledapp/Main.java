package com.ruma.gmaillabeledapp;

import com.ruma.gmaillabeledapp.config.Configuration;
import com.ruma.gmaillabeledapp.service.AlertService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        try {
            Configuration.start();
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("manage-email.fxml"));
            Scene scene = new Scene(fxmlLoader.load());

            stage.setTitle("Gestor de correos!");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            AlertService.showAlert(Alert.AlertType.ERROR, "Se ha producido una excepción", ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch();
    }
}