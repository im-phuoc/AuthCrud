package com.spring.authcrud.controllers;

import com.spring.authcrud.payload.request.PostRequest;
import com.spring.authcrud.payload.response.ApiResponse;
import com.spring.authcrud.payload.response.PagedResponse;
import com.spring.authcrud.payload.response.PostResponse;
import com.spring.authcrud.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private static PostService postService;
    public PostController(PostService postService) {
        PostController.postService = postService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createPost(@RequestBody PostRequest postRequest) {
        return ResponseEntity.ok().body(new ApiResponse<>(true,postService.createPost(postRequest),null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<PostResponse>>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                                                @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok().body(new ApiResponse<>(true,"success",postService.getAllPosts(page,size)));
    }

    @GetMapping("/{author}")
    public ResponseEntity<ApiResponse<PostResponse>> getPostByAuthor(@PathVariable String author) {
        PostResponse postResponse = postService.getPostByAuthor(author);
        return ResponseEntity.ok().body(new ApiResponse<>(true,"success",postResponse));
    }
}
