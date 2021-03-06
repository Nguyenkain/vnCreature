package com.vncreatures.model.discussion;

import java.util.ArrayList;

public class Thread {
    private String thread_id;
    private String user_id;
    private String thread_title;
    private String thread_content;
    private String thread_created_time;
    private ArrayList<String> thread_image = null;
    private String user_avatar;
    private String name;
    private String count_post;
    private String post_id;
    private String post_content;
    private String post_created_time;
    private String notification_id;
    private String viewed_status;
    private String last_modified_time;
    private String thread_image_id;
    private String image_link;

    public String getLast_modified_time() {
        return last_modified_time;
    }

    public void setLast_modified_time(String last_modified_time) {
        this.last_modified_time = last_modified_time;
    }

    public String getPost_created_time() {
        return post_created_time;
    }

    public void setPost_created_time(String post_created_time) {
        this.post_created_time = post_created_time;
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getViewed_status() {
        return viewed_status;
    }

    public void setViewed_status(String viewed_status) {
        this.viewed_status = viewed_status;
    }

    public String getCount_post() {
        return count_post;
    }

    public void setCount_post(String count_post) {
        this.count_post = count_post;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getPost_content() {
        return post_content;
    }

    public void setPost_content(String post_content) {
        this.post_content = post_content;
    }

    public String getPost_time_created() {
        return post_created_time;
    }

    public void setPost_time_created(String post_time_created) {
        this.post_created_time = post_time_created;
    }

    public String getPost_count() {
        return count_post;
    }

    public void setPost_count(String post_count) {
        this.count_post = post_count;
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThread_id() {
        return thread_id;
    }

    public void setThread_id(String thread_id) {
        this.thread_id = thread_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getThread_title() {
        return thread_title;
    }

    public void setThread_title(String thread_title) {
        this.thread_title = thread_title;
    }

    public String getThread_content() {
        return thread_content;
    }

    public void setThread_content(String thread_content) {
        this.thread_content = thread_content;
    }

    public String getThread_created_time() {
        return thread_created_time;
    }

    public void setThread_created_time(String thread_created_time) {
        this.thread_created_time = thread_created_time;
    }

	public void setThread_image(ArrayList<String> thread_image) {
		this.thread_image = thread_image;
	}

	public ArrayList<String> getThread_image() {
		return thread_image;
	}

	public void setThread_image_id(String thread_image_id) {
		this.thread_image_id = thread_image_id;
	}

	public String getThread_image_id() {
		return thread_image_id;
	}

	public void setImage_link(String image_link) {
		this.image_link = image_link;
	}

	public String getImage_link() {
		return image_link;
	}

}
