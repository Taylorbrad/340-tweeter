package edu.byu.cs.tweeter.model.net.request;


import edu.byu.cs.tweeter.model.domain.Status;

public class GetStoryRequest {

    private String authToken;
    private String targetUser;
    private int limit;
    private Status lastStatus;

    public GetStoryRequest() {
    }

    public GetStoryRequest(String authToken, String targetUser, int limit, Status lastStatus) {
        this.authToken = authToken;
        this.targetUser = targetUser;
        this.limit = limit;
        this.lastStatus = lastStatus;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(String targetUser) {
        this.targetUser = targetUser;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public Status getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Status lastStatus) {
        this.lastStatus = lastStatus;
    }
}