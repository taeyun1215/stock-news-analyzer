package com.example.demo.example;

import net.gpedro.integrations.slack.SlackApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

	private final SlackApi slackApi;

	// GET all posts
	@GetMapping
	public List<PostResponse> getAllPosts() {
		return postService.getAllPosts();
	}

	// GET post by ID
	@GetMapping("/{id}")
	public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
		return postService.getPostById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	// GET post by title (query parameter)
	@GetMapping("/search")
	public List<PostResponse> getPostsByTitle(@RequestParam String title) {
		return postService.getPostsByTitle(title);
	}

	// POST create a new post
	@PostMapping
	public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
		return new ResponseEntity<>(postService.createPost(postRequest), HttpStatus.CREATED);
	}

	// DELETE post by ID
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletePost(@PathVariable Long id) {
		if (postService.deletePost(id)) {
			return ResponseEntity.noContent().build();
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
	}
}
