package com.demo.task.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.demo.task.model.Role;
import com.demo.task.model.User;
import com.demo.task.repository.RoleRepository;
import com.demo.task.repository.TaskRepository;
import com.demo.task.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
	private static final String ADMIN="ADMIN";
	private static final String USER="USER";

	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	public UserServiceImpl(UserRepository userRepository,
			TaskRepository taskRepository,
			RoleRepository roleRepository,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public User createUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		Role userRole = roleRepository.findByRole(USER);
		user.setRoles(new ArrayList<>(Collections.singletonList(userRole)));
		return userRepository.save(user);
	}

	@Override
	public User changeRoleToAdmin(User user) {
		Role adminRole = roleRepository.findByRole(ADMIN);
		user.setRoles(new ArrayList<>(Collections.singletonList(adminRole)));
		return userRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User getUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public boolean isUserEmailPresent(String email) {
		return userRepository.findByEmail(email) != null;
	}

	@Override
	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public void deleteUser(Long id) {
		User user = userRepository.getReferenceById(id);
		user.getTasksOwned().forEach(task -> task.setOwner(null));
		userRepository.delete(user);
	}

}

