package com.example.demo.ui.slideshow;

public class SingleDish {
    public float average;
    public int usercnt;
    public String dish;
    //public String dishName;
    public String price;
    public int sum;

    public SingleDish(){}

    public SingleDish(float average, String dish, String price, int sum, int usercnt ) {
        this.average = average;
        this.dish = dish;
        this.price = price;
        this.sum = sum;
        this.usercnt = usercnt;
        //this.dishName = dishName;
    }
}