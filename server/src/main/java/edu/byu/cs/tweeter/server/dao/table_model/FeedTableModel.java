package edu.byu.cs.tweeter.server.dao.table_model;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class FeedTableModel {


        private String author_alias;
        private String datetime;
        private String post;

        @DynamoDbPartitionKey
        public String getAuthor_alias() {
            return author_alias;
        }

        public void setAuthor_alias(String author_alias) {
            this.author_alias = author_alias;
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
                    "author='" + author_alias + '\'' +
                    ", datetime='" + datetime + '\'' +
                    ", post='" + post + '\'' +
                    '}';
        }
    }