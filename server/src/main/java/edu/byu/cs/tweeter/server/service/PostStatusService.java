package edu.byu.cs.tweeter.server.service;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.StatusDAO;

public class PostStatusService {

    public PostStatusResponse postStatus(PostStatusRequest request) {
        if(request.getStatus() == null) {
            throw new RuntimeException("[Bad Request] Missing Status");
        } else if(request.getAuthToken() == null) {
            throw new RuntimeException("[Bad Request] Missing Authtoken");
        }
        return getStatusDAO().postStatus(request);
    }

    public StatusDAO getStatusDAO() {
        return new StatusDAO();
    }

}
