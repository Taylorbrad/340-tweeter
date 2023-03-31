package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

/**
 * Background task that queries how many followers a user has.
 */
public class GetFollowersCountTask extends GetCountTask {

    public static final String FOLLOWER_COUNT_KEY = "follower_count";

    private int followerCount;

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected void runCountTask() {

        GetFollowerCountResponse response = null;
        try
        {
            ServerFacade serverFacade = new ServerFacade();
            GetFollowerCountRequest request = new GetFollowerCountRequest(Cache.getInstance().getCurrUserAuthToken(), getTargetUser());

            response = serverFacade.getFollowerCount(request, "/getfollowercount");

        } catch (TweeterRemoteException te)
        {
            System.out.println(te.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        this.followerCount = response.getFollowerCount();
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putInt(FOLLOWER_COUNT_KEY, this.getFollowerCount());
    }

    public int getFollowerCount() {
        return followerCount;
    }
}
