package com.inventory.management.serviceImpl;

import com.inventory.management.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

@Service
@Slf4j
public class EmailServiceImpl implements EmailService {

    static final String FROM = "care@aiqahealth.com";
    static final String FROMNAME = "aiqa Resellers";

    static final String CONFIGSET = "ConfigSet";

    static final String HOST = "email-smtp.ap-south-1.amazonaws.com";

    static final int PORT = 587;
    @Override
    public void sendEmail(String subject, String message, String to) {

        String from = "arun.mudgal@sastechstudio.com";
        //variable for mail
        String host = "smtp.gmail.com";
        //get the system property
        Properties properties = System.getProperties();
        System.out.println("Properties " + properties);
        //setting import information to properties object

        //host set

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", 587);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("arun.mudgal@sastechstudio.com", "7877632410");
            }
        });
//		session.setDebug(true);
        session.setDebug(true);
        //step 2 : compose the message [text, multi media]
        MimeMessage m = new MimeMessage(session);
        try {
            //from email
            m.setFrom(from);
            //adding recipient to message
            m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            //adding subject to message
            m.setSubject(subject);

            //adding text to message
            m.setText(message);


            //send

            //step 3 send the message using Transport class
            Transport.send(m);

            System.out.println("send success.............");
            //f = true;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Async
    public Future<Boolean> sendMailFromGmail(String emailRecipients, String emailSubject, String emailBody,
                                             List<String> fileName, List<byte[]> ba, String cc) {
        final String username = "AKIARGODPHJ4EXKUZOXQ";
        final String password = "BASg21cSgA7OORoa99r/VHzdHaZ4gUJU0jDhdwwyBSr5";
        Properties props = new Properties();
        props.put("mail.smtp.host", HOST);
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.port", PORT);
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");
            msg.setFrom(new InternetAddress(FROM, FROMNAME));
            msg.setReplyTo(InternetAddress.parse(emailRecipients, false));
            msg.setSubject(emailSubject);
            if (!StringUtils.isEmpty(cc))
                msg.addRecipients(Message.RecipientType.BCC, InternetAddress.parse(cc));
            msg.setContent(emailBody, "text/html");
            Multipart multiPart = new MimeMultipart();
            msg.setSentDate(new Date());
            MimeBodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(emailBody, "utf-8", "html");
            multiPart.addBodyPart(messageBodyPart);
            for (int i = 0; i < ba.size(); i++) {
                byte[] bs = ba.get(i);
                if (bs != null && bs.length > 0) {
                    DataSource fds = new ByteArrayDataSource(bs, "application/octet-stream");
                    MimeBodyPart attachment = new MimeBodyPart();
                    attachment.setDataHandler(new DataHandler(fds));
                    attachment.setDisposition(Part.ATTACHMENT);
                    attachment.setFileName(fileName.get(i));
                    attachment.setContentID(String.valueOf("cid" + i));
                    multiPart.addBodyPart(attachment);
                }
            }
            msg.setContent(multiPart);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailRecipients, false));
            Transport transport = session.getTransport();
            transport.send(msg);
            log.info("Email Sent Successfully");
            return new AsyncResult<>(true);
        } catch (Exception e) {
            log.error("Exception while Sending Email ", e);
            return new AsyncResult<>(false);
        }
    }

}
