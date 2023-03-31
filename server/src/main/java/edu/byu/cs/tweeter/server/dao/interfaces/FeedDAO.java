package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;

public interface FeedDAO {

    GetFeedResponse getFeed(GetFeedRequest request);
    void addToFeed(Status status);
}
