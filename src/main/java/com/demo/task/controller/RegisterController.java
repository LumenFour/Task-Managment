package com.demo.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.demo.task.model.User;
import com.demo.task.service.UserService;

import jakarta.validation.Valid;

@Controller
public class RegisterController {

	private final UserService userService;

	public RegisterController(final UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/register")
	public String showRegisterForm(final Model model) {
		model.addAttribute("user", new User());
		return "forms/register";
	}

	@PostMapping("/register")
	public String registerUser(@Valid final User user, final BindingResult bindingResult, final Model model) {
		if (bindingResult.hasErrors()) {
			return "forms/register";
		}

		if (userService.isUserEmailPresent(user.getEmail())) {
			model.addAttribute("exist", true);
			return "register";
		}

		userService.createUser(user);
		return "views/success";
	}

}
