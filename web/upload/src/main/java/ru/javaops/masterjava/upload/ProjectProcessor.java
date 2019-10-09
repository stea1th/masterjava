package ru.javaops.masterjava.upload;

import lombok.extern.slf4j.Slf4j;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.ProjectDao;
import ru.javaops.masterjava.persist.model.Project;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ProjectProcessor {

    private final ProjectDao projectDao = DBIProvider.getDao(ProjectDao.class);

    public Map<String, Integer> process(StaxStreamProcessor processor) throws XMLStreamException {

        List<String> projectNames = projectDao.getAllNames();
        Map<String, Integer> mapOfGroups = new HashMap<>();

        while (processor.doUntil(XMLEvent.START_ELEMENT, "Project")) {
            String name = processor.getAttribute("name");
            if (!projectNames.contains(name)) {
                int id = projectDao.insertGeneratedId(new Project(name, processor.getElementValue("description")));
                while (processor.startElement("Group", "Project")) {
                    mapOfGroups.put(processor.getAttribute("name"), id);
                }
            }
        }
        return mapOfGroups;
    }
}
