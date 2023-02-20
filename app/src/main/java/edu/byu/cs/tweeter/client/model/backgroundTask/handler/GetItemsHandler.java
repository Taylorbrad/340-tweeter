package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsObserver;

public class GetItemsHandler<T> extends BackgroundTaskHandler<GetItemsObserver> {
    public GetItemsHandler(GetItemsObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetItemsObserver observer) {
        observer.handleSuccess(data);
    }

}
