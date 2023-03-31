package edu.byu.cs.tweeter.server.service;

import java.security.NoSuchAlgorithmException;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LoginResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.concrete.AuthTokenDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.FollowDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.UserDynamoDB;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.DAOProvider;
import edu.byu.cs.tweeter.server.dao.interfaces.DynamoDBFactory;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.util.FakeData;

public class UserService {

    DAOProvider daoSet = new DAOProvider(new DynamoDBFactory());

    public LoginResponse login(LoginRequest request) {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        }

        // TODO: Generates dummy data. Replace with a real implementation.

        User user = getUserDAO().login(request); //Fail if user does not exist
        AuthToken authToken = getAuthTokenDAO().getToken();

//        User user = getDummyUser();
//        AuthToken authToken = getDummyAuthToken();
        return new LoginResponse(user, authToken);
    }

    public LogoutResponse logout(LogoutRequest request) {

        if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        }
        getAuthTokenDAO().deleteToken(request.getAuthToken());

        return getUserDAO().logout(request);
    }

    public LoginResponse register(RegisterRequest request) throws NoSuchAlgorithmException {
        if(request.getUsername() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        } else if(request.getPassword() == null) {
            throw new RuntimeException("[Bad Request] Missing a password");
        } else if(request.getFirstName() == null) {
            throw new RuntimeException("[Bad Request] Missing a first name");
        } else if(request.getLastName() == null) {
            throw new RuntimeException("[Bad Request] Missing a last name");
        } else if(request.getImageEncodedToString() == null) {
            throw new RuntimeException("[Bad Request] Missing an image string");
        }

        User user = getUserDAO().register(request); //Fail if user does not exist
        AuthToken authToken = getAuthTokenDAO().getToken();

//        User user = getDummyUser();
//        AuthToken authToken = getDummyAuthToken();
        return new LoginResponse(user, authToken);
    }

    public GetUserResponse getUser(GetUserRequest request) {
        if(request.getAlias() == null){
            throw new RuntimeException("[Bad Request] Missing a username");
        }
        GetUserResponse response = getUserDAO().getUser(request);
//        User user = getDummyUser();
        if(response == null){
            throw new RuntimeException("[Bad Request] User not found");
        }
        return response;
    }

    public GetFollowerCountResponse getFollowerCount(GetFollowerCountRequest request) {
        if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        } else if(request.getTargetUser() == null){
            throw new RuntimeException("[Bad Request] Missing a user");
        }

        GetFollowerCountResponse response;

        try {
            response = getUserDAO().getFollowerCount(request);
            getAuthTokenDAO().updateToken(request.getAuthToken().getToken());
        } catch (RuntimeException e) {
            throw new RuntimeException("[Bad Request] AuthToken expired");
        }

        return response;

//        return getFollowDAO().getFollowerCount(request);
    }
    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {
        if(request.getAuthToken() == null){
            throw new RuntimeException("[Bad Request] Missing an authtoken");
        } else if(request.getTargetUser() == null){
            throw new RuntimeException("[Bad Request] Missing a user");
        }

        GetFollowingCountResponse response;

        try {
            response = getUserDAO().getFollowingCount(request);
            getAuthTokenDAO().updateToken(request.getAuthToken().getToken());
        } catch (RuntimeException e) {
            throw new RuntimeException("[Bad Request] AuthToken expired");
        }

        return response;

//        return getFollowDAO().getFollowingCount(request);
    }

    /**
     * Returns the dummy user to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy user.
     *
     * @return a dummy user.
     */
    User getDummyUser() {
        return getFakeData().getFirstUser();
    }

    /**
     * Returns the dummy auth token to be returned by the login operation.
     * This is written as a separate method to allow mocking of the dummy auth token.
     *
     * @return a dummy auth token.
     */
    AuthToken getDummyAuthToken() {
        return getFakeData().getAuthToken();
    }

    /**
     * Returns the {@link FakeData} object used to generate dummy users and auth tokens.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }

    UserDAO getUserDAO() {
        return daoSet.factory.getUserDAO();
    }
    FollowDAO getFollowDAO() {
        return daoSet.factory.getFollowDAO();
    }
    AuthTokenDAO getAuthTokenDAO() {
        return daoSet.factory.getAuthTokenDAO();
    }
}
