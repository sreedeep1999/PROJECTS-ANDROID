package com.example.smartscheduling.ModelClass;

public class BookingListModelClass {
    String id;
    String hotel_name;
    String hotel_place;
    String hotel_address;
    String hotel_latitude;
    String hotel_longitude;
    String hotel_image;
    String room_type;
    String rooms;
    String days;
    String from_date;
    String price;

    public BookingListModelClass(String id, String hotel_name, String hotel_place, String hotel_address, String hotel_latitude, String hotel_longitude, String room_type, String rooms, String days, String from_date, String price, String hotel_image) {
        this.id = id;
        this.hotel_name = hotel_name;
        this.hotel_place = hotel_place;
        this.hotel_address = hotel_address;
        this.hotel_latitude = hotel_latitude;
        this.hotel_longitude = hotel_longitude;
        this.room_type = room_type;
        this.rooms = rooms;
        this.days = days;
        this.from_date = from_date;
        this.price = price;
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

    public String getHotel_address() {
        return hotel_address;
    }

    public String getHotel_latitude() {
        return hotel_latitude;
    }

    public String getHotel_longitude() {
        return hotel_longitude;
    }

    public String getRoom_type() {
        return room_type;
    }

    public String getRooms() {
        return rooms;
    }

    public String getDays() {
        return days;
    }

    public String getFrom_date() {
        return from_date;
    }

    public String getPrice() {
        return price;
    }

    public String getHotel_image() {
        return hotel_image;
    }
}
