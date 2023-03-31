package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public class UnfollowRequest {

    private String sourceAlias;
    private String targetAlias;
    private AuthToken authToken;

    UnfollowRequest() {}

    public UnfollowRequest(String sourceAlias, String targetAlias, AuthToken authToken) {
        this.sourceAlias = sourceAlias;
        this.targetAlias = targetAlias;
        this.authToken = authToken;
    }

    public String getSourceAlias() {
        return sourceAlias;
    }

    public void setSourceAlias(String sourceAlias) {
        this.sourceAlias = sourceAlias;
    }

    public String getTargetAlias() {
        return targetAlias;
    }

    public void setTargetAlias(String targetAlias) {
        this.targetAlias = targetAlias;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }
}
