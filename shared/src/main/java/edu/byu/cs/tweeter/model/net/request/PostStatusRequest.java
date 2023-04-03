package edu.byu.cs.tweeter.model.net.request;

import java.util.List;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.User;

public class PostStatusRequest {

    private String status;
    private User author;
    private AuthToken authToken;
    private List<String> urls;
    public List<String> mentions;

    PostStatusRequest() {}

    public PostStatusRequest(String status, User author, AuthToken authToken, List<String> urls, List<String> mentions) {
        this.status = status;
        this.author = author;
        this.authToken = authToken;
        this.urls = urls;
        this.mentions = mentions;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    public List<String> getMentions() {
        return mentions;
    }

    public void setMentions(List<String> mentions) {
        this.mentions = mentions;
    }
}
