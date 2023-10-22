package com.example.kiprissearch.external.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "item")
public class Item {

    private String agentName;
    private String appReferenceNumber;
    private String applicantName;
    private String applicationDate;
    private String applicationNumber;
    private String applicationStatus;
    private String bigDrawing;
    private String classificationCode;
    private String drawing;
    private String fullText;
    private String indexNo;
    private String internationalRegisterDate;
    private String internationalRegisterNumber;
    private String priorityDate;
    private String priorityNumber;
    private String publicationDate;
    private String publicationNumber;
    private String regPrivilegeName;
    private String regReferenceNumber;
    private String registrationDate;
    private String registrationNumber;
    private String registrationPublicDate;
    private String registrationPublicNumber;
    private String title;
    private String viennaCode;

    @XmlElement(name = "agentName")
    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getAppReferenceNumber() {
        return appReferenceNumber;
    }

    public String getApplicantName() {
        return applicantName;
    }

    public String getApplicationDate() {
        return applicationDate;
    }

    public String getApplicationNumber() {
        return applicationNumber;
    }

    public String getApplicationStatus() {
        return applicationStatus;
    }

    public String getBigDrawing() {
        return bigDrawing;
    }

    public String getClassificationCode() {
        return classificationCode;
    }

    public String getDrawing() {
        return drawing;
    }

    public String getFullText() {
        return fullText;
    }

    public String getIndexNo() {
        return indexNo;
    }

    public String getInternationalRegisterDate() {
        return internationalRegisterDate;
    }

    public String getInternationalRegisterNumber() {
        return internationalRegisterNumber;
    }

    public String getPriorityDate() {
        return priorityDate;
    }

    public String getPriorityNumber() {
        return priorityNumber;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getPublicationNumber() {
        return publicationNumber;
    }

    public String getRegPrivilegeName() {
        return regPrivilegeName;
    }

    public String getRegReferenceNumber() {
        return regReferenceNumber;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public String getRegistrationPublicDate() {
        return registrationPublicDate;
    }

    public String getRegistrationPublicNumber() {
        return registrationPublicNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getViennaCode() {
        return viennaCode;
    }
}

