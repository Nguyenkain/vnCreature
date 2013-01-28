package com.example.vncreatures.model;

public class Province {
    private String id;
    private String creature_id;
    private String province_id;
    private String province_name;
    private String longitude;
    private String latitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreature_id() {
        return creature_id;
    }

    public void setCreature_id(String creature_id) {
        this.creature_id = creature_id;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public int getLongitude() {
        return (int) (Double.parseDouble(longitude)* 1E6);
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getLatitude() {
    	return (int) (Double.parseDouble(latitude)* 1E6);
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

}
