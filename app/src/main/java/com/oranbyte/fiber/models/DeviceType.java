package com.oranbyte.fiber.models;

public class DeviceType {

    public int id;
    public String name;
    public String type_key;

    public DeviceType() {
    }


    public DeviceType(int id, String name, String type_key) {
        this.id = id;
        this.name = name;
        this.type_key = type_key;
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

    public String getType_key() {
        return type_key;
    }

    public void setType_key(String type_key) {
        this.type_key = type_key;
    }
}
