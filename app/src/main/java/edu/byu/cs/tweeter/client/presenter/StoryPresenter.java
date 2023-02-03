package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
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

        userService.getUserStory(userAlias, new UserObserver());

    }

    public void loadMoreItems(User user, boolean isLoading, Status lastStatus) {
        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.

            view.setLoadingFooter(false);

            userService.loadMoreItems(user, PAGE_SIZE, lastStatus, new UserObserver());
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

    }

    private class UserObserver implements UserService.UserObserver {

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
    }
}
