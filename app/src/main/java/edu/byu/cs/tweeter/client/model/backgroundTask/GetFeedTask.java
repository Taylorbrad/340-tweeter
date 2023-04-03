package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's feed.
 */
public class GetFeedTask extends PagedStatusTask {

    public GetFeedTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                       Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    protected Pair<List<Status>, Boolean> getItems() {

        GetFeedResponse response = null;
        Status lastStatus = getLastItem();
        if (lastStatus == null) {
            lastStatus = new Status("", new User(""), Long.valueOf(1), new ArrayList<>(), new ArrayList<>());
        }

        try {
            ServerFacade serverFacade = new ServerFacade();
            GetFeedRequest request = new GetFeedRequest(Cache.getInstance().getCurrUserAuthToken(), getTargetUser().getAlias(), getLimit(), lastStatus);

            response = serverFacade.getFeed(request, "/getfeed");

            return new Pair<>(response.getFeed(), response.getHasMorePages());

        } catch (TweeterRemoteException te)
        {
            sendFailedMessage("task fail tr: " + te.getMessage());
//            System.out.println(te.getMessage());
            return new Pair<>(new ArrayList<>(), false);
        } catch (IOException e) {
            sendFailedMessage("task fail io: " + e.getMessage());
//            System.out.println(te.getMessage());
            return new Pair<>(new ArrayList<>(), false);
        }



//        User loggedInUser = response.getUser();
//        AuthToken authToken = response.getAuthToken();
//        return new Pair<>(loggedInUser, authToken);
//        return getFakeData().getPageOfStatus(getLastItem(), getLimit());
    }
}
