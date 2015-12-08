package com.kickbackapps.ghostcall.ApiObjects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ynott on 9/14/15.
 */
public class AreaCodeObject {
    @Expose
    private Boolean error;
    @Expose
    private String category;
    @Expose
    private String message;
    @SerializedName("area_code")
    @Expose
    private String areaCode;
    @Expose
    private Boolean available;

    /**
     *
     * @return
     * The error
     */
    public Boolean getError() {
        return error;
    }

    /**
     *
     * @param error
     * The error
     */
    public void setError(Boolean error) {
        this.error = error;
    }

    /**
     *
     * @return
     * The category
     */
    public String getCategory() {
        return category;
    }

    /**
     *
     * @param category
     * The category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     *
     * @return
     * The message
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     * The message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return
     * The areaCode
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     *
     * @param areaCode
     * The area_code
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     *
     * @return
     * The available
     */
    public Boolean getAvailable() {
        return available;
    }

    /**
     *
     * @param available
     * The available
     */
    public void setAvailable(Boolean available) {
        this.available = available;
    }
}
