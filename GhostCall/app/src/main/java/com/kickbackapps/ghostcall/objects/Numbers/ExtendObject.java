package com.kickbackapps.ghostcall.objects.Numbers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ynott on 9/15/15.
 */
public class ExtendObject {
    @SerializedName("expire_on")
    @Expose
    private String expireOn;

    /**
     *
     * @return
     * The expireOn
     */
    public String getExpireOn() {
        return expireOn;
    }

    /**
     *
     * @param expireOn
     * The expire_on
     */
    public void setExpireOn(String expireOn) {
        this.expireOn = expireOn;
    }
}
