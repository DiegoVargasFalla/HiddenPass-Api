package net.hiddenpass.hiddenpass.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import net.hiddenpass.hiddenpass.service.GmailService;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Service
public class GmailServiceImpl implements GmailService {

    private final JavaMailSender mailSender;

    public GmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendMail(String to, String subject, String url) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        mimeMessage.setFrom(new InternetAddress("hiddenpass@gmail.com"));
        mimeMessage.setRecipients(MimeMessage.RecipientType.TO, InternetAddress.parse(to));
        mimeMessage.setSubject(subject);

        ClassPathResource resource = new ClassPathResource("static/index.html");

        try(InputStream in = resource.getInputStream()) {
            String html = new String(in.readAllBytes(), StandardCharsets.UTF_8);

            StringBuilder htmlBuilder = new StringBuilder();

            for(char c : html.toCharArray()) {
                htmlBuilder.append(c);
            }

            String htmlString = htmlBuilder.toString().replace("%URL%", url);
            mimeMessage.setContent(htmlString, "text/html; charset=utf-8");
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            System.out.println("-> Error mail" + e.getMessage() );
        }
    }
}
