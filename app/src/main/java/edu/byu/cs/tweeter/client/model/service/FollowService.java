package edu.byu.cs.tweeter.client.model.service;

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
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.FollowHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFollowersCountHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFollowersHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFollowingCountHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.GetFollowingHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.IsFollowerHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.UnfollowHandler;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {


    public FollowService() {}


    public void isFollower(User selectedUser, MainActivityObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public void unfollow(User selectedUser, MainActivityObserver observer) {

        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);

    }

    public void follow(User selectedUser, MainActivityObserver observer) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    public void updateUserFollows(User selectedUser, MainActivityObserver observer) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler(observer));
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler(observer));
        executor.execute(followingCountTask);
    }

    public void loadMoreItemsFollowing(User user, int pageSize, User lastFollowee, FollowingObserver observer) {
        GetFollowingTask getFollowingTask = new GetFollowingTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollowee, new GetFollowingHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowingTask);
    }

    public void loadMoreItemsFollowers(User user, int pageSize, User lastFollower, FollowersObserver observer) {

        GetFollowersTask getFollowersTask = new GetFollowersTask(Cache.getInstance().getCurrUserAuthToken(),
                user, pageSize, lastFollower, new GetFollowersHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getFollowersTask);

    }


    public interface FollowingObserver {

        void displayError(String message);

        void displayException(Exception ex);

        void passFollowees(List<User> followees, boolean hasMorePages);
    }

    public interface FollowersObserver {

        void displayError(String message);

        void displayException(Exception ex);

        void passFollowers(List<User> followers, boolean hasMorePages);
    }

    public interface MainActivityObserver {

        void setFollowing(boolean isFollower);

        void displayMessage(String s);

        void updateUserFollows(boolean b);

        void setFollowButton(boolean b);

        void setFolloweeCountText(int count);

        void setFollowerCount(int count);
    }


}
