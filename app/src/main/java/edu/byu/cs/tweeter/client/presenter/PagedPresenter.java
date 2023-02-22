package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class PagedPresenter<T> extends Presenter {

    public static final int PAGE_SIZE = 10;

    private PagedView pagedView;
    private UserService userService;
    private FollowService followService;

    private User user;
    private boolean isLoading = false;
    private boolean hasMorePages;
    private T lastItem;


    public boolean isLoading() {
        return isLoading;
    }
    public boolean hasMorePages() {
        return hasMorePages;
    }

    public PagedView getMorePagesView() {
        return pagedView;
    }
    public UserService getUserService() {
        return userService;
    }
    public FollowService getFollowService() {
        return followService;
    }

    PagedPresenter (PagedView<T> view, UserService userService) {
        this.pagedView = view;
        this.userService = userService;
    }
    PagedPresenter (PagedView<T> view, UserService userService, FollowService followService) {
        this.pagedView = view;
        this.userService = userService;
        this.followService = followService;
    }

    public interface PagedView<T>{
        void displayMessage(String message);
        void displayUser(User user);
        void setLoadingFooter(boolean value);
        void addItems(List<T> items);
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            this.isLoading = true;

            pagedView.setLoadingFooter(true);

            loadItems(user, lastItem, new GetItemsObserver());

        }
    }

    public abstract void loadItems(User user, T lastItem, GetItemsObserver observer);

    public List<T> getItemsList(Bundle data) {
        return (List<T>) data.getSerializable(PagedTask.ITEMS_KEY);
    }

    public T getLastItem(List<T> items) {
        return (items.size() > 0) ? items.get(items.size() - 1) : null;
    }

    public class GetItemsObserver implements UserService.GetItemsHandlerObserver {

        @Override
        public void displayMessage(String message) {
            pagedView.displayMessage(message);
        }

        @Override
        public void displayError(String message) {
            pagedView.displayMessage("error because: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            pagedView.displayMessage("exception because: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(Bundle data) {
            pagedView.setLoadingFooter(false);

            isLoading = false;

            List<T> items = getItemsList(data);//   (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);

            hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);

            lastItem = getLastItem(items);

            pagedView.addItems(items);
        }
    }
    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void displayMessage(String message) {
            pagedView.displayMessage(message);
        }

        @Override
        public User getUser(Bundle data) {
            user = (User) data.getSerializable(GetUserTask.USER_KEY);
            return user;
        }

        @Override
        public void handleSuccess(User user) {
            pagedView.displayUser(user);
        }
    }
}
