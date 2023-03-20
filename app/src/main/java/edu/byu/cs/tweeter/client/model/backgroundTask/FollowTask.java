package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Follow;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that establishes a following relationship between two users.
 */
public class FollowTask extends AuthenticatedTask {
    /**
     * The user that is being followed.
     */
    private final User followee;

    public FollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(authToken, messageHandler);
        this.followee = followee;
    }

    @Override
    protected void runTask() {
        //TODO move the toggling stuff (presenter or view) to the server side in 4

        FollowResponse response = null;
        try
        {
            ServerFacade serverFacade = new ServerFacade();
            FollowRequest request = new FollowRequest(this.followee.getAlias(), Cache.getInstance().getCurrUser().getAlias());

            response = serverFacade.follow(request, "/follow");
        } catch (TweeterRemoteException te)
        {
            System.out.println(te.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

//        System.out.println("Server Follow Success");
        sendSuccessMessage();

//        return new Pair<>(loggedInUser, authToken);
        // We could do this from the presenter, without a task and handler, but we will
        // eventually access the database from here when we aren't using dummy data.

        // Call sendSuccessMessage if successful

        // or call sendFailedMessage if not successful
        // sendFailedMessage()
    }

}
