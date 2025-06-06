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
import org.springframework.web.bind.annotation.DeleteMapping;

import com.simplon.ttm.dto.SendMessageDto;
import com.simplon.ttm.models.Message;
import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.MongoService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MessageController {

    private final MongoService mongoService;
    private final SimpMessagingTemplate template;
    private final UserRepository userRepository;


    public MessageController(MongoService mongoService, SimpMessagingTemplate template, UserRepository userRepository) {
        this.mongoService = mongoService;
        this.template = template;
        this.userRepository = userRepository;
    }
    // Map qui permet de conserver une connexion établie entre deux interlocuteurs pour ne pas la répéter
    private final Map<String, Boolean> activeListeners = new ConcurrentHashMap<>();

    @MessageMapping("/requestMessages")
    public void openMessagePage(SendMessageDto request, Principal auth) {
        //Récupération du user authentifié
            String username = auth.getName();
            User sender = userRepository.findByUsername(username).orElseThrow();
            Long senderId = sender.getId();
            Long destId = request.getDestId();
            //garantit la reciprocité du binome
            String Key = senderId < destId ? senderId + "_" + destId : destId + "_" + senderId;


        var messages = mongoService.getMessagesForConversation(senderId, destId);
        template.convertAndSend("/getMessages", messages);

        if(!activeListeners.containsKey(Key)) {
            activeListeners.put(Key, true);
            mongoService.listenForNewMessages(senderId, destId)
                    .forEach(doc -> {
                        switch (Objects.requireNonNull(doc.getOperationType())) {
                            case INSERT:
//                              //conversion qui permet de passer l'ObjectId en string vers le front
                                Document fullDoc = doc.getFullDocument();
                                if (fullDoc != null && fullDoc.getObjectId("_id") != null) {
                                    fullDoc.put("_id", fullDoc.getObjectId("_id").toHexString());
                                }
                                template.convertAndSend("/newMessage", doc.getFullDocument().toJson());
                                break;
                            case DELETE:
                                assert doc.getDocumentKey() != null;
                                template.convertAndSend("/deleteMessage", doc.getDocumentKey().get("_id").asObjectId().getValue());
                                break;
                            case UPDATE:
                                assert doc.getUpdateDescription() != null;
                                assert doc.getUpdateDescription().getUpdatedFields() != null;
                                template.convertAndSend("/updateMessage", doc.getUpdateDescription().getUpdatedFields().toJson());
                                break;
                            case REPLACE:
                                assert doc.getFullDocument() != null;
                                template.convertAndSend("/updateMessage", doc.getFullDocument().toJson());
                                break;
                            default:
                                log.warn("Not yet implemented OperationType: {}", doc.getOperationType());
                        }
                    });
        }
    }
//@MessageMapping("/requestMessages")
//public void openMessagePage(SendMessageDto request, Principal auth) {
//    String username = auth.getName();
//    User sender = userRepository.findByUsername(username).orElseThrow();
//    Long senderId = sender.getId();
//    Long destId = request.getDestId();
//
//    var messages = mongoService.getMessagesForConversation(senderId, destId);
//    template.convertAndSend("/getMessages", messages);
//
//    // TEMP : toujours écouter les messages
//    mongoService.listenForNewMessages(senderId, destId)
//            .forEach(doc -> {
//                switch (Objects.requireNonNull(doc.getOperationType())) {
//                    case INSERT:
//                        Document fullDoc = doc.getFullDocument();
//                        if (fullDoc != null && fullDoc.getObjectId("_id") != null) {
//                            fullDoc.put("_id", fullDoc.getObjectId("_id").toHexString());
//                        }
//                        template.convertAndSend("/newMessage", doc.getFullDocument().toJson());
//                        break;
//                    case DELETE:
//                        assert doc.getDocumentKey() != null;
//                        template.convertAndSend("/deleteMessage", doc.getDocumentKey().get("_id").asObjectId().getValue());
//                        break;
//                    case UPDATE:
//                        assert doc.getUpdateDescription() != null;
//                        template.convertAndSend("/updateMessage", doc.getUpdateDescription().getUpdatedFields().toJson());
//                        break;
//                    case REPLACE:
//                        assert doc.getFullDocument() != null;
//                        template.convertAndSend("/updateMessage", doc.getFullDocument().toJson());
//                        break;
//                    default:
//                        log.warn("Not yet implemented OperationType: {}", doc.getOperationType());
//                }
//            });
//}
    @MessageMapping("/send")
    public void sendMessage(SendMessageDto message, Principal auth) throws Exception {
        //Récupération de mon user connecté
        String username = auth.getName();
        //Le rechercher dans la bdd
        User sender = userRepository.findByUsername(username)
                        .orElseThrow(()-> new RuntimeException("User not found"));
        //Récupération du destinataire
        User dest = userRepository.findById(message.getDestId())
                        .orElseThrow(()-> new RuntimeException("Destinataire not found"));
        //Récupération du content
        String content = message.getContent();

        mongoService.insert(sender, dest, content);
    }

    @MessageMapping("/delete")
    public void deleteMessage(Map<String, String> payload, Principal auth) {
        // Récupération de l'objectId du message
        String objectId = payload.get("_id");
        // Récupérer l'utilisateur authentifié
        String username = auth.getName();
        User sender = userRepository.findByUsername(username).orElseThrow();

        Optional<Document> messageToDelete = mongoService.findMessageById(objectId);

        if (messageToDelete.isEmpty()) {
            throw new RuntimeException("Message introuvable");
        }

        Document message = messageToDelete.get();

        // Vérifie si le sender du message correspond à l'utilisateur authentifié
        Long senderIdFromMessage = message.getLong("sender");
        if (!Objects.equals(senderIdFromMessage, sender.getId())) {
            throw new RuntimeException("Vous n'avez pas le droit de supprimer ce message");
        }
        // Supprimer le message
        mongoService.deleteMessageById(objectId);
        // Notifier les autres interlocuteurs
        template.convertAndSend("/deleteMessage", objectId);
    }
}