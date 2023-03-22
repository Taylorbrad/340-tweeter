package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

public class GetFollowersTest {

    private ServerFacade serverFacade;
    private FollowerRequest request;

    @BeforeEach
    public void setup() {

        serverFacade = new ServerFacade();

        AuthToken token = new AuthToken("asdf");
        request = new FollowerRequest(token, "@allen", 4, "");
    }

    @Test
    void successResponseTest() throws IOException, TweeterRemoteException {

        //Assert that this call gets a success code from the API gateway
        FollowerResponse response = serverFacade.getFollowers(request, "/getfollowers");
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void getsFourMembersTest() throws IOException, TweeterRemoteException {

        FollowerResponse response = serverFacade.getFollowers(request, "/getfollowers");
        Assertions.assertEquals(4, response.getFollowers().size());
    }

    @Test
    void exceptionThrownTest() throws IOException, TweeterRemoteException {

        FollowerResponse response = serverFacade.getFollowers(request, "/getfollowers");

        //Assert that a mistyped URL will cause a thrown RuntimeException
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            FollowerResponse response2 = serverFacade.getFollowers(request, "/getfollowerrs");
        });
    }
}
