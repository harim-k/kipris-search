package com.example.kiprissearch.external.response;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

public class XmlMapper {
    public static KiprisResponse convert(String xmlString) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(KiprisResponse.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            StringReader reader = new StringReader(xmlString);
            KiprisResponse kiprisResponse = (KiprisResponse) unmarshaller.unmarshal(reader);

//            // 이제 KiprisResponse 객체를 사용하여 XML 데이터를 Java 객체로 사용할 수 있습니다.
//            System.out.println("Response Time: " + kiprisResponse.getHeader().getResponseTime());
//            System.out.println("Applicant Name: " + kiprisResponse.getBody().getItems().get(0).getApplicantName());

            return kiprisResponse;
        } catch (JAXBException e) {
            e.printStackTrace();

            return null;
        }
    }
}

