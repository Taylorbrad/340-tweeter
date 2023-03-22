package edu.byu.cs.tweeter.client;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;

import edu.byu.cs.tweeter.client.model.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.TweeterRemoteException;
import edu.byu.cs.tweeter.util.FakeData;

public class GetStoryTest {

    TestingObserver observer;
    UserService spyService;

    CountDownLatch countDownLatch;

    private void resetLatch() {
        countDownLatch = new CountDownLatch(1);
    }
    private void awaitLatch() throws InterruptedException {
        countDownLatch.await();
        resetLatch();
    }

    @BeforeEach
    public void setup() {

        spyService = Mockito.spy(new UserService());
//        spyService = new UserService();

//        observer = Mockito.mock(TestingObserver.class);// new TestingObserver();
        observer = new TestingObserver();

        Cache.getInstance().setCurrUserAuthToken(new AuthToken("test"));

        countDownLatch = new CountDownLatch(1);
    }

    @Test
    void observerIsNotifiedTest() throws IOException, TweeterRemoteException, InterruptedException {
        User user = new User("allen", "allen", "@allen", "asdf");

//        Answer answer = new Answer() {
//            @Override
//            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
//                PagedPresenter.GetItemsObserver observer = invocationOnMock.getArgument(3);
//
//                observer.handleSuccess( new ArrayList(), true, "mockObserver");
//
//                return null;
//            }
//        };

//        Mockito.doAnswer(answer).when(spyService).loadMoreItems(user, 4, null, mockObserver);

        spyService.loadMoreItems(user, 4, null, observer);
        awaitLatch();

//        Mockito.verify(observer).handleSuccess(Mockito.anyList(), Mockito.anyBoolean(), Mockito.any());
        Assertions.assertTrue(observer.success);
        Assertions.assertTrue(observer.hasMorePages);

        FakeData data = FakeData.getInstance();

        List<Status> list = data.getPageOfStatus(null, 4).getFirst();

        Assertions.assertEquals(list, observer.statuses);
    }

    private class TestingObserver implements PagedObserver<Status> {

        private boolean success = false;
        private String message;
        private List<Status> statuses;
        private boolean hasMorePages;
        private Status lastItem;
        private Exception exception;

        @Override
        public void displayError(String message) {
            countDownLatch.countDown();
        }

        @Override
        public void displayException(Exception ex) {
            countDownLatch.countDown();
        }

        @Override
        public void handleSuccess(List<Status> items, boolean hasMorePages, Status lastItem) {
            this.success = true;
            this.message = null;
            this.statuses = items;
            this.hasMorePages = hasMorePages;
            this.lastItem = lastItem;

            countDownLatch.countDown();
        }

        @Override
        public void displayMessage(String message) {
            countDownLatch.countDown();
        }
    }
}
