package edu.byu.cs.tweeter.server.dao.table;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSecondaryPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class AuthTokenTableModel {


        private String token;
        private String datetime;

        @DynamoDbPartitionKey
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        @DynamoDbAttribute("datetime")
        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

    @Override
        public String toString() {
            return "authToken{" +
                    "token='" + token + '\'' +
                    ", datetime='" + datetime + '\'' +
                    '}';
        }
    }