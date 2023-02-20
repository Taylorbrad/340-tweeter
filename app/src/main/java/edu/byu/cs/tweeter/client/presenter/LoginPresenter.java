package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;
import android.widget.EditText;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.LoginTask;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class LoginPresenter {

    private View view;

    private LoginService loginService;

    public interface View {

        void displayLoggingIn();

        void setErrorText(Exception e);

        void logInUser(User loggedInUser);

        void displayMessage(String s);
    }

    public LoginPresenter(View view)
    {
        this.view = view;

        loginService = new LoginService();
    }

    public void attemptLogin(EditText alias, EditText password) {
        try {

            validateLogin(alias, password);

            view.displayLoggingIn();

            loginService.loginRequest(alias, password, new LoginObserver());

        } catch (Exception e) {
            view.setErrorText(e);
        }
    }

    public void validateLogin(EditText alias, EditText password) {

        if (alias.getText().length() > 0 && alias.getText().charAt(0) != '@') {
            throw new IllegalArgumentException("Alias must begin with @.");
        }
        if (alias.getText().length() < 2) {
            throw new IllegalArgumentException("Alias must contain 1 or more characters after the @.");
        }
        if (password.getText().length() == 0) {
            throw new IllegalArgumentException("Password cannot be empty.");
        }
    }

    public class LoginObserver implements LoginService.LoginObserver {

        @Override
        public User getUser(Bundle data) {

            User loggedInUser = (User) data.getSerializable(LoginTask.USER_KEY);
            AuthToken authToken = (AuthToken) data.getSerializable(LoginTask.AUTH_TOKEN_KEY);

            // Cache user session information
            Cache.getInstance().setCurrUser(loggedInUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            return loggedInUser;
        }

        @Override
        public void handleSuccess(User loggedInUser) {

            view.logInUser(loggedInUser);

        }

        @Override
        public void displayMessage(String s) {
            view.displayMessage(s);

        }
    }
}
