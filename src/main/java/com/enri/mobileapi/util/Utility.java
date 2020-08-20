package com.enri.mobileapi.util;

import java.util.HashMap;

public class Utility {
	
	public static HashMap<String, Object> createResponseObj () {
		HashMap<String, Object> response = new HashMap<String, Object>();
		response.put("success", Constant.SUCCESS);
		response.put("error", Constant.NO_ERROR);
		response.put("result", null);
		return response;
	}

}
