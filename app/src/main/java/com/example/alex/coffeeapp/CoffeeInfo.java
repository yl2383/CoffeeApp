package com.example.alex.coffeeapp;

import java.io.Serializable;

/**
 * Created by alex on 2015/3/20.
 */
public class CoffeeInfo implements Serializable {
    private String desc;
    private String image_url;
    private String id;
    private String name;

    public CoffeeInfo(String desc, String image_url, String id, String name) {
        this.desc = desc;
        this.image_url = image_url;
        this.id = id;
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
