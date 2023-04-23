//package edu.byu.cs.tweeter.client;
//
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
///**
// * This class exists purely to prove that tests in your androidTest/java folder have the correct dependencies.
// * Click on the green arrow to the left of the class declarations to run. These tests should pass if all
// * dependencies are correctly set up.
// */
//public class AndroidTestsWorkingTest {
//    class Foo {
//        public void foo() {
//
//        }
//    }
//
//    @BeforeEach
//    public void setup() {
//        // Called before each test, set up any common code between tests
//    }
//
//    @Test
//    public void testAsserts() {
//        Assertions.assertTrue(true);
//    }
//    @Test
//    public void testMockitoSpy() {
//        Foo fpackage edu.byu.cs.tweeter.client;
//
//import android.os.Looper;
//import android.widget.EditText;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//
//import java.util.concurrent.CountDownLatch;
//
//import edu.byu.cs.tweeter.client.cache.Cache;
//import edu.byu.cs.tweeter.client.model.service.LoginService;
//import edu.byu.cs.tweeter.client.model.service.StatusService;
//import edu.byu.cs.tweeter.client.presenter.LoginPresenter;
//import edu.byu.cs.tweeter.client.presenter.MainActivityPresenter;
//import edu.byu.cs.tweeter.client.view.login.LoginActivity;
//import edu.byu.cs.tweeter.model.domain.User;
//
//        public class MainPresenterUnitTest {
//
//            //TODO Spy here?
//            private LoginPresenter.View mockLoginView;
//            private MainActivityPresenter.View mockMainView;
//
//            private StatusService statusService;
//            private LoginService loginService;
//
//            private Cache cache;
//
//            private MainActivityPresenter mainPresenterSpy;
//            private LoginPresenter loginPresenterSpy;
//
//            CountDownLatch countDownLatch;
//
//            private void resetLatch() {
//                countDownLatch = new CountDownLatch(1);
//            }
//
//            private void awaitLatch() throws InterruptedException {
//                countDownLatch.await();
//                resetLatch();
//            }
//
//
//
//
//
//            @BeforeEach
//            public void setup() {
//                mockMainView = Mockito.mock(MainActivityPresenter.View.class);
//                mockLoginView = Mockito.mock(LoginPresenter.View.class);
//
//                loginService = new LoginService();
//                statusService = new StatusService();
//
//                cache = Cache.getInstance();
//
//                mainPresenterSpy = Mockito.spy(new MainActivityPresenter(mockMainView));
//                loginPresenterSpy = Mockito.spy(new LoginPresenter(mockLoginView));
//
//                resetLatch();
////        Cache.setInstance(mockCache);
//            }
//
//            @Test
//            public void testPostStatus_successful() throws InterruptedException {
//                //TODO login, get authtoken
//                System.out.println("login sent");
//                Looper.prepare();
////        loginPresenterSpy.attemptLogin();
//                loginService.loginRequest("@taylor", "asdfasdf", new LoginObserver());
//
////        awaitLatch();
//                countDownLatch.countDown();
//                System.out.println(cache.getCurrUser());
//
////        cache.set
//
////        Answer<Void> answer = new Answer<>() {
////            @Override
////            public Void answer(InvocationOnMock invocation) throws Throwable {
////                MainPresenter.StatusServiceObserver observer = invocation.getArgument(1, MainPresenter.StatusServiceObserver.class);
//////                observer.handleSuccess();
////                observer.handleSuccess();
////                return null;
////            }
////        };
//
////        Mockito.doAnswer(answer).when(statusService).postStatus(Mockito.any(Status.class),Mockito.any(StatusService.MainActivityObserver.class));
////
////        mainPresenterSpy.postStatus("Test success");
////
////        Mockito.verify(mockView).statusPostedMessage();
//
//
////        Mockito.verify(mainPresenterSpy).
//            }
//
//            public class LoginObserver implements LoginService.LoginObserver {
//
//                @Override
//                public void handleSuccess(User loggedInUser) {
//                    System.out.println("success");
//                    mockLoginView.logInUser(loggedInUser);
//                    countDownLatch.countDown();
//                }
//
//                @Override
//                public void displayMessage(String s) {
//                    System.out.println("message (failiure)");
//                    mockLoginView.displayMessage(s);
//
//                }
//            }
//
//        }
// = Mockito.spy(new Foo());
//        f.foo();
//        Mockito.verify(f).foo();
//    }
//    @Test
//    public void testMockitoMock() {
//        Foo f = Mockito.mock(Foo.class);
//        f.foo();
//        Mockito.verify(f).foo();
//    }
//}
