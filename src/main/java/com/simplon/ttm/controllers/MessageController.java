package com.simplon.ttm.controllers;

import java.util.Objects;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.simplon.ttm.models.Message;
import com.simplon.ttm.services.MongoService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class MessageController {

    private final MongoService mongoService;
    private final SimpMessagingTemplate template;

    public MessageController(MongoService mongoService, SimpMessagingTemplate template) {
        this.mongoService = mongoService;
        this.template = template;
    }

        @MessageMapping("/requestMessages")
    public void openMessagePage() {
        var messages = mongoService.getMessagesForConversation("1", "2");
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
    public void sendMessage(Message message) throws Exception {
        mongoService.insert(message.getUser1(), message.getUser2(), message.getContent());
    }
}