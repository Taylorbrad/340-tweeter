package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowersCountTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

public class GetFollowersCountHandler extends Handler {

    private FollowService.MainActivityObserver presenterObserver;


    public GetFollowersCountHandler(FollowService.MainActivityObserver observer) {
        super(Looper.getMainLooper());
        this.presenterObserver = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFollowersCountTask.SUCCESS_KEY);
        if (success) {
            int count = msg.getData().getInt(GetFollowersCountTask.COUNT_KEY);
            presenterObserver.setFollowerCount(count);

        } else if (msg.getData().containsKey(GetFollowersCountTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFollowersCountTask.MESSAGE_KEY);
            presenterObserver.displayMessage("Failed to get followers count: " + message);

        } else if (msg.getData().containsKey(GetFollowersCountTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFollowersCountTask.EXCEPTION_KEY);
            presenterObserver.displayMessage("Failed to get followers count because of exception: " + ex.getMessage());

        }
    }
}
