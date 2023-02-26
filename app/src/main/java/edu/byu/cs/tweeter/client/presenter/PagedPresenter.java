package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.observer.PagedObserver;
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
    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }
    public void setLastItem(T lastItem) {
        this.lastItem = lastItem;
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

    public void getUser(String userAlias) {
        getUserService().getUser(userAlias, new GetUserObserver());
    }

    public abstract void loadItems(User user, T lastItem, GetItemsObserver observer);

    public class GetItemsObserver implements PagedObserver<T> {//UserService.GetItemsHandlerObserver {

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
        public void handleSuccess(List items, boolean hasMorePages, Object lastItem) {
            pagedView.setLoadingFooter(false);

            isLoading = false;

            setHasMorePages(hasMorePages);

            setLastItem((T) lastItem);

            pagedView.addItems(items);
        }
    }
    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void displayMessage(String message) {
            pagedView.displayMessage(message);
        }


        @Override
        public void handleSuccess(User user) {
            pagedView.displayUser(user);
        }
    }
}
