package edu.byu.cs.tweeter.model.net.request;

import edu.byu.cs.tweeter.model.net.response.FollowResponse;

public class FollowRequest {
    private String followee;
    private String follower;


    private FollowRequest() {}

    public FollowRequest(String followee, String follower) {
        this.followee = followee;
        this.follower = follower;
    }

    public String getFollowee() {
        return followee;
    }

    public void setFollowee(String followee) {
        this.followee = followee;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }
}
