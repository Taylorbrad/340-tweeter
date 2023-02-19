package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowingTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.ServiceObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class PagedHandler<T> extends BackgroundTaskHandler<PagedObserver> {

    public PagedHandler(PagedObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedObserver observer) {
        observer.handleSuccess(data);
    }
}
