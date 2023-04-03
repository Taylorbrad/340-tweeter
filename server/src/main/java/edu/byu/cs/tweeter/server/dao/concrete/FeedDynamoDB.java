package edu.byu.cs.tweeter.server.dao.concrete;

import static edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO.expirySeconds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.GetFeedRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.GetFeedResponse;
import edu.byu.cs.tweeter.server.dao.DataPage;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FeedDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.server.dao.table_model.FeedTableModel;
import edu.byu.cs.tweeter.util.FakeData;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class FeedDynamoDB implements FeedDAO {

    DynamoDbClient dynamoDbClient;
    DynamoDbEnhancedClient enhancedClient;
    UserDAO userDynamoDB = new UserDynamoDB();

    AuthTokenDAO authTokenDAO = new AuthTokenDynamoDB();


    public GetFeedResponse getFeed(GetFeedRequest inRequest) {

        if (!authTokenDAO.validateToken(inRequest.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        DynamoDbTable<FeedTableModel> table = getEnhancedClient().table("Feed", TableSchema.fromBean(FeedTableModel.class));
//        DynamoDbTable<followsTableModel> table = enhancedClient.table(TableName, TableSchema.fromBean(followsTableModel.class)).index("follows_index");

        Key key = Key.builder()
                .partitionValue(inRequest.getTargetUser())
                .build();
//        Key key = Key.builder()
//                .sortValue(inRequest.())
//                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .scanIndexForward(false)
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(inRequest.getLimit());

        if(isNonEmptyString(inRequest.getLastStatus().getPost())) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put("alias", AttributeValue.builder().s(inRequest.getTargetUser()).build());
            startKey.put("datetime", AttributeValue.builder().s(String.valueOf(inRequest.getLastStatus().getTimestamp())).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FeedTableModel> result = new DataPage<>();

        SdkIterable<Page<FeedTableModel>> pages = table.query(request);
        pages.stream()
                .limit(inRequest.getLimit()) //was 1
                .forEach((Page<FeedTableModel> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(status -> result.getValues().add(status));
                });

//        ArrayList<User> userList = new ArrayList<>();
        ArrayList<Status> statusList = new ArrayList<>();


        for (FeedTableModel i : result.getValues())
        {
            Status statusToAdd = new Status(
                    i.getPost(),
                    userDynamoDB.getUser(new GetUserRequest(i.getAuthor_alias())).getUser(),
                    Long.valueOf(i.getDatetime()),
                    i.getUrls(),
                    i.getMentions()
            );

            statusList.add(statusToAdd);
        }

        return new GetFeedResponse(statusList, result.isHasMorePages());





//        if (!authTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
//            throw new RuntimeException("Token Expired");
//        }
//
//        //TODO Currently dummy. Use request in 4
//        Pair<List<Status>, Boolean> data = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
//
//        return new GetFeedResponse(data.getFirst(), data.getSecond());
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

    DynamoDbEnhancedClient getEnhancedClient() {
        if (enhancedClient == null) {
            return DynamoDbEnhancedClient.builder()
                    .dynamoDbClient(dynamoDbClient)
                    .build();
        }
        else {
            return enhancedClient;
        }
    }
    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }
    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }
}
