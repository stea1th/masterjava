package ru.javaops.masterjava.upload;

import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.UserDao;
import ru.javaops.masterjava.persist.model.User;
import ru.javaops.masterjava.persist.model.UserFlag;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.JaxbUnmarshaller;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UserProcessor {
    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);

    public void process(final InputStream is) throws XMLStreamException, JAXBException {
         process(is, Integer.MAX_VALUE);
    }

    public void process(final InputStream is, int chunk) throws XMLStreamException, JAXBException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        UserDao userDao = DBIProvider.getDao(UserDao.class);
        List<User> users = new ArrayList<>();


        JaxbUnmarshaller unmarshaller = jaxbParser.createUnmarshaller();
        int count = 0;
        while (processor.doUntil(XMLEvent.START_ELEMENT, "User")) {
            ru.javaops.masterjava.xml.schema.User xmlUser = unmarshaller.unmarshal(processor.getReader(), ru.javaops.masterjava.xml.schema.User.class);
            final User user = new User(xmlUser.getValue(), xmlUser.getEmail(), UserFlag.valueOf(xmlUser.getFlag().value()));
            users.add(user);
            count++;
            if(count == chunk) {
                int[] x = userDao.insertAll(users.iterator(), chunk);
                users.clear();
                count = 0;
            }
        }
        if(users.size() != 0) {
            int[] x = userDao.insertAll(users.iterator(), chunk);
        }
    }
}
