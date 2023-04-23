package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.AuthenticateUserHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.SimpleNotificationHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginService {

    private ExecutorService executor;

    public LoginService() {
        executor = Executors.newSingleThreadExecutor();
    }

    public interface LoginObserver extends UserObserver {
        void handleSuccess(User loggedInUser);
        void displayMessage(String s);
    }

    public interface MainActivityObserver extends SimpleNotificationObserver { }


    public void logout(MainActivityObserver observer) {
        LogoutTask logoutTask = new LogoutTask(Cache.getInstance().getCurrUserAuthToken(), new SimpleNotificationHandler(observer));
        executor.execute(logoutTask);
    }

    public void loginRequest(String alias, String password, LoginObserver observer) {
        // Send the login request.
        LoginTask loginTask = new LoginTask(alias,
                password,
                new AuthenticateUserHandler(observer));
        executor.execute(loginTask);
    }

}
