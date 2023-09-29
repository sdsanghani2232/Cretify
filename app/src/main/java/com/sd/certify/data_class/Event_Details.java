package com.sd.certify.data_class;

public class Event_Details {
    String event_name,img,date;

    public Event_Details(String event_name, String img, String date) {
        this.event_name = event_name;
        this.img = img;
        this.date = date;
    }

    public Event_Details() {
    }

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
