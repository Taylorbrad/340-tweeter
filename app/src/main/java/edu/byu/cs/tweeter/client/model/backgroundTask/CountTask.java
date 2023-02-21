package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Bundle;
import android.os.Handler;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public abstract class CountTask extends AuthenticatedTask {

    public static final String COUNT_KEY = "count";

    /**
     * The user whose following count is being retrieved.
     * (This can be any user, not just the currently logged-in user.)
     */
    private User targetUser;

    private int count;

    public CountTask(Handler messageHandler, AuthToken authToken, User targetUser) {
        super(messageHandler, authToken);
        this.targetUser = targetUser;
    }

    @Override
    protected void processTask() {
        this.count = getCount();
    }

    protected abstract int getCount();


    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putBoolean(SUCCESS_KEY, true);
        msgBundle.putInt(COUNT_KEY, count);
    }
}
