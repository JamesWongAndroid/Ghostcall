package com.tapfury.ghostcall;

/**
 * Created by Ynott on 7/8/15.
 */
public class GhostNumbers {

    private String ghostNumber;
    private String ghostTitle;
    private String ghostID;
    private String expirationDate;

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public String getGhostID() {
        return ghostID;
    }

    public void setGhostID(String ghostID) {
        this.ghostID = ghostID;
    }

    public String getGhostTitle() {
        return ghostTitle;
    }

    public void setGhostTitle(String ghostTitle) {

        this.ghostTitle = ghostTitle;
    }

    public String getGhostNumber() {

        return ghostNumber;
    }

    public void setGhostNumber(String ghostNumber) {

        this.ghostNumber = ghostNumber;
    }

}
