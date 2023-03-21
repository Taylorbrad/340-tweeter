package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Handler;

import java.io.IOException;

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

    public GetFollowersCountTask(AuthToken authToken, User targetUser, Handler messageHandler) {
        super(authToken, targetUser, messageHandler);
    }

    @Override
    protected int runCountTask() {

        GetFollowerCountResponse response = null;
        try
        {
            ServerFacade serverFacade = new ServerFacade();
            GetFollowerCountRequest request = new GetFollowerCountRequest("getAuthToken().getToken()", getTargetUser());

            response = serverFacade.getFollowerCount(request, "/getfollowercount");

        } catch (TweeterRemoteException te)
        {
            System.out.println(te.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

//        System.out.println(response.getFollowerCount());
        return response.getFollowerCount();
    }
}
