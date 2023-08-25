package com.example.smartscheduling.ModelClass;

public class HotelRoomsListModelClass {

    String id;
    String hotel_id;
    String room_type;
    String heads;
    String bed;
    String size;
    String price;
    String Available;
    String days;

    public HotelRoomsListModelClass(String id, String hotel_id, String room_type, String heads, String bed, String size, String price, String available, String days) {
        this.id = id;
        this.hotel_id = hotel_id;
        this.room_type = room_type;
        this.heads = heads;
        this.bed = bed;
        this.size = size;
        this.price = price;
        this.Available = available;
        this.days = days;
    }

    public String getId() {
        return id;
    }

    public String getHotel_id() {
        return hotel_id;
    }

    public String getRoom_type() {
        return room_type;
    }

    public String getHeads() {
        return heads;
    }

    public String getBed() {
        return bed;
    }

    public String getSize() {
        return size;
    }

    public String getPrice() {
        return price;
    }

    public String getAvailable() {
        return Available;
    }

    public String getDays() {
        return days;
    }
}
