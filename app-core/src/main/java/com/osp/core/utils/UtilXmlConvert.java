package com.osp.core.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;

public class UtilXmlConvert {
    private static Logger LOGGER = LoggerFactory.getLogger(UtilXmlConvert.class);

    public static String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in))) return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) ||
                    (current == 0xA) ||
                    (current == 0xD) ||
                    ((current >= 0x20) && (current <= 0xD7FF)) ||
                    ((current >= 0xE000) && (current <= 0xFFFD)) ||
                    ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }

    public static Object JaxbXmlToObj(String xmlString, Object obj) {
        JAXBContext jaxbContext;
        try {
            xmlString = xmlString.replaceAll( "(&(?!amp;))", "&amp;");
            xmlString = stripNonValidXMLCharacters(xmlString);
            jaxbContext = JAXBContext.newInstance(obj.getClass());

            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Object resultDataSet = (Object) jaxbUnmarshaller.unmarshal(new StringReader(xmlString));

            return resultDataSet;
        }
        catch (JAXBException e) {
            LOGGER.error("UtilXmlConvert.JaxbXmlToObj" + e.getMessage());
        }
        return null;
    }

    public static String JaxbObjToXML(Object object) {
        String xmlContent = null;
        try {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            jaxbMarshaller.setProperty(Marshaller.JAXB_FRAGMENT, Boolean.TRUE);

            //Print XML String to Console
            StringWriter sw = new StringWriter();

            //Write XML to StringWriter
            jaxbMarshaller.marshal(object, sw);

            //Verify XML Content
            xmlContent = sw.toString();
            //System.out.println(xmlContent);
        }
        catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlContent;
    }

    public static Object XmlMapperToObj(String xmlString, Object obj) {
        XmlMapper xmlMapper = new XmlMapper();
        try {
            xmlString = xmlString.replaceAll( "(&(?!amp;))", "&amp;");
            xmlString = stripNonValidXMLCharacters(xmlString);
            return xmlMapper.readValue(xmlString, obj.getClass());
        }
        catch (JsonProcessingException e) {
            LOGGER.error("UtilXmlConvert.XmlMapperToObj" + e.getMessage());
        }
        return null;
    }

    // json to Object
    public static Object ObjectMapperToObj(String xmlString, Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            return objectMapper.readValue(xmlString, obj.getClass());
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

}
