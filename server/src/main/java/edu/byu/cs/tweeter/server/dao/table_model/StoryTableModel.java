package edu.byu.cs.tweeter.server.dao.table_model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class StoryTableModel {


    private String alias;
    private String datetime;
    private String post;

    @DynamoDbPartitionKey
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @DynamoDbSortKey
    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @DynamoDbAttribute("post")
    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }




    @Override
    public String toString() {
        return "status{" +
                "author='" + alias + '\'' +
                ", datetime='" + datetime + '\'' +
                ", post='" + post + '\'' +
                '}';
    }
    }