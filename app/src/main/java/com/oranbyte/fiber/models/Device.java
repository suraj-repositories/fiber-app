package com.oranbyte.fiber.models;

public class Device {

    private int id;
    private String name;
    private String sub_title;
    private String device_key;
    private String device_type_id;
    private String value;
    private String is_online;
    private  String status;
    private String[] allowed_values;

    public Device() {
    }

    public Device(int id, String name, String sub_title, String device_key, String device_type_id, String value, String is_online, String status, String[] allowed_values) {
        this.id = id;
        this.name = name;
        this.sub_title = sub_title;
        this.device_key = device_key;
        this.device_type_id = device_type_id;
        this.value = value;
        this.is_online = is_online;
        this.status = status;
        this.allowed_values = allowed_values;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSub_title() {
        return sub_title;
    }

    public void setSub_title(String sub_title) {
        this.sub_title = sub_title;
    }

    public String getDevice_key() {
        return device_key;
    }

    public void setDevice_key(String device_key) {
        this.device_key = device_key;
    }

    public String getDevice_type_id() {
        return device_type_id;
    }

    public void setDevice_type_id(String device_type_id) {
        this.device_type_id = device_type_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getIs_online() {
        return is_online;
    }

    public void setIs_online(String is_online) {
        this.is_online = is_online;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String[] getAllowed_values() {
        return allowed_values;
    }

    public void setAllowed_values(String[] allowed_values) {
        this.allowed_values = allowed_values;
    }
}
