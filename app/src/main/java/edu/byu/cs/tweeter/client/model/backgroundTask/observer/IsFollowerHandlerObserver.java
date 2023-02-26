package edu.byu.cs.tweeter.client.model.backgroundTask.observer;


public interface IsFollowerHandlerObserver extends ServiceObserver {
    void handleSuccess(boolean isFollower);
}
