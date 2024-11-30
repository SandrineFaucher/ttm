package com.simplon.ttm.services;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;
import static org.mockito.Mockito.any;
import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.simplon.ttm.models.Post;
import com.simplon.ttm.models.User;

import com.simplon.ttm.repositories.PostRepository;
import com.simplon.ttm.repositories.UserRepository;
import com.simplon.ttm.services.impl.PostServiceImpl;

public class PostsTests {

    @ExtendWith(MockitoExtension.class)

    @Mock
    PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostServiceImpl postServiceImpl;

    /**
     * teste la méthode de création d'un post
     */
    @Test
    void createPost(){

        // given
        Long senderId = 2L;
        Long receiverId = 3L;
        User userSender = User.builder().id(senderId).username("Sandrine").build();
                User userReceiver = User.builder().id(receiverId).username("Juan").build();

                Post post1 = Post.builder().sender(userSender).receiver(userReceiver).content("content message").build();

                // when
                when(userRepository.findById(senderId)).thenReturn(Optional.of(userSender));
                when(userRepository.findById(receiverId)).thenReturn(Optional.of(userReceiver));
                when(postRepository.save(any(Post.class))).thenReturn(post1);

                // then
                Post createdPost = postServiceImpl.createPost(userSender.getId(), userReceiver.getId(), "content message");

            assertNotNull(createdPost);
            assertEquals("content message", createdPost.getContent());
            assertEquals(userSender, createdPost.getSender());
            assertEquals(userReceiver, createdPost.getReceiver());
    }

    @Test
    void getPostsBySender (){
        // given
        Long senderId = 2L;
        Long receiverId = 3L;
        User userSender = User.builder().id(senderId).username("Sandrine").build();
        User userReceiver = User.builder().id(receiverId).username("Juan").build();

        Post post1 = Post.builder().id(5L).sender(userSender).receiver(userReceiver).content("content message").build();
        Post post2 = Post.builder().id(6L).sender(userSender).receiver(userReceiver).content("hello").build();

        // when
        when(userRepository.findById(senderId)).thenReturn(Optional.of(userSender));
        when(postRepository.findBySenderId(senderId)).thenReturn(List.of(post1, post2));

        // then
        List<Post> foundPosts = postServiceImpl.getPostsBySender(senderId);

        assertEquals(2, foundPosts.size());

        // Vérifie les propriétés du premier post
        assertEquals("content message", foundPosts.get(0).getContent());
        assertEquals("Sandrine", foundPosts.get(0).getSender().getUsername());
        assertEquals(5L, foundPosts.get(0).getId());

        // Vérifie les propriétés du deuxième post
        assertEquals("hello", foundPosts.get(1).getContent());
        assertEquals("Sandrine", foundPosts.get(1).getSender().getUsername());
        assertEquals(6L, foundPosts.get(1).getId());
    }

    @Test
    void getPostsByReceiver(){
        // given
        Long senderId = 2L;
        Long receiverId = 3L;
        User userSender = User.builder().id(senderId).username("Sandrine").build();
        User userReceiver = User.builder().id(receiverId).username("Juan").build();

        Post post1 = Post.builder().id(5L).sender(userSender).receiver(userReceiver).content("content message").build();
        Post post2 = Post.builder().id(6L).sender(userSender).receiver(userReceiver).content("hello").build();

        // when
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(userReceiver));
        when(postRepository.findByReceiverId(receiverId)).thenReturn(List.of(post1, post2));

        // then
        List<Post> foundPosts = postServiceImpl.getPostsByReceiver(receiverId);  // Passage de receiverId à la méthode du service

        // Vérifie la taille de la liste
        assertEquals(2, foundPosts.size());

        // Vérifie les propriétés du premier post
        assertEquals("content message", foundPosts.get(0).getContent());
        assertEquals("Juan", foundPosts.get(0).getReceiver().getUsername());
        assertEquals(5L, foundPosts.get(0).getId());

        // Vérifie les propriétés du deuxième post
        assertEquals("hello", foundPosts.get(1).getContent());
        assertEquals("Juan", foundPosts.get(1).getReceiver().getUsername());
        assertEquals(6L, foundPosts.get(1).getId());

    }
}
