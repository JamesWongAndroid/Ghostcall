package com.tapfury.ghostcall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ynott on 7/30/15.
 */
public class CallData {

    @Expose
    private String category;
    @Expose
    private String message;
    @Expose
    private String dial;
    @SerializedName("resource_id")
    @Expose
    private String resourceId;

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
     * The dial
     */
    public String getDial() {
        return dial;
    }

    /**
     *
     * @param dial
     * The dial
     */
    public void setDial(String dial) {
        this.dial = dial;
    }

    /**
     *
     * @return
     * The resourceId
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     *
     * @param resourceId
     * The resource_id
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

}
