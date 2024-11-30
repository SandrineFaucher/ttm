package com.simplon.ttm.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simplon.ttm.models.Post;


public interface PostRepository extends JpaRepository <Post, Long> {

    List<Post> findBySenderId (Long senderId);

    List<Post> findByReceiverId(Long receiverId);

}
