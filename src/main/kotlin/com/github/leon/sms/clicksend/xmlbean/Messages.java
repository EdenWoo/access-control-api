package com.github.leon.sms.clicksend.xmlbean;

public class Messages {
    private Message message;

    private String recipientcount;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public String getRecipientcount() {
        return recipientcount;
    }

    public void setRecipientcount(String recipientcount) {
        this.recipientcount = recipientcount;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + message + ", recipientcount = " + recipientcount + "]";
    }
}
			
			