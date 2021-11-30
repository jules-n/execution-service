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
public class DBChangelogPipeline001 {

    @ChangeSet(order = "001", id = "creating pipeline indexes", author = "ynero")
    public void insertIndex(MongoDatabase mongo) {
        var pipelineCollection = mongo.getCollection(Pipeline.COLLECTION_NAME);
        pipelineCollection.createIndex(new BasicDBObject("pipelineId", 1), new IndexOptions().unique(true));
    }

    @ChangeSet(order = "002", id = "updated default userid", author = "ynero")
    public void updateUserId(MongoDatabase mongo) {
        var pipelineCollection = mongo.getCollection(Pipeline.COLLECTION_NAME);
        Bson filter = Filters.regex("tenantId", ".*");
        Bson updates = Updates.set("username", UUID.randomUUID());
        pipelineCollection.updateMany(filter, updates);
    }

    @ChangeSet(order = "003", id = "rename username to userid", author = "ynero")
    public void renameUserId(MongoDatabase mongo) {
        var pipelineCollection = mongo.getCollection(Pipeline.COLLECTION_NAME);
        pipelineCollection.updateMany(new Document(), Updates.rename("username", "userId"));
    }

    @ChangeSet(order = "004", id = "set as default userId id of zero-admin", author = "ynero")
    public void defaultUserId(MongoDatabase mongo) {
        var pipelineCollection = mongo.getCollection(Pipeline.COLLECTION_NAME);
        var userCollection = mongo.getCollection(User.COLLECTION_NAME);
        var user = userCollection.find(Filters.regex("username","rick"));
        UUID userId = (UUID) user.first().get("userId");
        pipelineCollection.updateMany(new Document(), Updates.set("userId", userId));
    }
}