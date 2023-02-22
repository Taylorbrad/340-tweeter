package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status>{

    private static final int PAGE_SIZE = 10;

    public FeedPresenter (PagedView<Status> view)
    {
        super(view, new UserService());
    }

    @Override
    public void loadItems(User user, Status lastStatus, GetItemsObserver observer) {
        getUserService().getFeed(user, PAGE_SIZE, lastStatus, new GetItemsObserver());
    }

    public void getUser(String userAlias) {
        getUserService().getUserFeed(userAlias, new GetUserObserver());
        getMorePagesView().displayMessage("Getting user's profile...");
    }


    
}
