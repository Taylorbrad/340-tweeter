package edu.byu.cs.tweeter.model.net.response;

public class GetFollowerCountResponse extends Response {

    private int followerCount;

    GetFollowerCountResponse(String message) {
        super(false, message);
    }

    public GetFollowerCountResponse(int followerCount) {
        super(true, null);
        this.followerCount = followerCount;

    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }
}
