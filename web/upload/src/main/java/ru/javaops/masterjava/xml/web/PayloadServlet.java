package ru.javaops.masterjava.xml.web;

import ru.javaops.masterjava.xml.schema.User;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@WebServlet("/upload")
@MultipartConfig
public class PayloadServlet extends HttpServlet {

//    @Override
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
////        getServletContext().getRequestDispatcher("/WEB-INF/jsp/upload.jsp").forward(req, resp);
//    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Collection<Part> parts = req.getParts();
        List<User> users = new ArrayList<>();
        for(Part part : parts) {
            try(StaxStreamProcessor processor = new StaxStreamProcessor(part.getInputStream())) {
                JaxbParser parser = new JaxbParser(User.class);
                while(processor.startElement("User", "Users")) {
                    User user = parser.unmarshal(processor.getReader(), User.class);
                    users.add(user);
                }
            } catch (XMLStreamException | JAXBException e) {
                e.printStackTrace();
            }
        }

        req.setAttribute("message", "File Uploaded Successfully");
        req.setAttribute("users", users);

        req.getRequestDispatcher("/WEB-INF/jsp/upload.jsp").forward(req, resp);
    }
}
