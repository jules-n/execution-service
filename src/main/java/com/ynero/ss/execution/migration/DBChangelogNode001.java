package com.ynero.ss.execution.migration;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.ynero.ss.execution.domain.Node;
import com.ynero.ss.execution.domain.Pipeline;
import lombok.extern.log4j.Log4j2;

@ChangeLog
@Log4j2
public class DBChangelogNode001 {

    @ChangeSet(order = "001", id = "creating node indexes", author = "ynero")
    public void insertIndex(MongoDatabase mongo) {
        var nodeCollection = mongo.getCollection(Node.COLLECTION_NAME);
        nodeCollection.createIndex(new BasicDBObject("nodeId", 1), new IndexOptions().unique(true));
    }
}