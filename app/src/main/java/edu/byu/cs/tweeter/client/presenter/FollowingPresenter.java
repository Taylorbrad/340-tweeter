package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.following.FollowingFragment;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter {

    private static final int PAGE_SIZE = 10;

    public void getUser(String userAlias) {

        userService.getUser(userAlias, new UserObserver());

    }


    public interface View {
        void setLoadingFooter(boolean value);

        void displayMessage(String message);

        void addMoreItems(List<User> followees);

        void displayUser(User user);
    }

    private View view;

    private FollowService followService;
    private UserService userService;

    private User lastFollowee;

    private boolean hasMorePages;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private boolean isLoading = false;


    public FollowingPresenter(View view) {
        this.view = view;

        followService = new FollowService();
        userService = new UserService();
    }


    public void loadMoreItems(User user) {


        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            followService.loadMoreItems(user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());

        }
    }




    public class GetFollowingObserver  implements FollowService.Observer {

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

    public class UserObserver implements UserService.Observer {

        @Override
        public void displayUser(User user) {
            view.displayUser(user);
        }

        @Override
        public void displayMessage(String s) {
            view.displayMessage(s);
        }
    }

}
