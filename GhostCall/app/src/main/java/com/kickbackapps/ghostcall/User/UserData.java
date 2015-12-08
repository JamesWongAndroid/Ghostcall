package com.kickbackapps.ghostcall.user;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Ynott on 7/24/15.
 */
public class UserData {

    @Expose
    private String id;
    @SerializedName("phone_number")
    @Expose
    private String phoneNumber;
    @SerializedName("device_token")
    @Expose
    private String deviceToken;
    @SerializedName("app_version")
    @Expose
    private String appVersion;
    @Expose
    private String platform;
    @SerializedName("platform_version")
    @Expose
    private String platformVersion;
    @SerializedName("api_key_id")
    @Expose
    private String apiKeyId;
    @Expose
    private String name;
    @Expose
    private String email;
    @Expose
    private String credits;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;
    @Expose
    private String deleted;
    @Expose
    private Balance balance;
    @Expose
    private Sip sip;

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
     * The phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     *
     * @param phoneNumber
     * The phone_number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     *
     * @return
     * The deviceToken
     */
    public String getDeviceToken() {
        return deviceToken;
    }

    /**
     *
     * @param deviceToken
     * The device_token
     */
    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    /**
     *
     * @return
     * The appVersion
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     *
     * @param appVersion
     * The app_version
     */
    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    /**
     *
     * @return
     * The platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     *
     * @param platform
     * The platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     *
     * @return
     * The platformVersion
     */
    public String getPlatformVersion() {
        return platformVersion;
    }

    /**
     *
     * @param platformVersion
     * The platform_version
     */
    public void setPlatformVersion(String platformVersion) {
        this.platformVersion = platformVersion;
    }

    /**
     *
     * @return
     * The apiKeyId
     */
    public String getApiKeyId() {
        return apiKeyId;
    }

    /**
     *
     * @param apiKeyId
     * The api_key_id
     */
    public void setApiKeyId(String apiKeyId) {
        this.apiKeyId = apiKeyId;
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
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param email
     * The email
     */
    public void setEmail(String email) {
        this.email = email;
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

    /**
     *
     * @return
     * The balance
     */
    public Balance getBalance() {
        return balance;
    }

    /**
     *
     * @param balance
     * The balance
     */
    public void setBalance(Balance balance) {
        this.balance = balance;
    }

    /**
     *
     * @return
     * The sip
     */
    public Sip getSip() {
        return sip;
    }

    /**
     *
     * @param sip
     * The sip
     */
    public void setSip(Sip sip) {
        this.sip = sip;
    }

}
