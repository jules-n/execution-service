package com.ynero.ss.execution.migration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Updates;
import com.ynero.ss.execution.domain.Node;
import com.ynero.ss.execution.domain.Pipeline;
import com.ynero.ss.execution.domain.User;
import lombok.extern.log4j.Log4j2;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.UUID;

@ChangeLog
@Log4j2
public class DBChangelogNode001 {

    @ChangeSet(order = "001", id = "creating node indexes", author = "ynero")
    public void insertIndex(MongoDatabase mongo) {
        var nodeCollection = mongo.getCollection(Node.COLLECTION_NAME);
        nodeCollection.createIndex(new BasicDBObject("nodeId", 1), new IndexOptions().unique(true));
    }

    @ChangeSet(order = "002", id = "updating with default user", author = "ynero")
    public void setDefaultUser(MongoDatabase mongo) {
        var nodeCollection = mongo.getCollection(Node.COLLECTION_NAME);
        var userCollection = mongo.getCollection(User.COLLECTION_NAME);
        var user = userCollection.find(Filters.regex("username","rick"));
        UUID userId = (UUID) user.first().get("userId");
        nodeCollection.updateMany(new Document(), Updates.set("userId", userId));
    }
}