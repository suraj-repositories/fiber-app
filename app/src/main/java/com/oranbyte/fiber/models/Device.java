package com.oranbyte.fiber.models;

import com.google.gson.Gson;

public class Device {

    private int id;
    private String name;
    private String sub_title;
    private String device_key;
    private String device_type_id;
    private String value_type;
    private String device_type_name;
    private String value;
    private String is_online;
    private  String status;
    private String allowed_values;

    private String point_time;

    private String minuteTime;

    private int device_icons_id;

    public Device() {
    }

    public Device(int id, String name, String sub_title, String device_key, String device_type_id, String value_type, String device_type_name, String value, String is_online, String status, String allowed_values) {
        this.id = id;
        this.name = name;
        this.sub_title = sub_title;
        this.device_key = device_key;
        this.device_type_id = device_type_id;
        this.value_type = value_type;
        this.device_type_name = device_type_name;
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
        return new Gson().fromJson(this.allowed_values, String[].class);
    }

    public void setAllowed_values(String allowed_values) {
        this.allowed_values = allowed_values;
    }


    public String getValue_type() {
        return value_type;
    }

    public void setValue_type(String value_type) {
        this.value_type = value_type;
    }

    public String getDevice_type_name() {
        return device_type_name;
    }

    public void setDevice_type_name(String device_type_name) {
        this.device_type_name = device_type_name;
    }

    public String getPoint_time() {
        return point_time;
    }

    public void setPoint_time(String point_time) {
        this.point_time = point_time;
    }

    public String getMinuteTime() {
        return minuteTime;
    }

    public void setMinuteTime(String minuteTime) {
        this.minuteTime = minuteTime;
    }

    public int getDevice_icons_id() {
        return device_icons_id;
    }

    public void setDevice_icons_id(int device_icons_id) {
        this.device_icons_id = device_icons_id;
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sub_title='" + sub_title + '\'' +
                ", device_key='" + device_key + '\'' +
                ", device_type_id='" + device_type_id + '\'' +
                ", value_type='" + value_type + '\'' +
                ", device_type_name='" + device_type_name + '\'' +
                ", value='" + value + '\'' +
                ", is_online='" + is_online + '\'' +
                ", status='" + status + '\'' +
                ", allowed_values='" + allowed_values + '\'' +
                ", point_time='" + point_time + '\'' +
                ", minuteTime='" + minuteTime + '\'' +
                ", device_icons_id=" + device_icons_id +
                '}';
    }
}
