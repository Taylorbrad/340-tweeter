package edu.byu.cs.tweeter.client;

import android.os.Looper;
import android.widget.EditText;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.backgroundTask.handler.PagedHandler;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.PagedObserver;
import edu.byu.cs.tweeter.client.model.backgroundTask.observer.SimpleNotificationObserver;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.client.model.service.UserService;
import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
//import edu.byu.cs.tweeter.client.presenter.MainActivityPresenter;
import edu.byu.cs.tweeter.client.presenter.MainPresenter;
import edu.byu.cs.tweeter.client.view.login.LoginActivity;
import edu.byu.cs.tweeter.client.view.main.MainActivity;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;

public class MainPresenterUnitTest {

    //TODO Spy here?
    private LoginPresenter.View mockLoginView;
    private MainPresenter.View mockMainView;

    private StatusService statusService;
    private LoginService loginService;
    private UserService userService;

    private Cache cache;

    private MainPresenter mainPresenterSpy;
    private LoginPresenter loginPresenterSpy;

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

        mockMainView = Mockito.mock(MainPresenter.View.class);
        mockLoginView = Mockito.mock(LoginPresenter.View.class);

        loginService = new LoginService();
        statusService = new StatusService();
        userService = new UserService();

        cache = Cache.getInstance();

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockMainView));
        loginPresenterSpy = Mockito.spy(new LoginPresenter(mockLoginView));

        countDownLatch = new CountDownLatch(1);
    }

    @Test
    public void testPostStatus_successful() throws InterruptedException {

        System.out.println("login sent");
        Looper.prepare();
        loginService.loginRequest("@taybrad", "asdfasdf", new LoginObserver());

        awaitLatch();

        User user = cache.getCurrUser();

        String postText = "My Test Post!";

        ArrayList<String> urls = new ArrayList<>();
        urls.add("test.com");

        ArrayList<String> mentions = new ArrayList<>();
        mentions.add("@test");


        resetLatch();
        statusService.postStatus(new Status(postText, user, 0L, urls, mentions), new StatusServiceObserver());
        awaitLatch();

        resetLatch();

        TestingObserver testingObserver = new TestingObserver();

        userService.getStory(user, 1, null, testingObserver);//new PagedHandler<Status>(TestingObserver));
        awaitLatch();

        Status retreivedStatus = testingObserver.lastItem;



        //Assertions
        Mockito.verify(mockMainView).statusPostedMessage();

        Assertions.assertEquals(user, retreivedStatus.getUser());

        Assertions.assertEquals(postText, retreivedStatus.getPost());

        Assertions.assertEquals(urls, retreivedStatus.getUrls());
        Assertions.assertEquals(mentions, retreivedStatus.getMentions());


//        cache.set

//        Answer<Void> answer = new Answer<>() {
//            @Override
//            public Void answer(InvocationOnMock invocation) throws Throwable {
//                MainPresenter.StatusServiceObserver observer = invocation.getArgument(1, MainPresenter.StatusServiceObserver.class);
////                observer.handleSuccess();
//                observer.handleSuccess();
//                return null;
//            }
//        };

//        Mockito.doAnswer(answer).when(statusService).postStatus(Mockito.any(Status.class),Mockito.any(StatusService.MainActivityObserver.class));
//
//        mainPresenterSpy.postStatus("Test success");
//
//        Mockito.verify(mockView).statusPostedMessage();


//        Mockito.verify(mainPresenterSpy).
    }

//    public class GetItemsObserver implements PagedObserver<T> {//UserService.GetItemsHandlerObserver {
//
//        @Override
//        public void displayMessage(String message) {
//            pagedView.displayMessage(message);
//        }
//
//        @Override
//        public void displayError(String message) {
//            pagedView.displayMessage("error because: " + message);
//        }
//
//        @Override
//        public void displayException(Exception ex) {
//            pagedView.displayMessage("exception because: " + ex.getMessage());
//        }
//
//        @Override
//        public void handleSuccess(List items, boolean hasMorePages, Object lastItem) {
//            pagedView.setLoadingFooter(false);
//
//            isLoading = false;
//
//            setHasMorePages(hasMorePages);
//
//            setLastItem((T) lastItem);
//
//            pagedView.addItems(items);
//        }
//    }
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
    public class StatusServiceObserver implements StatusService.MainActivityObserver {

        @Override
        public void handleSuccess() {
            mockMainView.statusPostedMessage();
            countDownLatch.countDown();
        }

        @Override
        public void displayMessage(String message) {
            mockMainView.displayMessage(message);
            countDownLatch.countDown();
        }
    }

    public class LoginObserver implements LoginService.LoginObserver {

        @Override
        public void handleSuccess(User loggedInUser) {
            mockLoginView.logInUser(loggedInUser);
            countDownLatch.countDown();
        }

        @Override
        public void displayMessage(String s) {
            mockLoginView.displayMessage(s);
            countDownLatch.countDown();

        }
    }


}
