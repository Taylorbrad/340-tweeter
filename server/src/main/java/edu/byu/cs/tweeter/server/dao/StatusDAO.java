package edu.byu.cs.tweeter.server.dao;

import java.util.Random;

import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.util.FakeData;

public class StatusDAO {

    public PostStatusResponse postStatus(PostStatusRequest request) {
        //TODO Currently dummy. Use request in 4
        return new PostStatusResponse();
    }

    FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
