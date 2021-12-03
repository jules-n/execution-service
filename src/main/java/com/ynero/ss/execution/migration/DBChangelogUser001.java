package com.ynero.ss.execution.migration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Updates;
import com.ynero.ss.execution.domain.User;
import lombok.Setter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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

    @ChangeSet(order = "003", id = "encrypting existing passwords", author = "ynero")
    public void encryptingPasswords(MongoDatabase mongo) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        var userCollection = mongo.getCollection(User.COLLECTION_NAME);
        var users = userCollection.find();
        users.forEach(
                user -> {
                    var password = user.get("password");
                    password = passwordEncoder.encode(password.toString());
                    var update = Updates.set("password", password);
                    var filter = Filters.eq("userId", user.get("userId"));
                    userCollection.updateOne(filter, update);
                }
        );
    }
}
