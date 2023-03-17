package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.List;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of other users being followed by a specified user.
 */
public class GetFollowingTask extends PagedUserTask {

    public GetFollowingTask(AuthToken authToken, User targetUser, int limit, User lastFollowee,
                            Handler messageHandler) {
        super(authToken, targetUser, limit, lastFollowee, messageHandler);
    }

    @Override
    protected Pair<List<User>, Boolean> getItems() {
        FollowingResponse response = null;
        try {
            User lastUser = getLastItem();
            String lastUserAlias = "";

            if (lastUser != null)
            {
                lastUserAlias = lastUser.getAlias();
            }

            ServerFacade serverFacade = new ServerFacade();

            FollowingRequest request = new FollowingRequest(this.getAuthToken(),
                    getTargetUser().getAlias(),
                    getLimit(),
                    lastUserAlias);

            response = serverFacade.getFollowees(request, "/getfollowing");

        } catch (TweeterRemoteException te) {
            System.out.println("te");
            System.out.println(te.getMessage());
        } catch (IOException e) {
            System.out.println("e");
            System.out.println(e.getMessage());
        }

        return new Pair<>(response.getFollowees(), response.getHasMorePages());
//        return getFakeData().getPageOfUsers(getLastItem(), getLimit(), getTargetUser());
    }
}
