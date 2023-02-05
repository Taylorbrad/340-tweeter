package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;

/**
 * Message handler (i.e., observer) for GetStoryTask.
 */
public class GetStoryHandler extends Handler {

    private UserService.UserObserver presenterObserver;

    public GetStoryHandler(UserService.UserObserver observer) {
        super(Looper.getMainLooper());

        this.presenterObserver = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {

        presenterObserver.setLoadingFooter(false);

        boolean success = msg.getData().getBoolean(GetStoryTask.SUCCESS_KEY);
        if (success) {
            List<Status> statuses = (List<Status>) msg.getData().getSerializable(GetStoryTask.STATUSES_KEY);

            boolean hasMorePages = msg.getData().getBoolean(GetStoryTask.MORE_PAGES_KEY);

            Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

            presenterObserver.addItems(statuses, hasMorePages, lastStatus);

        } else if (msg.getData().containsKey(GetStoryTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(GetStoryTask.MESSAGE_KEY);

            presenterObserver.displayMessage("Failed to get story: " + message);

        } else if (msg.getData().containsKey(GetStoryTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(GetStoryTask.EXCEPTION_KEY);

            presenterObserver.displayMessage("Failed to get story because of exception: " + ex.getMessage());
        }
    }
}
