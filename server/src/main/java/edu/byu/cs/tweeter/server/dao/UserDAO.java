package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class UserDAO {
    public GetUserResponse getUser(GetUserRequest request) {
        return new GetUserResponse(getFakeData().findUserByAlias(request.getAlias()));
    }

    public LogoutResponse logout(LogoutRequest request) {

        //todo remove authtoken from database or something
        return new LogoutResponse();
    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }

    public GetFollowerCountResponse getFollowerCount(GetFollowerCountRequest request) {
        GetFollowerCountResponse response = new GetFollowerCountResponse(22);

        return response;
    }
}