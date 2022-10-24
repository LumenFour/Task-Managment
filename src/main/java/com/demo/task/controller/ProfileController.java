package com.demo.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.demo.task.model.User;
import com.demo.task.service.TaskService;
import com.demo.task.service.UserService;

import java.security.Principal;

@Controller
public class ProfileController {

    private final UserService userService;
    private final TaskService taskService;

    public ProfileController(final UserService userService, final TaskService taskService) {
        this.userService = userService;
        this.taskService = taskService;
    }

    @GetMapping("/profile")
    public String showProfilePage(final Model model, final Principal principal) {
        final String email = principal.getName();
        final User user = userService.getUserByEmail(email);
        model.addAttribute("user", user);
        model.addAttribute("tasksOwned", taskService.findByOwnerOrderByDateDesc(user));
        return "views/profile";
    }

    @GetMapping("/profile/mark-done/{taskId}")
    public String setTaskCompleted(@PathVariable final Long taskId) {
        taskService.setTaskCompleted(taskId);
        return "redirect:/profile";
    }

    @GetMapping("/profile/unmark-done/{taskId}")
    public String setTaskNotCompleted(@PathVariable final Long taskId) {
        taskService.setTaskNotCompleted(taskId);
        return "redirect:/profile";
    }

}
