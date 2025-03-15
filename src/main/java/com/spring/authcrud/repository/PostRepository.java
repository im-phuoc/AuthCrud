package com.spring.authcrud.repository;

import com.spring.authcrud.models.Post;
import com.spring.authcrud.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByUser(User user);
}
