package edu.byu.cs.tweeter.server.dao.concrete;

import static edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO.expirySeconds;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.model.domain.Status;
import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetStoryRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.PostStatusRequest;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.GetStoryResponse;
import edu.byu.cs.tweeter.model.net.response.PostStatusResponse;
import edu.byu.cs.tweeter.server.dao.DataPage;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.StoryDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.UserDAO;
import edu.byu.cs.tweeter.server.dao.table_model.FollowsTableModel;
import edu.byu.cs.tweeter.server.dao.table_model.StoryTableModel;
import edu.byu.cs.tweeter.util.FakeData;
import edu.byu.cs.tweeter.util.Pair;
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

public class StoryDynamoDB implements StoryDAO {

    AuthTokenDAO authTokenDAO = new AuthTokenDynamoDB();
    UserDAO userDynamoDB = new UserDynamoDB();
    private static final String TableName = "Story";


    private static DynamoDbClient dynamoDbClient;

    private static DynamoDbEnhancedClient enhancedClient;

    public GetStoryResponse getStory(GetStoryRequest inRequest) {
        if (!authTokenDAO.validateToken(inRequest.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

//        try {
            DynamoDbTable<StoryTableModel> table = getEnhancedClient().table(TableName, TableSchema.fromBean(StoryTableModel.class));
//        DynamoDbTable<followsTableModel> table = enhancedClient.table(TableName, TableSchema.fromBean(followsTableModel.class)).index("follows_index");

            Key key = Key.builder()
                    .partitionValue(inRequest.getTargetUser())
                    .build();

            QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                    .queryConditional(QueryConditional.keyEqualTo(key))
                    .limit(inRequest.getLimit());

        //TODO fix with null object checking
            if(isNonEmptyString(inRequest.getLastStatus().getPost())) {
                // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
                Map<String, AttributeValue> startKey = new HashMap<>();
                startKey.put("author_alias", AttributeValue.builder().s(inRequest.getTargetUser()).build());
                startKey.put("datetime", AttributeValue.builder().s(String.valueOf(inRequest.getLastStatus().getTimestamp())).build());

                requestBuilder.exclusiveStartKey(startKey);
            }

            QueryEnhancedRequest request = requestBuilder.build();

            DataPage<StoryTableModel> result = new DataPage<>();

            SdkIterable<Page<StoryTableModel>> pages = table.query(request);
            pages.stream()
                    .limit(inRequest.getLimit()) //was 1
                    .forEach((Page<StoryTableModel> page) -> {
                        result.setHasMorePages(page.lastEvaluatedKey() != null);
                        page.items().forEach(status -> result.getValues().add(status));
                    });

//        ArrayList<User> userList = new ArrayList<>();
            ArrayList<Status> statusList = new ArrayList<>();


            for (StoryTableModel i : result.getValues())
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

            return new GetStoryResponse(statusList, result.isHasMorePages());
//        }
//        catch (RuntimeException e) {
//            throw new RuntimeException(e.getMessage());
//        }


//        //TODO Currently dummy. Use request in 4
//        Pair<List<Status>, Boolean> data = getFakeData().getPageOfStatus(request.getLastStatus(), request.getLimit());
//
//        return new GetStoryResponse(data.getFirst(), data.getSecond());
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
}
