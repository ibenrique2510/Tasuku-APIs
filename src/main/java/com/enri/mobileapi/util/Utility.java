package com.enri.mobileapi.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TimeZone;

public class Utility {

	public static HashMap<String, Object> createResponseObj() {
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("success", Constant.SUCCESS);
		response.put("error", Constant.NO_ERROR);
		response.put("result", null);
		return response;
	}
	
	public static long getSystemCurrentMilli() {
		Calendar calendar = new GregorianCalendar();
		TimeZone timeZone = calendar.getTimeZone();
		calendar.setTimeZone(timeZone);

		timeZone = TimeZone.getTimeZone("Asia/Singapore");
		calendar.setTimeZone(timeZone);

		return calendar.getTimeInMillis();
	}
}
