package edu.byu.cs.tweeter.client.model.service;

import android.os.Bundle;

import java.util.List;
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
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetItemsCountHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsCountObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {


    public FollowService() {}


    public void isFollower(User selectedUser, GetItemsObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public void unfollow(User selectedUser, MainActivityObserver observer) {

        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);

    }

    public void follow(User selectedUser, MainActivityObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new SimpleNotificationHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    public void updateUserFollows(User selectedUser, GetItemsCountObserver observer) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetItemsCountHandler(observer));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetItemsCountHandler(observer));
        executor.execute(followingCountTask);
    }

    public void loadMoreItemsFollowing(User user, int pageSize, User lastFollowee, FollowingObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new PagedHandler<User>(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void loadMoreItemsFollowers(User user, int pageSize, User lastFollower, FollowersObserver observer) {

        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new PagedHandler<User>(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);

    }


    public interface FollowingObserver extends PagedObserver<User> { }

    public interface FollowersObserver extends PagedObserver {

        void displayError(String message);

        void displayException(Exception ex);

//        void handleSuccess(List<User> followers, boolean hasMorePages);
    }

    public interface MainActivityObserver extends SimpleNotificationObserver {

        void setFollowing(boolean isFollower);

        void displayMessage(String s);

        void handleSuccess();

        void setFolloweeCountText(int count);

        void setFollowerCount(int count);
    }


}
