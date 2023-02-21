package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;

import java.util.List;

import edu.byu.cs.tweeter.client.model.backgroundTask.GetFeedTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetUserTask;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class FeedPresenter extends PagedPresenter<Status> {

    private static final int PAGE_SIZE = 10;


    public FeedPresenter(MorePagesView feedView)
    {
        super(feedView);
        setUserService(new UserService());
    }

    public void getUser(String userAlias) {

        getUserService().getUserFeed(userAlias, new GetUserObserver());

        getMorePagesView().displayMessage("Getting user's profile...");
    }

    @Override
    public void loadItems(User user, Status lastItem, PagedPresenter<Status>.GetItemsObserver observer) {
        getUserService().getFeed(user, PAGE_SIZE, lastItem, new GetItemsObserver());
    }

//    public class GetItemsObserver implements UserService.GetItemsObserver {
//
//        @Override
//        public void displayMessage(String message) {
//            feedView.displayMessage(message);
//        }
//
//        @Override
//        public void handleSuccess(Bundle data) {
//            feedView.setLoadingFooter(false);
//
//            List<Status> statuses = (List<Status>) data.getSerializable(GetFeedTask.ITEMS_KEY);
//
//            boolean hasMorePages = data.getBoolean(GetFeedTask.MORE_PAGES_KEY);
//
//            Status lastStatus = (statuses.size() > 0) ? statuses.get(statuses.size() - 1) : null;
//
//            feedView.addItems(statuses, hasMorePages, lastStatus);
//        }
//    }

    public class GetUserObserver implements UserService.GetUserObserver {

        @Override
        public void displayMessage(String message) {
            getMorePagesView().displayMessage(message);
        }

        @Override
        public User getUser(Bundle data) {
            User user = (User) data.getSerializable(GetUserTask.USER_KEY);
            return user;
        }

        @Override
        public void handleSuccess(User user) {
            getMorePagesView().displayUser(user);
        }
    }
    
}
