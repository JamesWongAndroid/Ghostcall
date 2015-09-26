package com.kickbackapps.ghostcall.SoundEffects;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ynott on 7/22/15.
 */
public class Item {

    @Expose
    private String id;
    @SerializedName("effect_id")
    @Expose
    private String effectId;
    @Expose
    private String name;
    @SerializedName("audio_name")
    @Expose
    private String audioName;
    @Expose
    private String volume;
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
    @SerializedName("audio_url")
    @Expose
    private String audioUrl;

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
     * The effectId
     */
    public String getEffectId() {
        return effectId;
    }

    /**
     *
     * @param effectId
     * The effect_id
     */
    public void setEffectId(String effectId) {
        this.effectId = effectId;
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
     * The audioName
     */
    public String getAudioName() {
        return audioName;
    }

    /**
     *
     * @param audioName
     * The audio_name
     */
    public void setAudioName(String audioName) {
        this.audioName = audioName;
    }

    /**
     *
     * @return
     * The volume
     */
    public String getVolume() {
        return volume;
    }

    /**
     *
     * @param volume
     * The volume
     */
    public void setVolume(String volume) {
        this.volume = volume;
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
     * The audioUrl
     */
    public String getAudioUrl() {
        return audioUrl;
    }

    /**
     *
     * @param audioUrl
     * The audio_url
     */
    public void setAudioUrl(String audioUrl) {
        this.audioUrl = audioUrl;
    }

}
