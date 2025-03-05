package com.ruma.gmaillabeledapp.model;

import java.util.List;

public class Email {
    private String subject;
    private String from;
    private List<String> label;
    private final long uid;

    public Email(String subject, String from, List<String> label, long uid) {
        this.subject = subject;
        this.from = from;
        this.label = label;
        this.uid = uid;
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

    public long getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return String.format("Asunto: %s - De: %s - Etiquetas: %s", subject, from, label);
    }
}
