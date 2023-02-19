package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {

    private static final int PAGE_SIZE = 10;

    private View view;
    private UserService userService;

    public void loadMoreItems(User user, boolean isLoading, Status lastStatus)
    {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;

            view.setLoadingFooter(true);


            userService.getFeed(user, PAGE_SIZE, lastStatus, new UserObserver());

        }
    }

    public interface View {

        void displayMessage(String s);

        void displayUser(User user);

        void setLoadingFooter(boolean b);

        void addItems(List<Status> statuses, boolean hasMorePages, Status lastStatus);
    }

    public FeedPresenter(View view)
    {
        this.view = view;

        this.userService = new UserService();
    }

    public void getUser(String userAlias) {

        userService.getUserFeed(userAlias, new UserObserver());

        view.displayMessage("Getting user's profile...");
    }

    public class UserObserver implements UserService.UserObserver
    {

        @Override
        public void displayUser(User user) {
            view.displayUser(user);

        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void setLoadingFooter(boolean b) {
            view.setLoadingFooter(b);
        }

        @Override
        public void addItems(List<Status> statuses, boolean hasMorePages, Status lastStatus) {
            view.addItems(statuses, hasMorePages, lastStatus);

        }

        @Override
        public void displayError(String message) {

        }

        @Override
        public void displayException(Exception ex) {

        }

        @Override
        public void handleSuccess(Bundle data) {

        }
    }
}
