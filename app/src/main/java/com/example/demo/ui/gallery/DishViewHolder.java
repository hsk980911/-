package com.example.demo.ui.gallery;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;

public class DishViewHolder extends RecyclerView.ViewHolder {

    TextView tvDate;
    RatingBar rtDish;

    public DishViewHolder(View item) {
        super(item);

        tvDate = (TextView) item.findViewById(R.id.tvDate);
        rtDish = (RatingBar) item.findViewById(R.id.rtDish);

    }
}