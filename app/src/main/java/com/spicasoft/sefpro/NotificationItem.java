package com.spicasoft.sefpro;

/**
 * Created by USER on 20-09-2017.
 */

public class NotificationItem {

    private String Title;
    private String Message;
    private String image;
    private String Datetime;


    public NotificationItem(String title, String message,String image, String datetime) {
        this.image = image;
        Title = title;
        Message = message;
        Datetime = datetime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDatetime() {
        return Datetime;
    }

    public void setDatetime(String datetime) {
        Datetime = datetime;
    }
}
