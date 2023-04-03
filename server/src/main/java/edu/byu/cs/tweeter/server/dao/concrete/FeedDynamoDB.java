package edu.byu.cs.tweeter.server.dao.concrete;

import static edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO.expirySeconds;

import java.util.HashMap;
import java.util.List;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class FeedDynamoDB implements FeedDAO {

    DynamoDbClient dynamoDbClient;

    AuthTokenDAO authTokenDAO = new AuthTokenDynamoDB();

    public GetFeedResponse getFeed(GetFeedRequest request) {

        if (!authTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        //TODO Currently dummy. Use request in 4
        Pair<List<Status>, Boolean> data = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());

        return new GetFeedResponse(data.getFirst(), data.getSecond());
    }

    public void addToFeed(PostStatusRequest inRequest) {

        if (!authTokenDAO.validateToken(inRequest.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        List<User> users = new FollowDynamoDB().getFollowers(new FollowerRequest(inRequest.getAuthToken(), inRequest.getAuthor().getAlias(), 100, "")).getFollowers();
         //TODO Get followers

        long dateTime = System.currentTimeMillis();

        for (User i: users) { //TODO batch write??
            HashMap<String, AttributeValue> keyToPut = new HashMap<>();

//        String token = UUID.randomUUID().toString();

            keyToPut.put("alias", AttributeValue.builder()
                    .s(i.getAlias())
                    .build());

            keyToPut.put("datetime", AttributeValue.builder()
                    .s(String.valueOf(dateTime))
                    .build());

            keyToPut.put("author_alias", AttributeValue.builder()
                    .s(inRequest.getAuthor().getAlias())
                    .build());

            keyToPut.put("post", AttributeValue.builder()
                    .s(inRequest.getStatus())
                    .build());

            List<String> urls = inRequest.getUrls();
            if (urls.size() == 0)
            {
                urls.add("");
            }
            keyToPut.put("urls", AttributeValue.builder()
                    .ss(urls)
                    .build());
            
            List<String> mentions = inRequest.getMentions();
            if (mentions.size() == 0)
            {
                mentions.add("");
            }
            keyToPut.put("mentions", AttributeValue.builder()
                    .ss(mentions)
                    .build());

            PutItemRequest request = PutItemRequest.builder()
                    .tableName("Feed")
                    .item(keyToPut)
                    .build();

            try {
                getClient().putItem(request);
//                new FeedDynamoDB().addToFeed(inRequest);
            } catch (
                    DynamoDbException e) {
                System.err.println(e.getMessage());
//            System.exit(1);
                throw new RuntimeException("put fail: " + e.getMessage());

            }
        }


        //Write to each followers feed

    }
    DynamoDbClient getClient() {
        if (dynamoDbClient == null)
        {
            return DynamoDbClient.builder()
                    .region(Region.US_EAST_1)
                    .build();
        }
        else return dynamoDbClient;

    }

//    DynamoDbEnhancedClient getEnhancedClient() {
//        if (enhancedClient == null) {
//            return DynamoDbEnhancedClient.builder()
//                    .dynamoDbClient(dynamoDbClient)
//                    .build();
//        }
//        else {
//            return enhancedClient;
//        }
//    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
