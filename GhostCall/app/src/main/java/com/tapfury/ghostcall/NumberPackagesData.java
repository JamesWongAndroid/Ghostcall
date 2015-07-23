package com.tapfury.ghostcall;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ynott on 7/22/15.
 */
public class NumberPackagesData {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String type;
    @Expose
    private String credits;
    @Expose
    private String cost;
    @SerializedName("ios_product_id")
    @Expose
    private String iosProductId;
    @SerializedName("android_product_id")
    @Expose
    private String androidProductId;
    @Expose
    private String expiration;
    @SerializedName("created_on")
    @Expose
    private String createdOn;

    /**
     *
     * @return
     * The id
     */
    public String getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The type
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param type
     * The type
     */
    public void setType(String type) {
        this.type = type;
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

    /**
     *
     * @return
     * The cost
     */
    public String getCost() {
        return cost;
    }

    /**
     *
     * @param cost
     * The cost
     */
    public void setCost(String cost) {
        this.cost = cost;
    }

    /**
     *
     * @return
     * The iosProductId
     */
    public String getIosProductId() {
        return iosProductId;
    }

    /**
     *
     * @param iosProductId
     * The ios_product_id
     */
    public void setIosProductId(String iosProductId) {
        this.iosProductId = iosProductId;
    }

    /**
     *
     * @return
     * The androidProductId
     */
    public String getAndroidProductId() {
        return androidProductId;
    }

    /**
     *
     * @param androidProductId
     * The android_product_id
     */
    public void setAndroidProductId(String androidProductId) {
        this.androidProductId = androidProductId;
    }

    /**
     *
     * @return
     * The expiration
     */
    public String getExpiration() {
        return expiration;
    }

    /**
     *
     * @param expiration
     * The expiration
     */
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    /**
     *
     * @return
     * The createdOn
     */
    public String getCreatedOn() {
        return createdOn;
    }

    /**
     *
     * @param createdOn
     * The created_on
     */
    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

}
