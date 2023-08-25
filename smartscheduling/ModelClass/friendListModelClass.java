package com.example.smartscheduling.ModelClass;

public class friendListModelClass {

    String id;
    String Name;
    String Image;

    public friendListModelClass(String id, String name, String image) {
        this.id = id;
        Name = name;
        Image = image;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return Name;
    }

    public String getImage() {
        return Image;
    }
}
