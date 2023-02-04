package edu.byu.cs.tweeter.client.model.service;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.EditText;

import androidx.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginService {

    public interface LoginObserver
    {

        void logInUser(User loggedInUser);

        void displayMessage(String s);
    }

    public interface MainActivityObserver
    {

        void logOut();

        void displayMessage(String s);
    }

    public void logout(MainActivityObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new LogoutHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }

    public void loginRequest(EditText alias, EditText password, LoginObserver observer) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias.getText().toString(),
                password.getText().toString(),
                new LoginHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

    // LogoutHandler
    private class LogoutHandler extends Handler {

        private MainActivityObserver presenterObserver;

        public LogoutHandler(MainActivityObserver observer) {
            super(Looper.getMainLooper());

            this.presenterObserver = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LogoutTask.SUCCESS_KEY);
            if (success) {

                presenterObserver.logOut();

            } else if (msg.getData().containsKey(LogoutTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LogoutTask.MESSAGE_KEY);
                presenterObserver.displayMessage("Failed to logout: " + message);

            } else if (msg.getData().containsKey(LogoutTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LogoutTask.EXCEPTION_KEY);
                presenterObserver.displayMessage("Failed to logout because of exception: " + ex.getMessage());
//
            }
        }
    }

    /**
     * Message handler (i.e., observer) for LoginTask
     */
    private class LoginHandler extends Handler {

        private LoginObserver presenterObserver;

        public LoginHandler(LoginObserver observer) {
            super(Looper.getMainLooper());
            presenterObserver = observer;
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            boolean success = msg.getData().getBoolean(LoginTask.SUCCESS_KEY);
            if (success) {
                User loggedInUser = (User) msg.getData().getSerializable(LoginTask.USER_KEY);
                AuthToken authToken = (AuthToken) msg.getData().getSerializable(LoginTask.AUTH_TOKEN_KEY);

                // Cache user session information
                Cache.getInstance().setCurrUser(loggedInUser);
                Cache.getInstance().setCurrUserAuthToken(authToken);

                presenterObserver.logInUser(loggedInUser);

            } else if (msg.getData().containsKey(LoginTask.MESSAGE_KEY)) {
                String message = msg.getData().getString(LoginTask.MESSAGE_KEY);
                presenterObserver.displayMessage("Failed to login: " + message);

            } else if (msg.getData().containsKey(LoginTask.EXCEPTION_KEY)) {
                Exception ex = (Exception) msg.getData().getSerializable(LoginTask.EXCEPTION_KEY);
                presenterObserver.displayMessage("Failed to login because of exception: " + ex.getMessage());
            }
        }
    }
}
