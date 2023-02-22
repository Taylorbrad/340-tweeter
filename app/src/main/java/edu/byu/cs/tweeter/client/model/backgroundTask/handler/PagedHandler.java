package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.observer.PagedObserver;

public class PagedHandler<T> extends BackgroundTaskHandler<PagedObserver> {

    public PagedHandler(PagedObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedObserver observer) {
        //TODO do stuff here
        observer.handleSuccess(data);
    }
}
