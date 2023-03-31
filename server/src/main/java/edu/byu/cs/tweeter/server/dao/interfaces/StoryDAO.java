package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;

public interface StoryDAO {

    GetStoryResponse getStory(GetStoryRequest request);
    void addToStory(Status inStatus);

}
