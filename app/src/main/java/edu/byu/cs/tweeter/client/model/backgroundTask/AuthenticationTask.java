package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.util.Pair;

public abstract class AuthenticationTask extends BackgroundTask {

    public static final String USER_KEY = "user";
    public static final String AUTH_TOKEN_KEY = "auth-token";

    /**
     * The user's username (or "alias" or "handle"). E.g., "@susan".
     */
    private String username;
    /**
     * The user's password.
     */
    private String password;

    private User user;
    private AuthToken token;

    public void setUser(User user) {
        this.user = user;
    }

    public void setToken(AuthToken token) {
        this.token = token;
    }

    public AuthenticationTask(Handler messageHandler, String username, String password) {
        super(messageHandler);
        this.username = username;
        this.password = password;
    }

    @Override
    protected void processTask() {
        Pair<User, AuthToken> registerResult = doAuthenticate();

        setUser(registerResult.getFirst());
        setToken(registerResult.getSecond());
    }

    protected abstract Pair<User, AuthToken> doAuthenticate();

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putSerializable(USER_KEY, user);
        msgBundle.putSerializable(AUTH_TOKEN_KEY, token);
    }
}
