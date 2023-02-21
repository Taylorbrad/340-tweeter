package edu.byu.cs.tweeter.client.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetStoryTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.PagedTask;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.client.view.main.story.StoryFragment;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class StoryPresenter extends PagedPresenter<Status> {

    private static final int PAGE_SIZE = 10;

    public StoryPresenter(MorePagesView<Status> view)
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


}
