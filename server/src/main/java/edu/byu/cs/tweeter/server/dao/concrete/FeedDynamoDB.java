package edu.byu.cs.tweeter.server.dao.concrete;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class FeedDynamoDB implements FeedDAO {

    public GetFeedResponse getFeed(GetFeedRequest request) {

//        if (!AuthTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
//            throw new RuntimeException("Token Expired");
//        }

        //TODO Currently dummy. Use request in 4
        Pair<List<Status>, Boolean> data = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());

        return new GetFeedResponse(data.getFirst(), data.getSecond());
    }

    public void addToFeed(PostStatusRequest request) {


    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
