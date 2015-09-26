package com.kickbackapps.ghostcall.User;

/**
 * Created by Ynott on 7/30/15.
 */
public class CallObject {

    public String getToNumber() {
        return toNumber;
    }

    public void setToNumber(String toNumber) {
        this.toNumber = toNumber;
    }

    public String getNumberID() {
        return numberID;
    }

    public void setNumberID(String numberID) {
        this.numberID = numberID;
    }

    public String getVoiceChanger() {
        return voiceChanger;
    }

    public void setVoiceChanger(String voiceChanger) {
        this.voiceChanger = voiceChanger;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    String toNumber;
    String numberID;
    String voiceChanger;
    String verified;

}
