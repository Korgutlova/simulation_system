package com.korgutlova.diplom.controller;

import java.io.IOException;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRef;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTreeBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
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
        GHRepository repository = github.createRepository("new_test_project_1").create();
        repository.setPrivate(true);
        repository.setDescription("New test project");

        //добавить в коллабораторы самого пользака + email разраба который может курировать
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/test/commit")
    public ResponseEntity<String> testCommit() throws IOException {
        GitHub github = new GitHubBuilder()
                .withOAuthToken("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")
                .build();

        String username = github.getMyself().getLogin();
        GHRepository repo = github.getRepository( username + "/new_test_project_1");

        //ВОТ РЕШЕНИЕ (первой инициализации репы)
//        repo.createContent()
//                .branch("master")
//                .content("some content")
//                .path("test/.gitignore")
//                .message("init repo")
//                .commit();

        //получение refs
        GHRef mainRef = repo.getRef("heads/master");

        //sha
        String mainTreeSha = repo.getTreeRecursive("master", 1).getSha();

        //само дерево с изменениями
        GHTreeBuilder treeBuilder = repo.createTree().baseTree(mainTreeSha);

        //добавляем файлы
        treeBuilder.add("test1/App.java", "public class GitHubController \n{\n}", true);
        treeBuilder.add("resources1/app.properties", "some.settings=456\n#", false);

        //новое sha tree
        String treeSha = treeBuilder.create().getSha();

        //сам коммит
        GHCommit commit = repo.createCommit()
                .message("Initialize project 'someName'")
                .tree(treeSha)
                .parent(mainRef.getObject().getSha())
                .create();

        String commitSha = commit.getSHA1();

        //обновление в репозитории
        mainRef.updateTo(commitSha);

        return ResponseEntity.ok("ok");
    }
}
