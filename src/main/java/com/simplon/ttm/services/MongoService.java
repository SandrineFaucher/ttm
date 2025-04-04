package com.simplon.ttm.services;

import java.io.IOException;
import java.util.Arrays;

import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.mongodb.client.ChangeStreamIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.changestream.FullDocument;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import com.simplon.ttm.models.Post;
import com.simplon.ttm.models.User;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MongoService {
    private MongoClient client;
    private MongoDatabase db;
    private MongoCollection<Post> messageCollection;

    @PostConstruct
    void init() throws IOException {
        log.info("MongoDB connection established");
        client = MongoClients.create("mongodb://localhost:27017/?replicaSet=rs0");
        db = client.getDatabase("ttm");
        messageCollection = db.getCollection("post", Post.class);
    }

    public BsonValue insert(Post post) {
        return messageCollection.insertOne(post)
                        .getInsertedId();
    }

    public UpdateResult update(Post post) {
        return messageCollection.replaceOne(new Document("_id", new ObjectId(String.valueOf(post.getId()))), post);
    }

    public MongoIterable<Post> getMessagesForDiscussion(Long user1, Long user2) {
        return messageCollection.aggregate(Arrays.asList(
                Aggregates.match(
                        Filters.and(
                                Filters.in("sender", user1, user2),
                                Filters.in("receiver", user1, user2)))))
                .map(p -> p);
    }

    public ChangeStreamIterable<Post> listenForNewDiscussion(Long user1, Long user2) {
        return messageCollection
                .watch(Arrays.asList(
                        Aggregates.match(
                                Filters.and(
                                        Filters.in("fullPost.sender", user1, user2),
                                        Filters.in("fullPost.receiver", user1, user2)))))
                .fullDocument(FullDocument.UPDATE_LOOKUP);
    }


}
