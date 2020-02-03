package com.example.shoppinglist.model;

public class Elements {
    private String sID, sName, sListID;
    private boolean bChecked;

    public Elements(String sID, String sName, String sListID, boolean bChecked) {
        this.sID = sID;
        this.sName = sName;
        this.sListID = sListID;
        this.bChecked = bChecked;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsListID() {
        return sListID;
    }

    public void setsListID(String sListID) {
        this.sListID = sListID;
    }

    public boolean isbChecked() {
        return bChecked;
    }

    public void setbChecked(boolean bChecked) {
        this.bChecked = bChecked;
    }
}
