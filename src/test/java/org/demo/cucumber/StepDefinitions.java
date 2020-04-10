package org.demo.cucumber;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.log4j.Log4j2;

import org.demo.cucumber.entity.Post;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class StepDefinitions {

    @LocalServerPort
    private int port;
    private RestTemplate restTemplate = new RestTemplate();
    private String postUrl = "http://localhost";

    private String postId = "";
    private String post_content = "";

    @Given("I hit API for creating new post")
    public void i_create_a_new_post() {
        String url = postUrl + ":" + port + "/post";
        List<Post> allPost = restTemplate.getForObject(url, List.class);
        
        assertTrue(!allPost.isEmpty());
    }

    @When("^I send a post to be created with post id (.*), title (.*) and content (.*)$")
    public void i_sending_post(String post_id, String title, String content) {
        String url = postUrl + ":" + port + "/post";
        Post newPost = new Post();
        newPost.setId(post_id);
        newPost.setTitle(title);
        newPost.setContent(content);
        Post post = restTemplate.postForObject(url, newPost, Post.class);
        postId = post.getId();
        post_content = post.getContent();
       
        assertNotNull(post);
    }

    @Then("I should be able to see my newly created post")
    public void i_should_see_my_newly_created_post() {
        String url = postUrl + ":" + port + "/post/" + postId;
        Post myPost = restTemplate.getForObject(url, Post.class);
       
        assertEquals(postId, myPost.getId());
        assertEquals(post_content, myPost.getContent());
        assertNotNull(myPost);
    }
}
