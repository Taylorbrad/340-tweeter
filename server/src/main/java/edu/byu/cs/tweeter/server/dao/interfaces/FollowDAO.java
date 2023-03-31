package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;

public interface FollowDAO {
    FollowerResponse getFollowers(FollowerRequest inRequest);
    FollowingResponse getFollowing(FollowingRequest inRequest);

    FollowResponse follow(FollowRequest inRequest);
    UnfollowResponse unfollow(UnfollowRequest inRequest);

    IsFollowerResponse isFollower(IsFollowerRequest request);

    GetFollowerCountResponse getFollowerCount(GetFollowerCountRequest request);
    GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request);
}
