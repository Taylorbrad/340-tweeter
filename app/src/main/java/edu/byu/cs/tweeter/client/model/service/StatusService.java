package edu.byu.cs.tweeter.client.model.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.PostStatusTask;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.PostStatusHandler;
import edu.byu.cs.tweeter.model.domain.Status;

public class StatusService {

    public interface MainActivityObserver {

        void postStatus();

        void displayMessage(String s);
    }


    public void postStatus(Status newStatus, MainActivityObserver observer) {
        PostStatusTask statusTask = new PostStatusTask(Cache.getInstance().getCurrUserAuthToken(),
                newStatus, new PostStatusHandler(observer));
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(statusTask);
    }

}
