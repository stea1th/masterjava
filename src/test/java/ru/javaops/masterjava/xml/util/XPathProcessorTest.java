package ru.javaops.masterjava.xml.util;

import com.google.common.io.Resources;
import org.junit.Test;
import org.xml.sax.InputSource;

import javax.xml.xpath.XPathExpression;
import java.io.InputStream;

public class XPathProcessorTest {

    @Test
    public void getCities() throws Exception {
        try (InputStream inputStream = Resources.getResource(XPathProcessorTest.class, "/payload.xml").openStream()) {
            InputSource source = new InputSource(inputStream);

            XPathExpression expression = XPathProcessor.getExpression("/*[name()='Payload']/*[name()='Cities']");
            String str = expression.evaluate(source);
            System.out.println(str.trim());
        }
    }
}