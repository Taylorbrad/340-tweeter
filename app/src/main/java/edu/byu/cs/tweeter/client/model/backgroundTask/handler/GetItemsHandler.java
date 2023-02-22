package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsHandlerObserver;

public class GetItemsHandler<T> extends BackgroundTaskHandler<GetItemsHandlerObserver> {
    public GetItemsHandler(GetItemsHandlerObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetItemsHandlerObserver observer) {
        observer.handleSuccess(data);
    }

}
