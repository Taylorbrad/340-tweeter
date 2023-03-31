package edu.byu.cs.tweeter.server.dao.concrete;

import static edu.byu.cs.tweeter.server.dao.DAOInterface.expirySeconds;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;

public class StoryDynamoDB implements StoryDAO {

    AuthTokenDAO authTokenDAO = new AuthTokenDynamoDB();

    public GetStoryResponse getStory(GetStoryRequest request) {
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }
        //TODO Currently dummy. Use request in 4
        Pair<List<Status>, Boolean> data = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());

        return new GetStoryResponse(data.getFirst(), data.getSecond());
    }

    public void addToStory(Status inStatus) {

    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
