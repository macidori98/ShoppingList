package com.example.shoppinglist.model;

public class Lists {
    private String sID;
    private String sCreatedByUserName;
    private String sLastEditDate, sTitle;

    public Lists(String sID, String sCreatedByUserName, String sLastEditDate, String sTitle) {
        this.sID = sID;
        this.sCreatedByUserName = sCreatedByUserName;
        this.sLastEditDate = sLastEditDate;
        this.sTitle = sTitle;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsCreatedByUserName() {
        return sCreatedByUserName;
    }

    public void setsCreatedByUserName(String sCreatedByUserName) {
        this.sCreatedByUserName = sCreatedByUserName;
    }

    public String getsLastEditDate() {
        return sLastEditDate;
    }

    public void setsLastEditDate(String sLastEditDate) {
        this.sLastEditDate = sLastEditDate;
    }

    public String getsTitle() {
        return sTitle;
    }

    public void setsTitle(String sTitle) {
        this.sTitle = sTitle;
    }
}
