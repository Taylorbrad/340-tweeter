package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.UserHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    private ExecutorService executor;

    public UserService() {
        executor = Executors.newSingleThreadExecutor();
    }

    public interface GetUserObserver extends edu.byu.cs.tweeter.client.model.backgroundTask.observer.UserObserver {
        void handleSuccess(User user);
    }

    public void loadMoreItems(User user, int pageSize, Status lastStatus, PagedObserver<Status> observer) {
        GetStoryTask getStoryTask = new GetStoryTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PagedHandler<Status>(observer));
        executor.execute(getStoryTask);
    }

    public void getFeed(User user, int pageSize, Status lastStatus, PagedObserver<Status> observer) {
        GetFeedTask getFeedTask = new GetFeedTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastStatus, new PagedHandler<Status>(observer));
        executor.execute(getFeedTask);
    }

    public void getUser(String userAlias, GetUserObserver observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new UserHandler(observer));
        executor.execute(getUserTask);
    }

}
