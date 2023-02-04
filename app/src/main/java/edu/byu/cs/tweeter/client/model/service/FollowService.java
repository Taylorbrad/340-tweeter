package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

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
import edu.byu.cs.tweeter.model.domain.User;

public class FollowService {

    private MainActivityObserver MAPresenterObserver;

    public FollowService() {}

    public FollowService(MainActivityObserver presenterObserver) {

        this.MAPresenterObserver = presenterObserver;

    }

    public void isFollower(User selectedUser, MainActivityObserver observer) {
        IsFollowerTask isFollowerTask = new IsFollowerTask(Cache.getInstance().getCurrUserAuthToken(),
                Cache.getInstance().getCurrUser(), selectedUser, new IsFollowerHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(isFollowerTask);
    }

    public void unfollow(User selectedUser) {

        UnfollowTask unfollowTask = new UnfollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new UnfollowHandler());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(unfollowTask);

    }

    public void follow(User selectedUser) {
        FollowTask followTask = new FollowTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new FollowHandler());
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(followTask);
    }

    public void updateUserFollows(User selectedUser) {
        ExecutorService executor = Executors.newFixedThreadPool(2);

        // Get count of most recently selected user's followers.
        GetFollowersCountTask followersCountTask = new GetFollowersCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowersCountHandler());
        executor.execute(followersCountTask);

        // Get count of most recently selected user's followees (who they are following)
        GetFollowingCountTask followingCountTask = new GetFollowingCountTask(Cache.getInstance().getCurrUserAuthToken(),
                selectedUser, new GetFollowingCountHandler());
        executor.execute(followingCountTask);
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


    // IsFollowerHandler
    private class IsFollowerHandler extends Handler {

//        private MainActivityObserver presenterObserver;

        public IsFollowerHandler(MainActivityObserver observer) {
            super(Looper.getMainLooper());
//            this.presenterObserver = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(IsFollowerTask.SUCCESS_KEY);
            if (success) {
                boolean isFollower = msg.getData().getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);

                MAPresenterObserver.setFollowing(isFollower);

            } else if (msg.getData().containsKey(IsFollowerTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(IsFollowerTask.MESSAGE_KEY);
                MAPresenterObserver.displayMessage("Failed to determine following relationship: " + message);

            } else if (msg.getData().containsKey(IsFollowerTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(IsFollowerTask.EXCEPTION_KEY);
                MAPresenterObserver.displayMessage("Failed to determine following relationship because of exception: " + ex.getMessage());
            }
        }
    }

    // GetFollowersCountHandler

    private class GetFollowersCountHandler extends Handler {

        public GetFollowersCountHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
                MAPresenterObserver.setFollowerCount(count);

            } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
                MAPresenterObserver.displayMessage("Failed to get followers count: " + message);

            } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
                MAPresenterObserver.displayMessage("Failed to get followers count because of exception: " + ex.getMessage());

            }
        }
    }


    // GetFollowingCountHandler

    private class GetFollowingCountHandler extends Handler {

        public GetFollowingCountHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
            if (success) {
                int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
                MAPresenterObserver.setFolloweeCountText(count);

            } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
                MAPresenterObserver.displayMessage("Failed to get following count: " + message);

            } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
                MAPresenterObserver.displayMessage("Failed to get following count because of exception: " + ex.getMessage());

            }
        }
    }

    // FollowHandler

    private class FollowHandler extends Handler {

        public FollowHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
            if (success) {
                MAPresenterObserver.updateUserFollows(false);

            } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
                MAPresenterObserver.displayMessage("Failed to follow: " + message);

            } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
                MAPresenterObserver.displayMessage("Failed to follow because of exception: " + ex.getMessage());
            }

            MAPresenterObserver.setFollowButton(true);

        }
    }

    // UnfollowHandler
    private class UnfollowHandler extends Handler {

        public UnfollowHandler() {
            super(Looper.getMainLooper());
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
            if (success) {
                MAPresenterObserver.updateUserFollows(true);

            } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
                MAPresenterObserver.displayMessage("Failed to unfollow: " + message);

            } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
                MAPresenterObserver.displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
            }

            MAPresenterObserver.setFollowButton(true);
        }
    }


    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */
    private class GetFollowingHandler extends Handler {

        private FollowingObserver presenterObserver;

        public GetFollowingHandler(FollowingObserver observer) {
            super(Looper.getMainLooper());

            this.presenterObserver = observer;
        }


        @Override
        public void handleMessage(@NonNull Message msg) {


            boolean success = msg.getData().getBoolean(GetFollowingTask.SUCCESS_KEY);
            if (success) {
                List<User> followees = (List<User>) msg.getData().getSerializable(GetFollowingTask.FOLLOWEES_KEY);

                boolean hasMorePages = msg.getData().getBoolean(GetFollowingTask.MORE_PAGES_KEY);

                presenterObserver.passFollowees(followees, hasMorePages);

            } else if (msg.getData().containsKey(GetFollowingTask.MESSAGE_KEY)) {

                String message = msg.getData().getString(GetFollowingTask.MESSAGE_KEY);
                presenterObserver.displayError(message);

            } else if (msg.getData().containsKey(GetFollowingTask.EXCEPTION_KEY)) {

                Exception ex = (Exception) msg.getData().getSerializable(GetFollowingTask.EXCEPTION_KEY);
                presenterObserver.displayException(ex);

            }
        }
    }

    /**
     * Message handler (i.e., observer) for GetFollowersTask.
     */
    private class GetFollowersHandler extends Handler {

        private FollowersObserver presenterObserver;

        public GetFollowersHandler(FollowersObserver observer) {
            super(Looper.getMainLooper());

            this.presenterObserver = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {

            boolean success = msg.getData().getBoolean(GetFollowersTask.SUCCESS_KEY);
            if (success) {

                List<User> followers = (List<User>) msg.getData().getSerializable(GetFollowersTask.FOLLOWERS_KEY);
                boolean hasMorePages = msg.getData().getBoolean(GetFollowersTask.MORE_PAGES_KEY);

                presenterObserver.passFollowers(followers, hasMorePages);

            } else if (msg.getData().containsKey(GetFollowersTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetFollowersTask.MESSAGE_KEY);
                presenterObserver.displayError("Failed to get followers: " + message);

            } else if (msg.getData().containsKey(GetFollowersTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetFollowersTask.EXCEPTION_KEY);
                presenterObserver.displayError("Failed to get followers because of exception: " + ex.getMessage());

            }
        }
    }

}
