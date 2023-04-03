package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of followers.
 */
public class GetFollowersTask extends PagedUserTask {

    public GetFollowersTask(AuthToken authToken, User targetUser, int limit, User lastFollower,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollower, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {

        FollowerResponse response = null;
        try {
            User lastUser = getLastItem();
            String lastUserAlias = "";

            if (lastUser != null)
            {
                lastUserAlias = lastUser.getAlias();
            }

            ServerFacade serverFacade = new ServerFacade();

            FollowerRequest request = new FollowerRequest(this.getAuthToken(),
                    getTargetUser().getAlias(),
                    11,
                    lastUserAlias);

            response = serverFacade.getFollowers(request, "/getfollowers");

            return new Pair<>(response.getFollowers(), response.getHasMorePages());

        } catch (TweeterRemoteException te) {
//            System.out.println("te");
//            System.out.println(te.getMessage());
            sendFailedMessage(te.getMessage());
            return new Pair<>(new ArrayList<User>(), false);
        } catch (IOException e) {
//            System.out.println("e");
//            System.out.println(e.getMessage());
            sendFailedMessage(e.getMessage());
            return new Pair<>(new ArrayList<User>(), false);
        }


//        return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }
}
