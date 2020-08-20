package com.enri.mobileapi.controllers;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enri.mobileapi.models.Group;
import com.enri.mobileapi.services.GroupService;

@RestController
@RequestMapping("/groups")
public class GroupController {

	@Autowired
	GroupService groupService;

	// get all groups
	@GetMapping("/getGroups")
	public HashMap<String, Object> getAllGroups() {
		return groupService.getAllGroups();
	}

	// get group by id
	@GetMapping("/getGroup/{id}")
	public HashMap<String, Object> getGroupById(@PathVariable(value = "id") long id) {
		return groupService.getGroupById(id);
	}
	
	// create a new group
	@PostMapping("/addGroup/{id}")
	public HashMap<String, Object> addGroup(@PathVariable(value = "id") long createdBy, @RequestBody Group groupDetails) {
		return groupService.addGroup(createdBy, groupDetails);
	}
	
	// update a group
	@PutMapping("/updateGroup/{id}")
	public HashMap<String, Object> updateGroup(@PathVariable(value = "id") long updaterId, @RequestBody Group groupDetails) {
		return groupService.updateGroup(updaterId, groupDetails);
	}
} 
