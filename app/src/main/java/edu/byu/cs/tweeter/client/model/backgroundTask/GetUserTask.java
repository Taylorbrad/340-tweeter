package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Bundle;
import android.os.DeadSystemException;
import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

/**
 * Background task that returns the profile for a specified user.
 */
public class GetUserTask extends AuthenticatedTask {

    public static final String USER_KEY = "user";

    /**
     * Alias (or handle) for user whose profile is being retrieved.
     */
    private final String alias;

    private User user;

    public GetUserTask(AuthToken authToken, String alias, Handler messageHandler) {
        super(authToken, messageHandler);
        this.alias = alias;
    }

    @Override
    protected void runTask() {

        GetUserResponse response = null;
        try
        {
            ServerFacade serverFacade = new ServerFacade();
            GetUserRequest request = new GetUserRequest(this.alias);

            response = serverFacade.getUser(request, "/getuser");
             response.getUser();
            sendSuccessMessage();

        } catch (TweeterRemoteException te)
        {
//            System.out.println(te.getMessage());
            sendFailedMessage(te.getMessage());
        } catch (IOException e) {
//            System.out.println(e.getMessage());
            sendFailedMessage(e.getMessage());
        }

        user = new User("");

    }

    @Override
    protected void loadSuccessBundle(Bundle msgBundle) {
        msgBundle.putSerializable(USER_KEY, user);
    }

//    private User getUser() {
//        return getFakeData().findUserByAlias(alias);
//    }
}
