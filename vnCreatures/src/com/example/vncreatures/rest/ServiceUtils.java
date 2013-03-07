package com.example.vncreatures.rest;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import android.os.Environment;

import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.model.Category;
import com.example.vncreatures.model.CategoryModel;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureGroup;
import com.example.vncreatures.model.CreatureGroupListModel;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.model.NewsItem;
import com.example.vncreatures.model.NewsModel;
import com.example.vncreatures.model.Province;
import com.example.vncreatures.model.ProvinceModel;
import com.example.vncreatures.model.discussion.FacebookUser;
import com.example.vncreatures.model.discussion.FacebookUser.Location;
import com.example.vncreatures.model.discussion.Report;
import com.example.vncreatures.model.discussion.ReportModel;
import com.example.vncreatures.model.discussion.Thread;
import com.example.vncreatures.model.discussion.ThreadModel;
import com.example.vncreatures.model.discussion.User;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ServiceUtils {

	// Parse from JSON
	public static CreatureModel parseCreatureModelFromJSON(String data) {
		if (data == null || data == "")
			return null;

		CreatureModel creatureModel = new CreatureModel();

		if (data.indexOf('{') > -1)
			data = data.substring(data.indexOf('{'));
		if (data.lastIndexOf('}') > -1)
			data = data.substring(0, data.lastIndexOf('}') + 1);

		try {
			JsonElement json = new JsonParser().parse(data);
			JsonObject jsonObject = json.getAsJsonObject();
			JsonArray array = jsonObject.getAsJsonArray("data");
			if (array.isJsonNull())
				return null;
			Iterator<?> iterator = array.iterator();
			while (iterator.hasNext()) {
				JsonElement json2 = (JsonElement) iterator.next();
				Gson gson = new Gson();
				Creature creature = gson.fromJson(json2, Creature.class);
				creatureModel.add(creature);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return creatureModel;
	}

	// Parse GroupModel
	public static CreatureGroupListModel parseGroupModelFromJSON(String data) {
		if (data == null || data == "")
			return null;

		CreatureGroupListModel crGroupListModel = new CreatureGroupListModel();

		if (data.indexOf('{') > -1)
			data = data.substring(data.indexOf('{'));
		if (data.lastIndexOf('}') > -1)
			data = data.substring(0, data.lastIndexOf('}') + 1);

		try {
			JsonElement json = new JsonParser().parse(data);
			JsonObject jsonObject = json.getAsJsonObject();
			JsonArray array = jsonObject.getAsJsonArray("data");
			if (array.isJsonNull())
				return null;
			Iterator<?> iterator = array.iterator();
			while (iterator.hasNext()) {
				JsonElement json2 = (JsonElement) iterator.next();
				Gson gson = new Gson();
				CreatureGroup creatureGroup = gson.fromJson(json2,
						CreatureGroup.class);
				crGroupListModel.add(creatureGroup);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return crGroupListModel;
	}

	// Parse Category Model
	public static CategoryModel parseCategoryModelFromJSON(String data) {
		if (data == null || data == "")
			return null;

		CategoryModel catListModel = new CategoryModel();

		if (data.indexOf('{') > -1)
			data = data.substring(data.indexOf('{'));
		if (data.lastIndexOf('}') > -1)
			data = data.substring(0, data.lastIndexOf('}') + 1);

		try {
			JsonElement json = new JsonParser().parse(data);
			JsonObject jsonObject = json.getAsJsonObject();
			JsonArray array = jsonObject.getAsJsonArray("data");
			if (array.isJsonNull())
				return null;
			Iterator<?> iterator = array.iterator();
			while (iterator.hasNext()) {
				JsonElement json2 = (JsonElement) iterator.next();
				Gson gson = new Gson();
				Category category = gson.fromJson(json2, Category.class);
				catListModel.add(category);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return catListModel;
	}

	// Parse News Model
	public static NewsModel parseNewsModelFromJSON(String data) {
		if (data == null || data == "")
			return null;

		NewsModel newsListModel = new NewsModel();

		if (data.indexOf('{') > -1)
			data = data.substring(data.indexOf('{'));
		if (data.lastIndexOf('}') > -1)
			data = data.substring(0, data.lastIndexOf('}') + 1);

		try {
			JsonElement json = new JsonParser().parse(data);
			JsonObject jsonObject = json.getAsJsonObject();
			JsonArray array = jsonObject.getAsJsonArray("data");
			if (array.isJsonNull())
				return null;
			Iterator<?> iterator = array.iterator();
			while (iterator.hasNext()) {
				JsonElement json2 = (JsonElement) iterator.next();
				Gson gson = new Gson();
				NewsItem newsItem = gson.fromJson(json2, NewsItem.class);
				newsListModel.add(newsItem);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return newsListModel;
	}

	// Parse Province Model
	public static ProvinceModel parseProvinceModelFromJSON(String data) {
		if (data == null || data == "")
			return null;

		ProvinceModel provinceModel = new ProvinceModel();

		if (data.indexOf('{') > -1)
			data = data.substring(data.indexOf('{'));
		if (data.lastIndexOf('}') > -1)
			data = data.substring(0, data.lastIndexOf('}') + 1);

		try {
			JsonElement json = new JsonParser().parse(data);
			JsonObject jsonObject = json.getAsJsonObject();
			JsonArray array = jsonObject.getAsJsonArray("data");
			if (array.isJsonNull())
				return null;
			Iterator<?> iterator = array.iterator();
			while (iterator.hasNext()) {
				JsonElement json2 = (JsonElement) iterator.next();
				Gson gson = new Gson();
				Province province = gson.fromJson(json2, Province.class);
				provinceModel.add(province);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return provinceModel;
	}

	// Parse Thread Model
	public static ThreadModel parseThreadModelFromJSON(String data) {
		if (data == null || data == "")
			return null;

		ThreadModel threadModel = new ThreadModel();

		if (data.indexOf('{') > -1)
			data = data.substring(data.indexOf('{'));
		if (data.lastIndexOf('}') > -1)
			data = data.substring(0, data.lastIndexOf('}') + 1);

		try {
			JsonElement json = new JsonParser().parse(data);
			JsonObject jsonObject = json.getAsJsonObject();
			JsonArray array = jsonObject.getAsJsonArray("data");
			if (array.isJsonNull())
				return null;
			Iterator<?> iterator = array.iterator();
			while (iterator.hasNext()) {
				JsonElement json2 = (JsonElement) iterator.next();
				Gson gson = new Gson();
				Thread thread = gson.fromJson(json2, Thread.class);
				threadModel.add(thread);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return threadModel;
	}

	// Parse Thread Model
	public static ReportModel parseReportModelFromJSON(String data) {
		if (data == null || data == "")
			return null;

		ReportModel reportModel = new ReportModel();

		if (data.indexOf('{') > -1)
			data = data.substring(data.indexOf('{'));
		if (data.lastIndexOf('}') > -1)
			data = data.substring(0, data.lastIndexOf('}') + 1);

		try {
			JsonElement json = new JsonParser().parse(data);
			JsonObject jsonObject = json.getAsJsonObject();
			JsonArray array = jsonObject.getAsJsonArray("data");
			if (array.isJsonNull())
				return null;
			Iterator<?> iterator = array.iterator();
			while (iterator.hasNext()) {
				JsonElement json2 = (JsonElement) iterator.next();
				Gson gson = new Gson();
				Report reportType = gson.fromJson(json2, Report.class);
				reportModel.add(reportType);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return reportModel;
	}

	// END PARSE FROM JSON

	// GET FROM JSON

	public static String getAllCreatures(String page, String kingdom) {
		String result = "";
		String request = String.format(ServerConfig.GET_CREATURE);
		RestClient client = new RestClient(request);
		client.addParam("page", page);
		client.addParam("recordPerPage", ServerConfig.NUM_PER_PAGE);
		client.addParam("kingdom", kingdom);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getCreaturesByName(String page, String kingdom,
			String name, String familyId, String orderId, String classId) {
		String result = "";
		String request = String.format(ServerConfig.GET_CREATURE);
		RestClient client = new RestClient(request);
		client.addParam("creatureName", name);
		client.addParam("page", page);
		client.addParam("recordPerPage", ServerConfig.NUM_PER_PAGE);
		client.addParam("family", familyId);
		client.addParam("order", orderId);
		client.addParam("class", classId);
		client.addParam("kingdom", kingdom);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getCreaturesById(String id) {
		String result = "";
		String request = String.format(ServerConfig.GET_CREATURE);
		RestClient client = new RestClient(request);
		client.addParam("id", id);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getFamily(String orderId, String classId,
			String kingdomId) {
		String result = "";

		String request = String.format(ServerConfig.GET_GROUP);
		RestClient client = new RestClient(request);
		client.addParam("table", "family");
		client.addParam("kingdom", kingdomId);
		client.addParam("class", classId);
		client.addParam("order", orderId);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getOrder(String familyId, String classId,
			String kingdomId) {
		String result = "";

		String request = String.format(ServerConfig.GET_GROUP);
		RestClient client = new RestClient(request);
		client.addParam("table", "order");
		client.addParam("kingdom", kingdomId);
		client.addParam("class", classId);
		client.addParam("family", familyId);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getClassType(String orderId, String familyId,
			String kingdomId) {
		String result = "";

		String request = String.format(ServerConfig.GET_GROUP);
		RestClient client = new RestClient(request);
		client.addParam("table", "class");
		client.addParam("kingdom", kingdomId);
		client.addParam("order", orderId);
		client.addParam("family", familyId);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getCategory() {
		String result = "";

		String request = String.format(ServerConfig.GET_CAREGORY);
		RestClient client = new RestClient(request);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getNews(String catId, String page) {
		String result = "";

		String request = String.format(ServerConfig.GET_NEWS);
		RestClient client = new RestClient(request);
		client.addParam("category", catId);
		client.addParam("page", page);
		client.addParam("recordPerPage", ServerConfig.NUM_PER_PAGE);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getNewsDetail(String newsId) {
		String result = "";

		String request = String.format(ServerConfig.GET_NEWS);
		RestClient client = new RestClient(request);
		client.addParam("id", newsId);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getProvince(String id) {
		String result = "";

		String request = String.format(ServerConfig.GET_PROVINCE);
		RestClient client = new RestClient(request);
		client.addParam("id", id);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getNationalPark(String id) {
		String result = "";

		String request = String.format(ServerConfig.GET_NATIONAL_PARK);
		RestClient client = new RestClient(request);
		client.addParam("id", id);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getAllThread() {
		String result = "";

		String request = String.format(ServerConfig.GET_THREAD);
		RestClient client = new RestClient(request);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getProfilePictureUrl(String profileId) {
		String result = "";

		String request = String.format(ServerConfig.PROFILE_PICTURE, profileId);
		RestClient client = new RestClient(request);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getThreadById(String threadId) {
		String result = "";

		String request = String.format(ServerConfig.GET_THREAD);
		RestClient client = new RestClient(request);
		client.addParam("id", threadId);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}
	
	public static String getThreadImageById(String threadId) {
		String result = "";

		String request = String.format(ServerConfig.GET_THREAD_IMAGE);
		RestClient client = new RestClient(request);
		client.addParam("id", threadId);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getPostByThreadId(String threadId) {
		String result = "";

		String request = String.format(ServerConfig.GET_POST);
		RestClient client = new RestClient(request);
		client.addParam("id", threadId);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}
    
    public static String getSuggestion(String title) {
        String result = "";

        String request = String.format(ServerConfig.GET_SUGGESTION);
        RestClient client = new RestClient(request);
        client.addParam("title", title);

        try {
            client.execute(RequestMethod.GET);
            result = client.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

    public static String getNotification(String userId) {
		String result = "";

		String request = String.format(ServerConfig.GET_NOTIFICATION);
		RestClient client = new RestClient(request);
		client.addParam("id", userId);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getReportType() {
		String result = "";

		String request = String.format(ServerConfig.GET_REPORT_TYPE);
		RestClient client = new RestClient(request);

		try {
			client.execute(RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	// END GET FROM JSOn

	// POST DATA TO SERVER
	public static String addUser(FacebookUser fb) {
		String result = "";

		Gson gson = new Gson();
		Location location = fb.getLocation();
		String locationName = "";
		if (location != null) {
			locationName = location.getName();
		}
		User us = new User(fb.getId(), fb.getUsername(), fb.getName(),
				fb.getBirthday(), locationName, fb.getEmail(), "", fb.getId());
		String json = gson.toJson(us);

		String request = String.format(ServerConfig.ADD_USER);
		RestClient client = new RestClient(request);
		client.addParam("data", json);

		try {
			client.execute(RequestMethod.POST);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String addThread(Thread thread) {
		String result = "";

		Gson gson = new Gson();
		String json = gson.toJson(thread);
		System.out.println("JSon thread " + json);
		String request = String.format(ServerConfig.ADD_THREAD);
		RestClient client = new RestClient(request);
		client.addParam("data", json);

		try {
			client.execute(RequestMethod.POST);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String addPost(Thread thread) {
		String result = "";

		Gson gson = new Gson();
		String json = gson.toJson(thread);

		String request = String.format(ServerConfig.ADD_POST);
		RestClient client = new RestClient(request);
		client.addParam("data", json);

		try {
			client.execute(RequestMethod.POST);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String updateNotification(Thread thread) {
		String result = "";

		Gson gson = new Gson();
		String json = gson.toJson(thread);

		String request = String.format(ServerConfig.SET_NOTIFICATION);
		RestClient client = new RestClient(request);
		client.addParam("data", json);

		try {
			client.execute(RequestMethod.POST);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String addReport(Report report) {
		String result = "";

		Gson gson = new Gson();
		String json = gson.toJson(report);

		String request = String.format(ServerConfig.ADD_REPORT);
		RestClient client = new RestClient(request);
		client.addParam("data", json);

		try {
			client.execute(RequestMethod.POST);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}
}
