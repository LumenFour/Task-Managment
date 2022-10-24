package com.demo.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.demo.task.model.Task;
import com.demo.task.model.User;
import com.demo.task.service.TaskService;
import com.demo.task.service.UserService;

@Controller
public class AssigmentController {
    private final UserService userService;
    private final TaskService taskService;

    public AssigmentController(final UserService userService, final TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/assignment")
    public String showAssigmentForm(final Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("freeTasks", taskService.findFreeTasks());
        return "forms/assignment";
    }

    @GetMapping("/assignment/{userId}")
    public String showUserAssigmentForm(@PathVariable final Long userId, final Model model) {
        model.addAttribute("selectedUser", userService.getUserById(userId));
        model.addAttribute("users", userService.findAll());
        model.addAttribute("freeTasks", taskService.findFreeTasks());
        return "forms/assignment";
    }

    @GetMapping("/assignment/assign/{userId}/{taskId}")
    public String assignTaskToUser(@PathVariable final Long userId, @PathVariable final Long taskId) {
        final Task selectedTask = taskService.getTaskById(taskId);
        final User selectedUser = userService.getUserById(userId);
        taskService.assignTaskToUser(selectedTask, selectedUser);
        return "redirect:/assignment/" + userId;
    }

    @GetMapping("assignment/unassign/{userId}/{taskId}")
    public String unassignTaskFromUser(@PathVariable final Long userId, @PathVariable final Long taskId) {
        final Task selectedTask = taskService.getTaskById(taskId);
        taskService.unassignTask(selectedTask);
        return "redirect:/assignment/" + userId;
    }

}



