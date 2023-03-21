package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowerCountRequest {

    private String authToken;
    private User targetUser;

    public GetFollowerCountRequest() {
    }

    public GetFollowerCountRequest(String authToken, User targetUser) {
        this.authToken = authToken;
        this.targetUser = targetUser;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public User getTargetUser() {
        return targetUser;
    }

    public void setTargetUser(User targetUser) {
        this.targetUser = targetUser;
    }
}
