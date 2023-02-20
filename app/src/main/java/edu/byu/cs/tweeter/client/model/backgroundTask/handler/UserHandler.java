package edu.byu.cs.tweeter.client.model.backgroundTask.handler;


import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.observer.UserObserver;
import edu.byu.cs.tweeter.model.domain.User;

public class UserHandler extends BackgroundTaskHandler<UserObserver>{
    public UserHandler(UserObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, UserObserver observer) {
        User user = observer.getUser(data);
        observer.handleSuccess(user);
    }
}
