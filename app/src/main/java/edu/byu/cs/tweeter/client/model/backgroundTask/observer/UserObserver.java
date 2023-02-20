package edu.byu.cs.tweeter.client.model.backgroundTask.observer;

import android.os.Bundle;

import edu.byu.cs.tweeter.model.domain.User;

public interface UserObserver extends ServiceObserver {
    User getUser(Bundle data);
    void handleSuccess(User user);
}
