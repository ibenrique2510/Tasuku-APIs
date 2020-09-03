package com.enri.mobileapi.services;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.enri.mobileapi.models.Group;
import com.enri.mobileapi.models.Notification;
import com.enri.mobileapi.models.Task;
import com.enri.mobileapi.models.User;
import com.enri.mobileapi.repositories.GroupRepository;
import com.enri.mobileapi.repositories.NotificationRepository;
import com.enri.mobileapi.repositories.TaskRepository;
import com.enri.mobileapi.repositories.UserRepository;
import com.enri.mobileapi.util.Constant;
import com.enri.mobileapi.util.Utility;
import com.google.gson.Gson;

@Service
public class TaskService {

	@Autowired
	TaskRepository taskRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	NotificationRepository notificationRepository;

	// get all tasks
	// GetMapping("/getTasks")
	public HashMap<String, Object> getAllTasks(Long callerId) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User caller = userRepository.findById(callerId).get();
//			System.out.println("==========> " + caller.getRole());
//			Task criteriaTask = new Task();
//			criteriaTask.setIsDeleted(Constant.IS_NOT_DELETED);
//			Example<Task> criteria = Example.of(criteriaTask);
			List<Task> listResult = new ArrayList<Task>();
			
			Task taskCriteria = new Task();
			taskCriteria.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<Task> taskCrit = Example.of(taskCriteria);
			List<Task> listTasks = taskRepository.findAll(taskCrit);
			
			if (caller.getRole().equals(Constant.USER)) {
				for (Task task : listTasks) {
					if (task.getAssigneeId() == caller.getId()) {
						listResult.add(task);
					}
				}
			}
			
			if (caller.getRole().equals(Constant.MANAGER)) {
				User userCriteria = new User();
				userCriteria.setIsDeleted(Constant.IS_NOT_DELETED);
				userCriteria.setGroupId(caller.getGroupId());
				Example<User> userCrit = Example.of(userCriteria);
				List<User> listUsers = userRepository.findAll(userCrit);
				
				for (Task task : listTasks) {
					for (User user : listUsers) {
						if (user.getRole().equals(Constant.USER)) {
							if (task.getAssigneeId() == user.getId()) {
								listResult.add(task);
							}
						}
						if (user.getRole().equals(Constant.MANAGER)) {
							if (task.getAssigneeId() == user.getId() || task.getAssignerId() == user.getId() || task.getApproverId() == user.getId()) {
								if (!listResult.contains(task)) { listResult.add(task); }
							}
						}
					}
				}
			}
			
			if (caller.getRole().equals(Constant.ADMIN)) {
				listResult = listTasks;
			}

			for (Task task : listResult) {
				task.setAssignee(userRepository.findById(task.getAssigneeId()).get().getName());
			}
			if (listResult.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_TASK);
			} else {
				response.replace("result", listResult);
			}
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_TASK);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	// get all assigned tasks
	// GetMapping("/getTask/{id}")
	public HashMap<String, Object> getTask(long taskId) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Task task = taskRepository.findById(taskId).get();
			if (task.getAssigneeId() != null) {
				task.setAssignee(userRepository.findById(task.getAssigneeId()).get().getName());
			}
			if (task.getAssignerId() != null) {
				task.setAssigner(userRepository.findById(task.getAssignerId()).get().getName());
			}
			if (task.getApproverId() != null) {
				task.setApprover(userRepository.findById(task.getApproverId()).get().getName());
			}
			if (task.getCommenterId() != null) {
				task.setCommenter(userRepository.findById(task.getCommenterId()).get().getName());
			}
			if (task.getOldTask() != null) {
				task.setOldTaskName(taskRepository.findById(task.getOldTask()).get().getName());
			}
			response.replace("result", task);
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_TASK);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	// get all assigned tasks
	// GetMapping("/assignedTasks/{id}")
	public HashMap<String, Object> getAssignedTasks(long assigneeId) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Task criteriaTask = new Task();
			criteriaTask.setAssigneeId(assigneeId);
			criteriaTask.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<Task> criteria = Example.of(criteriaTask);
			List<Task> list = taskRepository.findAll(criteria);

			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_TASK);
			} else {
				response.replace("result", list);
			}
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_TASK);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	// get old tasks
	// GetMapping("/getOldTasks/{id}")
	public HashMap<String, Object> getOldTasks(long callerId) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User caller = userRepository.findById(callerId).get(); 
			
			Task criteriaTask = new Task();
			
			criteriaTask.setStatus(Constant.CANNOT_FINISH_CONFIRMED);
			criteriaTask.setIsDeleted(Constant.IS_NOT_DELETED);
			
			if (caller.getRole().equals(Constant.USER)) {
				criteriaTask.setAssigneeId(caller.getId());
			} 
			Example<Task> criteria = Example.of(criteriaTask);
			List<Task> list = taskRepository.findAll(criteria);
			List<Task> listOldTasks = new ArrayList<Task>();

			
			if (caller.getRole().equals(Constant.MANAGER)) {
				User criteriaUser = new User();
				criteriaUser.setGroupId(caller.getGroupId());
				criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
				criteriaUser.setRole(Constant.USER);
				Example<User> userCrit = Example.of(criteriaUser);
				List<User> listUsers = userRepository.findAll(userCrit);
				listUsers.add(caller);
				
				for (Task task : list) {
					for (User user : listUsers) {
						if (task.getAssigneeId() == user.getId()) {
							listOldTasks.add(task);
						}
					}
				}
			} else {
				listOldTasks = list;
			}
			if (listOldTasks.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_TASK);
			} else {
				response.replace("result", listOldTasks);
			}
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_TASK);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// get all approve tasks
	// GetMapping("/getApproveTasks/{id}")
	public HashMap<String, Object> getApproveTasks(long assignerId) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Task criteriaTask = new Task();
			criteriaTask.setAssignerId(assignerId);
			criteriaTask.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<Task> criteria = Example.of(criteriaTask);
			List<Task> list = taskRepository.findAll(criteria);
			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_TASK);
			} else {
				response.replace("result", list);
			}
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_TASK);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// delete task
	// DeleteMapping("/deleteTask/{id}")
	public HashMap<String, Object> deleteTask(long updaterId, Task taskDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Task task = taskRepository.findById(taskDetails.getId()).get();
			if (!task.getStatus().equals(Constant.NOT_STARTED_YET)) {
				response.put("result", Constant.CANNOT_UPDATE_TASK_NOW);
			} else {
				task.setIsDeleted(Constant.IS_DELETED);
				task.setUpdaterId(updaterId);
				task.setUpdatedAt(Utility.getSystemCurrentMilli());
				response.put("result", taskRepository.save(task));

				ArrayList<Long> relators = new ArrayList<Long>();
				User updater = userRepository.findById(updaterId).get();
				if (task.getAssigneeId() != null) {
					relators.add(task.getAssigneeId());
				}
				if (task.getAssignerId() != null && task.getAssignerId() != updaterId) {
					relators.add(task.getAssignerId());
				}
				String titleMsg = "Task deleted";
				String bodyMsg = task.getName() + " has been deleted by " + updater.getRole() + " " + updater.getName();
				HashMap<Long, String> pushToTokens = getPushTokensByIds(relators);
				pushNotifications(titleMsg, bodyMsg, pushToTokens);
			}
		} catch (Exception e) {
			response.replace("success", Constant.FAILED);
			response.replace("error", Constant.SERVER_ERROR);
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// update accept status of task
	// PutMapping("/updateTaskAcceptStatus/{id}")
	public HashMap<String, Object> updateTaskAcceptStatus(long updaterId, Task taskDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Task task = taskRepository.findById(taskDetails.getId()).get();
			if (!task.getStatus().equals(Constant.WAITING_FOR_ACCEPT)) {
				response.put("result", Constant.CANNOT_UPDATE_TASK_NOW);
			} else {

				ArrayList<Long> relators = new ArrayList<Long>();
				User updater = userRepository.findById(updaterId).get();
				HashMap<Long, String> pushToTokens = null;
				String titleMsg = "Task updated";
				String bodyMsg = null;

				if (taskDetails.getStatus() != null) {
					task.setApprovedAt(Utility.getSystemCurrentMilli());
					task.setApproverId(updaterId);

					String acceptStatus = taskDetails.getStatus();
					if (acceptStatus.equals(Constant.ACCEPTED)) {
						task.setStatus(Constant.ON_GOING);
						bodyMsg = updater.getRole() + " " + updater.getName() + " has accepted task: " + task.getName();
					} else if (acceptStatus.equals(Constant.DECLINED)) {
						task.setStatus(Constant.DECLINED);
						bodyMsg = updater.getRole() + " " + updater.getName() + " has declined your task: "
								+ task.getName();
					}

					if (task.getAssignerId() != updaterId) {
						relators.add(task.getAssignerId());
					}
					if (task.getAssigneeId() != updaterId) {
						relators.add(task.getAssigneeId());
					}
				}
				task.setUpdaterId(updaterId);
				task.setUpdatedAt(Utility.getSystemCurrentMilli());
				response.put("result", taskRepository.save(task));

				pushToTokens = getPushTokensByIds(relators);
				pushNotifications(titleMsg, bodyMsg, pushToTokens);
			}
		} catch (Exception e) {
			response.replace("success", Constant.FAILED);
			response.replace("error", Constant.SERVER_ERROR);
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// commit task
	// PutMapping("/commitTask/{id}")
	public HashMap<String, Object> commitTask(long updaterId, Task taskDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Task task = taskRepository.findById(taskDetails.getId()).get();
			if (!task.getStatus().equals(Constant.NOT_STARTED_YET) && !task.getStatus().equals(Constant.ON_GOING)) {
				response.put("result", Constant.CANNOT_UPDATE_TASK_NOW);
			} else {

				ArrayList<Long> relators = new ArrayList<Long>();
				User updater = userRepository.findById(updaterId).get();
				HashMap<Long, String> pushToTokens = null;
				String titleMsg = "Task updated";
				String bodyMsg = updater.getRole() + " " + updater.getName() + " has commited task: " + task.getName()
						+ "\nWaiting for your confirmation";

				relators.add(task.getApproverId());
				Group group = groupRepository.findById(updater.getGroupId()).get();
				if (group != null) {
					if (task.getApproverId() != group.getManagerId()) {
						relators.add(group.getManagerId());
					}
				}

				if (taskDetails.getHandlingContent() != null) {
					task.setHandlingContent(taskDetails.getHandlingContent());
				}
				task.setStatus(Constant.COMMITED);
				if (taskDetails.getImage() != null) {
					task.setImage(taskDetails.getImage());
				}

				task.setUpdaterId(updaterId);
				task.setCommittedAt(Utility.getSystemCurrentMilli());
				task.setUpdatedAt(Utility.getSystemCurrentMilli());
				response.put("result", taskRepository.save(task));

				pushToTokens = getPushTokensByIds(relators);
				pushNotifications(titleMsg, bodyMsg, pushToTokens);
			}
		} catch (Exception e) {
			response.replace("success", Constant.FAILED);
			response.replace("error", Constant.SERVER_ERROR);
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// evaluate task
	// PutMapping("/evaluateTask/{id}")
	public HashMap<String, Object> evaluateTask(long updaterId, Task taskDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Task task = taskRepository.findById(taskDetails.getId()).get();
			if (!task.getStatus().equals(Constant.OVERDUE)
					&& !task.getStatus().equals(Constant.COMMITED)) {
				response.put("result", Constant.CANNOT_UPDATE_TASK_NOW);
			} else {

				ArrayList<Long> relators = new ArrayList<Long>();
				User updater = userRepository.findById(updaterId).get();
				HashMap<Long, String> pushToTokens = null;
				String titleMsg = "Task updated";
				String bodyMsg = updater.getRole() + " " + updater.getName() + " has confirmed task: " + task.getName();

				relators.add(task.getAssigneeId());
				
				
				User user = userRepository.findById(task.getAssigneeId()).get();
				Group group = null;
				if (user != null) {
					group = groupRepository.findById(user.getGroupId()).get();
				}
				if (group != null) {
					if (group.getManagerId() != updaterId) {
						relators.add(group.getManagerId());
					}
				}

				if (taskDetails.getComment() != null) {
					task.setComment(taskDetails.getComment());
				}
				if (taskDetails.getStatus() != null) {
					task.setStatus(taskDetails.getStatus());
				}
				if (taskDetails.getRating() != null) {
					task.setRating(taskDetails.getRating());
				}

				task.setCommenterId(updaterId);
				task.setEvaluatedAt(Utility.getSystemCurrentMilli());
				task.setUpdaterId(updaterId);
				task.setUpdatedAt(Utility.getSystemCurrentMilli());
				response.put("result", taskRepository.save(task));

				pushToTokens = getPushTokensByIds(relators);
				pushNotifications(titleMsg, bodyMsg, pushToTokens);
			}
		} catch (Exception e) {
			response.replace("success", Constant.FAILED);
			response.replace("error", Constant.SERVER_ERROR);
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// update task
	// PutMapping("/updateTask/{id}")
	public HashMap<String, Object> updateTask(long updaterId, Task taskDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Task task = taskRepository.findById(taskDetails.getId()).get();
			if (!task.getStatus().equals(Constant.NOT_STARTED_YET)) {
				response.put("result", Constant.CANNOT_UPDATE_TASK_NOW);
			} else {

				ArrayList<Long> relators = new ArrayList<Long>();
				User updater = userRepository.findById(updaterId).get();
				HashMap<Long, String> pushToTokens = null;
				String titleMsg = "Task updated";
				String bodyMsg = updater.getRole() + " " + updater.getName() + " has updated task " + task.getName();

				if (taskDetails.getName() != null) {
					task.setName(taskDetails.getName());
				}
				if (taskDetails.getStartAt() != null) {
					task.setStartAt(taskDetails.getStartAt());
					if (task.getStartAt() <= Utility.getSystemCurrentMilli()) {
						task.setStatus(Constant.ON_GOING);
					}
				}
				if (taskDetails.getEndAt() != null) {
					task.setEndAt(taskDetails.getEndAt());
				}
				if (taskDetails.getAssignedContent() != null) {
					task.setAssignedContent(taskDetails.getAssignedContent());
				}
				if (taskDetails.getAssigneeId() != null) {
					task.setAssigneeId(taskDetails.getAssigneeId());
				}
				if (taskDetails.getOldTask() != null) {
					task.setOldTask(taskDetails.getOldTask());
				}

				if (task.getApproverId() != null) {
					if (task.getApproverId() != updaterId) {
						relators.add(task.getApproverId());
					}
				}
				if (task.getAssigneeId() != null) {
					if (task.getAssigneeId() != updaterId) {
						relators.add(task.getAssigneeId());
					}
				}
				if (updater.getGroupId() != null) {
					Group group = groupRepository.findById(updater.getGroupId()).get();
					if (group != null) {
						if (group.getManagerId() != updaterId) {
							relators.add(group.getManagerId());
						}
					}
				}
				
				
				task.setUpdaterId(updaterId);
				task.setUpdatedAt(Utility.getSystemCurrentMilli());
				response.put("result", taskRepository.save(task));

				pushToTokens = getPushTokensByIds(relators);
				pushNotifications(titleMsg, bodyMsg, pushToTokens);
			}
		} catch (Exception e) {
			response.replace("success", Constant.FAILED);
			response.replace("error", Constant.SERVER_ERROR);
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// add task
	// PostMapping("/
	/{id}")
	public HashMap<String, Object> addTask(long creatorId, Task taskDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User creator = userRepository.findById(creatorId).get();
			ArrayList<Long> relators = new ArrayList<Long>();
			HashMap<Long, String> pushToTokens = null;
			String titleMsg = null;
			String bodyMsg = null;
			if (creator.getRole().equals(Constant.USER)) {
				taskDetails.setStatus(Constant.WAITING_FOR_ACCEPT);
				taskDetails.setAssigneeId(creatorId);
				Group group = groupRepository.findById(creator.getGroupId()).get();
				relators.add(group.getManagerId());
				pushToTokens = getPushTokensByIds(relators);
				titleMsg = "A Task is waiting for your approval";
				bodyMsg = "User " + creator.getName() + " has created a task and waiting for your approval";
			}
			if (creator.getRole().equals(Constant.MANAGER) || creator.getRole().equals(Constant.ADMIN)) {
// 				if (taskDetails.getStartAt() > Utility.getSystemCurrentMilli()) {
// 					taskDetails.setStatus(Constant.NOT_STARTED_YET);
// 				} else {
// 					taskDetails.setStatus(Constant.ON_GOING);
// 				}
				taskDetails.setStatus(Constant.ON_GOING);
				taskDetails.setAssigneeId(taskDetails.getAssigneeId());
				taskDetails.setApprovedAt(Utility.getSystemCurrentMilli());
				relators.add(taskDetails.getAssigneeId());
				pushToTokens = getPushTokensByIds(relators);
				titleMsg = "You has been assigned a task";
				bodyMsg = creator.getRole() + " " + creator.getName() + " has created a task for you \n" + "Details: "
						+ taskDetails.getAssignedContent();
			}

			taskDetails.setAssignerId(creatorId);
			taskDetails.setIsDeleted(Constant.IS_NOT_DELETED);
			taskDetails.setCreatorId(creatorId);
			taskDetails.setCreatedAt(Utility.getSystemCurrentMilli());
			taskDetails.setUpdaterId(creatorId);
			taskDetails.setUpdatedAt(Utility.getSystemCurrentMilli());
			response.put("result", taskRepository.save(taskDetails));
			pushNotifications(titleMsg, bodyMsg, pushToTokens);
		} catch (Exception e) {
			response.replace("success", Constant.FAILED);
			response.replace("error", Constant.SERVER_ERROR);
			response.put("message", e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

	// ---------------------------------------------------------------------------------------------------------
	// Handling push notifications and store them into db

	public HashMap<Long, String> getPushTokensByIds(ArrayList<Long> listId) {
		HashMap<Long, String> result = new HashMap<Long, String>();
		try {
			for (Long id : listId) {
				result.put(id, userRepository.findById(id).get().getPushToken());
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("===> Error at getPushTokensByIds");
		}
		return result;
	}

	public void pushNotifications(String titleMsg, String bodyMsg, HashMap<Long, String> pushTokens) {
		for (HashMap.Entry<Long, String> entry : pushTokens.entrySet()) {
			Notification noti = new Notification();
			noti.setUserId(entry.getKey());
			noti.setDetails(titleMsg + "\n" + bodyMsg);
			noti.setCreatedAt(Utility.getSystemCurrentMilli());
			noti.setIsRead(Constant.IS_NOT_DELETED);
			notificationRepository.save(noti);
		}

		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost request = new HttpPost(Constant.EXPO_PUSH_NOTI_URL);

			HashMap<String, Object> body = new HashMap<String, Object>();

			body.put("title", titleMsg);
			body.put("body", bodyMsg);
			body.put("sound", "default");
//			body.put("data", "{\"name\":\"Enri\"}");
			// body.put("subtitle", "This is Subtitle message");
			// body.put("badge", "1"); // this indicates the number of notification number
			// on your application icon

			ArrayList<String> pushToTokens = new ArrayList<String>();
			for (HashMap.Entry<Long, String> entry : pushTokens.entrySet()) {
//				if (!pushToTokens.contains(entry.getValue())) {
				pushToTokens.add(entry.getValue());
//				}
			}
			body.put("to", pushToTokens);
			System.out.println(pushToTokens);
			StringEntity bodyJson = new StringEntity(new Gson().toJson(body));

			// headers specified by Expo to request push notifications
			request.setHeader(HttpHeaders.HOST, "exp.host");
			request.setHeader(HttpHeaders.ACCEPT, "application/json");
			request.setHeader(HttpHeaders.ACCEPT_ENCODING, "gzip, deflate");
			request.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
			request.setEntity(bodyJson);

			HttpResponse response = httpClient.execute(request);
			// handle response here...
			System.out.println(response.getStatusLine());

		} catch (Exception ex) {
			System.out.println("===> Error at Push Notification API");
			ex.printStackTrace();
		}
	}
	
	// ---------------------------------------------------------------------------------------------------------
	// Automatically update status of tasks and push notifcation to related users
	public void runScheduledTasks() {
		try {
			Thread scheduledTasks = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						autoApprove();
						autoUpdateStatus();
						autoRemindDeadline();
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			});
//			// calculating time left until the first run at 1 AM after calling
//			// runScheduledTasks() for the first time
			ZonedDateTime current = ZonedDateTime.now(ZoneId.of(Constant.TIME_ZONE));
			ZonedDateTime nextRun = current.withHour(Constant.HOUR_TO_RUN_SCHEDULED_TASKS)
					.withMinute(Constant.MINUTE_TO_RUN_SCHEDULED_TASKS)
					.withSecond(Constant.SECOND_TO_RUN_SCHEDULED_TASKS);
			if (current.compareTo(nextRun) > 0) {
				nextRun = nextRun.plusDays(1);
			}
			long delay = Duration.between(current, nextRun).getSeconds();
			// creates a thread pool that can schedule commands to run
			// after a given delay, or to execute periodically.
			ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

			// scheduledTasks: thread will be executed periodically
			// delay: time to delay the first execution
			// TimeUnit.DAYS.toSeconds(1): time between successive executions
			// TimeUnit.SECONDS: unit of time for the 2 above parameters
//			scheduler.scheduleAtFixedRate(scheduledTasks, delay, TimeUnit.HOURS.toSeconds(1), TimeUnit.SECONDS);
			scheduler.scheduleAtFixedRate(scheduledTasks, 0, TimeUnit.MINUTES.toSeconds(2), TimeUnit.SECONDS);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	public void autoApprove() {
		try {
			Task taskCriteria = new Task();
			taskCriteria.setIsDeleted(Constant.IS_NOT_DELETED);
			taskCriteria.setStatus(Constant.WAITING_FOR_ACCEPT);
			Example<Task> criteria = Example.of(taskCriteria);
			List<Task> list = taskRepository.findAll(criteria);
			
			
			ArrayList<Long> relators = new ArrayList<Long>();
			HashMap<Long, String> pushToTokens = null;
			String titleMsg = "Task Declined";
			String bodyMsg = "Your task was declined by the System due to overtime";
			
			for (Task task : list) {
				if (task.getStartAt() <= Utility.getSystemCurrentMilli()) {
					task.setStatus(Constant.DECLINED);
					task.setApproverId(Constant.SYSTEM_ID);
					task.setApprovedAt(Utility.getSystemCurrentMilli());
					task.setUpdatedAt(Utility.getSystemCurrentMilli());
					task.setUpdaterId(Constant.SYSTEM_ID);
					taskRepository.save(task);
					relators.add(task.getAssigneeId());
				}
			}
			
			pushToTokens = getPushTokensByIds(relators);
			pushNotifications(titleMsg, bodyMsg, pushToTokens);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void autoUpdateStatus() {
		try {
			Task taskCriteria = new Task();
			taskCriteria.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<Task> criteria = Example.of(taskCriteria);
			List<Task> list = taskRepository.findAll(criteria);
			
			
			ArrayList<Long> relators = new ArrayList<Long>();
			HashMap<Long, String> pushToTokens = null;
			String titleMsg = "Task Status Updated";
			String bodyMsg = "Your task has been updated by the System";
			
			for (Task task : list) {
				System.out.println("========> start at: " + task.getStartAt());
				System.out.println("========> current time of system: " + Utility.getSystemCurrentMilli());
				if (task.getStatus().equals(Constant.ON_GOING) || task.getStatus().equals(Constant.NOT_STARTED_YET)) {
					if (task.getStatus().contentEquals(Constant.NOT_STARTED_YET)) {
						if (task.getStartAt() <= Utility.getSystemCurrentMilli()) {
							task.setStatus(Constant.ON_GOING);
							
							task.setUpdatedAt(Utility.getSystemCurrentMilli());
							task.setUpdaterId(Constant.SYSTEM_ID);
							taskRepository.save(task);
							relators.add(task.getAssigneeId());
						}
					}
					if (task.getStatus().equals(Constant.ON_GOING)) {
						if (task.getEndAt() <= Utility.getSystemCurrentMilli()) {
							task.setStatus(Constant.OVERDUE);
							
							task.setUpdatedAt(Utility.getSystemCurrentMilli());
							task.setUpdaterId(Constant.SYSTEM_ID);
							taskRepository.save(task);
							relators.add(task.getAssigneeId());
						}
					}
				}
			}
			
			pushToTokens = getPushTokensByIds(relators);
			pushNotifications(titleMsg, bodyMsg, pushToTokens);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void autoRemindDeadline() {
		try {
			Task taskCriteria = new Task();
			taskCriteria.setIsDeleted(Constant.IS_NOT_DELETED);
			taskCriteria.setStatus(Constant.ON_GOING);
			Example<Task> criteria = Example.of(taskCriteria);
			List<Task> list = taskRepository.findAll(criteria);
			
			
			ArrayList<Long> relators = new ArrayList<Long>();
			HashMap<Long, String> pushToTokens = null;
			String titleMsg = "Task Reminder";
			String bodyMsg = null;
			
			for (Task task : list) {
				long currentDateTime = Utility.getSystemCurrentMilli();
				long interval = task.getEndAt() - currentDateTime;
				int hoursLeft = (int) interval / Constant.MILLI_TO_HOUR;
				
				if (hoursLeft <= 24) {
					relators.add(task.getAssigneeId());
					bodyMsg = "You have " + hoursLeft + "h left to finish task " + task.getName();
					pushToTokens = getPushTokensByIds(relators);
					pushNotifications(titleMsg, bodyMsg, pushToTokens);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
