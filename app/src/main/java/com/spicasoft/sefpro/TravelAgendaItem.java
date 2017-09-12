package com.spicasoft.sefpro;

/**
 * Created by USER on 12-09-2017.
 */

public class TravelAgendaItem {

    public static final int FLIGHT=0;
    public static final int TAXI=1;
    public static final int HOTEL=2;
    public static final int SERVICE=3;

    private int TypeId;
    private String Name;
    private String From;
    private String To;
    private String From_DateTime;
    private String To_DateTime;
    private String From_Code;
    private String To_Code;
    private String From_Gate;
    private String To_Gate;
    private String Booking_Name;
    private String Room_No;
    private String Mobile;
    private String ImgUrl;



    public TravelAgendaItem(int TypeId,String Name, String From,String To,
                            String From_DateTime,String To_DateTime,String From_Code,
                            String To_Code,String From_Gate,String To_Gate,String Booking_Name
            ,String Room_No,String Mobile, String ImgUrl
    ) {

        this.TypeId=TypeId;
        this.Name=Name;
        this.From=From;
        this.To=To;
        this.From_DateTime=From_DateTime;
        this.To_DateTime=To_DateTime;
        this.From_Code=From_Code;
        this.To_Code=To_Code;
        this.From_Gate=From_Gate;
        this.To_Gate=To_Gate;
        this.Booking_Name=Booking_Name;
        this.Room_No=Room_No;
        this.Mobile = Mobile;
        this.ImgUrl = ImgUrl;


    }

    public int getTypeId() {
        return TypeId;
    }

    public void setTypeId(int typeId) {
        TypeId = typeId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        From = from;
    }

    public String getTo() {
        return To;
    }

    public void setTo(String to) {
        To = to;
    }

    public String getFrom_DateTime() {
        return From_DateTime;
    }

    public void setFrom_DateTime(String from_DateTime) {
        From_DateTime = from_DateTime;
    }

    public String getTo_DateTime() {
        return To_DateTime;
    }

    public void setTo_DateTime(String to_DateTime) {
        To_DateTime = to_DateTime;
    }

    public String getFrom_Code() {
        return From_Code;
    }

    public void setFrom_Code(String from_Code) {
        From_Code = from_Code;
    }

    public String getTo_Code() {
        return To_Code;
    }

    public void setTo_Code(String to_Code) {
        To_Code = to_Code;
    }

    public String getFrom_Gate() {
        return From_Gate;
    }

    public void setFrom_Gate(String from_Gate) {
        From_Gate = from_Gate;
    }

    public String getTo_Gate() {
        return To_Gate;
    }

    public void setTo_Gate(String to_Gate) {
        To_Gate = to_Gate;
    }

    public String getBooking_Name() {
        return Booking_Name;
    }

    public void setBooking_Name(String booking_Name) {
        Booking_Name = booking_Name;
    }

    public String getRoom_No() {
        return Room_No;
    }

    public void setRoom_No(String room_No) {
        Room_No = room_No;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
