package com.simplon.ttm.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import org.bson.BsonValue;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
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
import com.simplon.ttm.models.User;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MongoService {

    // Récupère l'URL de connexion à MongoDB depuis le fichier application.yml
    @Value("${spring.data.mongodb.uri}")
    private String mongoUrl;

    // Récupère le nom de la base de données MongoDB
    @Value("${spring.data.mongodb.db}")
    private String mongoDb;

    // Déclaration du client MongoDB
    private MongoClient client;

    // Référence à la base de données MongoDB
    private MongoDatabase db;

    // Référence à la collection "message" dans la base MongoDB
    private MongoCollection<Document> messageCollection;

    // Méthode exécutée automatiquement après l’instanciation du service
    @PostConstruct
    void init() throws IOException {
        log.info("start"); // Log de démarrage
        // Création du client MongoDB avec l’URL configurée
        client = MongoClients.create(mongoUrl);
        // Connexion à la base de données spécifiée
        db = client.getDatabase(mongoDb);
        // Récupération de la collection "message"
        messageCollection = db.getCollection("message");
    }
    // Méthode pour insérer un message dans la collection
    public BsonValue insert(User sender, User dest, String content) {
        return messageCollection.insertOne(
                        new Document("sender", sender.getId()) // ID de l'expéditeur
                                .append("dest", dest.getId()) // ID du destinataire
                                .append("content", content)) // Contenu du message
                .getInsertedId(); // Retourne l'ID du document inséré
    }

    // Méthode pour récupérer tous les messages entre deux utilisateurs
    public MongoIterable<Document> getMessagesForConversation(Long user1, Long user2) {
        return messageCollection.aggregate(Arrays.asList(
                        Aggregates.match(
                                Filters.and(
                                        Filters.in("sender", user1, user2), // Expéditeur = user1 ou user2
                                        Filters.in("dest", user1, user2)    // Destinataire = user1 ou user2
                                ))))
                .map(document -> {
                    ObjectId objectId = document.getObjectId("_id");
                    if (objectId != null) {
                        // Conversion de l'ObjectId en chaîne de caractères pour le front-end
                        document.put("_id", objectId.toHexString());
                    }
                    return document;
                });
    }

    // Méthode pour écouter les nouveaux messages en temps réel entre deux utilisateurs
    public ChangeStreamIterable<Document> listenForNewMessages(Long user1, Long user2) {
        return messageCollection
                // On crée un Change Stream sur la collection des messages
                .watch(Arrays.asList(
                        Aggregates.match(
                                Filters.and(
                                        // On filtre les messages envoyés par l'un ou l'autre utilisateur
                                        Filters.in("fullDocument.sender", user1, user2),
                                        // On filtre les messages dont le destinataire est l'un ou l'autre utilisateur
                                        Filters.in("fullDocument.dest", user1, user2)
                                ))))
                // Permet de récupérer le document complet à chaque changement
                .fullDocument(FullDocument.UPDATE_LOOKUP);
    }


    // Méthode pour supprimer un message par son ID
    public void deleteMessageById(String objectId) {
        ObjectId id = new ObjectId(objectId); // Conversion du string en ObjectId
        messageCollection.deleteOne(Filters.eq("_id", id)); // Suppression du document
    }

    // Méthode pour retrouver un message par son ID
    public Optional<Document> findMessageById(String objectId) {
        ObjectId id = new ObjectId(objectId); // Conversion du string en ObjectId
        Document doc = messageCollection.find(Filters.eq("_id", id)).first(); // Recherche du document
        return Optional.ofNullable(doc); // Retourne le document s’il existe
    }
}


