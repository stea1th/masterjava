package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.schema2.*;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.Schemas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainXml {

    private static final JaxbParser JAXB_PARSER = new JaxbParser(ObjectFactory.class);
    private static final String PROJECT_NAME = "masterjava";

    static {
        JAXB_PARSER.setSchema(Schemas.ofClasspath("payload.xsd"));
    }

    public static void main(String[] args) throws Exception {
        Payload payload = JAXB_PARSER.unmarshal(
                Resources.getResource("payload.xml").openStream());
        Project project = getProjectByName(payload);
        List<Group> groups = getGroupsByProject(payload, project);
        getUsersByGroups(payload, groups)
                .forEach(i-> System.out.println(i.getFullName()));
    }

    private static Project getProjectByName(Payload payload) {
        return payload
                .getProjects()
                .getProject()
                .stream()
                .filter(p -> p.getTitle().equals(PROJECT_NAME))
                .findFirst()
                .orElse(null);
    }

    private static List<Group> getGroupsByProject(Payload payload, Project project) {
        return payload
                .getGroups()
                .getGroup()
                .stream()
                .filter(i->   i.getProject().equals(project))
                .collect(Collectors.toList());
    }

    private static List<User> getUsersByGroups(Payload payload, List<Group> groups) {
        List<User> list = new ArrayList<>();
        for(User user : payload.getUsers().getUser()) {
            Group group = (Group) user.getGroup()
                    .stream()
            .filter(groups::contains)
            .findFirst().orElse(null);
            if(group != null) {
                list.add(user);
            }
        }
        return list;
    }
}
