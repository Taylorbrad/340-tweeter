package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

/**
 * Background task that queries how many other users a specified user is following.
 */
public class GetFollowingCountTask extends GetCountTask {

    public static final String FOLLOWING_COUNT_KEY = "following_count";

    private int followingCount;

    public GetFollowingCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected void runCountTask() {
        GetFollowingCountResponse response = null;
        try
        {
            ServerFacade serverFacade = new ServerFacade();
            GetFollowingCountRequest request = new GetFollowingCountRequest(Cache.getInstance().getCurrUserAuthToken(), this.getTargetUser());

            response = serverFacade.getFollowingCount(request, "/getfollowingcount");

        } catch (TweeterRemoteException te)
        {
            System.out.println(te.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        this.followingCount = response.getFollowingCount();
//        return response.getFollowingCount();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(FOLLOWING_COUNT_KEY, this.getFollowingCount());
    }

    public int getFollowingCount() {
        return followingCount;
    }
}
