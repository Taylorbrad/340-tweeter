package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.PagedObserver;

public class PagedHandler<T> extends BackgroundTaskHandler<PagedObserver> {

    public PagedHandler(PagedObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, PagedObserver observer) {
        List<T> items = getItemsList(data);//   (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);

        boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);

        T lastItem = getLastItem(items);

        observer.handleSuccess(items, hasMorePages, lastItem);
    }
    public List<T> getItemsList(Bundle data) {
        return (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
    }
    public T getLastItem(List<T> items) {
        return (items.size() > 0) ? items.get(items.size() - 1) : null;
    }
}
