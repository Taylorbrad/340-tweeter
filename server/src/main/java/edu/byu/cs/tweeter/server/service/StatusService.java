package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.server.dao.concrete.AuthTokenDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.FeedDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.StoryDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.UserDynamoDB;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;

public class StatusService {

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if(request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Missing Status");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing Authtoken");
        }

        PostStatusResponse response;

        try {
            response = getStoryDAO().postStatus(request);

            getAuthTokenDAO().updateToken(request.getAuthToken().getToken());

        } catch (RuntimeException e) {
            throw new RuntimeException("[Bad Request] AuthToken expired");
//            response = new UnfollowResponse("Authtoken expired");
        }


        return response;
//        return getUserDAO().postStatus(request);
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Missing User designation");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing Authtoken");
        } else if(request.getLimit() < 1) {
            throw new RuntimeException("[Bad Request] Missing Limit (or < 1)");
        }

        GetFeedResponse response;

        try {
            response = getFeedDAO().getFeed(request);
            getAuthTokenDAO().updateToken(request.getAuthToken().getToken());
        } catch (RuntimeException e) {
            throw new RuntimeException("[Bad Request] AuthToken expired");
        }

        return response;
//
//        return getFeedDAO().getFeed(request);
//        getAuthTokenDAO().updateToken(request.getAuthToken().getToken());
    }
    public GetStoryResponse getStory(GetStoryRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Missing User designation");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing Authtoken");
        } else if(request.getLimit() < 1) {
            throw new RuntimeException("[Bad Request] Missing Limit (or < 1)");
        }

        GetStoryResponse response;

        try {
            response = getStoryDAO().getStory(request);
            getAuthTokenDAO().updateToken(request.getAuthToken().getToken());
        } catch (RuntimeException e) {
            throw new RuntimeException("[Bad Request] AuthToken expired");
        }

        return response;

//        return getStoryDAO().getStory(request);
//        getAuthTokenDAO().updateToken(request.getAuthToken().getToken());
    }

    StoryDAO getStoryDAO() {
        return new StoryDynamoDB();
    }
    FeedDAO getFeedDAO() {
        return new FeedDynamoDB() ;
    }
    UserDAO getUserDAO() {
        return new UserDynamoDB();
    }
    AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDynamoDB();
    }

}

