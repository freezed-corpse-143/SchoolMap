package com.example.project_map;

import com.supermap.data.Geometry;
import com.supermap.data.Point2D;

public class Detail {
    private String Name;
    private String Address;
    private String Phone;
    private String Time;
    private String Score;
    private String Category;
    private Point2D Point;
    private boolean Jud;
    private double distance;

    public Detail() {
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public boolean isJud() {
        return Jud;
    }

    public void setJud(boolean jud) {
        Jud = jud;
    }

    public Detail(String name, String address) {
        Name = name;
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public String getAddress() {
        return Address;
    }

    public String getPhone() {
        return Phone;
    }

    public String getTime() {
        return Time;
    }

    public String getScore() {
        return Score;
    }

    public String getCategory() {
        return Category;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public void setTime(String time) {
        Time = time;
    }

    public void setScore(String score) {
        Score = score;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public Point2D getPoint() {
        return Point;
    }

    public void setPoint(Point2D point) {
        Point = point;
    }
}
