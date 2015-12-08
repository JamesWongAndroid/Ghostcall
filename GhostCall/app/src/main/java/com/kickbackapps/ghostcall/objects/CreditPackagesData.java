package com.kickbackapps.ghostcall.objects;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ynott on 7/22/15.
 */
public class CreditPackagesData {

    private String id;
    private String name;
    private String description;
    private String cost;
    private String credits;
    @SerializedName("ios_product_id")
    private String iosProductId;
    @SerializedName("android_product_id")
    private String androidProductId;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("updated_at")
    private String updatedAt;
    private String deleted;

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
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
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
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     *
     * @return
     * The deleted
     */
    public String getDeleted() {
        return deleted;
    }

    /**
     *
     * @param deleted
     * The deleted
     */
    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }
}
