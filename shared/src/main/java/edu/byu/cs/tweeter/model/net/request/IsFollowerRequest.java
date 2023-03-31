package edu.byu.cs.tweeter.model.net.request;

public class IsFollowerRequest {

    private String follower;
    private String followee;

    /**
     * Allows construction of the object from Json. Private so it won't be called in normal code.
     */
    private IsFollowerRequest() {}

    /**
     * Creates an instance.
     *
     * @param follower the username of the user to be logged in.
     * @param followee the password of the user to be logged in.
     */
    public IsFollowerRequest(String follower, String followee) {
        this.follower = follower;
        this.followee = followee;
    }

    /**
     * Returns the username of the user to be logged in by this request.
     *
     * @return the username.
     */
    public String getFollower() {
        return follower;
    }

    /**
     * Sets the username.
     *
     * @param follower the username.
     */
    public void setFollower(String follower) {
        this.follower = follower;
    }
    /**
     * Returns the password of the user to be logged in by this request.
     *
     * @return the password.
     */
    public String getFollowee() {
        return followee;
    }

    /**
     * Sets the password.
     *
     * @param followee the password.
     */
    public void setFollowee(String followee) {
        this.followee = followee;
    }
}
