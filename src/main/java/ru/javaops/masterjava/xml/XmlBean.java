package ru.javaops.masterjava.xml;

import com.google.common.io.Resources;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.ejb.Singleton;
import javax.xml.stream.events.Attribute;
import java.util.List;

//@Singleton
public class XmlBean {

    public static void getUsersByProjectName(String projectName) throws Exception{
        try (StaxStreamProcessor processor =
                     new StaxStreamProcessor(Resources.getResource("payload.xml").openStream(), Resources.getResource("payload.xml").openStream())) {
            List<Attribute> projectAttr = processor.getElementAttributes("project", "masterjava");
//            System.out.println(projectAttr.toString());
            List<String> projectIds = processor.getAttributeValues(projectAttr, "id");

            System.out.println(projectIds.toString());
            List<Attribute> projectAttr2 = processor.getElementAttributes("project", "masterjava");
        }
    }

}
