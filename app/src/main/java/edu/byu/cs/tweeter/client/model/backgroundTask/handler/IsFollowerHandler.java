package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsHandlerObserver;

// IsFollowerHandler
public class IsFollowerHandler extends BackgroundTaskHandler<GetItemsHandlerObserver> {

    public IsFollowerHandler(GetItemsHandlerObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetItemsHandlerObserver observer) {
        observer.handleSuccess(data);
    }
}
