package edu.byu.cs.tweeter.server.dao.interfaces;

public class DAOProvider {

    public DAOFactory factory;

    public DAOProvider(DAOFactory factory) {
        this.factory = factory;
    }

    public interface DAOFactory {

        AuthTokenDAO getAuthTokenDAO();
        UserDAO getUserDAO();
        FollowDAO getFollowDAO();
        FeedDAO getFeedDAO();
        StoryDAO getStoryDAO();

    }
}
