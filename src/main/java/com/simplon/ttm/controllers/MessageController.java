package com.simplon.ttm.controllers;

import java.security.Principal;
import java.util.Objects;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

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

        @MessageMapping("/requestMessages")
    public void openMessagePage(SendMessageDto request, Principal auth) {
        //Récupération du user authentifié
            String username = auth.getName();
            User sender = userRepository.findByUsername(username).orElseThrow();
            Long senderId = sender.getId();
        //Conversion de mes Long id du sender et de dest  en string
        var messages = mongoService.getMessagesForConversation(
                String.valueOf(senderId),
                String.valueOf(request.getDestId())
        );
        template.convertAndSend("/getMessages", messages);

        mongoService.listenForNewMessages("1", "2")
                .forEach(doc -> {
                    switch (Objects.requireNonNull(doc.getOperationType())) {
                        case INSERT:
                            assert doc.getFullDocument() != null;
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
}