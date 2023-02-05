package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.backgroundTask.FollowTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

// FollowHandler
public class FollowHandler extends Handler {

    private FollowService.MainActivityObserver presenterObserver;


    public FollowHandler(FollowService.MainActivityObserver observer) {
        super(Looper.getMainLooper());
        this.presenterObserver = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(FollowTask.SUCCESS_KEY);
        if (success) {
            presenterObserver.updateUserFollows(false);

        } else if (msg.getData().containsKey(FollowTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(FollowTask.MESSAGE_KEY);
            presenterObserver.displayMessage("Failed to follow: " + message);

        } else if (msg.getData().containsKey(FollowTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(FollowTask.EXCEPTION_KEY);
            presenterObserver.displayMessage("Failed to follow because of exception: " + ex.getMessage());
        }

        presenterObserver.setFollowButton(true);

    }
}
