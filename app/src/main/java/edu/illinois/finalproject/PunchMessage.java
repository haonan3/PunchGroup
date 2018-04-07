package edu.illinois.finalproject;

import java.util.Date;

/**
 * Created by haonanwang on 11/22/17.
 */

public class PunchMessage {

    private String messageText;
    private String messageUser;
    private long messageTime;
    private String planname;
    private String startingDate;
    private String endDate;
    private String currentPosition;
    private int thumbUpNumber;
    private String messageKey;
    private String trash;
    private String whoGiveThumbUp;

    public PunchMessage(String messageText, String messageUser, String planname, String startingDate,
                        String endDate, String currentPosition) {
        this.messageText = messageText;
        this.messageUser = messageUser;
        this.planname = planname;
        this.startingDate = startingDate;
        this.endDate = endDate;
        this.currentPosition = currentPosition;
        thumbUpNumber = 0;
        messageTime = new Date().getTime();
        messageKey = "";
        trash = "1";
        whoGiveThumbUp = "__";
    }

    //this is a default constructor for firebase
    public PunchMessage(){
        thumbUpNumber = 0;
        messageTime = new Date().getTime();
        messageKey = "";
        trash = "1";
        whoGiveThumbUp = "__";
    }



    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(String messageUser) {
        this.messageUser = messageUser;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getPlanname() {
        return planname;
    }

    public String getStartingDate() {
        return startingDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getCurrentPosition() {
        return currentPosition;
    }

    public void setPlanname(String planname) {
        this.planname = planname;
    }

    public void setStartingDate(String startingDate) {
        this.startingDate = startingDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setCurrentPosition(String currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getThumbUpNumber() {
        return thumbUpNumber;
    }

    public void setThumbUpNumber(int thumbUpNumber) {
        this.thumbUpNumber = thumbUpNumber;
    }

    public String getMessageKey() {
        return messageKey;
    }

    public void setMessageKey(String messageKey) {
        this.messageKey = messageKey;
    }

    public String getTrash() {
        return trash;
    }

    public void setTrash(String trash) {
        this.trash = trash;
    }

    public String getWhoGiveThumbUp() {
        return whoGiveThumbUp;
    }

    public void setWhoGiveThumbUp(String whoGiveThumbUp) {
        this.whoGiveThumbUp = whoGiveThumbUp;
    }
}
