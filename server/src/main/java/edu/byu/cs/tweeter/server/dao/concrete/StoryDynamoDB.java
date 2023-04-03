package edu.byu.cs.tweeter.server.dao.concrete;

import static edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO.expirySeconds;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAO;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class StoryDynamoDB implements StoryDAO {

    AuthTokenDAO authTokenDAO = new AuthTokenDynamoDB();
    private static final String TableName = "Story";


    private static DynamoDbClient dynamoDbClient;

    private static DynamoDbEnhancedClient enhancedClient;

    public GetStoryResponse getStory(GetStoryRequest request) {
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }
        //TODO Currently dummy. Use request in 4
        Pair<List<Status>, Boolean> data = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());

        return new GetStoryResponse(data.getFirst(), data.getSecond());
    }

    public PostStatusResponse addToStory(PostStatusRequest inRequest) {

        HashMap<String, AttributeValue> keyToPut = new HashMap<>();

//        String token = UUID.randomUUID().toString();
        long dateTime = System.currentTimeMillis();

        keyToPut.put("author_alias", AttributeValue.builder()
                .s(inRequest.getAuthor().getAlias())
                .build());

        keyToPut.put("datetime", AttributeValue.builder()
                .s(String.valueOf(dateTime))
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
                .ss(inRequest.getUrls())
                .build());
//
        List<String> mentions = inRequest.getMentions();
        if (mentions.size() == 0)
        {
            mentions.add("");
        }
        keyToPut.put("mentions", AttributeValue.builder()
                .ss(inRequest.getMentions())
                .build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TableName)
                .item(keyToPut)
                .build();

        try {
            getClient().putItem(request);
        } catch (
                DynamoDbException e) {
            System.err.println(e.getMessage());
//            System.exit(1);
            throw new RuntimeException("put fail: " + e.getMessage());

        }

        return new PostStatusResponse();
    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
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

}
