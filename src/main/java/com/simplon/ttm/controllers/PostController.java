package com.simplon.ttm.controllers;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

import com.simplon.ttm.models.Post;
import com.simplon.ttm.models.User;
import com.simplon.ttm.services.MongoService;
import com.simplon.ttm.services.UserService;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class PostController {
    private MongoService mongoService;
    private SimpMessagingTemplate postTemplate;
    private UserService userService;

    public PostController(MongoService mongoService,
                          SimpMessagingTemplate postTemplate,
                          UserService userService) {
        this.mongoService = mongoService;
        this.postTemplate = postTemplate;
        this.userService = userService;
    }

    @MessageMapping("/requestPosts/{userId2}")
    public void openMessagePage(@PathVariable Long userId2){
        // Récupération de l'utilisateur authentifié
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authenticatedUsername = authentication.getName();

        // Récupération de celui-ci dans la base à partir du userService
        User authenticatedUser = userService.findByUsername(authenticatedUsername).orElseThrow();
        Long authenticatedUserId = authenticatedUser.getId();

        // Récupération des messages pour la discussion (le userId2 est passé par l'url)
        var messages = mongoService.getMessagesForDiscussion(authenticatedUserId, userId2);
        postTemplate.convertAndSend("/getMessages", messages);

        mongoService.listenForNewDiscussion(authenticatedUserId, userId2)
                .forEach(doc -> {
                    switch (doc.getOperationType()){
                        case INSERT:
                            postTemplate.convertAndSend("/newPost", doc.getFullDocument());
                            break;
                        case DELETE:
                            postTemplate.convertAndSend("/deletePost", doc.getDocumentKey().get("_id").asObjectId().getValue());
                            break;
                        case UPDATE:
                            postTemplate.convertAndSend("/updatePost", doc.getUpdateDescription().getUpdatedFields().toJson());
                            break;
                        case REPLACE:
                            postTemplate.convertAndSend("/updatePost", doc.getFullDocument());
                            break;
                        default:
                            log.warn("Not yet implemented OperationType: {}", doc.getOperationType());
                    }
                });
    }
    @MessageMapping("/send")
    public void sendPost(Post post) throws Exception{
        mongoService.insert(post);
    }
}
