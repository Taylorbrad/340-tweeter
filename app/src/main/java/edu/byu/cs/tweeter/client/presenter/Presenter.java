package edu.byu.cs.tweeter.client.presenter;

import edu.byu.cs.tweeter.client.model.service.UserService;

public abstract class Presenter {

//    private View view;
    private UserService userService;

//    public View getView() {
//        return view;
//    }
//
//    public void setView(View view) {
//        this.view = view;
//    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public interface View {
        void displayMessage(String message);
    }
}
