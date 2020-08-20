package com.enri.mobileapi.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.enri.mobileapi.models.User;
import com.enri.mobileapi.repositories.GroupRepository;
import com.enri.mobileapi.repositories.NotificationRepository;
import com.enri.mobileapi.repositories.UserRepository;
import com.enri.mobileapi.util.Constant;
import com.enri.mobileapi.util.Utility;
import com.google.gson.Gson;

@Service
public class GroupService {

	@Autowired
	GroupRepository groupRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	NotificationRepository notificationRepository;

	// get all groups
	// GetMapping("/getGroups")
	public HashMap<String, Object> getAllGroups() {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Group groupCriteria = new Group();
			groupCriteria.setIsDeleted(Constant.IS_NOT_DELETED);
			Example<Group> criteria = Example.of(groupCriteria);
			List<Group> list = groupRepository.findAll(criteria);
			if (list.size() <= 0) {
				response.put("error", Constant.FOUND_NO_GROUP);
			} else {
				for (Group group : list) {
					group.setManagerName(userRepository.findById(group.getManagerId()).get().getName());
				}
				response.put("result", list);
			}
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_GROUP);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// get group by id
	// GetMapping("/getGroup/{id}")
	public HashMap<String, Object> getGroupById(long id) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			Group group = groupRepository.findById(id).get();
			
			User userCriteria = new User();
			userCriteria.setRole(Constant.USER);
			userCriteria.setIsDeleted(Constant.IS_NOT_DELETED);
			userCriteria.setGroupId(group.getId());
			
			Example<User> criteria = Example.of(userCriteria);
			List<User> listUsers = userRepository.findAll(criteria);
			group.setUsersInGroup(listUsers);
			group.setManagerName(userRepository.findById(group.getManagerId()).get().getName());
			
			response.replace("result", group);
		} catch (Exception e) {
			if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
				response.replace("error", Constant.FOUND_NO_GROUP);
			} else {
				response.replace("success", Constant.FAILED);
				response.replace("error", Constant.SERVER_ERROR);
			}
			response.put("message", e.getMessage());
		}
		return response;
	}

	// create a group
	// PostMapping("/addGroup/{id}")
	public HashMap<String, Object> addGroup(long createdBy, Group groupDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			
			ArrayList<Long> relators = new ArrayList<Long>();
			User updater = userRepository.findById(createdBy).get();
			HashMap<Long, String> pushToTokens = null;
			String titleMsg = "Added To Group";
			String bodyMsg = null;
			
			groupDetails.setIsDeleted(Constant.IS_NOT_DELETED);
			groupDetails.setCreatedAt(System.currentTimeMillis());
			groupDetails.setCreatedBy(createdBy);
			Group addedGroup = groupRepository.save(groupDetails);
			response.replace("result", addedGroup);

			bodyMsg = "You has been added to " + addedGroup.getName() + " by " + updater.getRole() + " " + updater.getName();
			relators.add(addedGroup.getManagerId());
			
			User manager = userRepository.findById(addedGroup.getManagerId()).get();
			manager.setGroupId(addedGroup.getId());
			for (Long userId : groupDetails.getUsers()) {
				User user = userRepository.findById(userId).get();
				user.setGroupId(addedGroup.getId());
				relators.add(userId);
			}
			
			pushToTokens = getPushTokensByIds(relators);
			pushNotifications(titleMsg, bodyMsg, pushToTokens);
		} catch (Exception e) {
			response.replace("success", Constant.FAILED);
			response.replace("error", Constant.SERVER_ERROR);
			response.put("message", e.getMessage());
		}
		return response;
	}

	// update a group
	// PutMapping("/updateGroup/{id}")
	public HashMap<String, Object> updateGroup(Long updaterId, Group groupDetails) {
		HashMap<String, Object> response = Utility.createResponseObj();
		try {
			ArrayList<Long> relators = new ArrayList<Long>();
			User updater = userRepository.findById(updaterId).get();
			HashMap<Long, String> pushToTokens = null;
			String titleMsg = "Group updated";
			String bodyMsg = updater.getRole() + " " + updater.getName() + " has updated your group info";
			
			Group group = null;
			group = groupRepository.findById(groupDetails.getId()).get();
			if (group != null) {
				if (groupDetails.getName() != null) {
					group.setName(groupDetails.getName());
				}
				response.replace("result", groupRepository.save(group));
			}
			
			User criteriaUser = new User();
			criteriaUser.setIsDeleted(Constant.IS_NOT_DELETED);
			criteriaUser.setGroupId(groupDetails.getId());
			Example<User> criteria = Example.of(criteriaUser);
			List<User> listUsers = userRepository.findAll(criteria);
			
			for (User user : listUsers) {
				if (user.getId() != updaterId) {
					relators.add(user.getId());
				}
			}
			relators.add(group.getManagerId());
			
			pushToTokens = getPushTokensByIds(relators);
			pushNotifications(titleMsg, bodyMsg, pushToTokens);
		} catch (Exception e) {
			response.replace("success", Constant.FAILED);
			response.replace("error", Constant.SERVER_ERROR);
			response.put("message", e.getMessage());
		}
		return response;
	}

// ---------------------------------------------------------------------------------------------------------
// From this line below are codes that handling push notifications and store
// them into db

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
			noti.setCreatedAt(System.currentTimeMillis());
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
//		body.put("data", "{\"name\":\"Enri\"}");
			// body.put("subtitle", "This is Subtitle message");
			// body.put("badge", "1"); // this indicates the number of notification number
			// on your application icon

			ArrayList<String> pushToTokens = new ArrayList<String>();
			for (HashMap.Entry<Long, String> entry : pushTokens.entrySet()) {
//			if (!pushToTokens.contains(entry.getValue())) {
				pushToTokens.add(entry.getValue());
//			}
			}
			body.put("to", pushToTokens);
			System.out.println(pushToTokens);
			StringEntity bodyJson = new StringEntity(new Gson().toJson(body));
			System.out.println(new Gson().toJson(body));
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

}
/**
 * HashMap<String, Object> response = Utility.createResponseObj(); try { } catch
 * (Exception e) { if (e.getMessage().contains(Constant.NO_VALUE_PRESENT)) {
 * response.replace("error", null); } else { response.replace("success",
 * Constant.FAILED); response.replace("error", Constant.SERVER_ERROR); }
 * response.put("message", e.getMessage()); } return response;
 **/