package com.example.golu.zombatomap;

/**
 * Created by GOLU on 30-06-2017.
 */

public class Restaurant {
    private  String name,address,locality,latitude,longitude,city,rating, cuisines,thumb;

    public Restaurant(String name, String address, String locality, String latitude, String longitude, String city, String rating, String cuisines, String thumb) {
        this.name = name;
        this.address = address;
        this.locality = locality;
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.rating = rating;
        this.cuisines = cuisines;
        this.thumb = thumb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getCuisines() {
        return cuisines;
    }

    public void setCuisines(String cuisines) {
        this.cuisines = cuisines;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }
}
