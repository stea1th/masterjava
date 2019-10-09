package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.dao.UserGroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.UserGroup;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
public class UserGroupProcessor {

    private final UserGroupDao userGroupDao = DBIProvider.getDao(UserGroupDao.class);
    private final UserDao userDao = DBIProvider.getDao(UserDao.class);


    public void process(StaxStreamProcessor processor, List<PayloadProcessor.FailedEmails> userFailedEmails, Map<String, Group> groups) throws XMLStreamException {

        List<UserGroup> userGroups = new ArrayList<>();
        List<String> failedEmails = userFailedEmails.stream().map(i-> i.emailsOrRange).collect(Collectors.toList());

        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            String email = processor.getAttribute("email");
            if(!failedEmails.contains(email)) {
                int userId = userDao.getIdByEmail(email);
                String groupRefs = processor.getAttribute("groupRefs");
                if(groupRefs != null) {
                    Arrays.stream(groupRefs.split(" "))
                            .forEach(i-> userGroups.add(new UserGroup(userId, groups.get(i).getId())));
                }
            }
        }
        userGroupDao.insertBatch(userGroups);
    }
}
