package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    private MorePagesView morePagesView;
    private UserService userService;
    public MorePagesView getMorePagesView() {
        return morePagesView;
    }
    public UserService getUserService() {
        return userService;
    }


    PagedPresenter (MorePagesView<T> view, UserService userService) {
        this.morePagesView = view;
        this.userService = userService;
    }

    public interface PagedView extends View {
        void displayUser(User user);
        void setLoadingFooter(boolean value);
    }
    public interface MorePagesView<T> extends PagedView {
        void addItems(List<T> items, boolean hasMorePages, T lastItem);
    }


    public void loadMoreItems(User user, boolean isLoading, Status lastStatus) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;

            morePagesView.setLoadingFooter(true);


            loadItems(user, lastStatus, new GetItemsObserver());

        }
    }
    public abstract void loadItems(User user, Status lastStatus, GetItemsObserver observer);

    public class GetItemsObserver implements UserService.GetItemsObserver {

        @Override
        public void displayMessage(String message) {
            morePagesView.displayMessage(message);
        }

        @Override
        public void handleSuccess(Bundle data) {
            morePagesView.setLoadingFooter(false);
            List<Status> statuses = (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);

            boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);

            Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
            morePagesView.addItems(statuses, hasMorePages, lastStatus);
        }
    }
    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void displayMessage(String message) {
            getMorePagesView().displayMessage(message);
        }

        @Override
        public User getUser(Bundle data) {
            User user = (User) data.getSerializable(GetUserTask.USER_KEY);
            return user;
        }

        @Override
        public void handleSuccess(User user) {
            getMorePagesView().displayUser(user);
        }
    }
}
