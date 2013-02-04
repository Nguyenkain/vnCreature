package com.example.vncreatures.model;

public class Province {
    private String id;
    private String creature_id;
    private String province_name;
    private String park_name;
    private String park_description;
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

	public void setPark_name(String park_name) {
		this.park_name = park_name;
	}

	public String getPark_name() {
		return park_name;
	}

	public void setPark_description(String park_description) {
		this.park_description = park_description;
	}

	public String getPark_description() {
		return park_description;
	}

}
