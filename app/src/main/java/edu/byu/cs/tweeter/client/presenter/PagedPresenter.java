package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.model.domain.User;

public class PagedPresenter<T> extends Presenter {

    private PagedView view;

    public interface PagedView extends View {
        void displayUser(User user);
        void setLoadingFooter(boolean value);

    }

}
