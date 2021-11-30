package com.ynero.ss.execution.migration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.ynero.ss.execution.domain.User;
import org.bson.Document;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@ChangeLog
public class DBChangelogUser001 {

    @ChangeSet(order = "001", id = "zero admin", author = "ynero")
    public void insertZeroAdmin(MongoDatabase mongo) {
        Set<String> roles = new HashSet<>();
        roles.add("privacy.users.read");
        roles.add("privacy.users.create");
        roles.add("privacy.users.update");
        roles.add("privacy.users.delete");
        roles.add("data.nodes.read");
        roles.add("data.nodes.update");
        roles.add("data.nodes.create");
        roles.add("data.nodes.delete");
        roles.add("data.pipelines.read");
        roles.add("data.pipelines.update");
        roles.add("data.pipelines.create");
        roles.add("data.pipelines.delete");

        Document document = new Document();
        document.put("userId", UUID.randomUUID());
        document.put("username", "rick");
        document.put("password", "15mSDH#xjvtk");
        document.put("roles", roles);
        document.put("tenantId", "single-system");
        mongo.createCollection("users");
        mongo.getCollection("users").insertOne(document);
    }

    @ChangeSet(order = "002", id = "creating unique username index", author = "ynero")
    public void insertIndex(MongoDatabase mongo) {
        var userCollection = mongo.getCollection(User.COLLECTION_NAME);
        userCollection.createIndex(new BasicDBObject("username", 1), new IndexOptions().unique(true));
    }
}
