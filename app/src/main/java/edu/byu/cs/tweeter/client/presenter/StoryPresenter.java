package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    private static final int PAGE_SIZE = 10;

    public StoryPresenter(PagedView<Status> view)
    {
        super(view, new UserService());
    }

    public void getUser(String userAlias) {
        getUserService().getUserStory(userAlias, new GetUserObserver());
    }

    @Override
    public void loadItems(User user, Status lastStatus, PagedPresenter<Status>.GetItemsObserver observer) {
        getUserService().loadMoreItems(user, PAGE_SIZE, lastStatus, new GetItemsObserver());
    }

    @Override
    public List<Status> getItemsList(Bundle data) {
        return (List<Status>) data.getSerializable(PagedTask.ITEMS_KEY);
    }

    @Override
    public Status getLastItem(List<Status> items) {
        return (items.size() > 0) ? items.get(items.size() - 1) : null;
    }


}
