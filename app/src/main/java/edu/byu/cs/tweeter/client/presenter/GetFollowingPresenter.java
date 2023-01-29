package edu.byu.cs.tweeter.client.presenter;

import java.util.List;

import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.model.domain.User;

public class GetFollowingPresenter {

    private static final int PAGE_SIZE = 10;



    public interface View {
        void setLoadingFooter(boolean value);

        void displayMessage(String message);

        void addMoreItems(List<User> followees);
    }

    private View view;

    private FollowService followService;

    private User lastFollowee;

    private boolean hasMorePages;

    public boolean hasMorePages() {
        return hasMorePages;
    }

    public void setHasMorePages(boolean hasMorePages) {
        this.hasMorePages = hasMorePages;
    }

    public boolean isLoading() {
        return isLoading;
    }

    private boolean isLoading = false;

    public GetFollowingPresenter(View view) {
        this.view = view;

        followService = new FollowService();
    }


    public void loadMoreItems(User user) {
        

        if (!isLoading) {   // This guard is important for avoiding a race condition in the scrolling code.
            isLoading = true;
            view.setLoadingFooter(true);
            followService.loadMoreItems(user, PAGE_SIZE, lastFollowee, new GetFollowingObserver());

        }
    }

    /**
     * Message handler (i.e., observer) for GetFollowingTask.
     */


    public class GetFollowingObserver  implements FollowService.Observer {

        @Override
        public void displayError(String message) {
            isLoading = false;
            view.setLoadingFooter(false);

            view.displayMessage("Failed to get following: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            isLoading = false;
            view.setLoadingFooter(false);

            view.displayMessage("Failed to get following because of exception: " + ex.getMessage());
//            view.displayException();
        }

        @Override
        public void passFollowees(List<User> followees, boolean hasMorePages) {
            isLoading = false;
            view.setLoadingFooter(false);

            lastFollowee = (followees.size() > 0) ? followees.get(followees.size() - 1) : null;


//            setHasMorePages(hasMorePages); would do the same as the below statement
            GetFollowingPresenter.this.hasMorePages = hasMorePages;

            view.addMoreItems(followees);

        }
    }

}
