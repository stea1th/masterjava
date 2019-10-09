package ru.javaops.masterjava.service.mail;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.typesafe.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import ru.javaops.masterjava.config.Configs;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.service.mail.dao.MailDao;
import ru.javaops.masterjava.service.mail.model.Mail;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Slf4j
public class MailSender {

    private static LoadingCache<String, String> cache = createEmailConfigCache();

    static void sendMail(List<Addressee> to, List<Addressee> cc, String subject, String body) throws ExecutionException {
        log.info("Send mail to \'" + to + "\' cc \'" + cc + "\' subject \'" + subject + (log.isDebugEnabled() ? "\nbody=" + body : ""));
        Email email = createSimpleEmailWithConfig(to, cc, subject, body);
        MailDao dao = DBIProvider.getDao(MailDao.class);
        Mail mail = new Mail();
        mail.setToList(createString(to));
        mail.setCcList(createString(cc));
        mail.setSubject(subject);
        mail.setBody(body);
        try {
            email.send();
            mail.setSentDate(email.getSentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            mail.setIsSuccessful(true);
        } catch (EmailException e) {
            mail.setSentDate(LocalDateTime.now());
            mail.setIsSuccessful(false);
        } finally {
            dao.insert(mail);
        }
    }

    private static Collection<InternetAddress> createCollection(List<Addressee> list) {
        if (list != null) {
            return list.stream().map(i -> {
                InternetAddress internetAddress = new InternetAddress();
                try {
                    internetAddress.setAddress(i.getEmail());
                    internetAddress.setPersonal(i.getName());
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                return internetAddress;
            }).collect(Collectors.toList());
        }
        return new ArrayList<InternetAddress>() {
        };
    }

    private static String createString(List<Addressee> list) {
        return list.stream().map(Addressee::getEmail).collect(Collectors.joining(","));
    }

    private static LoadingCache<String, String> createEmailConfigCache() {
        Config config = Configs.getConfig("mail.conf", "mail");
        return CacheBuilder.newBuilder()
                .maximumSize(20)
                .expireAfterWrite(24, TimeUnit.HOURS)
                .build(
                        new CacheLoader<String, String>() {
                            @Override
                            public String load(String s) throws Exception {
                                return config.getString(s);
                            }
                        }
                );
    }

    private static Email createSimpleEmailWithConfig(List<Addressee> to, List<Addressee> cc, String subject, String body) throws ExecutionException {
        Email email = new SimpleEmail();
        try {
            email.setHostName(getProperty("host"));
            email.setSmtpPort(Integer.parseInt(getProperty("port")));
            email.setAuthentication(getProperty("username"), getProperty("password"));
            email.setSSLOnConnect(Boolean.parseBoolean(getProperty("useSSL")));
            email.setFrom(getProperty("username"));
            email.setDebug(Boolean.parseBoolean(getProperty("debug")));
            email.setSubject(subject);
            email.setMsg(body);
            email.setTo(createCollection(to));
            Collection<InternetAddress> ccCollection = createCollection(cc);
            if (ccCollection.size() != 0) {
                email.setCc(ccCollection);
            }

        } catch (EmailException exp) {
            exp.printStackTrace();
        }
        return email;
    }

    private static String getProperty(String name) throws ExecutionException {
        return cache.get(name);
    }
}
