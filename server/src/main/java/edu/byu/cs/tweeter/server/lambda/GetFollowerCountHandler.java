package edu.byu.cs.tweeter.server.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.server.service.UserService;

public class GetFollowerCountHandler implements RequestHandler<GetFollowerCountRequest, GetFollowerCountResponse> {

    @Override
    public GetFollowerCountResponse handleRequest(GetFollowerCountRequest request, Context context) {
        UserService service = new UserService();
        return service.getFollowerCount(request);
    }
}
