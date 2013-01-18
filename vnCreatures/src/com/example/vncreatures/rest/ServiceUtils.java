package com.example.vncreatures.rest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.vncreatures.common.ServerConfig;
import com.example.vncreatures.model.Category;
import com.example.vncreatures.model.CategoryModel;
import com.example.vncreatures.model.Creature;
import com.example.vncreatures.model.CreatureGroup;
import com.example.vncreatures.model.CreatureGroupListModel;
import com.example.vncreatures.model.CreatureModel;
import com.example.vncreatures.model.NewsItem;
import com.example.vncreatures.model.NewsModel;

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
			JSONObject creaturesObj = new JSONObject(data);
			if (!creaturesObj.has("creatures"))
				return null;

			JSONArray creatureListObj = creaturesObj.getJSONArray("creatures");
			for (int i = 0; i < creatureListObj.length(); i++) {
				Creature creature = new Creature();
				JSONObject creatureObj = creatureListObj.getJSONObject(i);
				String stringVal = "", key = "";
				int intVal = -1;
				key = "creature";
				if (!creatureObj.has(key))
					continue;
				creatureObj = creatureObj.getJSONObject(key);

				key = "ID";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setId(stringVal);

				key = "Viet";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setVName(stringVal);

				key = "Latin";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setLatin(stringVal);

				key = "Img";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setImageId(stringVal);

				key = "Loai";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setKingdom(stringVal);

				key = "Description";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				stringVal = stringVal.replace("charset=windows-1252", " ");
				creature.setDescription(stringVal);

				key = "AuthorName";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creature.setAuthor(stringVal);

				// Add to the model
				creatureModel.add(creature);
			}

		} catch (JSONException e) {
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
			JSONObject creaturesObj = new JSONObject(data);
			if (!creaturesObj.has("groups"))
				return null;

			JSONArray creatureListObj = creaturesObj.getJSONArray("groups");
			for (int i = 0; i < creatureListObj.length(); i++) {
				CreatureGroup creatureGroup = new CreatureGroup();
				JSONObject creatureObj = creatureListObj.getJSONObject(i);
				String stringVal = "", key = "";
				int intVal = -1;
				key = "group";
				if (!creatureObj.has(key))
					continue;
				creatureObj = creatureObj.getJSONObject(key);

				key = "ID";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creatureGroup.setId(stringVal);

				key = "Viet";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creatureGroup.setViet(stringVal);

				key = "Latin";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				creatureGroup.setLatin(stringVal);

				// Add to the model
				crGroupListModel.add(creatureGroup);
			}

		} catch (JSONException e) {
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
			JSONObject creaturesObj = new JSONObject(data);
			if (!creaturesObj.has("categories"))
				return null;

			JSONArray creatureListObj = creaturesObj.getJSONArray("categories");
			for (int i = 0; i < creatureListObj.length(); i++) {
				Category cat = new Category();
				JSONObject creatureObj = creatureListObj.getJSONObject(i);
				String stringVal = "", key = "";
				int intVal = -1;
				key = "category";
				if (!creatureObj.has(key))
					continue;
				creatureObj = creatureObj.getJSONObject(key);

				key = "category_id";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				cat.setCatId(stringVal);

				key = "category_name";
				stringVal = creatureObj.has(key) ? creatureObj.getString(key)
						: "";
				cat.setCatName(stringVal);

				// Add to the model
				catListModel.add(cat);
			}

		} catch (JSONException e) {
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
				JSONObject obj = new JSONObject(data);
				if (!obj.has("data"))
					return null;

				JSONArray creatureListObj = obj.getJSONArray("data");
				for (int i = 0; i < creatureListObj.length(); i++) {
					NewsItem news = new NewsItem();
					JSONObject creatureObj = creatureListObj.getJSONObject(i);
					String stringVal = "", key = "";
					int intVal = -1;
					key = "news";
					if (!creatureObj.has(key))
						continue;
					creatureObj = creatureObj.getJSONObject(key);

					key = "news_id";
					stringVal = creatureObj.has(key) ? creatureObj.getString(key)
							: "";
					news.setId(stringVal);

					key = "short_description";
					stringVal = creatureObj.has(key) ? creatureObj.getString(key)
							: "";
					news.setDescription(stringVal);
					
					key = "title";
					stringVal = creatureObj.has(key) ? creatureObj.getString(key)
							: "";
					news.setTitle(stringVal);
					
					key = "image";
					stringVal = creatureObj.has(key) ? creatureObj.getString(key)
							: "";
					news.setImage(stringVal);
					
					key = "news_content";
                    stringVal = creatureObj.has(key) ? creatureObj.getString(key)
                            : "";
                    news.setContent(stringVal);

					// Add to the model
					newsListModel.add(news);
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}

			return newsListModel;
		}

	// END PARSE FROM JSON

	// GET FROM JSON

	public static String getAllCreatures(String page, String kingdom) {
		String result = "";
		String request = String.format(ServerConfig.GET_ALL_CREATURE);
		RestClient client = new RestClient(request);
		client.addParam("getAllNameCreature", "");
		client.addParam("format", "json");
		client.addParam("page", page);
		client.addParam("recordPerPage", ServerConfig.NUM_PER_PAGE);
		client.addParam("kingdom", kingdom);

		try {
			client.execute(RestClient.RequestMethod.GET);
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
		String request = String.format(ServerConfig.GET_ALL_CREATURE_BY_NAME2);
		RestClient client = new RestClient(request);
		client.addParam("format", "json");
		client.addParam("creatureName", name);
		client.addParam("page", page);
		client.addParam("recordPerPage", ServerConfig.NUM_PER_PAGE);
		client.addParam("family", familyId);
		client.addParam("order", orderId);
		client.addParam("class", classId);
		client.addParam("kingdom", kingdom);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getCreaturesById(String id) {
		String result = "";
		String request = String.format(ServerConfig.GET_CREATURE_BY_ID);
		RestClient client = new RestClient(request);
		client.addParam("getCreatureById", "");
		client.addParam("format", "json");
		client.addParam("id", id);

		try {
			client.execute(RestClient.RequestMethod.GET);
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

		String request = String.format(ServerConfig.GET_FAMILY);
		RestClient client = new RestClient(request);
		client.addParam("format", "json");
		client.addParam("kingdom", kingdomId);
		client.addParam("class", classId);
		client.addParam("order", orderId);

		try {
			client.execute(RestClient.RequestMethod.GET);
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

		String request = String.format(ServerConfig.GET_ORDER);
		RestClient client = new RestClient(request);
		client.addParam("format", "json");
		client.addParam("kingdom", kingdomId);
		client.addParam("class", classId);
		client.addParam("family", familyId);

		try {
			client.execute(RestClient.RequestMethod.GET);
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

		String request = String.format(ServerConfig.GET_CLASS);
		RestClient client = new RestClient(request);
		client.addParam("format", "json");
		client.addParam("kingdom", kingdomId);
		client.addParam("order", orderId);
		client.addParam("family", familyId);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getCategory() {
		String result = "";

		String request = String.format(ServerConfig.GET_ALL_CAREGORY);
		RestClient client = new RestClient(request);

		try {
			client.execute(RestClient.RequestMethod.GET);
			result = client.getResponse();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return result;
	}

	public static String getNews(String catId) {
		String result = "";

		String request = String.format(ServerConfig.GET_ALL_NEWS);
		RestClient client = new RestClient(request);
		client.addParam("format", "json");
		client.addParam("category", catId);

		try {
			client.execute(RestClient.RequestMethod.GET);
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
        client.addParam("format", "json");
        client.addParam("id", newsId);

        try {
            client.execute(RestClient.RequestMethod.GET);
            result = client.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }

	// END GET FROM JSOn
}
