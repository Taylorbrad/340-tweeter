package edu.byu.cs.tweeter.model.net.response;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;

public class GetFeedResponse extends PagedResponse {

    private List<Status> feed;

    GetFeedResponse(String message) {
        super(false, message, false);
    }

    public GetFeedResponse(List<Status> feed, boolean hasMorePages) {
        super(true, hasMorePages);
        this.feed = feed;
    }

    public List<Status> getFeed() {
        return feed;
    }

    public void setFeed(List<Status> feed) {
        this.feed = feed;
    }
}
