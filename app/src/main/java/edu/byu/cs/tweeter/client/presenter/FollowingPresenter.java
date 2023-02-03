package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
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
        void setLoadingFooter(boolean value);

        void displayMessage(String message);

        void addMoreItems(List<User> followees);

        void displayUser(User user);
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
        userService.getUserFollowing(userAlias, new UserObserver());
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
        public void passFollowees(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);

            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;


//            setHasMorePages(hasMorePages); would do the same as the below statement
            FollowingPresenter.this.hasMorePages = hasMorePages;

            view.addMoreItems(followees);

        }
    }

    public class UserObserver implements UserService.UserObserver {

        @Override
        public void displayUser(User user) {
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
        public void addItems(List<Status> statuses, boolean hasMorePages, Status lastStatus) {

        }
    }

}