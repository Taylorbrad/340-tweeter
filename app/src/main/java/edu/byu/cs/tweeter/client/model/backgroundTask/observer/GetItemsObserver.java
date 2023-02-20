package edu.byu.cs.tweeter.client.model.backgroundTask.observer;

import android.os.Bundle;

public interface GetItemsObserver extends ServiceObserver {

    void handleSuccess(Bundle data);
}
