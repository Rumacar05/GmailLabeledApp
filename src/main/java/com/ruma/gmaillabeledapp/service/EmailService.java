package com.ruma.gmaillabeledapp.service;

import com.ruma.gmaillabeledapp.model.Email;
import jakarta.mail.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class EmailService {

    private static final String[] EMAILS_LABELS = {"Done", "Work.in.Progress", "To.be.Done"};

    private final String emailAddress;
    private final String password;

    public EmailService(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }

    private Session getSessionImap() {
        Properties properties = new Properties();
        properties.put("mail.store.protocol", "imaps");
        properties.put("mail.imaps.host", "imap.gmail.com");
        properties.put("mail.imaps.port", "993");
        properties.put("mail.imaps.ssl.enable", "true");

        return Session.getDefaultInstance(properties);
    }

    public List<Email> readInbox() throws Exception {
        List<Email> emails = new ArrayList<>();
        Session session = this.getSessionImap();

        try (Store store = session.getStore("imaps")) {
            store.connect("imap.gmail.com", 993, emailAddress, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_ONLY);

            UIDFolder uidFolder = (UIDFolder) inbox;

            Folder[] labelFolders = new Folder[EMAILS_LABELS.length];
            for (int i = 0; i < EMAILS_LABELS.length; i++) {
                labelFolders[i] = store.getFolder(EMAILS_LABELS[i]);

                if (!labelFolders[i].exists()) {
                    labelFolders[i].create(Folder.HOLDS_MESSAGES);
                }
            }

            Message[] messages = inbox.getMessages();

            for (Message message : messages) {
                String from = message.getFrom()[0].toString();
                String subject = message.getSubject();

                long uid = uidFolder.getUID(message);

                List<String> labels = getLabelsForMessage(message, labelFolders);

                emails.add(new Email(subject, from, labels, uid));
            }

            inbox.close();
        }

        return emails;
    }

    public void setFolderForEmail(Email email, String folder) throws Exception {
        Session session = this.getSessionImap();

        try (Store store = session.getStore("imaps")) {
            store.connect("imap.gmail.com", 993, emailAddress, password);
            Folder inbox = store.getFolder("INBOX");
            inbox.open(Folder.READ_WRITE);

            UIDFolder uidFolder = (UIDFolder) inbox;

            Message message = uidFolder.getMessageByUID(email.getUid());

            Folder destinationFolder = store.getFolder(folder);
            if (!destinationFolder.exists()) {
                destinationFolder.create(Folder.HOLDS_MESSAGES);
            }
            destinationFolder.open(Folder.READ_WRITE);

            inbox.copyMessages(new Message[]{message}, destinationFolder);

            email.getLabel().add(folder);

            inbox.close();
        }
    }

    private List<String> getLabelsForMessage(Message message, Folder[] labelFolders) throws MessagingException {
        List<String> labels = new ArrayList<>();

        String messageId = getMessageId(message);
        if (messageId == null) {
            return labels;
        }

        for (Folder folder : labelFolders) {
            folder.open(Folder.READ_ONLY);

            for (Message labeledMessage : folder.getMessages()) {
                String labeledMessageId = getMessageId(labeledMessage);
                if (messageId.equals(labeledMessageId)) {
                    labels.add(folder.getName());
                }
            }

            folder.close();
        }

        return labels;
    }

    private String getMessageId(Message message) throws MessagingException {
        String[] messageIdHeader = message.getHeader("Message-ID");
        if (messageIdHeader != null && messageIdHeader.length > 0) {
            return messageIdHeader[0];
        }
        return null;
    }
}
