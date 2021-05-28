package com.korgutlova.diplom.controller;

import java.io.IOException;
import org.kohsuke.github.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/github")
public class GitHubController {

    @Value("${github.oauth}")
    private String oauth;

    @PostMapping("/create")
    public ResponseEntity<String> testGitHub() throws IOException {
        GitHub github = new GitHubBuilder()
                .withOAuthToken(oauth)
                .build();
        GHRepository repository = github.createRepository("new_test_project_1").create();
        repository.setPrivate(true);
        repository.setDescription("New test project");

        //добавить в коллабораторы самого пользователь + email разраба (TeamLead)
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/commit")
    public ResponseEntity<String> testCommit() throws IOException {
        GitHub github = new GitHubBuilder()
                .withOAuthToken(oauth)
                .build();

        String username = github.getMyself().getLogin();
        GHRepository repo = github.getRepository( username + "/new_test_project_1");

//        repo.getWorkflow(4) - получить github actions
//        repo.getWorkflowJob(3).getStatus() - получить статус по github actions (только как узнать этот id ?)

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
