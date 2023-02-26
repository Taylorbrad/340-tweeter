package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsHandlerObserver;

public class GetItemsHandler<T> extends BackgroundTaskHandler<GetItemsHandlerObserver> {
    public GetItemsHandler(GetItemsHandlerObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetItemsHandlerObserver observer) {

        List<T> items = getItemsList(data);//   (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);

        boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);

        observer.handleSuccess(items, hasMorePages);
    }
    public List<T> getItemsList(Bundle data) {
        return (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
    }
}
