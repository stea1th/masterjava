package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class GroupProcessor {

    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);

    public Map<String, Group> process(StaxStreamProcessor processor, Map<String, Integer> mapOfGroups) throws XMLStreamException {
        List<String> groupNames = groupDao.getAllNames();
        List<Group> newGroups = new ArrayList<>();

        while(processor.doUntil(XMLEvent.START_ELEMENT, "Group")) {
            String name = processor.getAttribute("name");
            if(!groupNames.contains(name)) {
                newGroups.add(new Group(name, GroupType.valueOf(processor.getAttribute("type")), mapOfGroups.get(name)));
            }
        }
        groupDao.insertBatch(newGroups);
        return groupDao.getAsMap();
    }
}
