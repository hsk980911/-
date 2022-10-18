package com.example.demo.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.ui.slideshow.ShowDetail;
import java.util.ArrayList;

public class DishAdapter extends RecyclerView.Adapter<DishViewHolder> {

    private ArrayList<Dish> dishDataset;
    private int imageId;
    private String dishName;

    public DishAdapter(ArrayList<Dish> dDataset) { dishDataset = dDataset; }

    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("hwang","onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.dish_item, parent, false);
        DishViewHolder vh = new DishViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder( DishViewHolder holder, int position) {
        Log.d("hwang","onBindViewHolder");

        holder.tvDate.setText(dishDataset.get(position).date);
        holder.rtDish.setRating(dishDataset.get(position).average);


//        holder.tvRanking.setText((position+1) + "");
//        holder.imageView.setImageResource(imageId);
//        holder.ratingBar.setRating(dishDataset.get(position).average);
//        holder.tvDish.setText(dishName);
//        holder.tvPrice.setText(dishDataset.get(position).price);

        final Context mycontext = holder.itemView.getContext();

    }

    @Override
    public int getItemCount() {
        return dishDataset.size();
    }
}
