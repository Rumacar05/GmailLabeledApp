package com.ruma.gmaillabeledapp.model;

import jakarta.mail.Message;

import java.util.List;

public class Email {
    private String subject;
    private String from;
    private List<String> label;
    private Message message;

    public Email(String subject, String from, List<String> label, Message message) {
        this.subject = subject;
        this.from = from;
        this.label = label;
        this.message = message;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public List<String> getLabel() {
        return label;
    }

    public void setLabel(List<String> label) {
        this.label = label;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("Asunto: %s - De: %s - Etiqueta: %s", subject, from, label);
    }
}
