package com.korgutlova.diplom.controller;

import java.io.IOException;
import org.kohsuke.github.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    @GetMapping("/test")
    public ResponseEntity<String> testGitHub() throws IOException {
        GitHub github = new GitHubBuilder()
                .withOAuthToken("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")
                .build();
        GHRepository repository = github.createRepository("new_test_project").create();
        repository.setPrivate(true);
        repository.setDescription("New test project");
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/test/commit")
    public ResponseEntity<String> testCommit() throws IOException {
        GitHub github = new GitHubBuilder()
                .withOAuthToken("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")
                .build();
        //not working
        String username = github.getMyself().getLogin();
        GHRepository repo = github.getRepository( username + "/new_test_project");
        String sha = repo.createTree().create().getSha();
//        String commitSha = repo.createCommit()
//                .message(username + " updates")
//                .create()
//                .getSHA1();
        GHRef masterRef = repo.createRef("master", sha);
//        String masterTreeSha = repo.getTreeRecursive("master", 1).getSha();
//        GHTreeBuilder treeBuilder = repo.createTree().baseTree(masterTreeSha);
//        treeBuilder.add("src/file/GitHubController.java", "public class GitHubController {}", true);
//        String treeSha = treeBuilder.create().getSha();
//        String commitSha = repo.createCommit()
//                .message(username + " updates")
//                .tree(treeSha)
//                .parent(masterRef.getObject().getSha())
//                .create()
//                .getSHA1();
//        masterRef.updateTo(commitSha);
        return ResponseEntity.ok("ok");
    }
}
