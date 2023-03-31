package edu.byu.cs.tweeter.server.dao.table;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class UserTableModel {


    private String first_name;
    private String last_name;
    private String image_url;
    private String alias;
    private String hashed_password;

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }

    @DynamoDbAttribute("first_name")
    public String getFirst_name() {
        return first_name;
    }
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    @DynamoDbAttribute("last_name")
    public String getLast_name() {
        return last_name;
    }
    public void setLast_name(String last_name) {
            this.last_name = last_name;
        }

    @DynamoDbAttribute("image_url")
    public String getImage_url() {
        return image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    @DynamoDbAttribute("hashed_password")
    public String getHashed_password() {
        return hashed_password;
    }

    public void setHashed_password(String hashed_password) {
        this.hashed_password = hashed_password;
    }

    @Override
        public String toString() {
            return "user{" +
                    "alias='" + alias + '\'' +
                    ", name='" + first_name + " " + last_name + '\'' +
                    '}';
        }
    }