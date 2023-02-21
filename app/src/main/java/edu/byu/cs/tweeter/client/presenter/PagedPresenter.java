package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

//    private PagedView view;

    private MorePagesView<Status> morePagesView;

    public MorePagesView<Status> getMorePagesView() {
        return morePagesView;
    }

    public void setMorePagesView(MorePagesView<Status> morePagesView) {
        this.morePagesView = morePagesView;
    }

    public PagedPresenter(MorePagesView<Status> morePagesView) {
        this.morePagesView = morePagesView;
    }

//    public PagedPresenter(otherView<User> )

    public interface PagedView extends View {
        void displayUser(User user);
        void setLoadingFooter(boolean value);
        void displayMessage(String message);
    }

    public interface MorePagesView<T> extends PagedView {
        void addItems(List<T> items, boolean hasMorePages, T lastItem);
    }


    public void loadMoreItems(User user, boolean isLoading, T lastItem)
    {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;

            morePagesView.setLoadingFooter(true);


            loadItems(user, lastItem, new GetItemsObserver());
//            getUserService().getFeed(user, PAGE_SIZE, lastItem, new GetItemsObserver());

        }
    }

    public abstract void loadItems(User user, T lastItem, GetItemsObserver observer);


    public class GetItemsObserver implements UserService.GetItemsObserver {

        @Override
        public void displayMessage(String message) {
            morePagesView.displayMessage(message);
        }

        @Override
        public void handleSuccess(Bundle data) {
            morePagesView.setLoadingFooter(false);

            List<Status> statuses = (List<Status>) data.getSerializable(PagedTask.ITEMS_KEY);

            boolean hasMorePages = data.getBoolean(GetStoryTask.MORE_PAGES_KEY);

            Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

            morePagesView.addItems(statuses, hasMorePages, lastStatus);
        }
    }
}
