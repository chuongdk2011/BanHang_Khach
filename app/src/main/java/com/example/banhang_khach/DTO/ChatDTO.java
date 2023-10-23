package com.example.banhang_khach.DTO;

public class ChatDTO {
    String id;
    String message;
    String senderid;
    long timeStamp;

    public ChatDTO() {
    }

    public ChatDTO(String id,String message, String senderid, long timeStamp) {
        this.id = id;
        this.message = message;
        this.senderid = senderid;
        this.timeStamp = timeStamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
