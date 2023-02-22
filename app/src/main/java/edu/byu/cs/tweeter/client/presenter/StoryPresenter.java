package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    public StoryPresenter(PagedView<Status> view)
    {
        super(view, new UserService());
    }

    @Override
    public void loadItems(User user, Status lastStatus, PagedPresenter<Status>.GetItemsObserver observer) {
        getUserService().loadMoreItems(user, PAGE_SIZE, lastStatus, new GetItemsObserver());
    }
}
