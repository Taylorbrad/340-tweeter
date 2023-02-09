package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.RegisterTask;
import edu.byu.cs.tweeter.client.model.service.RegisterService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

// RegisterHandler
public class RegisterHandler extends Handler {

    private RegisterService.RegisterObserver presenterObserver;

    public RegisterHandler(RegisterService.RegisterObserver observer) {
        super(Looper.getMainLooper());
        this.presenterObserver = observer;
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        boolean success = msg.getData().getBoolean(RegisterTask.SUCCESS_KEY);
        if (success) {
            User registeredUser = (User) msg.getData().getSerializable(RegisterTask.USER_KEY);
            AuthToken authToken = (AuthToken) msg.getData().getSerializable(RegisterTask.AUTH_TOKEN_KEY);

            Cache.getInstance().setCurrUser(registeredUser);
            Cache.getInstance().setCurrUserAuthToken(authToken);

            presenterObserver.displayRegistered(registeredUser);

        } else if (msg.getData().containsKey(RegisterTask.MESSAGE_KEY)) {
            String message = msg.getData().getString(RegisterTask.MESSAGE_KEY);

            presenterObserver.displayMessage("Failed to register: " + message);

        } else if (msg.getData().containsKey(RegisterTask.EXCEPTION_KEY)) {
            Exception ex = (Exception) msg.getData().getSerializable(RegisterTask.EXCEPTION_KEY);

            presenterObserver.displayMessage("Failed to register because of exception: " + ex.getMessage());
        }
    }
}