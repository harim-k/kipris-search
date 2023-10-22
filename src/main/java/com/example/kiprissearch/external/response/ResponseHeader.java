package com.example.kiprissearch.external.response;

import javax.xml.bind.annotation.XmlElement;

public class ResponseHeader {

    private String responseTime;
    private String successYN;
    private String resultCode;
    private String resultMsg;

    @XmlElement(name = "responseTime")
    public String getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(String responseTime) {
        this.responseTime = responseTime;
    }

    @XmlElement(name = "successYN")
    public String getSuccessYN() {
        return successYN;
    }

    public void setSuccessYN(String successYN) {
        this.successYN = successYN;
    }

    @XmlElement(name = "resultCode")
    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @XmlElement(name = "resultMsg")
    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }
}
