package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.concrete.FeedDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.StoryDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.UserDynamoDB;

public class StatusService {

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if(request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Missing Status");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing Authtoken");
        }
        return getUserDAO().postStatus(request);
    }

    public GetFeedResponse getFeed(GetFeedRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Missing User designation");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing Authtoken");
        } else if(request.getLimit() < 1) {
            throw new RuntimeException("[Bad Request] Missing Limit (or < 1)");
        }
        return getFeedDAO().getFeed(request);
    }

    public GetStoryResponse getStory(GetStoryRequest request) {
        if(request.getTargetUser() == null) {
            throw new RuntimeException("[Bad Request] Missing User designation");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing Authtoken");
        } else if(request.getLimit() < 1) {
            throw new RuntimeException("[Bad Request] Missing Limit (or < 1)");
        }
        return getStoryDAO().getStory(request);
    }

    StoryDynamoDB getStoryDAO() {
        return new StoryDynamoDB();
    }
    FeedDynamoDB getFeedDAO() {
        return new FeedDynamoDB() ;
    }
    UserDynamoDB getUserDAO() {
        return new UserDynamoDB();
    }

}
