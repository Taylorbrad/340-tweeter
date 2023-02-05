package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.byu.cs.tweeter.client.model.backgroundTask.LogoutTask;
import edu.byu.cs.tweeter.client.model.service.LoginService;

// LogoutHandler
public class LogoutHandler extends Handler {

    private LoginService.MainActivityObserver presenterObserver;

    public LogoutHandler(LoginService.MainActivityObserver observer) {
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
