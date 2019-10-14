package ru.javaops.masterjava.webapp;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import org.thymeleaf.context.WebContext;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.service.mail.Addressee;
import ru.javaops.masterjava.service.mail.MailWSClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import static ru.javaops.masterjava.common.web.ThymeleafListener.engine;

@WebServlet(urlPatterns = "/user")
public class UsersServlet extends HttpServlet {
    private UserDao userDao = DBIProvider.getDao(UserDao.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final WebContext webContext = new WebContext(req, resp, req.getServletContext(), req.getLocale(),
                ImmutableMap.of("users", userDao.getWithLimit(20)));
        engine.process("users", webContext, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String[] ccIds = req.getParameterValues("cc-id");
        Set<Addressee> toSet = Sets.newHashSet(new Addressee(req.getParameter("to-email")));



        String subject = req.getParameter("subject");
        String body = req.getParameter("body");
        if(ccIds != null) {
            Set<Addressee> ccSet = Arrays.stream(ccIds)
                    .map(i-> userDao.get(Integer.parseInt(i)))
                    .map(a-> new Addressee(a.getEmail(), a.getFullName()))
                    .collect(Collectors.toSet());
            MailWSClient.sendToGroup(toSet, ccSet, subject, body);
        } else {
            MailWSClient.sendBulk(toSet, subject, body);
        }
    }
}
