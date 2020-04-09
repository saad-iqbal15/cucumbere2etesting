package org.demo.cucumber.controller;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import org.demo.cucumber.entity.Post;
import org.demo.cucumber.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @GetMapping
    @HystrixCommand(groupKey = "get all post", fallbackMethod = "getAllPostFallback")
    public List<Post> getAllPost() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public Post getPostById(@PathVariable(value = "id")String id) {
        return postService.findById(id);
    }

    @PostMapping
    public Post savePost(@RequestBody Post post) {
        return postService.save(post);
    }

    public List<Post> getAllPostFallback() {
        //return Collections.emptyList();
        return Arrays.asList(new Post("ID:123456", "Empty title", "Empty content"));
    }
}
