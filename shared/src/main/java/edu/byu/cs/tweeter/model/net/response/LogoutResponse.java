package edu.byu.cs.tweeter.model.net.response;

public class LogoutResponse extends Response {

    LogoutResponse(String message) {
        super(false, message);
    }

    public LogoutResponse() {
        super(true);
    }
}
