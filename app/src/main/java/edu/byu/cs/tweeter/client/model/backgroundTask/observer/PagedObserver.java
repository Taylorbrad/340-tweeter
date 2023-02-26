package edu.byu.cs.tweeter.client.model.backgroundTask.observer;

import android.os.Bundle;

public interface PagedObserver<T> extends ServiceObserver {

    void displayError(String message);
    void displayException(Exception ex);
    void handleSuccess(Bundle data);
}
