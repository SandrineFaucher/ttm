package com.simplon.ttm.controllers;

import java.security.Principal;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.Document;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;


import com.simplon.ttm.dto.SendMessageDto;
import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.MongoService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MessageController {

    // Injection des dépendances via le constructeur
    private final MongoService mongoService; // Service pour interagir avec MongoDB
    private final SimpMessagingTemplate template; // Permet d’envoyer des messages via WebSocket
    private final UserRepository userRepository; // Accès aux utilisateurs en base

    // Map pour éviter de créer plusieurs écouteurs pour une même conversation
    private final Map<String, Boolean> activeListeners = new ConcurrentHashMap<>();

    // Méthode appelée lorsqu’un utilisateur ouvre une page de conversation
    @MessageMapping("/requestMessages")
    public void openMessagePage(SendMessageDto request, Principal auth) {
        // Récupération du nom d’utilisateur connecté
        String username = auth.getName();
        // Récupération de l’objet User correspondant
        User sender = userRepository.findByUsername(username).orElseThrow();
        Long senderId = sender.getId();
        Long destId = request.getDestId();

        // Création d’une clé unique pour identifier la conversation (ordre stable)
        String Key = senderId < destId ? senderId + "_" + destId : destId + "_" + senderId;

        // Récupération des messages existants entre les deux utilisateurs
        var messages = mongoService.getMessagesForConversation(senderId, destId);
        // Envoi des messages au front via WebSocket
        template.convertAndSend("/getMessages", messages);

        // Si aucun écouteur n’est actif pour cette conversation, on en crée un
        if (!activeListeners.containsKey(Key)) {
            activeListeners.put(Key, true);

            // Écoute des nouveaux événements dans MongoDB (Change Stream)
            mongoService.listenForNewMessages(senderId, destId)
                    .forEach(doc -> {
                        switch (Objects.requireNonNull(doc.getOperationType())) {
                            case INSERT:
                                // Conversion de l’ObjectId en string pour le front
                                Document fullDoc = doc.getFullDocument();
                                if (fullDoc != null && fullDoc.getObjectId("_id") != null) {
                                    fullDoc.put("_id", fullDoc.getObjectId("_id").toHexString());
                                }
                                // Envoi du nouveau message au front
                                template.convertAndSend("/newMessage", doc.getFullDocument().toJson());
                                break;

                            case DELETE:
                                // Envoi de l’ID du message supprimé au front
                                assert doc.getDocumentKey() != null;
                                template.convertAndSend("/deleteMessage", doc.getDocumentKey().get("_id").asObjectId().getValue());
                                break;

                            case UPDATE:
                                // Envoi des champs modifiés au front
                                assert doc.getUpdateDescription() != null;
                                assert doc.getUpdateDescription().getUpdatedFields() != null;
                                template.convertAndSend("/updateMessage", doc.getUpdateDescription().getUpdatedFields().toJson());
                                break;

                            case REPLACE:
                                // Envoi du document remplacé au front
                                assert doc.getFullDocument() != null;
                                template.convertAndSend("/updateMessage", doc.getFullDocument().toJson());
                                break;

                            default:
                                // Log si un type d’opération non géré est rencontré
                                log.warn("Not yet implemented OperationType: {}", doc.getOperationType());
                        }
                    });
        }
    }

    // Méthode appelée lorsqu’un utilisateur envoie un message
    @MessageMapping("/send")
    public void sendMessage(SendMessageDto message, Principal auth) throws Exception {
        // Récupération de l’utilisateur connecté
        String username = auth.getName();
        User sender = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Récupération du destinataire
        User dest = userRepository.findById(message.getDestId())
                .orElseThrow(() -> new RuntimeException("Destinataire not found"));

        // Récupération du contenu du message
        String content = message.getContent();

        // Insertion du message dans MongoDB
        mongoService.insert(sender, dest, content);
    }

    // Méthode appelée lorsqu’un utilisateur souhaite supprimer un message
    @MessageMapping("/delete")
    public void deleteMessage(Map<String, String> payload, Principal auth) {
        // Récupération de l’ID du message à supprimer
        String objectId = payload.get("_id");

        // Récupération de l’utilisateur connecté
        String username = auth.getName();
        User sender = userRepository.findByUsername(username).orElseThrow();

        // Recherche du message dans MongoDB
        Optional<Document> messageToDelete = mongoService.findMessageById(objectId);

        if (messageToDelete.isEmpty()) {
            throw new RuntimeException("Message introuvable");
        }

        Document message = messageToDelete.get();

        // Vérifie que l’utilisateur est bien l’auteur du message
        Long senderIdFromMessage = message.getLong("sender");
        if (!Objects.equals(senderIdFromMessage, sender.getId())) {
            throw new RuntimeException("Vous n'avez pas le droit de supprimer ce message");
        }

        // Suppression du message
        mongoService.deleteMessageById(objectId);

        // Notification au front pour retirer le message
        template.convertAndSend("/deleteMessage", objectId);
    }
}
