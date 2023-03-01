package edu.byu.cs.tweeter.client.presenter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import edu.byu.cs.tweeter.client.cache.Cache;
import edu.byu.cs.tweeter.client.model.service.FollowService;
import edu.byu.cs.tweeter.client.model.service.LoginService;
import edu.byu.cs.tweeter.client.model.service.StatusService;
import edu.byu.cs.tweeter.model.domain.Status;

public class MainPresenterUnitTest {

    private MainPresenter.View mockView;
    private FollowService mockFollowService;
    private StatusService mockStatusService;
    private LoginService mockLoginService;

    private Cache mockCache;

    private MainPresenter mainPresenterSpy;

    @BeforeEach
    public void setup() {
        mockView = Mockito.mock(MainPresenter.View.class);
        mockFollowService = Mockito.mock(FollowService.class);
        mockStatusService = Mockito.mock(StatusService.class);
        mockLoginService = Mockito.mock(LoginService.class);
        mockCache = Mockito.mock(Cache.class);

        mainPresenterSpy = Mockito.spy(new MainPresenter(mockView));

//        Mockito.doReturn(mockLoginService).when(mainPresenterSpy).getLoginService();
        Mockito.when(mainPresenterSpy.getLoginService()).thenReturn(mockLoginService);
        Mockito.when(mainPresenterSpy.getStatusService()).thenReturn(mockStatusService);

        Cache.setInstance(mockCache);
    }

    @Test
    public void testLogout_successful()
    {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.LoginServiceObserver observer = invocation.getArgument(0, MainPresenter.LoginServiceObserver.class);
                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockLoginService).logout(Mockito.any());

        mainPresenterSpy.logout();

        Mockito.verify(mockView).logOut();
        Mockito.verify(mockCache).clearCache();
    }
    @Test
    public void testLogout_error()
    {
        Answer<Void> answer = new Answer<>() {
        @Override
        public Void answer(InvocationOnMock invocation) throws Throwable {
            MainPresenter.LoginServiceObserver observer = invocation.getArgument(0, MainPresenter.LoginServiceObserver.class);
            observer.displayMessage("test");
            return null;
        }
    };

        Mockito.doAnswer(answer).when(mockLoginService).logout(Mockito.any());

        mainPresenterSpy.logout();

        Mockito.verify(mockView).displayMessage("test");
        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
    }
    @Test
    public void testLogout_exception()
    {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.LoginServiceObserver observer = invocation.getArgument(0, MainPresenter.LoginServiceObserver.class);
                observer.displayMessage("exception");
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockLoginService).logout(Mockito.any());

        mainPresenterSpy.logout();

        Mockito.verify(mockView).displayMessage("exception");
        Mockito.verify(mockCache, Mockito.times(0)).clearCache();
    }

    @Test
    public void testPostStatus_successful()
    {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.StatusServiceObserver observer = invocation.getArgument(1, MainPresenter.StatusServiceObserver.class);
//                observer.handleSuccess();
                observer.handleSuccess();
                return null;
            }
        };

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(Status.class),Mockito.any(StatusService.MainActivityObserver.class));

        mainPresenterSpy.postStatus("Test success");

        Mockito.verify(mockView).statusPostedMessage();
    }
    @Test
    public void testPostStatus_error()
    {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MainPresenter.StatusServiceObserver observer = invocation.getArgument(1, MainPresenter.StatusServiceObserver.class);

                observer.displayMessage("error");
                return null;
            }
        };

        verifyErrorStatus(answer, "error");
    }
    @Test
    public void testPostStatus_exception()
    {
        Answer<Void> answer = new Answer<>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {

                MainPresenter.StatusServiceObserver observer = invocation.getArgument(1, MainPresenter.StatusServiceObserver.class);

                observer.displayMessage("exception");
                return null;
            }
        };

        verifyErrorStatus(answer, "exception");
    }

    private void verifyErrorStatus(Answer<Void> answer, String error) {

        Mockito.doAnswer(answer).when(mockStatusService).postStatus(Mockito.any(Status.class),Mockito.any(StatusService.MainActivityObserver.class));

        mainPresenterSpy.postStatus("test status");

        Mockito.verify(mockView).displayMessage(error);
    }

}
