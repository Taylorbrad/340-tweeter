package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFollowerCountHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsCountObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.IsFollowerHandlerObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.presenter.PagedPresenter;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    private ExecutorService executor;

    public FollowService() {
        executor = Executors.newSingleThreadExecutor();
    }

    public void isFollower(User selectedUser, IsFollowerHandlerObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        executor.execute(isFollowerTask);
    }

    public void unfollow(User selectedUser, MainActivityObserver observer) {

        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        executor.execute(unfollowTask);

    }

    public void follow(User selectedUser, MainActivityObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        executor.execute(followTask);
    }

    public void updateUserFollows(User selectedUser, GetItemsCountObserver observer) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowerCountHandler(observer));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);
    }

    public void loadMoreItemsFollowing(User user, int pageSize, User lastFollowee, PagedPresenter.GetItemsObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new PagedHandler<User>(observer));
        executor.execute(getFollowingTask);
    }

    public void loadMoreItemsFollowers(User user, int pageSize, User lastFollower, PagedPresenter.GetItemsObserver observer) {

        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new PagedHandler<User>(observer));
        executor.execute(getFollowersTask);

    }

    public interface MainActivityObserver extends SimpleNotificationObserver {

        void setFollowing(boolean isFollower);

        void displayMessage(String s);

        void handleSuccess();

        void setFolloweeCountText(int count);

        void setFollowerCount(int count);
    }


}
