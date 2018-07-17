package hillelauto.Robert_cools.Utils.sendMail;

import Utils.helpers.EnvironmentHelper;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.util.Properties;


public class SendAttachmentInEmail {
    private final static String username = "skyfenceodessa@gmail.com";
    private final static String password = "Barbapapa1@";
    private static Multipart multipart = new MimeMultipart();

    private static Session getSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", true);
        props.put("mail.smtp.starttls.enable", true);
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        return Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
    }

    public static void addAttachments(String name, int percents, File screen) {
        DataSource source = new FileDataSource(screen);
        BodyPart messageBodyPart = new MimeBodyPart();
        String fileName;
        try {
            if (percents == -1) {
                fileName = "Test_" + name + ".jpg";
            } else {
                fileName = "Service_" + name + "_Matching_" + percents + "%.jpg";
            }
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public static void sendMail() {
        String subject;
        String recpients;
        try {
            if (multipart.getCount() != 0) {
                Session session = getSession();
                Message message = new MimeMessage(session);
                message.setFrom(new InternetAddress("skyfenceodessa@gmail.com"));
                subject = "Test :: failure"+ EnvironmentHelper.getCurrentDate();
                recpients = "mail4testing.qa@gmail.com";
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recpients));
                message.setSubject(subject);
                message.setContent(multipart);
                System.out.println("Sending letter ...");
                Transport.send(message);
                System.out.println("Done");
            } else {
                System.out.println("well done");
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}



