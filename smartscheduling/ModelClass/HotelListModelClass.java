package com.example.smartscheduling.ModelClass;

public class HotelListModelClass {
    private String id;
    private String hotel_name;
    private String hotel_place;
    private String hotel_image;

    public HotelListModelClass(String id, String hotel_name, String hotel_place, String hotel_image) {
        this.id = id;
        this.hotel_name = hotel_name;
        this.hotel_place = hotel_place;
        this.hotel_image = hotel_image;
    }

    public String getId() {
        return id;
    }

    public String getHotel_name() {
        return hotel_name;
    }

    public String getHotel_place() {
        return hotel_place;
    }

    public String getHotel_image() {
        return hotel_image;
    }
}
