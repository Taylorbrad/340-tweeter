package edu.byu.cs.tweeter.client.model.backgroundTask.handler;

import static edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowersCountTask.FOLLOWER_COUNT_KEY;
import static edu.byu.cs.tweeter.client.model.backgroundTask.GetFollowingCountTask.FOLLOWING_COUNT_KEY;

import android.os.Bundle;

import edu.byu.cs.tweeter.client.model.backgroundTask.observer.GetItemsCountObserver;

public class GetFollowingCountHandler extends BackgroundTaskHandler<GetItemsCountObserver> {

    public GetFollowingCountHandler(GetItemsCountObserver observer) {
        super(observer);
    }

    @Override
    protected void handleSuccess(Bundle data, GetItemsCountObserver observer) {

        int followingCount = data.getInt(FOLLOWING_COUNT_KEY);

        observer.handleFollowingSuccess(followingCount);
    }
}
