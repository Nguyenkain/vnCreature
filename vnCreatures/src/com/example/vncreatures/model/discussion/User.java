package com.example.vncreatures.model.discussion;

public class User {
    private String facebook_id;
    private String username;
    private String user_dob;
    private String user_address;
    private String user_email;
    private String last_time_login;
    private String user_avatar;
    private String name;

    public User(String facebook_id, String username, String name, String dob,
            String user_address, String user_email, String last_time_login,
            String user_avatar) {
        super();
        this.facebook_id = facebook_id;
        this.username = username;
        this.name = name;
        this.user_dob = dob;
        this.user_address = user_address;
        this.user_email = user_email;
        this.last_time_login = last_time_login;
        this.user_avatar = user_avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFacebook_id() {
        return facebook_id;
    }

    public void setFacebook_id(String facebook_id) {
        this.facebook_id = facebook_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUser_age() {
        return user_dob;
    }

    public void setUser_age(String user_age) {
        this.user_dob = user_age;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getLast_time_login() {
        return last_time_login;
    }

    public void setLast_time_login(String last_time_login) {
    }

    public String getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(String user_avatar) {
        this.user_avatar = user_avatar;
    }

}
