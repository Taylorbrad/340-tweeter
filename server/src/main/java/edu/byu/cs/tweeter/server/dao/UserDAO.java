package edu.byu.cs.tweeter.server.dao;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.util.FakeData;

public class UserDAO {
    public User getUser(GetUserRequest request) {
        return getFakeData().findUserByAlias(request.getAlias());
//        return getUserByAlias(request.getAlias());
    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
