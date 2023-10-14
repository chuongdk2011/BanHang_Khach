package com.example.banhang_khach.DTO;

public class ChatDTO {
    String message;
    String senderid;
    String timeStamp;

    public ChatDTO() {
    }

    public ChatDTO(String message, String senderid, String timeStamp) {
        this.message = message;
        this.senderid = senderid;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }
}
