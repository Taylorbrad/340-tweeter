package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.RegisterService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.feed.FeedFragment;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter {

    private View view;
    private UserService userService;

    public interface View {

        void displayMessage(String s);

        void displayUser(User user);
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
    }
}
