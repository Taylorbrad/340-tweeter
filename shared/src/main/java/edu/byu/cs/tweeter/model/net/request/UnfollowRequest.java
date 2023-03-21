package edu.byu.cs.tweeter.model.net.request;

public class UnfollowRequest {
    private String sourceAlias;
    private String targetAlias;

    UnfollowRequest() {}

    public UnfollowRequest(String sourceAlias, String targetAlias) {
        this.sourceAlias = sourceAlias;
        this.targetAlias = targetAlias;
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
}
