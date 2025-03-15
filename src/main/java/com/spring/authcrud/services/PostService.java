package com.spring.authcrud.services;

import com.spring.authcrud.payload.request.PostRequest;
import com.spring.authcrud.payload.response.PagedResponse;
import com.spring.authcrud.payload.response.PostResponse;

public interface PostService {
    String createPost(PostRequest postRequest);
    PagedResponse<PostResponse> getAllPosts(int page, int size);
    PostResponse getPostByAuthor(String author);
}
