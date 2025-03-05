package com.ruma.gmaillabeledapp.controller;

import com.ruma.gmaillabeledapp.config.Configuration;
import com.ruma.gmaillabeledapp.model.Email;
import com.ruma.gmaillabeledapp.service.AlertService;
import com.ruma.gmaillabeledapp.service.EmailService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class ManageEmailController {

    @FXML
    private Button btnLoadEmails;
    @FXML
    private Button btnSetDone;
    @FXML
    private Button btnSetInProgress;
    @FXML
    private Button btnSetToBeDone;
    @FXML
    private Label lblEmail;
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ListView<Email> lvEmails;

    private EmailService emailService;

    @FXML
    void initialize() {
        emailService = new EmailService(Configuration.GMAIL_USERNAME, Configuration.GMAIL_PASSWORD);
        lblEmail.setText("Correo electrónico " + Configuration.GMAIL_USERNAME);
    }

    @FXML
    void onLoadEmailsClicked() {
        progressBar.setVisible(true);
        disableButtons(true);

        Task<ObservableList<Email>> task = new Task<>() {
            @Override
            protected ObservableList<Email> call() throws Exception {
                return FXCollections.observableArrayList(emailService.readInbox());
            }
        };

        task.setOnSucceeded(_ -> {
            ObservableList<Email> emails = task.getValue();
            lvEmails.setItems(emails);
            disableButtons(false);
            progressBar.setVisible(false);
        });

        task.setOnFailed(_ -> {
            Throwable ex = task.getException();
            AlertService.showAlert(Alert.AlertType.ERROR, "Error",
                    "No se han podido cargar los emails: " + ex.getMessage());

            disableButtons(false);
            progressBar.setVisible(false);
        });

        new Thread(task).start();
    }

    @FXML
    void onSetDoneClicked() {
        setLabelForEmail(Configuration.FOLDER_DONE);
    }

    @FXML
    void onSetInProgressClicked() {
        setLabelForEmail(Configuration.FOLDER_WORK_IN_PROGRESS);
    }

    @FXML
    void onSetToBeDoneClicked() {
        setLabelForEmail(Configuration.FOLDER_TO_BE_DONE);
    }

    private void setLabelForEmail(String labelFolder) {
        Email email = lvEmails.getSelectionModel().getSelectedItem();
        if (email != null) {
            progressBar.setVisible(true);
            disableButtons(true);

            Task<Void> task = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    emailService.setFolderForEmail(email, labelFolder);
                    return null;
                }
            };

            task.setOnSucceeded(_ -> {
                lvEmails.refresh();
                progressBar.setVisible(false);
                disableButtons(false);
            });


            task.setOnFailed(_ -> {
                progressBar.setVisible(false);
                disableButtons(false);

                Throwable ex = task.getException();
                AlertService.showAlert(Alert.AlertType.ERROR, "Error",
                        "No se ha podido poner la etiqueta: " + ex.getMessage());
            });

            new Thread(task).start();
        } else {
            AlertService.showAlert(Alert.AlertType.WARNING, "Advertencia",
                    "No se ha seleccionado ningún correo");
        }
    }

    private void disableButtons(boolean b) {
        btnLoadEmails.setDisable(b);
        btnSetDone.setDisable(b);
        btnSetInProgress.setDisable(b);
        btnSetToBeDone.setDisable(b);
    }
}
