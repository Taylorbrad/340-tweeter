package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.story.StoryFragment;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter {

    private View view;

    private UserService userService;

    private static final int PAGE_SIZE = 10;

    public void getUser(String userAlias) {
        userService.getUserStory(userAlias, new GetUserObserver());

    }

    public void loadMoreItems(User user, boolean isLoading, Status lastStatus) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.

            view.setLoadingFooter(true);

            userService.loadMoreItems(user, PAGE_SIZE, lastStatus, new GetItemsObserver());
        }
    }

    public interface View {

        void displayUser(User user);

        void displayMessage(String message);

        void setLoadingFooter(boolean b);

        void addItems(List<Status> statuses, boolean hasMorePages, Status lastStatus);

    }

    public StoryPresenter(View view)
    {
        this.view = view;

        this.userService = new UserService();
    }

    private class GetItemsObserver implements UserService.GetItemsObserver {

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleSuccess(Bundle data) {
            view.setLoadingFooter(false);

            List<Status> statuses = (List<Status>) data.getSerializable(PagedTask.ITEMS_KEY);

            boolean hasMorePages = data.getBoolean(GetStoryTask.MORE_PAGES_KEY);

            Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;

            view.addItems(statuses, hasMorePages, lastStatus);
        }
    }
    private class GetUserObserver implements UserService.GetUserObserver {

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
}
