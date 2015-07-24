package com.tapfury.ghostcall.Numbers;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ynott on 7/23/15.
 */

public class NumbersData {

    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String number;
    @SerializedName("expire_on")
    @Expose
    private String expireOn;
    @Expose
    private String voicemail;
    @SerializedName("disable_calls")
    @Expose
    private String disableCalls;
    @SerializedName("disable_messages")
    @Expose
    private String disableMessages;
    @Expose
    private List<Call> calls = new ArrayList<Call>();
    @Expose
    private List<Message> messages = new ArrayList<Message>();
    @Expose
    private List<Voicemail> voicemails = new ArrayList<Voicemail>();

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
     * The number
     */
    public String getNumber() {
        return number;
    }

    /**
     *
     * @param number
     * The number
     */
    public void setNumber(String number) {
        this.number = number;
    }

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

    /**
     *
     * @return
     * The voicemail
     */
    public String getVoicemail() {
        return voicemail;
    }

    /**
     *
     * @param voicemail
     * The voicemail
     */
    public void setVoicemail(String voicemail) {
        this.voicemail = voicemail;
    }

    /**
     *
     * @return
     * The disableCalls
     */
    public String getDisableCalls() {
        return disableCalls;
    }

    /**
     *
     * @param disableCalls
     * The disable_calls
     */
    public void setDisableCalls(String disableCalls) {
        this.disableCalls = disableCalls;
    }

    /**
     *
     * @return
     * The disableMessages
     */
    public String getDisableMessages() {
        return disableMessages;
    }

    /**
     *
     * @param disableMessages
     * The disable_messages
     */
    public void setDisableMessages(String disableMessages) {
        this.disableMessages = disableMessages;
    }

    /**
     *
     * @return
     * The calls
     */
    public List<Call> getCalls() {
        return calls;
    }

    /**
     *
     * @param calls
     * The calls
     */
    public void setCalls(List<Call> calls) {
        this.calls = calls;
    }

    /**
     *
     * @return
     * The messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     *
     * @param messages
     * The messages
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    /**
     *
     * @return
     * The voicemails
     */
    public List<Voicemail> getVoicemails() {
        return voicemails;
    }

    /**
     *
     * @param voicemails
     * The voicemails
     */
    public void setVoicemails(List<Voicemail> voicemails) {
        this.voicemails = voicemails;
    }
}
