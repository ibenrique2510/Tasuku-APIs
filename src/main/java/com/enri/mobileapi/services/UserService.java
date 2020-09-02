package com.enri.mobileapi.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import com.enri.mobileapi.models.Group;
import com.enri.mobileapi.models.Notification;
import com.enri.mobileapi.models.User;
import com.enri.mobileapi.repositories.GroupRepository;
import com.enri.mobileapi.repositories.NotificationRepository;
import com.enri.mobileapi.repositories.UserRepository;
import com.enri.mobileapi.util.Constant;
import com.enri.mobileapi.util.Utility;
import com.google.gson.Gson;

@Service
public class UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	NotificationRepository notificationRepository;

	@Autowired
	GroupRepository groupRepository;

	// get all users
	// GetMapping("/getUsers")
	public HashMap<String, Object> getUsers() {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User criteriaUser = new User();
			criteriaUser.setRole(Constant.USER);
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> list = userRepository.findAll(criteria);
			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("result", list);
			}
		} catch (Exception e) {

			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// get all users
	// GetMapping("/getAllUsers")
	public HashMap<String, Object> getAllUsers() {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User criteriaUser = new User();
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> list = userRepository.findAll(criteria);
			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("result", list);
			}
		} catch (Exception e) {

			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// get all users but self
	// GetMapping("/getAllUsersButSelf/{id}")
	public HashMap<String, Object> getAllUsersButSelf(Long id) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User criteriaUser = new User();
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> list = userRepository.findAll(criteria);
			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				List<User> resultList = new ArrayList<User>();
				for (User user : list) {
					if (user.getId() != id && !user.getRole().equals(Constant.ADMIN)) {
						resultList.add(user);
					}
				}
				response.replace("result", resultList);
			}
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// get all managers
	// GetMapping("/getManagers)
	public HashMap<String, Object> getAllManagers() {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User criteriaUser = new User();
			criteriaUser.setRole(Constant.MANAGER);
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> list = userRepository.findAll(criteria);
			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_MANAGER);
			} else {
				response.replace("result", list);
			}
		} catch (Exception e) {

			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_MANAGER);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// get all admins
	// GetMapping("/getAdmins")
	public HashMap<String, Object> getAllAdmins() {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User criteriaUser = new User();
			criteriaUser.setRole(Constant.ADMIN);
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> list = userRepository.findAll(criteria);
			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_ADMIN);
			} else {
				response.replace("result", list);
			}
		} catch (Exception e) {

			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_ADMIN);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// get all assignees for listing
	// GetMapping("/getAssignees")
	public HashMap<String, Object> getAssignees(User caller) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			caller = userRepository.findById(caller.getId()).get();
			
			User criteriaUser = new User();
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> list = userRepository.findAll(criteria);

			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_USER);
			}

			if (caller.getRole().equals(Constant.ADMIN)) {
				List<User> resultList = new ArrayList<User>();
				for (User user : list) {
					if (!user.getRole().equals(Constant.ADMIN)) {
						resultList.add(user);
					}
				}
				response.replace("result", resultList);
			} else if (caller.getRole().equals(Constant.MANAGER)) {
				List<User> resultList = new ArrayList<User>();
				for (User user : list) {
					if (!user.getRole().equals(Constant.ADMIN) && user.getGroupId() == caller.getGroupId()) {
						resultList.add(user);
					}
				}
				response.replace("result", resultList);
			} else {
				response.replace("result", null);
			}
		} catch (Exception e) {

			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_ADMIN);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// get a user by id
	// GetMapping("/getUser/{id}")
	public HashMap<String, Object> getUserById(long id) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User user = null;
			user = userRepository.findById(id).get();
			if (user != null) {
				response.replace("result", user);
				if (user.getRole().equals(Constant.USER)) {
					Group group = null;
					group = groupRepository.findById(user.getGroupId()).get();
					if (group != null) {
						response.put("inGroup", group.getName());
					}
				} else if (user.getRole().equals(Constant.MANAGER)) {
					Group groupCriteria = new Group();
					groupCriteria.setManagerId(id);
					Example<Group> criteria = Example.of(groupCriteria);
					Group group = null;
					group = groupRepository.findOne(criteria).get();
					if (group != null) {
						response.put("manageGroup", group.getName());
					}
				}
			}
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// get all users
	// GetMapping("/getUsers")
	public HashMap<String, Object> getUsersByGroupIdAdmin(Long groupId) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User criteriaUser = new User();
			criteriaUser.setGroupId(groupId);
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> list = userRepository.findAll(criteria);
			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("result", list);
			}
		} catch (Exception e) {

			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// get all users
	// GetMapping("/getUsers")
	public HashMap<String, Object> getUsersByGroupIdManager(Long userId) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User manager = userRepository.findById(userId).get();

			User criteriaUser = new User();
			criteriaUser.setGroupId(manager.getGroupId());
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			criteriaUser.setRole(Constant.USER);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> list = userRepository.findAll(criteria);
			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("result", list);
			}
		} catch (Exception e) {

			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}
	
	// get all users in group
	// GetMapping("/getUsersByGroupIdAsManager")
	public HashMap<String, Object> getUsersByGroupIdAsManager(Long managerId) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
//			Group group = groupRepository.findById(groupId).get();
			
			User manager = userRepository.findById(managerId).get();

			User criteriaUser = new User();
			criteriaUser.setGroupId(manager.getGroupId());
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			criteriaUser.setRole(Constant.USER);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> list = userRepository.findAll(criteria);
			list.add(manager);
			if (list.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("result", list);
			}
		} catch (Exception e) {

			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	public HashMap<String, Object> getNoGroupManagers() {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {

			User criteriaUser = new User();
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			criteriaUser.setRole(Constant.MANAGER);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> listManagers = userRepository.findAll(criteria);

			List<User> listResult = new ArrayList<User>();

			for (User manager : listManagers) {
				if (manager.getGroupId() == null) {
					listResult.add(manager);
				}
			}

			if (listResult.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("result", listResult);
			}
		} catch (Exception e) {

			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	public HashMap<String, Object> getNoGroupUsers() {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {

			User criteriaUser = new User();
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			criteriaUser.setRole(Constant.USER);
			Example<User> criteria = Example.of(criteriaUser);
			List<User> listUsers = userRepository.findAll(criteria);

			List<User> listResult = new ArrayList<User>();

			for (User user : listUsers) {
				if (user.getGroupId() == null) {
					listResult.add(user);
				}
			}

			if (listResult.size() <= 0) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("result", listResult);
			}
		} catch (Exception e) {

			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// delete a user
	// DeleteMapping("/deleteUser/{id}")
	public HashMap<String, Object> deleteUser(long id) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User user = userRepository.findById(id).get();
			user.setIsDeleted(Constant.IS_DELETED);
			response.replace("result", userRepository.save(user));
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER_TO_OPERATE_ACTION);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// login
	// PostMapping("/login")
	public HashMap<String, Object> login(User userDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		System.out.println("====> " + userDetails.getPushToken());
		try {
			User criteriaUser = new User();
			criteriaUser.setEmail(userDetails.getEmail());
			criteriaUser.setPassword(userDetails.getPassword());
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<User> criteria = Example.of(criteriaUser);
			User user = userRepository.findOne(criteria).get();
			if (user.getPushToken() == null || !user.getPushToken().equals(userDetails.getPushToken())) {
				user.setPushToken(userDetails.getPushToken());
				userRepository.save(user);
			}
			response.replace("result", user);
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.WRONG_EMAIL_PASSWORD);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// add a user
	// PostMapping("/addUser/{id}")
	public HashMap<String, Object> addUser(long createdBy, User userDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			// check if userName is already existed or not
			User criteriaUser = new User();
			criteriaUser.setEmail(userDetails.getEmail());
			Example<User> criteria = Example.of(criteriaUser);
			List<User> userList = userRepository.findAll(criteria);
			if (userList.size() > 0) {
				// email is already existed
				response.replace("error", Constant.EMAIL_ALREADY_EXISTED);
			} else {
				userDetails.setIsDeleted(Constant.IS_NOT_DELETED);
				userDetails.setCreatedBy(createdBy);
				userDetails.setCreatedAt(Utility.getSystemCurrentMilli());
				response.put("result", userRepository.save(userDetails));
			}
		} catch (Exception e) {
			response.replace("success", Constant.FAILED);
			response.replace("error", Constant.SERVER_ERROR);
			response.put("message", e.getMessage());
		}
		return response;
	}

	// update a user
	// PutMapping("/updateUser/{id}")
	public HashMap<String, Object> updateUser(long id, User userDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			User user = userRepository.findById(id).get();
			user.setName(userDetails.getName());
			if (userDetails.getPassword() != null) {
				user.setPassword(userDetails.getPassword());
			}
			response.put("result", userRepository.save(user));
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_USER_TO_OPERATE_ACTION);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

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

	public String pushNotifications(String titleMsg, String bodyMsg, HashMap<Long, String> pushTokens) {
		for (HashMap.Entry<Long, String> entry : pushTokens.entrySet()) {
			Notification noti = new Notification();
			noti.setUserId(entry.getKey());
			noti.setDetails(titleMsg + " - " + bodyMsg);
			noti.setIsRead(Constant.IS_NOT_DELETED);
			noti.setCreatedAt(Utility.getSystemCurrentMilli());
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

			// String pushToTokens = null;
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
		return "Done calling Push Notifcations";
	}

	public HashMap<String, Object> getAllNotiByUserId(Long id) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Notification criteriaNoti = new Notification();
			criteriaNoti.setUserId(id);
			Example<Notification> criteria = Example.of(criteriaNoti);
			List<Notification> list = notificationRepository.findAll(criteria);
			Comparator<Notification> comparator = new Comparator<Notification>() {
				@Override
				public int compare(Notification o1, Notification o2) {
					return Integer.parseInt(o2.getCreatedAt() - o1.getCreatedAt() + "");
				}
			};
			list.sort(comparator);
			if (list.size() > 0) {
				response.replace("result", list);
			}
		} catch (Exception e) {
			response.replace("success", Constant.FAILED);
			response.replace("error", Constant.SERVER_ERROR);
			response.put("message", e.getMessage());
		}
		return response;
	}

	public Notification setNotificationIsRead(Long id) {
		Notification noti = notificationRepository.findById(id).get();
		noti.setIsRead(Constant.IS_READ);
		return notificationRepository.save(noti);
	}
}

//Notification criteriaNoti = new Notification();
//criteriaNoti.setUserId(id);
//ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("").withIgnoreNullValues();
//Example<Notification> criteria = Example.of(criteriaNoti, matcher);
