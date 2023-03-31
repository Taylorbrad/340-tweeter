package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.server.dao.concrete.AuthTokenDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.FeedDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.FollowDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.StoryDynamoDB;
import edu.byu.cs.tweeter.server.dao.concrete.UserDynamoDB;


public class DynamoDBFactory implements DAOProvider.DAOFactory {

    @Override
    public AuthTokenDAO getAuthTokenDAO() {
        return new AuthTokenDynamoDB();
    }

    @Override
    public UserDAO getUserDAO() {
        return new UserDynamoDB();
    }

    @Override
    public FollowDAO getFollowDAO() {
        return new FollowDynamoDB();
    }

    @Override
    public FeedDAO getFeedDAO() {
        return new FeedDynamoDB();
    }

    @Override
    public StoryDAO getStoryDAO() {
        return new StoryDynamoDB();
    }
}
