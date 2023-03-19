package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Handler;

import java.io.IOException;
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
                    getLimit(),
                    lastUserAlias);

            response = serverFacade.getFollowers(request, "/getfollowers");

        } catch (TweeterRemoteException te) {
            System.out.println("te");
            System.out.println(te.getMessage());
        } catch (IOException e) {
            System.out.println("e");
            System.out.println(e.getMessage());
        }

        return new Pair<>(response.getFollowers(), response.getHasMorePages());
//        return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }
}
