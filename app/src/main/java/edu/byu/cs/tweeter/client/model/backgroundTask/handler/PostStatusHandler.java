package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.service.StatusService;

// PostStatusHandler
public class PostStatusHandler extends Handler {

    private StatusService.MainActivityObserver presenterObserver;

    public PostStatusHandler(StatusService.MainActivityObserver observer) {
        super(Looper.getMainLooper());

        this.presenterObserver = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(PostStatusTask.SUCCESS_KEY);
        if (success) {

            presenterObserver.postStatus();

        } else if (msg.getData().containsKey(PostStatusTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(PostStatusTask.MESSAGE_KEY);
            presenterObserver.displayMessage("Failed to post status: " + message);

        } else if (msg.getData().containsKey(PostStatusTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(PostStatusTask.EXCEPTION_KEY);

            presenterObserver.displayMessage("Failed to post status because of exception: " + ex.getMessage());
        }
    }
}
