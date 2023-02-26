package edu.byu.cs.tweeter.client.model.backgroundTask.observer;

import android.os.Bundle;

public interface GetItemsCountObserver extends ServiceObserver {
    void handleSuccess(int followeeCount, int followingCount);
}
