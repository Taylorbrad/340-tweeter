package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

/**
 * Background task that removes a following relationship between two users.
 */
public class UnfollowTask extends AuthenticatedTask {
    private static final String LOG_TAG = "UnfollowTask";


//    /**
//     * The user that is being followed.
//     */
//    private User followee;
//    /**
//     * Message handler that will receive task results.
//     */
//    private Handler messageHandler;

    public UnfollowTask(AuthToken authToken, User followee, Handler messageHandler) {
        super(messageHandler, authToken);
        this.authToken = authToken;
//        this.followee = followee;
//        this.messageHandler = messageHandler;
    }


    @Override
    protected void processTask() {
        //Done
    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        //Done
    }
}
