package edu.byu.cs.tweeter.client.presenter;

import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowersTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter {

    // Observer for our View so we can notify it
    private View view;

    private UserService userService;
    private FollowService followService;

    private static final int PAGE_SIZE = 10;

    private User lastFollower;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    private boolean hasMorePages;
    private boolean isLoading = false;


    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public interface View {

        void displayUser(User user);

        void displayMessage(String s);

        void setLoadingFooter(boolean b);

        void addMoreItems(List<User> followers);
    }

    public FollowersPresenter(View view)
    {
        this.view = view;

        userService = new UserService();
        followService = new FollowService();
    }

    public void loadMoreItems(User user) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;

            view.setLoadingFooter(true);

            followService.loadMoreItemsFollowers(user, PAGE_SIZE, lastFollower, new FollowersObserver());
        }
    }

    public void getUser(String userAlias) {

        userService.getUserFollowers(userAlias, new GetUserObserver());
    }

    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public User getUser(Bundle data) {
            User user = (User) data.getSerializable(GetUserTask.USER_KEY);
            return user;
        }

        @Override
        public void handleSuccess(User user) {
            view.displayUser(user);
        }
    }

    public class UserObserver implements UserService.UserObserver {

        @Override
        public void handleSuccess(User user) {
            view.displayUser(user);
        }

        @Override
        public void displayMessage(String s) {
            view.displayMessage(s);
        }

        @Override
        public void setLoadingFooter(boolean b) {

        }

        @Override
        public void handleSuccess(List<Status> statuses, boolean hasMorePages, Status lastStatus) {

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

    public class FollowersObserver implements FollowService.FollowersObserver {

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);

            view.displayMessage(message);
//            Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
        }

        @Override
        public void displayException(Exception ex) {

        }

        @Override
        public void handleSuccess(Bundle data) {

            List<User> followers = (List<User>) data.getSerializable(PagedTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);

            isLoading = false;

            view.setLoadingFooter(false);

            FollowersPresenter.this.hasMorePages = hasMorePages;

            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;

            view.addMoreItems(followers);
        }

        @Override
        public void displayMessage(String message) {

        }
    }
}
