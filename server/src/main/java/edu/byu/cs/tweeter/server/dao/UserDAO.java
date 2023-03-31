package edu.byu.cs.tweeter.server.dao;

import static edu.byu.cs.tweeter.server.dao.DAOInterface.expirySeconds;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import edu.byu.cs.tweeter.model.domain.User;
import edu.byu.cs.tweeter.model.net.request.GetFollowerCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetFollowingCountRequest;
import edu.byu.cs.tweeter.model.net.request.GetUserRequest;
import edu.byu.cs.tweeter.model.net.request.LoginRequest;
import edu.byu.cs.tweeter.model.net.request.LogoutRequest;
import edu.byu.cs.tweeter.model.net.request.RegisterRequest;
import edu.byu.cs.tweeter.model.net.response.FollowResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowerCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetFollowingCountResponse;
import edu.byu.cs.tweeter.model.net.response.GetUserResponse;
import edu.byu.cs.tweeter.model.net.response.LogoutResponse;
import edu.byu.cs.tweeter.server.dao.table.AuthTokenTableModel;
import edu.byu.cs.tweeter.server.dao.table.UserTableModel;
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

public class UserDAO {

    private static final String TableName = "User";

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    public static User register(RegisterRequest inRequest) throws NoSuchAlgorithmException {

        String hashedPassword = hashPassword(inRequest.getPassword());

        //TODO add checking for if user exists?
        HashMap<String, AttributeValue> keyToPut = new HashMap<>();

        keyToPut.put("alias", AttributeValue.builder()
                .s(inRequest.getUsername())
                .build());

        keyToPut.put("hashed_password", AttributeValue.builder()
                .s(hashedPassword)
                .build());

        keyToPut.put("first_name", AttributeValue.builder()
                .s(inRequest.getFirstName())
                .build());

        keyToPut.put("last_name", AttributeValue.builder()
                .s(inRequest.getLastName())
                .build());

        //TODO upload to S3, get url
        keyToPut.put("image_url", AttributeValue.builder()
                .s("inRequest.getImageEncodedToString()")
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
        return new User(inRequest.getFirstName(), inRequest.getLastName(), inRequest.getUsername(), "inRequest.getImageEncodedToString()");
    }

    public static User login(LoginRequest inRequest) {

        String hashedPassword = hashPassword(inRequest.getPassword());

        DynamoDbTable<UserTableModel> table = enhancedClient.table(TableName, TableSchema.fromBean(UserTableModel.class));

        Key key = Key.builder()
                .partitionValue(inRequest.getUsername())
                .build();

        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<UserTableModel> result = new DataPage<>();

        SdkIterable<Page<UserTableModel>> pages = table.query(request);

        pages.stream()
                .limit(1)
                .forEach((Page<UserTableModel> page) -> {

                    result.setHasMorePages(page.lastEvaluatedKey() != null);

                    page.items().forEach(token2 -> result.getValues().add(token2));
                });
        if (result.getValues().get(0).getHashed_password() == hashedPassword) {
            throw new RuntimeException("Bad Password");
        }
        UserTableModel user = result.getValues().get(0);

        return new User(user.getFirst_name(), user.getLast_name(), user.getAlias(), user.getImage_url());
    }

    public GetUserResponse getUser(GetUserRequest request) {
        return new GetUserResponse(getFakeData().findUserByAlias(request.getAlias()));
    }

    public LogoutResponse logout(LogoutRequest request) {
        return new LogoutResponse();
    }

    protected FakeData getFakeData() {
        return FakeData.getInstance();
    }

    public GetFollowerCountResponse getFollowerCount(GetFollowerCountRequest request) {

        if (!AuthTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        GetFollowerCountResponse response = new GetFollowerCountResponse(22);
        return response;
    }

    public GetFollowingCountResponse getFollowingCount(GetFollowingCountRequest request) {

        if (!AuthTokenDAO.validateToken(request.getAuthToken().getToken(), expirySeconds)) {
            throw new RuntimeException("Token Expired");
        }

        GetFollowingCountResponse response = new GetFollowingCountResponse(25);
        return response;
    }

    private static String hashPassword(String passwordToHash) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(passwordToHash.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "FAILED TO HASH";
    }
}