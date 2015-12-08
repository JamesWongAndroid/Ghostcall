package com.kickbackapps.ghostcall.objects;

/**
 * Created by Ynott on 8/24/15.
 */
public class SmsObject {

    String messageName;
    String messageText;
    String messageDate;
    String messageDirection;
    String messageNumber;

    public String getMessageName() {
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }

    public String getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(String messageDate) {
        this.messageDate = messageDate;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageDirection() {
        return messageDirection;
    }

    public void setMessageDirection(String messageDirection) {
        this.messageDirection = messageDirection;
    }

    public String getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(String messageNumber) {
        this.messageNumber = messageNumber;
    }
}
