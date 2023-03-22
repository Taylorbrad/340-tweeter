package edu.byu.cs.tweeter.client;

import static org.mockito.Mockito.spy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mockito;

import java.io.IOException;

import edu.byu.cs.tweeter.client.net.ClientCommunicator;
import edu.byu.cs.tweeter.client.net.ServerFacade;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;

public class RegisterIntegrationTest {

    private ServerFacade serverFacade;
    private RegisterRequest request;

    @BeforeEach
    public void setup() {

        serverFacade = new ServerFacade();
        request = new RegisterRequest("Guy", "Fieri", "@guy", "flavortown123", "asdf");
    }


    @Test
    void successResponseTest() throws IOException, TweeterRemoteException {

        //Assert that this call gets a success code from the API gateway
        LoginResponse response = serverFacade.register(request, "/register");
        Assertions.assertTrue(response.isSuccess());

    }

    @Test
    void getAliasRegisterTest() throws IOException, TweeterRemoteException {
        LoginResponse response = serverFacade.register(request, "/register");

        //Assert that we actually got a user back from the server (@allen because we are using mock data)
        Assertions.assertEquals(response.getUser().getAlias(), "@allen");
    }


    @Test
    void exceptionThrownTest() throws IOException, TweeterRemoteException {
        LoginResponse response = serverFacade.register(request, "/register");
        //Assert that a mistyped URL will cause a thrown RuntimeException
        RuntimeException thrown = Assertions.assertThrows(RuntimeException.class, () -> {
            LoginResponse response2 = serverFacade.register(request, "/rregister");
        });
    }



}
