package com.demo.task.service;

import java.util.List;

import com.demo.task.model.Role;

public interface RoleService {
    Role createRole(Role role);

    List<Role> findAll();
}
