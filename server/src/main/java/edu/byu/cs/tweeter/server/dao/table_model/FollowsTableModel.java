package edu.byu.cs.tweeter.server.dao.table_model;

import edu.byu.cs.tweeter.server.dao.concrete.FollowDynamoDB;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.*;

@DynamoDbBean
public class FollowsTableModel {


        private String follower_handle;
        private String followee_handle;

//        private String follower_fname;
//        private String follower_lname;
//        private String follower_url;
//
//        private String followee_fname;
//        private String followee_lname;
//        private String followee_url;

        @DynamoDbPartitionKey
        @DynamoDbSecondarySortKey(indexNames = FollowDynamoDB.IndexName)
        public String getFollower_handle() {
            return follower_handle;
        }

        public void setFollower_handle(String follower_handle) {
            this.follower_handle = follower_handle;
        }

        @DynamoDbSortKey
        @DynamoDbSecondaryPartitionKey(indexNames = FollowDynamoDB.IndexName)
        public String getFollowee_handle() {
            return followee_handle;
        }

        public void setFollowee_handle(String followee_handle) {
            this.followee_handle = followee_handle;
        }


//        @DynamoDbAttribute("follower_fname")
//        public String getFollower_fname() {
//            return follower_fname;
//        }
//        public void setFollower_fname(String follower_fname) {
//            this.follower_fname = follower_fname;
//        }
//        @DynamoDbAttribute("follower_lname")
//        public String getFollower_lname() {
//            return follower_lname;
//        }
//        public void setFollower_lname(String follower_lname) {
//            this.follower_lname = follower_lname;
//        }
//        @DynamoDbAttribute("follower_url")
//        public String getFollower_url() {
//            return follower_url;
//        }
//        public void setFollower_url(String follower_url) {
//            this.follower_url = follower_url;
//        }



//        @DynamoDbAttribute("followee_fname")
//        public String getFollowee_fname() {
//            return followee_fname;
//        }
//        public void setFollowee_fname(String followee_fname) {
//            this.followee_fname = followee_fname;
//        }
//        @DynamoDbAttribute("followee_lname")
//        public String getFollowee_lname() {
//            return followee_lname;
//        }
//        public void setFollowee_lname(String followee_lname) {
//            this.followee_lname = followee_lname;
//        }
//        @DynamoDbAttribute("followee_url")
//        public String getFollowee_url() {
//            return followee_url;
//            }
//        public void setFollowee_url(String followee_url) {
//            this.followee_url = followee_url;
//        }

    @Override
        public String toString() {
            return "follows{" +
                    "follower='" + follower_handle + '\'' +
                    ", followee='" + followee_handle + '\'' +
                    '}';
        }
    }