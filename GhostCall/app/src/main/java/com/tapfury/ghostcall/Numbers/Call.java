package com.tapfury.ghostcall.Numbers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ynott on 7/23/15.
 */
public class Call {

    @Expose
    private String id;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("number_id")
    @Expose
    private String numberId;
    @Expose
    private String to;
    @Expose
    private String from;
    @Expose
    private String direction;
    @Expose
    private String status;
    @Expose
    private String pitch;
    @SerializedName("background_item_id")
    @Expose
    private String backgroundItemId;
    @Expose
    private String duration;
    @SerializedName("resource_id")
    @Expose
    private String resourceId;
    @Expose
    private String record;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

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
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The numberId
     */
    public String getNumberId() {
        return numberId;
    }

    /**
     *
     * @param numberId
     * The number_id
     */
    public void setNumberId(String numberId) {
        this.numberId = numberId;
    }

    /**
     *
     * @return
     * The to
     */
    public String getTo() {
        return to;
    }

    /**
     *
     * @param to
     * The to
     */
    public void setTo(String to) {
        this.to = to;
    }

    /**
     *
     * @return
     * The from
     */
    public String getFrom() {
        return from;
    }

    /**
     *
     * @param from
     * The from
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     *
     * @return
     * The direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     *
     * @param direction
     * The direction
     */
    public void setDirection(String direction) {
        this.direction = direction;
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
     * The pitch
     */
    public String getPitch() {
        return pitch;
    }

    /**
     *
     * @param pitch
     * The pitch
     */
    public void setPitch(String pitch) {
        this.pitch = pitch;
    }

    /**
     *
     * @return
     * The backgroundItemId
     */
    public String getBackgroundItemId() {
        return backgroundItemId;
    }

    /**
     *
     * @param backgroundItemId
     * The background_item_id
     */
    public void setBackgroundItemId(String backgroundItemId) {
        this.backgroundItemId = backgroundItemId;
    }

    /**
     *
     * @return
     * The duration
     */
    public String getDuration() {
        return duration;
    }

    /**
     *
     * @param duration
     * The duration
     */
    public void setDuration(String duration) {
        this.duration = duration;
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

    /**
     *
     * @return
     * The record
     */
    public String getRecord() {
        return record;
    }

    /**
     *
     * @param record
     * The record
     */
    public void setRecord(String record) {
        this.record = record;
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

}
