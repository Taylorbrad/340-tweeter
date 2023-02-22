package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowingPresenter extends PagedPresenter<User> {

    private static final int PAGE_SIZE = 10;

    public FollowingPresenter(PagedView<User> view) {
        super(view, new UserService(), new FollowService());
    }

    @Override
    public void loadItems(User user, User lastItem, PagedPresenter.GetItemsObserver observer) {
        getFollowService().loadMoreItemsFollowing(user, PAGE_SIZE, lastItem, observer);
    }

    public void getUser(String userAlias) {
        getUserService().getUserFollowing(userAlias, new GetUserObserver());
    }

}
