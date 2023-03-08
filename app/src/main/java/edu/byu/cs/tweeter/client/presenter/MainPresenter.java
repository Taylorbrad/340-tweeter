package edu.byu.cs.tweeter.client.presenter;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsCountObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.IsFollowerHandlerObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenter {

    private View view;
    private FollowService followService;
    private LoginService loginService;
    private StatusService statusService;

    private User selectedUser;

    public void setSelectedUser(User selectedUser)
    {
        this.selectedUser = selectedUser;
    }
    public User getSelectedUser()
    {
        return selectedUser;
    }

    private static final String LOG_TAG = "MainActivityPresenter";


    public interface View {

        void follow();

        void unfollow();

        void displayMessage(String message);

        void setFollowButton();

        void setFolloweeCount(int count);

        void setFollowerCount(int count);

        void logOut();

        void statusPostedMessage();
    }


    public MainPresenter(View view)
    {
        this.view = view;
    }

    protected FollowService getFollowService() {
        if (followService == null) {
            followService = new FollowService();
        }
        return followService;
    }
    protected LoginService getLoginService() {
        if (loginService == null) {
            loginService = new LoginService();
        }
        return loginService;
    }

    protected StatusService getStatusService() {
        if (statusService == null) {
            statusService = new StatusService();
        }
        return statusService;
    }

    public void updateSelectedUserFollowingAndFollowers() {
        getFollowService().updateUserFollows(selectedUser, new GetItemsCountObserverMain());
    }

    public String getFormattedDateTime() throws ParseException {
        SimpleDateFormat userFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        SimpleDateFormat statusFormat = new SimpleDateFormat("MMM d yyyy h:mm aaa");

        return statusFormat.format(userFormat.parse(LocalDate.now().toString() + " " + LocalTime.now().toString().substring(0, 8)));
    }

    public List<String> parseURLs(String post) {
        List<String> containedUrls = new ArrayList<>();
        for (String word : post.split("\\s")) {
            if (word.startsWith("http://") || word.startsWith("https://")) {

                int index = findUrlEndIndex(word);

                word = word.substring(0, index);

                containedUrls.add(word);
            }
        }

        return containedUrls;
    }

    public List<String> parseMentions(String post) {
        List<String> containedMentions = new ArrayList<>();

        for (String word : post.split("\\s")) {
            if (word.startsWith("@")) {
                word = word.replaceAll("[^a-zA-Z0-9]", "");
                word = "@".concat(word);

                containedMentions.add(word);
            }
        }

        return containedMentions;
    }

    public int findUrlEndIndex(String word) {
        if (word.contains(".com")) {
            int index = word.indexOf(".com");
            index += 4;
            return index;
        } else if (word.contains(".org")) {
            int index = word.indexOf(".org");
            index += 4;
            return index;
        } else if (word.contains(".edu")) {
            int index = word.indexOf(".edu");
            index += 4;
            return index;
        } else if (word.contains(".net")) {
            int index = word.indexOf(".net");
            index += 4;
            return index;
        } else if (word.contains(".mil")) {
            int index = word.indexOf(".mil");
            index += 4;
            return index;
        } else {
            return word.length();
        }
    }

    public void isFollower() {
        getFollowService().isFollower(this.selectedUser, new IsFollowerHandlerObserverMain());
    }

    public void toggleFollowing(boolean isFollowing) {
        if (isFollowing) {
            getFollowService().unfollow(this.selectedUser, new MainActivitysObserver());
            view.displayMessage("Removing " + this.selectedUser.getName() + "...");
        } else {
            getFollowService().follow(this.selectedUser, new MainActivitysObserver());
            view.displayMessage("Adding " +this. selectedUser.getName() + "...");
        }
    }

    public void logout() {
        getLoginService().logout(new LoginServiceObserver());
    }

    public void postStatus(String post) {

        try {
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), LocalDate.now().toEpochDay()/*getFormattedDateTime()*/, parseURLs(post), parseMentions(post));
            getStatusService().postStatus(newStatus, new StatusServiceObserver());

        } catch (Exception ex) {
//            Log.e(LOG_TAG, ex.getMessage(), ex);
            view.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }


    }

    public class IsFollowerHandlerObserverMain implements IsFollowerHandlerObserver {
        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleSuccess(boolean isFollower) {
            if (isFollower)
            {
                view.follow();
            }
            else {
                view.unfollow();
            }
        }
    }
    public class GetItemsCountObserverMain implements GetItemsCountObserver {

        @Override
        public void handleSuccess(int followeeCount, int followingCount) {
            view.setFollowerCount(followeeCount);
            view.setFolloweeCount(followingCount);
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }
    }
    public class MainActivitysObserver implements FollowService.MainActivityObserver {
        @Override
        public void setFollowing(boolean isFollower) {
            // If logged in user if a follower of the selected user, display the follow button as "following"
            if (isFollower) {
                view.follow();
            } else {
                view.unfollow();
            }
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }

        @Override
        public void handleSuccess() {
            updateSelectedUserFollowingAndFollowers();
//            view.refreshFollowButton(false);
            view.setFollowButton();
        }

        @Override
        public void setFolloweeCountText(int count) {
            view.setFolloweeCount(count);

        }

        @Override
        public void setFollowerCount(int count) {
            view.setFollowerCount(count);

        }
    }
    public class LoginServiceObserver implements LoginService.MainActivityObserver {

        @Override
        public void handleSuccess() {
            Cache.getInstance().clearCache();
            view.logOut();
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }
    }
    public class StatusServiceObserver implements StatusService.MainActivityObserver {

        @Override
        public void handleSuccess() {
            view.statusPostedMessage();
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }
    }
}
