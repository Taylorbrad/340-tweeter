package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowingCountTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;

// GetFollowingCountHandler
public class GetFollowingCountHandler extends Handler {

    private FollowService.MainActivityObserver presenterObserver;


    public GetFollowingCountHandler(FollowService.MainActivityObserver observer) {
        super(Looper.getMainLooper());
        this.presenterObserver = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(GetFollowingCountTask.SUCCESS_KEY);
        if (success) {
            int count = msg.getData().getInt(GetFollowingCountTask.COUNT_KEY);
            presenterObserver.setFolloweeCountText(count);

        } else if (msg.getData().containsKey(GetFollowingCountTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetFollowingCountTask.MESSAGE_KEY);
            presenterObserver.displayMessage("Failed to get following count: " + message);

        } else if (msg.getData().containsKey(GetFollowingCountTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetFollowingCountTask.EXCEPTION_KEY);
            presenterObserver.displayMessage("Failed to get following count because of exception: " + ex.getMessage());

        }
    }
}
