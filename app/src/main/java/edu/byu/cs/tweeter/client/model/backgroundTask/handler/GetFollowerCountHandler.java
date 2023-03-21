package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import static edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowersCountTask.FOLLOWER_COUNT_KEY;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsCountObserver;

public class GetFollowerCountHandler extends BackgroundTaskHandler<GetItemsCountObserver> {

    public GetFollowerCountHandler(GetItemsCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetItemsCountObserver observer) {

        int followerCount = data.getInt(FOLLOWER_COUNT_KEY);

        observer.handleFollowerSuccess(followerCount);
    }
}
