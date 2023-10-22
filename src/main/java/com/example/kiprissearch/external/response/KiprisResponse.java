package com.example.kiprissearch.external.response;

import org.apache.commons.lang3.StringUtils;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlRootElement(name = "response")
@XmlType(propOrder = {"header", "body", "count"})
public class KiprisResponse {
    private ResponseHeader header;
    private ResponseBody body;
    private ResponseCount count;


    @XmlElement(name = "header")
    public ResponseHeader getHeader() {
        return header;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    @XmlElement(name = "body")
    public ResponseBody getBody() {
        return body;
    }

    public void setBody(ResponseBody body) {
        this.body = body;
    }

    @XmlElement(name = "count")
    public ResponseCount getCount() {
        return count;
    }

    public void setCount(ResponseCount count) {
        this.count = count;
    }


    public boolean searchKeyword(String keyword) {
        List<Item> items = this.getBody().getItems();

        if (items == null) {
            return false;
        }

        return items.stream().anyMatch(item -> StringUtils.equals(item.getTitle(), keyword));
    }
}