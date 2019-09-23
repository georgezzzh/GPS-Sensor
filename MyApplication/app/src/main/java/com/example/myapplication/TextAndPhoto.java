package com.example.myapplication;

public class TextAndPhoto {
    private String name;
    private int imageId;

    public TextAndPhoto(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
