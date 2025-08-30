package com.simplon.ttm.config;

import java.net.URI;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.MongoCommandException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

@Configuration
public class MongoReplicaInitializer {

    @Value("${spring.data.mongodb.uri}")
    private String MONGO_HOST;



    @Bean
    public CommandLineRunner initReplicaSetIfNeeded() {
        // Cette méthode est exécutée automatiquement au démarrage de l'application Spring Boot.
        return args -> {
            // On nettoie l'URL MongoDB pour retirer le paramètre replicaSet s'il est déjà présent.
            String host = MONGO_HOST.replaceFirst("[\\?|&]replicaSet=[a-zA-Z0-9]+&?", "");

            // On reconstruit l'adresse du serveur MongoDB à partir de l'URI.
            String mongoHost = new URI(host).getHost() + ":" + new URI(host).getPort();

            // Nombre de tentatives pour se connecter à MongoDB
            int retries = 10;
            // Délai entre chaque tentative (en millisecondes)
            int delay = 3000; // 3 secondes

            // Boucle de tentative de connexion à MongoDB
            for (int i = 1; i <= retries; i++) {
                try (MongoClient mongoClient = MongoClients.create(host)) {
                    // Connexion à la base "admin" pour exécuter des commandes d'administration
                    MongoDatabase adminDb = mongoClient.getDatabase("admin");

                    try {
                        // On vérifie si le ReplicaSet est déjà initialisé
                        adminDb.runCommand(new Document("replSetGetStatus", 1));
                        System.out.println("Replica Set déjà initialisé.");
                        return; // Si oui, on quitte la méthode
                    } catch (MongoCommandException e) {
                        // Code d'erreur 94 = ReplicaSet non initialisé
                        if (e.getErrorCode() == 94) {
                            System.out.println("Replica Set non initialisé, on l'initialise...");

                            // Configuration du ReplicaSet avec un seul membre (le serveur actuel)
                            Document config = new Document("_id", "rs0")
                                    .append("members", List.of(
                                            new Document("_id", 0).append("host", mongoHost)
                                    ));

                            // Initialisation du ReplicaSet
                            adminDb.runCommand(new Document("replSetInitiate", config));
                            System.out.println("Replica Set initialisé avec succès !");
                            return;
                        } else {
                            // Si l'erreur est autre, on la relance
                            throw e;
                        }
                    }
                } catch (Exception e) {
                    // Si MongoDB n'est pas encore prêt, on attend et on réessaie
                    System.err.printf("Tentative %d/%d : MongoDB pas encore prêt (%s)\n", i, retries, e.getMessage());
                    if (i == retries)
                        throw new RuntimeException("MongoDB ne répond pas après plusieurs tentatives", e);
                    Thread.sleep(delay); // Pause avant la prochaine tentative
                }
            }
        };
    }
}