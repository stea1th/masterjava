package ru.javaops.masterjava.upload;


import lombok.extern.slf4j.Slf4j;
import lombok.val;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;

import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.xml.schema.CityType;
import ru.javaops.masterjava.xml.schema.ObjectFactory;
import ru.javaops.masterjava.xml.util.JaxbParser;
import ru.javaops.masterjava.xml.util.StaxStreamProcessor;

import javax.xml.bind.JAXBException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;

@Slf4j
public class CityProcessor {

    private static final JaxbParser jaxbParser = new JaxbParser(ObjectFactory.class);
    private static CityDao cityDao = DBIProvider.getDao(CityDao.class);

    public void process(final InputStream is) throws XMLStreamException, JAXBException {
        log.info("Start city processing");

        val processor = new StaxStreamProcessor(is);
        val unmarshaller = jaxbParser.createUnmarshaller();

        while(processor.doUntil(XMLEvent.START_ELEMENT, "City")){
            CityType xmlCity = unmarshaller.unmarshal(processor.getReader(), CityType.class);
            final City city = new City(xmlCity.getId(), xmlCity.getValue());
            cityDao.insert(city);
        }


    }

}
