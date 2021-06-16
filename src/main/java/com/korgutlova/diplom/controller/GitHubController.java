package com.korgutlova.diplom.controller;

import com.korgutlova.diplom.model.entity.Simulation;
import com.korgutlova.diplom.model.entity.User;
import com.korgutlova.diplom.model.entity.tasktracker.TaskInSimulation;
import com.korgutlova.diplom.model.enums.task.TaskStatus;
import com.korgutlova.diplom.service.api.GitHubService;
import com.korgutlova.diplom.service.api.SimulationService;
import com.korgutlova.diplom.service.api.TaskService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRef;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTreeBuilder;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/github")
@RequiredArgsConstructor
public class GitHubController {

    @Value("${github.oauth}")
    private String oauth;


    private final GitHubService gitHubService;
    private final SimulationService simulationService;
    private final TaskService taskService;

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


    @PostMapping(value = "/add_file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addFile(@RequestParam Long projectId,
                                          @RequestPart MultipartFile file) {
        gitHubService.addFileForRepository(projectId, file);
        return ResponseEntity.ok("Loaded");
    }

    @PostMapping("/init_repo")
    public ResponseEntity<String> initRepository(@RequestParam String login) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Simulation simulation = simulationService.findInitSimulation(currentUser);

        if (simulation == null || login == null || login.isEmpty()) {
            return ResponseEntity.badRequest().body("Repo was not initialize");
        }

        gitHubService.initRepository(simulation, login);
        return ResponseEntity.ok("Repo initialized");
    }

    @PostMapping("/check_repository")
    public ResponseEntity<String> checkRepository(@RequestParam Long taskId) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Simulation simulation = simulationService.findInitSimulation(currentUser);
        TaskInSimulation task = taskService.findById(taskId);

        if (simulation == null || taskId == null || task == null || !task.getIsViewed()
                || task.getStatus() == TaskStatus.DONE || task.getStatus() == TaskStatus.CANCELLED ) {
            return ResponseEntity.badRequest().body("Check is cancelled");
        }

        gitHubService.checkRepository(simulation, task);
        return ResponseEntity.ok("Loaded");
    }
}
