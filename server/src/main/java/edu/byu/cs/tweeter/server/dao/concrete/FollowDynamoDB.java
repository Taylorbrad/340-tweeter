package edu.byu.cs.tweeter.server.dao.concrete;

//import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
//import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

//import static edu.byu.cs.tweeter.server.dao.DAOInterface.TableName;

import static edu.byu.cs.tweeter.server.dao.DAOInterface.expirySeconds;

import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.server.dao.DataPage;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.interfaces.FollowDAO;
import edu.byu.cs.tweeter.server.dao.table_model.FollowsTableModel;
import software.amazon.awssdk.core.pagination.sync.SdkIterable;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbIndex;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.Page;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.regions.Region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.FollowRequest;
import edu.byu.cs.tweeter.model.net.request.FollowerRequest;
import edu.byu.cs.tweeter.model.net.request.FollowingRequest;
import edu.byu.cs.tweeter.model.net.request.IsFollowerRequest;
import edu.byu.cs.tweeter.model.net.request.UnfollowRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.FollowerResponse;
import edu.byu.cs.tweeter.model.net.response.FollowingResponse;
import edu.byu.cs.tweeter.model.net.response.IsFollowerResponse;
import edu.byu.cs.tweeter.model.net.response.UnfollowResponse;
import edu.byu.cs.tweeter.util.FakeData;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

/**
 * A DAO for accessing 'following' data from the database.
 */
public class FollowDynamoDB implements FollowDAO {

    private static final String TableName = "follows";
    public static final String IndexName = "follows_index";

    private static final String followerAttr = "follower_handle";
    private static final String followeeAttr = "followee_handle";

    AuthTokenDAO authTokenDAO = new AuthTokenDynamoDB();

    //TODO make it return the instance if its already been created
    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    /**
     * Gets the count of users from the database that the user specified is following. The
     * current implementation uses generated data and doesn't actually access a database.
     *
     * @param request the User whose count of how many following is desired.
     * @return said count.
     */
    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {
        //        if (!authTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
//            throw new RuntimeException("Token Expired");
//        }
//
//        GetFollowerCountResponse response = new GetFollowerCountResponse(22);
//        return response;
        // TODO: uses the dummy data.  Replace with a real implementation.
//        assert follower != null;
//        return getDummyFollowees().size();
        return new GetFollowingCountResponse(getDummyFollowees().size());
    }
    public GetFollowerCountResponse getFollowerCount(GetFollowerCountRequest request) {
        if (!authTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        // TODO: uses the dummy data.  Replace with a real implementation.
//        assert follower != null;
        return new GetFollowerCountResponse(getDummyFollowers().size());
    }

    /**
     * Gets the users from the database that the user specified in the request is following. Uses
     * information in the request object to limit the number of followees returned and to return the
     * next set of followees after any that were returned in a previous request. The current
     * implementation returns generated data and doesn't actually access a database.
     *
     * @param inRequest contains information about the user whose followees are to be returned and any
     *                other information required to satisfy the request.
     * @return the followees.
     */
    public FollowingResponse getFollowing(FollowingRequest inRequest) {

        if (!authTokenDAO.validateToken(inRequest.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        DynamoDbTable<FollowsTableModel> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowsTableModel.class));
//        DynamoDbTable<followsTableModel> table = enhancedClient.table(TableName, TableSchema.fromBean(followsTableModel.class)).index("follows_index");

        Key key = Key.builder()
                .partitionValue(inRequest.getFollowerAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(inRequest.getLimit());

        if(isNonEmptyString(inRequest.getLastFolloweeAlias())) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(followeeAttr, AttributeValue.builder().s(inRequest.getFollowerAlias()).build());
            startKey.put(followerAttr, AttributeValue.builder().s(inRequest.getLastFolloweeAlias()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowsTableModel> result = new DataPage<FollowsTableModel>();

        SdkIterable<Page<FollowsTableModel>> pages = table.query(request);
        pages.stream()
                .limit(inRequest.getLimit()) //was 1
                .forEach((Page<FollowsTableModel> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(follow -> result.getValues().add(follow));
                });

        ArrayList<User> userList = new ArrayList<>();


        for (FollowsTableModel i : result.getValues())
        {
            User userToAdd = new User(
                    i.getFollowee_fname(),
                    i.getFollowee_lname(),
                    i.getFollowee_handle(),
                    i.getFollowee_url()
            );

            userList.add(userToAdd);
        }

        return new FollowingResponse(userList, result.isHasMorePages());
    }
    public FollowerResponse getFollowers(FollowerRequest inRequest) {

        if (!authTokenDAO.validateToken(inRequest.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        DynamoDbIndex<FollowsTableModel> index = enhancedClient.table(TableName, TableSchema.fromBean(FollowsTableModel.class)).index("follows_index");
//        DynamoDbTable<followsTableModel> table = enhancedClient.table(TableName, TableSchema.fromBean(followsTableModel.class)).index("follows_index");

        Key key = Key.builder()
                .partitionValue(inRequest.getFolloweeAlias())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key))
                .limit(inRequest.getLimit());

        if(isNonEmptyString(inRequest.getLastFollowerAlias())) {
            // Build up the Exclusive Start Key (telling DynamoDB where you left off reading items)
            Map<String, AttributeValue> startKey = new HashMap<>();
            startKey.put(followeeAttr, AttributeValue.builder().s(inRequest.getFolloweeAlias()).build());
            startKey.put(followerAttr, AttributeValue.builder().s(inRequest.getLastFollowerAlias()).build());

            requestBuilder.exclusiveStartKey(startKey);
        }

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<FollowsTableModel> result = new DataPage<FollowsTableModel>();

        SdkIterable<Page<FollowsTableModel>> pages = index.query(request);
        pages.stream()
                .limit(inRequest.getLimit()) //was 1
                .forEach((Page<FollowsTableModel> page) -> {
                    result.setHasMorePages(page.lastEvaluatedKey() != null);
                    page.items().forEach(follow -> result.getValues().add(follow));
                });

        ArrayList<User> userList = new ArrayList<>();

        for (FollowsTableModel i : result.getValues())
        {
            User userToAdd = new User(
                    i.getFollower_fname(),
                    i.getFollower_lname(),
                    i.getFollower_handle(),
                    i.getFollower_url()
            );

            userList.add(userToAdd);
        }



        return new FollowerResponse(userList, result.isHasMorePages());

    }

    public FollowResponse follow(FollowRequest inRequest) {

        if (!authTokenDAO.validateToken(inRequest.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        User follower = inRequest.getFollower();
        User followee = inRequest.getFollowee();

        //TODO add checking for if user exists?
        HashMap<String, AttributeValue> keyToPut = new HashMap<>();

        keyToPut.put("follower_handle", AttributeValue.builder()
                .s(follower.getAlias())
                .build());

        keyToPut.put("followee_handle", AttributeValue.builder()
                .s(followee.getAlias())
                .build());

        keyToPut.put("follower_fname", AttributeValue.builder()
                .s(follower.getFirstName())
                .build());
        keyToPut.put("follower_lname", AttributeValue.builder()
                .s(follower.getLastName())
                .build());
        keyToPut.put("follower_url", AttributeValue.builder()
                .s(follower.getImageUrl())
                .build());


        keyToPut.put("followee_fname", AttributeValue.builder()
                .s(followee.getFirstName())
                .build());
        keyToPut.put("followee_lname", AttributeValue.builder()
                .s(followee.getLastName())
                .build());
        keyToPut.put("followee_url", AttributeValue.builder()
                .s(followee.getImageUrl())
                .build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TableName)
                .item(keyToPut)
                .build();

        try {
            dynamoDbClient.putItem(request);
//            ddb.deleteItem(deleteReq);
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return new FollowResponse();
    }
    public UnfollowResponse unfollow(UnfollowRequest inRequest) {
//        DynamoDbTable<FollowsTableModel> table = enhancedClient.table(TableName, TableSchema.fromBean(FollowsTableModel.class));
//        Key key = Key.builder()
//                .partitionValue("keyVal2").sortValue("asdf")
//                .build();
        if (!authTokenDAO.validateToken(inRequest.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        HashMap<String, AttributeValue> keyToPut = new HashMap<>();

        keyToPut.put(followerAttr, AttributeValue.builder()
                .s(inRequest.getSourceAlias())
                .build());

        keyToPut.put(followeeAttr, AttributeValue.builder()
                .s(inRequest.getTargetAlias())
                .build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(TableName)
                .key(keyToPut)
                .build();

        dynamoDbClient.deleteItem(request);

        return new UnfollowResponse();
    }

    public IsFollowerResponse isFollower(IsFollowerRequest request) {

        //TODO Currently dummy. Use request in 4
        return new IsFollowerResponse(new Random().nextInt() > 0);
    }



    /**
     * Returns the {@link FakeData} object used to generate dummy followees.
     * This is written as a separate method to allow mocking of the {@link FakeData}.
     *
     * @return a {@link FakeData} instance.
     */
    FakeData getFakeData() {
        return FakeData.getInstance();
    }

    /**
     * Returns the list of dummy followee data. This is written as a separate method to allow
     * mocking of the followees.
     *
     * @return the followees.
     */
    List<User> getDummyFollowees() {
        return getFakeData().getFakeUsers();
    }
    List<User> getDummyFollowers() {
        return getFakeData().getFakeUsers();
    }

    /**
     * Determines the index for the first followee in the specified 'allFollowees' list that should
     * be returned in the current request. This will be the index of the next followee after the
     * specified 'lastFollowee'.
     *
     * @param lastFolloweeAlias the alias of the last followee that was returned in the previous
     *                          request or null if there was no previous request.
     * @param allFollowees the generated list of followees from which we are returning paged results.
     * @return the index of the first followee to be returned.
     */
    private int getFolloweesStartingIndex(String lastFolloweeAlias, List<User> allFollowees) {

        int followeesIndex = 0;

        if(lastFolloweeAlias != null) {
            // This is a paged request for something after the first page. Find the first item
            // we should return
            for (int i = 0; i < allFollowees.size(); i++) {
                if(lastFolloweeAlias.equals(allFollowees.get(i).getAlias())) {
                    // We found the index of the last item returned last time. Increment to get
                    // to the first one we should return
                    followeesIndex = i + 1;
                    break;
                }
            }
        }

        return followeesIndex;
    }

    private static boolean isNonEmptyString(String value) {
        return (value != null && value.length() > 0);
    }
}
