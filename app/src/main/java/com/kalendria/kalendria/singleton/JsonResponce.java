package com.kalendria.kalendria.singleton;

import java.util.ArrayList;
import java.util.List;

public class JsonResponce {
	private static JsonResponce myBrand_Singleton;
	public List<String> All_Brand_array_list;

	private JsonResponce() {
		All_Brand_array_list = new ArrayList<String>();

	}

	public static JsonResponce getInstance() {
		if (myBrand_Singleton == null) {
			myBrand_Singleton = new JsonResponce();
		}

		return myBrand_Singleton;
	}

	public List<String> getParamList() {
		return All_Brand_array_list;
	}

	public void setParamList(List<String> paramList) {
		this.All_Brand_array_list = paramList;
	}
}