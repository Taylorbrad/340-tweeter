package edu.byu.cs.tweeter.client.model.backgroundTask.observer;

import android.os.Bundle;

public interface GetItemsHandlerObserver extends PagedObserver {
    void handleSuccess(Bundle data);
}
