package com.ruma.gmaillabeledapp.controller;

import com.ruma.gmaillabeledapp.config.Configuration;
import com.ruma.gmaillabeledapp.model.Email;
import com.ruma.gmaillabeledapp.service.AlertService;
import com.ruma.gmaillabeledapp.service.EmailService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ManageEmailController {

    @FXML
    private Button btnLoadEmails;
    @FXML
    private Label lblEmail;
    @FXML
    private ListView<Email> lvEmails;

    private EmailService emailService;

    @FXML
    void initialize() {
        emailService = new EmailService();
        lblEmail.setText("Correo electrónico " + Configuration.GMAIL_USERNAME);
    }

    @FXML
    void onLoadEmailsClicked() {
        btnLoadEmails.setDisable(true);

        try {
            ObservableList<Email> emails =
                    FXCollections.observableArrayList(emailService.readInbox(Configuration.GMAIL_USERNAME,
                            Configuration.GMAIL_PASSWORD));

            lvEmails.setItems(emails);
        } catch (Exception ex) {
            AlertService.showAlert(Alert.AlertType.ERROR, "Error",
                    "No se han podido cargar los emails: " + ex.getMessage());
        } finally {
            btnLoadEmails.setDisable(false);
        }
    }

}
