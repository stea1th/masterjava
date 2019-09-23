package ru.javaops.masterjava.xml.util;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class StaxStreamProcessor implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private final XMLStreamReader reader;

    private final XMLEventReader eventReader ;

    public StaxStreamProcessor(InputStream... is) throws XMLStreamException {
        if(is.length == 1) {
            reader = FACTORY.createXMLStreamReader(is[0]);
            eventReader = null;
        } else {
            reader = FACTORY.createXMLStreamReader(is[0]);
            eventReader = FACTORY.createXMLEventReader(is[1]);
        }
    }


    public XMLStreamReader getReader() {
        return reader;
    }

    public boolean doUntil(int stopEvent, String value) throws XMLStreamException {
        while (reader.hasNext()) {
            int event = reader.next();
            if (event == stopEvent) {
                if (value.equals(getValue(event))) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getValue(int event) throws XMLStreamException {
        return (event == XMLEvent.CHARACTERS) ? reader.getText() : reader.getLocalName();
    }

    public String getElementValue(String element) throws XMLStreamException {
        return doUntil(XMLEvent.START_ELEMENT, element) ? reader.getElementText() : null;
    }

    public String getText() throws XMLStreamException {
        return reader.getElementText();
    }

    public List<Attribute> getElementAttributes(String element, String name) throws XMLStreamException {

        List<Attribute> attributes = null;
        boolean isThisElement = false;
        while (eventReader.hasNext()) {
            XMLEvent event = eventReader.nextEvent();

            Attribute attr = null;
            if(!isThisElement) {
                attributes = new ArrayList<>();
            }
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equalsIgnoreCase(element)) {
                    isThisElement = true;
                    @SuppressWarnings("unchecked")
                    Iterator<Attribute> iter = startElement.getAttributes();
                    while (iter.hasNext()) {
                        attr = iter.next();
                        attributes.add(attr);
                    }
                }
            }
            if(event.isCharacters()) {
                String eventString = event.asCharacters().toString();
               if(eventString.equalsIgnoreCase(name) && isThisElement){
                   System.out.println(attributes.size());
                  return attributes;
                } else if(!eventString.contains(" ")){
                   isThisElement = false;
               }
            }
        }
        return null;
    }

    public List<String> getAttributeValues(List<Attribute> attributes, String attrName) {
        return attributes
                .stream()
                .filter(i-> i.getName().getLocalPart().equalsIgnoreCase(attrName))
                .map(Attribute::getValue)
                .collect(Collectors.toList());
    }

    @Override
    public void close() {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) {
                // empty
            }
        }

        if(eventReader !=null) {
            try {
                eventReader.close();
            }catch (XMLStreamException e) {

            }
        }
    }
}
