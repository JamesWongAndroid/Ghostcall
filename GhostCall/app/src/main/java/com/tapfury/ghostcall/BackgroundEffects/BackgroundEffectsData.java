package com.tapfury.ghostcall.BackgroundEffects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ynott on 7/23/15.
 */
public class BackgroundEffectsData {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String type;
    @Expose
    private String cost;
    @SerializedName("ios_product_id")
    @Expose
    private String iosProductId;
    @SerializedName("android_product_id")
    @Expose
    private String androidProductId;
    @SerializedName("image_active")
    @Expose
    private String imageActive;
    @SerializedName("image_on")
    @Expose
    private String imageOn;
    @SerializedName("image_off")
    @Expose
    private String imageOff;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @Expose
    private String status;
    @Expose
    private List<Item> items = new ArrayList<Item>();

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
     * The imageActive
     */
    public String getImageActive() {
        return imageActive;
    }

    /**
     *
     * @param imageActive
     * The image_active
     */
    public void setImageActive(String imageActive) {
        this.imageActive = imageActive;
    }

    /**
     *
     * @return
     * The imageOn
     */
    public String getImageOn() {
        return imageOn;
    }

    /**
     *
     * @param imageOn
     * The image_on
     */
    public void setImageOn(String imageOn) {
        this.imageOn = imageOn;
    }

    /**
     *
     * @return
     * The imageOff
     */
    public String getImageOff() {
        return imageOff;
    }

    /**
     *
     * @param imageOff
     * The image_off
     */
    public void setImageOff(String imageOff) {
        this.imageOff = imageOff;
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
     * The status
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The items
     */
    public List<Item> getItems() {
        return items;
    }

    /**
     *
     * @param items
     * The items
     */
    public void setItems(List<Item> items) {
        this.items = items;
    }

}
