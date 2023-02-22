package edu.byu.cs.tweeter.client.presenter;

import android.os.Bundle;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.GetCountTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.IsFollowerTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsHandlerObserver;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainActivityPresenter {

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

        void postStatusMessage();
    }


    public MainActivityPresenter(View view)
    {
        this.view = view;

        this.followService = new FollowService();
        this.loginService = new LoginService();
        this.statusService = new StatusService();
    }

    public void updateSelectedUserFollowingAndFollowers() {
        followService.updateUserFollows(selectedUser, new GetItemsCountObserver());
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
        followService.isFollower(this.selectedUser, new IsFollowerHandlerObserver());
    }

    public void toggleFollowing(boolean isFollowing) {
        if (isFollowing) {
            followService.unfollow(this.selectedUser, new MainActivitysObserver());
            view.displayMessage("Removing " + this.selectedUser.getName() + "...");
        } else {
            followService.follow(this.selectedUser, new MainActivitysObserver());
            view.displayMessage("Adding " +this. selectedUser.getName() + "...");
        }
    }

    public void logout() {

        loginService.logout(new LoginServiceObserver());
    }

    public void postStatus(String post) {

        try {
            Status newStatus = new Status(post, Cache.getInstance().getCurrUser(), getFormattedDateTime(), parseURLs(post), parseMentions(post));
            statusService.postStatus(newStatus, new StatusServiceObserver());

        } catch (Exception ex) {
            Log.e(LOG_TAG, ex.getMessage(), ex);
            view.displayMessage("Failed to post the status because of exception: " + ex.getMessage());
        }


    }

    public class IsFollowerHandlerObserver implements GetItemsHandlerObserver {

        @Override
        public void displayError(String message) {
            view.displayMessage("error because: " + message);
        }

        @Override
        public void displayException(Exception ex) {
            view.displayMessage("exception because: " + ex.getMessage());
        }

        @Override
        public void handleSuccess(Bundle data) {
            boolean isFollower = data.getBoolean(IsFollowerTask.IS_FOLLOWER_KEY);

            if (isFollower)
            {
                view.follow();
            }
            else {
                view.unfollow();
            }

        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }
    }
    public class GetItemsCountObserver implements edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsCountObserver {

        @Override
        public void handleSuccess(Bundle data) {

            //TODO dont depend on bundle
            int followeeCount = data.getInt(GetCountTask.COUNT_KEY);
            view.setFollowerCount(followeeCount);

            int followingCount = data.getInt(GetCountTask.COUNT_KEY);
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
            view.postStatusMessage();
        }

        @Override
        public void displayMessage(String message) {
            view.displayMessage(message);
        }
    }
}
