package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.User;

public class FollowersPresenter extends PagedPresenter<User> {

    public FollowersPresenter(PagedView<User> view)
    {
        super(view, new UserService(), new FollowService());
    }

    public void getUser(String userAlias) {
        getUserService().getUserFollowers(userAlias, new GetUserObserver());
    }

    @Override
    public void loadItems(User user, User lastItem, PagedPresenter.GetItemsObserver observer) {
        getFollowService().loadMoreItemsFollowers(user, PAGE_SIZE, lastItem, observer);
    }
}
