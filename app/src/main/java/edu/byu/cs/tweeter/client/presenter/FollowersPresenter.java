package edu.byu.cs.tweeter.client.presenter;

import android.widget.Toast;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
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

        userService.getUserFollowers(userAlias, new UserObserver());
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
        public void passFollowers(List<User> followers, boolean hasMorePages) {
            isLoading = false;

            view.setLoadingFooter(false);

            FollowersPresenter.this.hasMorePages = hasMorePages;

            lastFollower = (followers.size() > 0) ? followers.get(followers.size() - 1) : null;

            view.addMoreItems(followers);
        }
    }
}
