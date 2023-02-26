package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.IsFollowerHandlerObserver;

// IsFollowerHandler
public class IsFollowerHandler extends BackgroundTaskHandler<IsFollowerHandlerObserver> {

    public IsFollowerHandler(IsFollowerHandlerObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, IsFollowerHandlerObserver observer) {
        boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);
        observer.handleSuccess(isFollower);
    }

//    @Override
//    protected void handleSuccess(Bundle data, GetItemsHandlerObserver observer) {
//        observer.handleSuccess(data);
//    }
}
