package edu.byu.cs.tweeter.client;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;

public class GetFollowingCountTest {

    private ServerFacade serverFacade;
    private GetFollowingCountRequest request;

    @BeforeEach
    public void setup() {

        serverFacade = new ServerFacade();

        User user = new User("allen", "allen", "@allen", "asdf");

        request = new GetFollowingCountRequest("token", user);
    }

    @Test
    void successResponseTest() throws IOException, TweeterRemoteException {

        //Assert that this call gets a success code from the API gateway
        GetFollowingCountResponse response = serverFacade.getFollowingCount(request, "/getfollowingcount");
        Assertions.assertTrue(response.isSuccess());
    }

    @Test
    void getsCorrectCountTest() throws IOException, TweeterRemoteException {
        GetFollowingCountResponse response = serverFacade.getFollowingCount(request, "/getfollowingcount");

        Assertions.assertEquals(25, response.getFollowingCount());
        Assertions.assertNotEquals(22, response.getFollowingCount());
    }

    @Test
    void exceptionThrownTest() throws IOException, TweeterRemoteException {

        GetFollowingCountResponse response = serverFacade.getFollowingCount(request, "/getfollowingcount");

        //Assert that a mistyped URL will cause a thrown RuntimeException
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            GetFollowingCountResponse response2 = serverFacade.getFollowingCount(request, "/getfollowingcountttt");
        });
    }

}
