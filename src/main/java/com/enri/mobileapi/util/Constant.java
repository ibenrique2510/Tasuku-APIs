package com.enri.mobileapi.util;

public class Constant {
	// boolean values
	public static final boolean IS_NOT_DELETED = false;
	public static final boolean IS_DELETED = true;
	public static final boolean IS_NOT_READ = false;
	public static final boolean IS_READ = true;
	
	// role values
	public static final String USER = "User";
	public static final String MANAGER = "Manager";
	public static final String ADMIN = "Admin";
	
	
	// status values
	public static final String WAITING_FOR_ACCEPT = "0";
	public static final String ACCEPTED = "1";
	public static final String DECLINED = "2";
	public static final String NOT_STARTED_YET = "3";
	public static final String ON_GOING = "4";
	public static final String OVERDUE = "5";
	public static final String COMMITED = "6";
	public static final String FINISHED_CONFIRMED = "7";
	public static final String CANNOT_FINISH_CONFIRMED = "8";
	
	// error messages
	public static final String NO_VALUE_PRESENT = "No value present";
	
	//response values
	public static final int CODE_200 = 200;
	public static final int CODE_400 = 400;
	public static final int CODE_404 = 404;
	public static final int CODE_500 = 500;
	public static final String NO_ERROR = "No Error";
	public static final boolean SUCCESS = true;
	public static final boolean FAILED = false;
	
	public static final String SERVER_ERROR = "Server error";
	public static final String FOUND_NO_USER = "Found no user";
	public static final String FOUND_NO_MANAGER = "Found no manager";
	public static final String FOUND_NO_ADMIN = "Found no admin";
	public static final String FOUND_NO_TASK = "Found no task";
	public static final String FOUND_NO_USER_TO_OPERATE_ACTION = "Found no user to operate action";
	public static final String WRONG_EMAIL_PASSWORD = "Wrong email or password";
	public static final String EMAIL_ALREADY_EXISTED = "Email is already existed";
	public static final String CANNOT_UPDATE_TASK_NOW = "Cannot update task right now";
	
	public static final String FOUND_NO_GROUP = "Found no group";
	public static final String FOUND_NO_GROUP_TO_OPERATE_ACTION = "Found no group to operate action";
	
	public static final String EXPO_PUSH_NOTI_URL = "https://exp.host/--/api/v2/push/send";
	
	
	public static final Long SYSTEM_ID = Long.parseLong("0");
	public static final int MILLI_TO_HOUR = 3600000;
	public static final String TIME_ZONE = "Asia/Ho_Chi_Minh";
	public static final long TIME_TO_LIVE_IN_MILLISECOND = 1800000; // == 30 mins
	public static final int HOUR_TO_RUN_SCHEDULED_TASKS = 23;
	public static final int MINUTE_TO_RUN_SCHEDULED_TASKS = 55;
	public static final int SECOND_TO_RUN_SCHEDULED_TASKS = 55;
}
