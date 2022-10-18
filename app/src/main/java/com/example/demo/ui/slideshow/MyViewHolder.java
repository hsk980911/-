package com.example.demo.ui.slideshow;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tvRanking, tvDish, tvPrice;
    ImageView imageView;
    RatingBar ratingBar;
    Button btn;

    public MyViewHolder(View item) {
        super(item);

        tvRanking = (TextView) item.findViewById(R.id.tvRanking);
        tvDish = (TextView) item.findViewById(R.id.tvDish);
        tvPrice = (TextView) item.findViewById(R.id.tvPrice);
        imageView = (ImageView) item.findViewById(R.id.itemImageview);
        ratingBar = (RatingBar) item.findViewById(R.id.ratingBar);
        btn = (Button) item.findViewById(R.id.itembutton);
    }
}