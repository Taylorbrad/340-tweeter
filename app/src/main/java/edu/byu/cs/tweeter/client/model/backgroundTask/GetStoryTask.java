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
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.util.Pair;

/**
 * Background task that retrieves a page of statuses from a user's story.
 */
public class GetStoryTask extends PagedStatusTask {

    public GetStoryTask(AuthToken authToken, User targetUser, int limit, Status lastStatus,
                        Handler messageHandler) {
        super(authToken, targetUser, limit, lastStatus, messageHandler);
    }

    @Override
    public Pair<List<Status>, Boolean> getItems() {

        GetStoryResponse response = null;
        try
        {
            Status lastStatus = getLastItem();
            if (lastStatus == null)
            {
                lastStatus = new Status("", new User(""), Long.valueOf(1), new ArrayList<>(), new ArrayList<>());
            }
            ServerFacade serverFacade = new ServerFacade();
            GetStoryRequest request = new GetStoryRequest(Cache.getInstance().getCurrUserAuthToken(), Cache.getInstance().getCurrUser().getAlias(), getLimit(), lastStatus);


            response = serverFacade.getStory(request, "/getstory");
            return new Pair<>(response.getStory(), response.getHasMorePages());

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



//        return getFakeData().getPageOfStatus(getLastItem(), getLimit());
    }
}
