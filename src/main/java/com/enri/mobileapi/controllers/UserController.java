package com.enri.mobileapi.controllers;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.enri.mobileapi.models.Notification;
import com.enri.mobileapi.models.User;
import com.enri.mobileapi.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	UserService userService;

	// Get users
	@GetMapping("/getUsers/{list}")
	public HashMap<String, Object> getUsers() {
		return userService.getUsers();
	}
	
	// Get all users
	@GetMapping("/getAllUsers")
	public HashMap<String, Object> getAllUsers() {
		return userService.getAllUsers();
	}
	
	// Get all users but self
	@GetMapping("/getAllUsersButSelf/{id}")
	public HashMap<String, Object> getAllUsersButSelf(@PathVariable(value = "id") long id) {
		return userService.getAllUsersButSelf(id);
	}

	// Get all managers
	@GetMapping("/getManagers")
	public HashMap<String, Object> getAllManagers() {
		return userService.getAllManagers();
	}
	
	// Get all admins
	@GetMapping("/getAdmins")
	public HashMap<String, Object> getAllAdmins() {
		return userService.getAllAdmins();
	}

	// Get user by id
	@GetMapping("/getUser/{id}")
	public HashMap<String, Object> getUserById(@PathVariable(value = "id") long id) {
		return userService.getUserById(id);
	}
	
	// Get user by group id
	@GetMapping("/getUsersByGroupIdAdmin/{id}")
	public HashMap<String, Object> getUsersByGroupIdAdmin(@PathVariable(value = "id") long id) {
		return userService.getUsersByGroupIdAdmin(id);
	}
	
	// Get user by group id
	@GetMapping("/getUsersByGroupIdManager/{id}")
	public HashMap<String, Object> getUsersByGroupIdManager(@PathVariable(value = "id") long id) {
		return userService.getUsersByGroupIdManager(id);
	}
	
	// Get user by group id
	@GetMapping("/getUsersByGroupIdAsManager/{id}")
	public HashMap<String, Object> getUsersByGroupIdAsManager(@PathVariable(value = "id") long id) {
		return userService.getUsersByGroupIdAsManager(id);
	}
	
	// Get managers that are not managing any group
	@GetMapping("/getNoGroupManagers")
	public HashMap<String, Object> getNoGroupManagers() {
		return userService.getNoGroupManagers();
	}
	
	// Get users that are not in any group
	@GetMapping("/getNoGroupUsers")
	public HashMap<String, Object> getNoGroupUsers() {
		return userService.getNoGroupUsers();
	}
	
	// Get assignees
	@PostMapping("/getAssignees")
	public HashMap<String, Object> getAssignees(@RequestBody User caller) {
		return userService.getAssignees(caller);
	}

	// Delete a user
	@DeleteMapping("/deleteUser/{id}")
	public HashMap<String, Object> deleteUser(@PathVariable(value = "id") long id) {
		return userService.deleteUser(id);
	}

	// Login
	@PostMapping("/login")
	public HashMap<String, Object> login(@RequestBody User userDetails) {
		return userService.login(userDetails);
	}

	// Add a user
	@PostMapping("/addUser/{id}")
	public HashMap<String, Object> addUser(@PathVariable(value = "id") long createdBy, @RequestBody User userDetails) {
		return userService.addUser(createdBy, userDetails);
	}

	// Update a user
	@PutMapping("/updateUser/{id}")
	public HashMap<String, Object> updateUser(@PathVariable(value = "id") long id, @RequestBody User userDetails) {
		return userService.updateUser(id, userDetails);
	}
	
	@GetMapping("/getNotifications/{id}")
	public HashMap<String, Object> pushNoti(@PathVariable(value = "id") long id) {
		return userService.getAllNotiByUserId(id);
	}
	
	@PutMapping("/readNotification/{id}")
	public Notification readNotification(@PathVariable(value = "id") long id) {
		return userService.setNotificationIsRead(id);
	}
}
