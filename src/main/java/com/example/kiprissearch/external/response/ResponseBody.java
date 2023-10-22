package com.example.kiprissearch.external.response;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlType(propOrder = {"items"})
public class ResponseBody {

    private List<Item> items;

    @XmlElement(name = "items")
    public List<Item> getItems() {
        return items;
    }

    @XmlElement(name = "item")
    public void setItems(List<Item> items) {
        this.items = items;
    }


}

