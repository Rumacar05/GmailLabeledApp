package com.ruma.gmaillabeledapp.service;

import com.ruma.gmaillabeledapp.model.Email;
import jakarta.mail.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailService {

    private static final String[] EMAILS_LABELS = {"Done", "Work.in.Progress", "To.be.Done"};

    private Session getSessionImap() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");

        return Session.getDefaultInstance(properties);
    }

    public List<Email> readInbox(String email, String password) throws Exception {
        List<Email> emails = new ArrayList<>();
        Session session = this.getSessionImap();

        try (Store store = session.getStore("imaps")) {
            store.connect("imap.gmail.com", 993, email, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            Folder[] labelFolders = new Folder[EMAILS_LABELS.length];
            for (int i = 0; i < EMAILS_LABELS.length; i++) {
                labelFolders[i] = store.getFolder(EMAILS_LABELS[i]);
                if (!labelFolders[i].exists()) {
                    labelFolders[i].create(Folder.HOLDS_MESSAGES);
                }
                labelFolders[i].open(Folder.READ_ONLY);
            }

            Message[] messages = inbox.getMessages();

            for (Message message : messages) {
                String from = message.getFrom()[0].toString();
                String subject = message.getSubject();

                List<String> labels = getLabelsForMessage(inbox, message, labelFolders);

                emails.add(new Email(subject, from, labels, message));
            }

            for (Folder folder : labelFolders) {
                folder.close();
            }

            inbox.close();
        }

        return emails;
    }

    private List<String> getLabelsForMessage(Folder inbox, Message message, Folder[] labelFolders) throws MessagingException {
        List<String> labels = new ArrayList<>();

        UIDFolder uidFolder = (UIDFolder) inbox;
        long uid = uidFolder.getUID(message);

        for (Folder folder : labelFolders) {
            UIDFolder labelUidFolder = (UIDFolder) folder;
            if (labelUidFolder.getMessageByUID(uid) != null) {
                labels.add(folder.getName());
            }
        }

        return labels;
    }
}
