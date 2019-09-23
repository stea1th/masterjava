package ru.javaops.masterjava.xml;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("/")
public class MainXmlServlet {

//    @Inject
//    XmlBean xmlBean;

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);
//    }

    public static void main(String[] args) throws Exception{
       XmlBean.getUsersByProjectName("");
    }
}
