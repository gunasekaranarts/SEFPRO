package com.spicasoft.sefpro;

/**
 * Created by USER on 12-09-2017.
 */



public class RowItem {
    private int imageId;
    private String Name;
    private String Email;
    private String Mobile;


    public RowItem(int imageId,String Name, String Email, String Mobile) {
        this.imageId = imageId;
        this.Name = Name;
        this.Email = Email;
        this.Mobile = Mobile;
    }
    public int getImageId() {
        return imageId;
    }
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
    public String getEmail() {
        return Email;
    }
    public void setEmail(String Email) {
        this.Email = Email;
    }
    public String getName() {
        return Name;
    }
    public void setName(String Name) {
        this.Name = Name;
    }
    public String getMobile() {
        return Mobile;
    }
    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }
}

