package edu.byu.cs.tweeter.server.dao.concrete;

import java.util.HashMap;
import java.util.UUID;

import edu.byu.cs.tweeter.model.domain.AuthToken;
import edu.byu.cs.tweeter.server.dao.DataPage;
import edu.byu.cs.tweeter.server.dao.interfaces.AuthTokenDAO;
import edu.byu.cs.tweeter.server.dao.table_model.AuthTokenTableModel;
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
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class AuthTokenDynamoDB implements AuthTokenDAO {

    private static final String TableName = "AuthToken";

    private static DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.US_EAST_1)
            .build();

    private static DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    public AuthToken getToken() {

        HashMap<String, AttributeValue> keyToPut = new HashMap<>();

        String token = UUID.randomUUID().toString();
        long dateTime = System.currentTimeMillis();

        keyToPut.put("token", AttributeValue.builder()
                .s(token)
                .build());

        keyToPut.put("datetime", AttributeValue.builder()
                .s(String.valueOf(dateTime))
                .build());

        PutItemRequest request = PutItemRequest.builder()
                .tableName(TableName)
                .item(keyToPut)
                .build();

        try {
            dynamoDbClient.putItem(request);
        } catch (
                DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
        return new AuthToken(token, String.valueOf(dateTime));
    }

    public boolean validateToken(String token, int expirySeconds) {

        DynamoDbTable<AuthTokenTableModel> table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenTableModel.class));

        Key key = Key.builder()
                .partitionValue(token)
                .build();


        QueryEnhancedRequest.Builder requestBuilder = QueryEnhancedRequest.builder()
                .queryConditional(QueryConditional.keyEqualTo(key));

        QueryEnhancedRequest request = requestBuilder.build();

        DataPage<AuthTokenTableModel> result = new DataPage<>();

        SdkIterable<Page<AuthTokenTableModel>> pages = table.query(request);

        pages.stream()
                .limit(1)
                .forEach((Page<AuthTokenTableModel> page) -> {

                    result.setHasMorePages(page.lastEvaluatedKey() != null);

                    page.items().forEach(token2 -> result.getValues().add(token2));
                });

        return ((System.currentTimeMillis() - Long.parseLong(result.getValues().get(0).getDatetime()) ) / 1000) < expirySeconds;
    }

    public void updateToken(String token) {

        DynamoDbTable<AuthTokenTableModel> table = enhancedClient.table(TableName, TableSchema.fromBean(AuthTokenTableModel.class));

        long dateTime = System.currentTimeMillis();

        AuthTokenTableModel tableModel = new AuthTokenTableModel();

        tableModel.setToken(token);
        tableModel.setDatetime(String.valueOf(dateTime));

        try {
            table.updateItem(tableModel);
        } catch (
                DynamoDbException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

    }

    public void deleteToken(String authToken) {

        HashMap<String, AttributeValue> keyToPut = new HashMap<>();

        keyToPut.put("token", AttributeValue.builder()
                .s(authToken)
                .build());

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(TableName)
                .key(keyToPut)
                .build();

        dynamoDbClient.deleteItem(request);
    }
}
