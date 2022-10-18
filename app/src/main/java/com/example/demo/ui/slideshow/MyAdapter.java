package com.example.demo.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;


import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private ArrayList<SingleDish> mDataset;
    private int imageId;
    private String dishName;

    public MyAdapter(ArrayList<SingleDish> myDataset) { mDataset = myDataset; }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("hwang","onCreateViewHolder");
        View v = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.item, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, int position) {
        Log.d("hwang","onBindViewHolder");
        switch (mDataset.get(position).dish){
            case("cheesecutlet"):
                imageId = R.drawable.cheesecutlet;
                dishName = "치즈돈까스";
                break;
            case("chickenmayo"):
                imageId = R.drawable.chickenmayo;
                dishName="치킨마요덮밥";
                break;
            case("chickenomurice"):
                imageId = R.drawable.chickenomurice;
                dishName="치킨강정오므라이스";
                break;
            case("coldnoodles"):
                imageId = R.drawable.coldnoodles;
                dishName="냉면";
                break;
            case("flyingfishroerice"):
                imageId = R.drawable.flyingfishroerice;
                dishName="뚝배기날치알밥";
                break;
            case("gochujangbulgogi"):
                imageId = R.drawable.gochujangbulgogi;
                dishName="고추장불고기뚝배기";
                break;
            case("gogumacutlet"):
                imageId = R.drawable.gogumacutlet;
                dishName="고구마치즈돈까스";
                break;
            case("jjolmen"):
                imageId = R.drawable.jjolmen;
                dishName="쫄면 + 주먹밥";
                break;
            case("kimchistew"):
                imageId = R.drawable.kimchistew;
                dishName="촌돼지김치찌개";
                break;
            case("partynoodle"):
                imageId = R.drawable.partynoodle;
                dishName="잔치국수 + 주먹밥";
                break;
            case("porkcutlet"):
                imageId = R.drawable.porkcutlet;
                dishName="직생돈(직접두드린생돈까스)";
                break;
            case("porkcutletwihtalbab"):
                imageId = R.drawable.porkcutletwihtalbab;
                dishName="직생돈 + 알밥";
                break;
            case("porkcutletwithudon"):
                imageId = R.drawable.porkcutletwithudon;
                dishName="직생돈 + 우동";
                break;
            case("raddishbibimbab"):
                imageId = R.drawable.raddishbibimbab;
                dishName="냉면 + 열무비빔밥";
                break;
            case("ramenwithrice"):
                imageId = R.drawable.ramenwithrice;
                dishName="해장라면 + 공깃밥";
                break;
            case("ramenwithtoppingrice"):
                imageId = R.drawable.ramenwithtoppingrice;
                dishName = "떡만두라면 + 공깃밥";
                break;
            case("ricetoppedwithpork"):
                imageId = R.drawable.ricetoppedwithpork;
                dishName="제육덮밥";
                break;
            case("seafoodsofttofustew"):
                imageId = R.drawable.seafoodsofttofustew;
                dishName="해물순두부찌개";
                break;
            case("spicygrilledchicken"):
                imageId = R.drawable.spicygrilledchicken;
                dishName="닭갈비뚝배기";
                break;
            case("spicynoodle"):
                imageId = R.drawable.spicynoodle;
                dishName="얼큰이칼국수 + 공깃밥";
                break;
            case("spicyporkbibimbap"):
                imageId = R.drawable.spicyporkbibimbap;
                dishName="돝솥제육비빔밥";
                break;
            case("spicysausagestew"):
                imageId = R.drawable.spicysausagestew;
                dishName="부대찌개";
                break;

        }

        holder.tvRanking.setText((position+1) + "");
        holder.imageView.setImageResource(imageId);
        holder.ratingBar.setRating(mDataset.get(position).average);
        holder.tvDish.setText(dishName);
        holder.tvPrice.setText(mDataset.get(position).price);

        final Context mycontext = holder.itemView.getContext();

        holder.btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Toast.makeText(mycontext, mDataset.get(position).title, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(v.getContext(), ShowDetail.class);

                intent.putExtra("dish", mDataset.get(position).dish);
                intent.putExtra("imageId", imageId);
                intent.putExtra("dishName", dishName);
                intent.putExtra("average", mDataset.get(position).average);
                intent.putExtra("price", mDataset.get(position).price);


                mycontext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
