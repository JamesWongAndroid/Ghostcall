package com.tapfury.ghostcall.User;

import com.google.gson.annotations.Expose;

/**
 * Created by Ynott on 7/24/15.
 */
public class Balance {

    @Expose
    private String sms;
    @Expose
    private String minutes;
    @Expose
    private String credits;

    /**
     *
     * @return
     * The sms
     */
    public String getSms() {
        return sms;
    }

    /**
     *
     * @param sms
     * The sms
     */
    public void setSms(String sms) {
        this.sms = sms;
    }

    /**
     *
     * @return
     * The minutes
     */
    public String getMinutes() {
        return minutes;
    }

    /**
     *
     * @param minutes
     * The minutes
     */
    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    /**
     *
     * @return
     * The credits
     */
    public String getCredits() {
        return credits;
    }

    /**
     *
     * @param credits
     * The credits
     */
    public void setCredits(String credits) {
        this.credits = credits;
    }

}
