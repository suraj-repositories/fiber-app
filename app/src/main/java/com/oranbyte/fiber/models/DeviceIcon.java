package com.oranbyte.fiber.models;

public class DeviceIcon {

    private int id;
    private String name;
    private String icon_key;
    private boolean selected = false;

    public DeviceIcon() {
    }


    public DeviceIcon(int id, String name, String icon_key) {
        this.id = id;
        this.name = name;
        this.icon_key = icon_key;
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

    public String getIcon_key() {
        return icon_key;
    }

    public void setIcon_key(String icon_key) {
        this.icon_key = icon_key;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
