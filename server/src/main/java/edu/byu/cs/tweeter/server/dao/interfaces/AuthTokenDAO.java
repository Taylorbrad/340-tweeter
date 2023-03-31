package edu.byu.cs.tweeter.server.dao.interfaces;

import edu.byu.cs.tweeter.model.domain.AuthToken;

public interface AuthTokenDAO {

    AuthToken getToken();
    boolean validateToken(String token, int expirySeconds);
    void updateToken(String token);
    void deleteToken(String token);

}
