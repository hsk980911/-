package com.example.demo.ui.gallery;

public class Dish {
    public String date;
    public float average;
    public int usercnt;
    public int sum;

    public Dish() { }

    public Dish(String date, float average, int sum, int usercnt) {
        this.date = date;
        this.average = average;
        this.sum = sum;
        this.usercnt = usercnt;
    }
}

