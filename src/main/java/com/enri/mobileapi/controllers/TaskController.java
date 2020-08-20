package com.enri.mobileapi.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.enri.mobileapi.models.Task;
import com.enri.mobileapi.services.TaskService;

@RestController
@RequestMapping("/tasks")
public class TaskController {
	
	@Autowired
	TaskService taskService;
	
	// get all assigned tasks
	@GetMapping("/getTask/{id}")
	public HashMap<String, Object> getTask(@PathVariable(value = "id")Long id) {
		return taskService.getTask(id);
	}
	
	// get all assigned tasks
	@GetMapping("/getTasks/{id}")
	public HashMap<String, Object> getAssignedTasks(@PathVariable(value = "id")Long id) {
		return taskService.getAllTasks(id);
	}
	
	// get all assigned tasks
	@GetMapping("/getAssignedTasks/{id}")
	public HashMap<String, Object> getAssignedTasks(@PathVariable(value = "id") long assigneeId) {
		return taskService.getAssignedTasks(assigneeId);
	}
	
	// get old tasks
	// GetMapping("/getOldTasks/{id}")
	@GetMapping("/getOldTasks/{id}")
	public HashMap<String, Object> getOldTasks(@PathVariable(value = "id") long callerId) {
		return taskService.getOldTasks(callerId);
	}
	
	// get all approve tasks
	@GetMapping("/getApproveTasks/{id}")
	public HashMap<String, Object> getApproveTasks(@PathVariable(value = "id") long assignerId) {
		return taskService.getApproveTasks(assignerId);
	}
	
	// delete task
	@DeleteMapping("/deleteTask/{id}")
	public HashMap<String, Object> deleteTask(@PathVariable(value = "id") long updaterId, @RequestBody Task taskDetails) {
		return taskService.deleteTask(updaterId, taskDetails);
	}
	
	// update task accept status task
	@PutMapping("/updateTaskAcceptStatus/{id}")
	public HashMap<String, Object> updateTaskAcceptStatus(@PathVariable(value = "id") long updaterId, @RequestBody Task taskDetails) {
		return taskService.updateTaskAcceptStatus(updaterId, taskDetails);
	}	
	
	// commit task
	@PutMapping("/commitTask/{id}")
	public HashMap<String, Object> commitTask(@PathVariable(value = "id") long updaterId, @RequestBody Task taskDetails) {
		return taskService.commitTask(updaterId, taskDetails);
	}
	
	// commit task
	@PutMapping("/evaluateTask/{id}")
	public HashMap<String, Object> evaluateTask(@PathVariable(value = "id") long updaterId, @RequestBody Task taskDetails) {
		return taskService.evaluateTask(updaterId, taskDetails);
	}
	
	// add task
	@PostMapping("/addTask/{id}")
	public HashMap<String, Object> addTask(@PathVariable(value = "id") long creatorId, @RequestBody Task taskDetails) {
		return taskService.addTask(creatorId, taskDetails);
	}
	
	// update task
	@PutMapping("/updateTask/{id}")
	public HashMap<String, Object> updateTask(@PathVariable(value = "id") long updaterId, @RequestBody Task taskDetails) {
		return taskService.updateTask(updaterId, taskDetails);
	}
	
	// activate auto scheduler
	@GetMapping("/auto")
	public void activateAutoThread() {
		taskService.runScheduledTasks();
	}
}
