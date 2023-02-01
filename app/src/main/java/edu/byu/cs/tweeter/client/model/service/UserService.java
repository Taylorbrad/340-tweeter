package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.model.domain.User;

public class UserService {

    public interface Observer {

        void displayUser(User user);

        void displayMessage(String s);
    }

    public void getUserFollowers(String userAlias, Observer observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandlerFollowers(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }

    public void getUserFollowing(String userAlias, Observer observer) {
        GetUserTask getUserTask = new GetUserTask(Cache.getInstance().getCurrUserAuthToken(),
                userAlias, new GetUserHandlerFollowing(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getUserTask);
    }


    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandlerFollowing extends Handler {

        private Observer presenterObserver;

        public GetUserHandlerFollowing(Observer observer) {
            super(Looper.getMainLooper());

            this.presenterObserver = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {

                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                presenterObserver.displayUser(user);

            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {

                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                presenterObserver.displayMessage("Failed to get user's profile: " + message);

            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                presenterObserver.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
            }
        }
    }

    /**
     * Message handler (i.e., observer) for GetUserTask.
     */
    private class GetUserHandlerFollowers extends Handler {

        private Observer presenterObserver;

        public GetUserHandlerFollowers(Observer observer) {
            super(Looper.getMainLooper());

            this.presenterObserver = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(GetUserTask.SUCCESS_KEY);
            if (success) {
                User user = (User) msg.getData().getSerializable(GetUserTask.USER_KEY);
                presenterObserver.displayUser(user);

            } else if (msg.getData().containsKey(GetUserTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(GetUserTask.MESSAGE_KEY);
                presenterObserver.displayMessage("Failed to get user's profile: " + message);
            } else if (msg.getData().containsKey(GetUserTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(GetUserTask.EXCEPTION_KEY);
                presenterObserver.displayMessage("Failed to get user's profile because of exception: " + ex.getMessage());
            }
        }
    }

}
