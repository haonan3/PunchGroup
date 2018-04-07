package edu.illinois.finalproject;

import java.util.Date;

/**
 * Created by haonanwang on 11/25/17.
 */

public class Plan {
    private String planName;
    private String start;
    private String end;
    private long planCreatedTime;

    public Plan() {
        planCreatedTime = new Date().getTime();
    }

    public Plan(String planName, String start, String end) {
        this.planName = planName;
        this.start = start;
        this.end = end;
        planCreatedTime = new Date().getTime();
    }


    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getPlanName() {
        return planName;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public long getPlanCreatedTime() {
        return planCreatedTime;
    }
}
