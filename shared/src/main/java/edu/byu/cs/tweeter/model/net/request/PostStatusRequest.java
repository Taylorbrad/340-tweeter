package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class PostStatusRequest {

    private String status;
    private AuthToken authToken;

    PostStatusRequest() {}

    public PostStatusRequest(String status, AuthToken authToken) {
        this.status = status;
        this.authToken = authToken;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
