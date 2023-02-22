package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetItemsHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.UserHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    private ExecutorService executor;

    public UserService() {
        executor = Executors.newSingleThreadExecutor();
    }

    public interface UserObserver extends PagedObserver {

        void handleSuccess(User user);

        void displayMessage(String message);

        void setLoadingFooter(boolean b);

        void handleSuccess(List<Status> statuses, boolean hasMorePages, Status lastStatus);
    }

    public interface GetItemsHandlerObserver extends edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsHandlerObserver {
        @Override
        void handleSuccess(Bundle data);
    }

    public interface GetUserObserver extends edu.byu.cs.tweeter.client.model.backgroundTask.observer.UserObserver {
        void handleSuccess(User user);
    }


    public void loadMoreItems(User user, int pageSize, Status lastStatus, GetItemsHandlerObserver observer) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new GetItemsHandler<Status>(observer));
        executor.execute(getStoryTask);
    }

    public void getFeed(User user, int pageSize, Status lastStatus, GetItemsHandlerObserver observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new GetItemsHandler<Status>(observer));
        executor.execute(getFeedTask);
    }

    public void getUserStory(String userAlias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new UserHandler(observer));
        executor.execute(getUserTask);
    }

    public void getUserFeed(String userAlias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new UserHandler(observer));
        executor.execute(getUserTask);
    }

    public void getUserFollowers(String userAlias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new UserHandler(observer));
        executor.execute(getUserTask);
    }

    public void getUserFollowing(String userAlias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new UserHandler(observer));
        executor.execute(getUserTask);
    }

}
