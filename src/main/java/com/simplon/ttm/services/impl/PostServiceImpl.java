package com.simplon.ttm.services.impl;

import java.time.LocalDate;
import java.util.List;

import com.simplon.ttm.models.Post;
import com.simplon.ttm.models.User;
import com.simplon.ttm.repositories.PostRepository;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.PostService;

import jakarta.persistence.EntityNotFoundException;

public class PostServiceImpl implements PostService {

    private UserRepository userRepository;

    private PostRepository postRepository;

    public PostServiceImpl(UserRepository userRepository, PostRepository postRepository){
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    /**
     * Permet la création d'un post
     * @param senderId l'expéditeur du post
     * @param receiverId le destinataire du post
     * @param content le message du post
     * @return le post
     */
    public Post createPost(Long senderId, Long receiverId, String content) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("the message cannot be empty");
        }
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("User sender not found with id " + senderId));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("User receiver not found with id " + receiverId));

        Post post = Post.builder()
                .sender(sender)
                .receiver(receiver)
                .content(content)
                .creationDate(LocalDate.now())
                .build();

        return postRepository.save(post);
    }

    public List<Post> getPostsBySender(Long senderId) {
        // On vérifie si l'utilisateur existe dans le repository
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new EntityNotFoundException("User receiver not found with id " + senderId));

        // Recherche des posts reçus par l'utilisateur
        return postRepository.findBySenderId(senderId);  // Appel au repository avec l'ID du récepteur
    }

    public List<Post> getPostsByReceiver(Long receiverId) {
        // On vérifie si l'utilisateur existe dans le repository
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new EntityNotFoundException("User receiver not found with id " + receiverId));

        // Recherche des posts reçus par l'utilisateur
        return postRepository.findByReceiverId(receiverId);  // Appel au repository avec l'ID du récepteur
    }

}
