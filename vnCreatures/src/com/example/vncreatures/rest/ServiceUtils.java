package com.example.vncreatures.rest;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.vncreatures.common.Common;
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
import com.example.vncreatures.model.discussion.User;
import com.example.vncreatures.model.discussion.FacebookUser.Location;
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
        String request = String.format(ServerConfig.GET_CREATURE);
        RestClient client = new RestClient(request);
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

        String request = String.format(ServerConfig.GET_GROUP);
        RestClient client = new RestClient(request);
        client.addParam("table", "family");
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

        String request = String.format(ServerConfig.GET_GROUP);
        RestClient client = new RestClient(request);
        client.addParam("table", "order");
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

        String request = String.format(ServerConfig.GET_GROUP);
        RestClient client = new RestClient(request);
        client.addParam("table", "class");
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

        String request = String.format(ServerConfig.GET_CAREGORY);
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

    public static String getNews(String catId, String page) {
        String result = "";

        String request = String.format(ServerConfig.GET_NEWS);
        RestClient client = new RestClient(request);
        client.addParam("category", catId);
        client.addParam("page", page);
        client.addParam("recordperpage", ServerConfig.NUM_PER_PAGE);

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

    public static String getProvince(String id) {
        String result = "";

        String request = String.format(ServerConfig.GET_PROVINCE);
        RestClient client = new RestClient(request);
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
    
    public static String getNationalPark(String id) {
        String result = "";

        String request = String.format(ServerConfig.GET_NATIONAL_PARK);
        RestClient client = new RestClient(request);
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

    // END GET FROM JSOn
    
    //POST DATA TO SERVER
    public static String addUser(FacebookUser fb) {
        String result = "";
        
        Gson gson = new Gson();
        Location location = fb.getLocation();
        User us = new User(fb.getId(), fb.getUsername(), fb
                .getName(), fb.getBirthday(), location
                .getName(), fb.getEmail(), "", fb.getId());
        String json = gson.toJson(us);

        String request = String.format(ServerConfig.ADD_USER);
        RestClient client = new RestClient(request);
        client.addParam("data", json);

        try {
            client.execute(RestClient.RequestMethod.POST);
            result = client.getResponse();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }
}