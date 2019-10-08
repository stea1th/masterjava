package ru.javaops.masterjava.service.mail;

import com.google.common.collect.ImmutableList;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

public class MailServiceClient {

    public static void main(String[] args) throws MalformedURLException {
//        Service service = Service.create(
//                new URL("http://localhost:8080/mail/mailService?wsdl"),
//                new QName("http://mail.service.masterjava.javaops.ru/", "MailServiceImplService"));
//
//        MailService mailService = service.getPort(MailService.class);
        MailService mailService = new MailServiceImpl();
        mailService.sendMail(ImmutableList.of(new Addressee("yxcyxcyxca@jycyxcyxcyxcyxc.ru", null)),
                ImmutableList.of(new Addressee("stea1th@mail.ru", "Vadim"),
                        new Addressee("sadasdasdkasjdkajsdkasd@dffgdfgdfgdfg.ru", null)),
                "Subject", "Body");
    }
}
