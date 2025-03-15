package com.spring.authcrud.services.impl;

import com.spring.authcrud.models.Post;
import com.spring.authcrud.models.User;
import com.spring.authcrud.payload.request.PostRequest;
import com.spring.authcrud.payload.response.PagedResponse;
import com.spring.authcrud.payload.response.PostResponse;
import com.spring.authcrud.repository.PostRepository;
import com.spring.authcrud.repository.UserRepository;
import com.spring.authcrud.services.PostService;
import com.spring.authcrud.services.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public String createPost(PostRequest postRequest) {
        User user = userRepository.findByUsername(userService.getInfo().getUsername()).orElseThrow(
                () -> new RuntimeException("User not found")
        );
        Post post = new Post(postRequest.getTitle(),postRequest.getDescription(),postRequest.getContent(),user,postRequest.isPublished());
        postRepository.save(post);
        return "Post created";
    }

    @Override
    public PagedResponse<PostResponse> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> posts = postRepository.findAll(pageable);
        Set<PostResponse> postResponses = posts.getContent().stream().map(
                post -> new PostResponse(post.getTitle(),post.getDescription(),post.getContent(),post.getUser().getUsername())
        ).collect(Collectors.toSet());
        return new PagedResponse<>(postResponses,posts.getNumber(),posts.getSize(), posts.getTotalPages(),posts.getTotalElements(),posts.isLast());

    }

    @Override
    public PostResponse getPostByAuthor(String author) {
        User user = userRepository.findByUsername(author).orElseThrow(
                () -> new RuntimeException("Author not found")
        );
        Post post = postRepository.findByUser(user).orElseThrow(
                () -> new RuntimeException("Author do not have any post")
        );
        return new PostResponse(post.getTitle(),post.getDescription(),post.getContent(),post.getUser().getUsername());

    }
}
