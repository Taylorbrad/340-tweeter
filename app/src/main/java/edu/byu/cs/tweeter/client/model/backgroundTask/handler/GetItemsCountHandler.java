package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsCountObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.ServiceObserver;


public class GetItemsCountHandler extends BackgroundTaskHandler<GetItemsCountObserver> {

    public GetItemsCountHandler(GetItemsCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetItemsCountObserver observer) {
        observer.handleSuccess(data);
    }
}
