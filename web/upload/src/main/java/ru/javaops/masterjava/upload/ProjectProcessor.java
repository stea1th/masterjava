package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.GroupDao;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.persist.model.type.GroupType;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProjectProcessor {

    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);
    private final GroupDao groupDao = DBIProvider.getDao(GroupDao.class);

    public Map<String, Group> process(StaxStreamProcessor processor) throws XMLStreamException {

        List<String> projectNames = projectDao.getAllNames();
        List<String> groupNames = groupDao.getAllNames();
        List<Group> newGroups = new ArrayList<>();

        while (processor.startElement("Project", "Projects")) {
            String name = processor.getAttribute("name");
            if (!projectNames.contains(name)) {
                int id = projectDao.insertGeneratedId(new Project(name, processor.getElementValue("description")));
                while (processor.startElement("Group", "Project")) {
                    String groupName = processor.getAttribute("name");
                    if (!groupNames.contains(groupName)) {
                        newGroups.add(new Group(groupName, GroupType.valueOf(processor.getAttribute("type")), id));
                    }
                }
            }
        }
        if (!newGroups.isEmpty())
            groupDao.insertBatch(newGroups);

        return groupDao.getAsMap();
    }
}
