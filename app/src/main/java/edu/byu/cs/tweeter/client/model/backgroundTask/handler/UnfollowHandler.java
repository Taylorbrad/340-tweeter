package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.backgroundTask.UnfollowTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

// UnfollowHandler
public class UnfollowHandler extends Handler {


    private FollowService.MainActivityObserver presenterObserver;

    public UnfollowHandler(FollowService.MainActivityObserver observer) {
        super(Looper.getMainLooper());
        this.presenterObserver = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(UnfollowTask.SUCCESS_KEY);
        if (success) {
            presenterObserver.updateUserFollows(true);

        } else if (msg.getData().containsKey(UnfollowTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(UnfollowTask.MESSAGE_KEY);
            presenterObserver.displayMessage("Failed to unfollow: " + message);

        } else if (msg.getData().containsKey(UnfollowTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(UnfollowTask.EXCEPTION_KEY);
            presenterObserver.displayMessage("Failed to unfollow because of exception: " + ex.getMessage());
        }

        presenterObserver.setFollowButton(true);
    }
}
