package com.demo.task.controller;

import java.security.Principal;

import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.demo.task.model.Task;
import com.demo.task.model.User;
import com.demo.task.service.TaskService;
import com.demo.task.service.UserService;

import jakarta.validation.Valid;

@Controller
public class TaskController {

    private final TaskService taskService;
    private final UserService userService;

    public TaskController(final TaskService taskService, final UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    @GetMapping("/tasks")
    public String listTasks(final Model model, final Principal principal, final SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", false);
        return "views/tasks";
    }

    @GetMapping("/tasks/in-progress")
    public String listTasksInProgress(final Model model, final Principal principal, final SecurityContextHolderAwareRequestWrapper request) {
        prepareTasksListModel(model, principal, request);
        model.addAttribute("onlyInProgress", true);
        return "views/tasks";
    }

    private void prepareTasksListModel(final Model model, final Principal principal, final SecurityContextHolderAwareRequestWrapper request) {
        final String email = principal.getName();
        final User signedUser = userService.getUserByEmail(email);
        final boolean isAdminSigned = request.isUserInRole("ROLE_ADMIN");

        model.addAttribute("tasks", taskService.findAll());
        model.addAttribute("users", userService.findAll());
        model.addAttribute("signedUser", signedUser);
        model.addAttribute("isAdminSigned", isAdminSigned);

    }

    @GetMapping("/task/create")
    public String showEmptyTaskForm(final Model model, final Principal principal, final SecurityContextHolderAwareRequestWrapper request) {
        final String email = principal.getName();
        final User user = userService.getUserByEmail(email);

        final Task task = new Task();
        task.setCreatorName(user.getName());
        if (request.isUserInRole("ROLE_USER")) {
            task.setOwner(user);
        }
        model.addAttribute("task", task);
        return "forms/task-new";
    }

    @PostMapping("/task/create")
    public String createTask(@Valid final Task task, final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "forms/task-new";
        }
        taskService.createTask(task);

        return "redirect:/tasks";
    }

    @GetMapping("/task/edit/{id}")
    public String showFilledTaskForm(@PathVariable final Long id, final Model model) {
        model.addAttribute("task", taskService.getTaskById(id));
        return "forms/task-edit";
    }
    
    @PostMapping("/task/edit/{id}")
    public String updateTask(@Valid final Task task, final BindingResult bindingResult, @PathVariable final Long id, final Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/task-edit";
        }
        taskService.updateTask(id, task);
        return "redirect:/tasks";
    }
    
    @GetMapping("/task/delete/{id}")
    public String deleteTask(@PathVariable final Long id) {
        taskService.deleteTask(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/mark-done/{id}")
    public String setTaskCompleted(@PathVariable final Long id) {
        taskService.setTaskCompleted(id);
        return "redirect:/tasks";
    }

    @GetMapping("/task/unmark-done/{id}")
    public String setTaskNotCompleted(@PathVariable final Long id) {
        taskService.setTaskNotCompleted(id);
        return "redirect:/tasks";
    }

}
