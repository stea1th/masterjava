package ru.javaops.masterjava.upload;

import lombok.AllArgsConstructor;
import lombok.val;
import ru.javaops.masterjava.persist.model.Group;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class PayloadProcessor {
    private final CityProcessor cityProcessor = new CityProcessor();
    private final UserProcessor userProcessor = new UserProcessor();
    private final ProjectProcessor projectProcessor = new ProjectProcessor();

    @AllArgsConstructor
    public static class FailedEmails {
        public String emailsOrRange;
        public String reason;

        @Override
        public String toString() {
            return emailsOrRange + " : " + reason;
        }
    }


    public List<FailedEmails> process(InputStream is, int chunkSize) throws XMLStreamException, JAXBException {
        final StaxStreamProcessor processor = new StaxStreamProcessor(is);
        Map<String, Group> groupMap = projectProcessor.process(processor);
        val cities = cityProcessor.process(processor);
        return userProcessor.process(processor, cities, groupMap, chunkSize);
    }
}