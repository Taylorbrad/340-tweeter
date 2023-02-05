package edu.byu.cs.tweeter.client.model.service;

import android.widget.EditText;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.LoginHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.LogoutHandler;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginService {

    public interface LoginObserver {

        void logInUser(User loggedInUser);

        void displayMessage(String s);
    }

    public interface MainActivityObserver {

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

}
