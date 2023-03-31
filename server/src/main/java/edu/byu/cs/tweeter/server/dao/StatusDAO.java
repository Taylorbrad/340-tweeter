package edu.byu.cs.tweeter.server.dao;

import static edu.byu.cs.tweeter.server.dao.DAOInterface.expirySeconds;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StatusDAO {

    public PostStatusResponse postStatus(PostStatusRequest request) {

        if (!AuthTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        //TODO Currently dummy. Use request in 4
        return new PostStatusResponse();
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {

//        if (!AuthTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
//            throw new RuntimeException("Token Expired");
//        }

        //TODO Currently dummy. Use request in 4
        Pair<List<Status>, Boolean> data = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());

        return new GetFeedResponse(data.getFirst(), data.getSecond());
    }

    public GetStoryResponse getStory(GetStoryRequest request) {
//        if (!AuthTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
//            throw new RuntimeException("Token Expired");
//        }
        //TODO Currently dummy. Use request in 4
        Pair<List<Status>, Boolean> data = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());

        return new GetStoryResponse(data.getFirst(), data.getSecond());
    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }


}
