package edu.byu.cs.tweeter.client.model.service;

import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.UserHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginService {

    public interface LoginObserver extends UserObserver {
        void handleSuccess(User loggedInUser);

        void displayMessage(String s);
    }

    public interface MainActivityObserver extends SimpleNotificationObserver { }


    public void logout(MainActivityObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new SimpleNotificationHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(logoutTask);
    }

    public void loginRequest(EditText alias, EditText password, LoginObserver observer) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias.getText().toString(),
                password.getText().toString(),
                new UserHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(loginTask);
    }

}
