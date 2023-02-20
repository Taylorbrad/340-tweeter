package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;

// IsFollowerHandler
public class IsFollowerHandler extends BackgroundTaskHandler<GetItemsObserver> {

    public IsFollowerHandler(GetItemsObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetItemsObserver observer) {
        observer.handleSuccess(data);
    }



}
