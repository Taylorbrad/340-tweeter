package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingCountRequest {

    private String authToken;
    private User targetUser;

    public GetFollowingCountRequest() {
    }

    public GetFollowingCountRequest(String authToken, User targetUser) {
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
