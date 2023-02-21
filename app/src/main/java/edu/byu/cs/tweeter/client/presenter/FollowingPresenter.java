package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter {

    private static final int PAGE_SIZE = 10;

    private View view;

    private FollowService followService;
    private UserService userService;

    private User lastFollowee;

    private boolean hasMorePages;
    private boolean isLoading = false;

    public FollowingPresenter(View view) {
        this.view = view;

        followService = new FollowService();
        userService = new UserService();
    }

    public interface View {

        void displayUser(User user);

        void displayMessage(String message);

        void setLoadingFooter(boolean value);

        void addMoreItems(List<User> followees);
    }

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }


    public void loadMoreItems(User user) {

        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            followService.loadMoreItemsFollowing(user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());

        }
    }
    public void getUser(String userAlias) {
        userService.getUserFollowing(userAlias, new GetUserObserver());
    }

    public class GetFollowingObserver implements FollowService.FollowingObserver {

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);

            view.displayMessage("Failed to get following: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);

            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
//            view.displayException();
        }

        @Override
        public void handleSuccess(Bundle data) {
            List<User> followees = (List<User>) data.getSerializable(PagedTask.ITEMS_KEY);
            boolean hasMorePages = data.getBoolean(PagedTask.MORE_PAGES_KEY);

            isLoading = false;
            view.setLoadingFooter(false);

            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;


            setHasMorePages(hasMorePages);// would do the same as the below statement
//            FollowingPresenter.this.hasMorePages = hasMorePages;

            view.addMoreItems(followees);
        }


        @Override
        public void displayMessage(String message) {

        }
    }

    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void displayMessage(String message) {

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

//    public class UserObserver implements UserService.GetUserObserver {
//
//        @Override
//        public User getUser(Bundle data) {
//            return null;
//        }
//
//        @Override
//        public void handleSuccess(User user) {
//            view.displayUser(user);
//        }
//
//        @Override
//        public void displayMessage(String s) {
//            view.displayMessage(s);
//        }
//    }

}
