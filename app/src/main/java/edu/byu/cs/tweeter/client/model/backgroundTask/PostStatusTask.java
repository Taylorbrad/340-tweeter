package edu.byu.cs.tweeter.client.model.backgroundTask;

import android.os.Handler;

import java.io.IOException;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;

/**
 * Background task that posts a new status sent by a user.
 */
public class PostStatusTask extends AuthenticatedTask {

    /**
     * The new status being sent. Contains all properties of the status,
     * including the identity of the user sending the status.
     */
    private final Status status;

    public PostStatusTask(AuthToken authToken, Status status, Handler messageHandler) {
        super(authToken, messageHandler);
        this.status = status;
    }

    @Override
    protected void runTask() {

        PostStatusResponse response = null;
        try
        {
            ServerFacade serverFacade = new ServerFacade();
            PostStatusRequest request = new PostStatusRequest(status.getPost(), Cache.getInstance().getCurrUser(), Cache.getInstance().getCurrUserAuthToken());

            response = serverFacade.postStatus(request, "/poststatus");
        } catch (TweeterRemoteException te)
        {
            System.out.println(te.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        // We could do this from the presenter, without a task and handler, but we will
        // eventually access the database from here when we aren't using dummy data.
        if (response.isSuccess())
        {
            // Call sendSuccessMessage if successful
            sendSuccessMessage();
        }
        else
        {
            // or call sendFailedMessage if not successful
            sendFailedMessage("Message failed to send because of <if statement condition> (probably server access failure)");
        }


    }

}
