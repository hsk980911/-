package com.example.demo.ui.slideshow;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShowDetail extends AppCompatActivity {
    View dialogView;
    ListView lv;
    ImageView img;
    RatingBar rbSD;
    TextView tvDish, tvPrice;
    Button btnReview, back;
    String dish, price;
    int imageId;
    String dishName;
    float average;

    float attrSum, attrAvg;
    int usercnt;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sd_details);

        setTitle("상세보기");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        img = (ImageView) findViewById(R.id.imageView);
        rbSD = (RatingBar) findViewById(R.id.rbSD);
        tvDish = (TextView) findViewById(R.id.tvDish);
        tvPrice = (TextView) findViewById(R.id.tvPrice);
        btnReview = (Button) findViewById(R.id.btn_review);
        back = (Button) findViewById(R.id.btn_back);
        lv = (ListView) findViewById(R.id.listview);

        ArrayList<String> cmmt = new ArrayList();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        String uid = currentUser.getUid();

        Intent intent = getIntent();
        if (intent != null) {
            dish = intent.getStringExtra("dish");
            average = intent.getFloatExtra("average",0);
            price = intent.getStringExtra("price");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, cmmt);

        readComment(cmmt, dish, adapter);

//        db.collection("User")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                try{
//                                    cmmt.add(document.getData().get(dish).toString());
//                                    Log.d("TAG", document.getId() + " => " + document.getData().get(dish));
//                                }
//                                catch (NullPointerException e){
//
//                                }
//
//                            }
//                        } else {
//                            Log.d("TAG", "Error getting documents: ", task.getException());
//                        }
//
//                        adapter.notifyDataSetChanged();
//                        lv.setAdapter(adapter);
//                    }
//                });

        dialogView =(View) View.inflate(this, R.layout.singledishdialog,null);
        AlertDialog.Builder dlg = new AlertDialog.Builder(this);
        dlg.setView(dialogView);

        dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText review = dialogView.findViewById(R.id.edtReview);
                String rv = review.getText().toString();
                RatingBar rvSD = dialogView.findViewById(R.id.rvSD);

                Map<String, Object> cmt = new HashMap<>();
                cmt.put(dish, rv);


                db.collection("SDattributes").document(dish)
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            attrSum = Float.parseFloat(document.getData().get("sum").toString());
                            attrAvg = Float.parseFloat(document.getData().get("average").toString());
                            usercnt = Integer.parseInt(document.getData().get("usercnt").toString());
                        }
                    }
                });

                // uid에 Comment 저장
                db.collection("User").document(uid)
                        .set(cmt, SetOptions.merge())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // 성공했을 때 리뷰 불러오기
                                readComment(cmmt, dish, adapter);
//                                db.collection("User")
//                                        .get()
//                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                                if (task.isSuccessful()) {
//                                                    cmmt.clear();
//                                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                                        try{
//                                                            cmmt.add(document.getData().get(dish).toString());
//                                                            Log.d("TAG", document.getId() + " => " + document.getData().get(dish));
//                                                        }
//                                                        catch (NullPointerException e){
//
//                                                        }
//
//                                                    }
//                                                } else {
//                                                    Log.d("TAG", "Error getting documents: ", task.getException());
//                                                }
//                                                adapter.notifyDataSetChanged();
//                                                lv.setAdapter(adapter);
//                                            }
//                                        });
                            }
                        });

                // uid에 Rating 저장

                // SingleDish sd = document.toObject(SingleDish.class);


                db.collection("User").document(uid)
                        .collection("SDset").document("rating")
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                Map<String, Object> attr = new HashMap<>();
                                Map<String, Object> rt = new HashMap<>();
                                rt.put(dish, rvSD.getRating());

                                DocumentSnapshot document = task.getResult();

                                // 이전에 별점이 있다면
                                try
                                {
                                    Float prev_rt = Float.parseFloat(document.getData().get(dish).toString());
                                    attrSum -= prev_rt;
                                    attrSum += rvSD.getRating();
                                    attrAvg = (attrSum/usercnt)*100/100.0f;

                                    attr.put("average", attrAvg);
                                    attr.put("sum", attrSum);

                                    db.collection("SDattributes").document(dish)
                                            .set(attr, SetOptions.merge());
                                    db.collection("User").document(uid).collection("SDset").document("rating")
                                            .set(rt, SetOptions.merge());
                                    Toast.makeText(getApplicationContext(), "리뷰가 수정되었습니다.", Toast.LENGTH_SHORT).show();

                                }
                                catch(NullPointerException e) {
                                    attrSum += rvSD.getRating();
                                    usercnt += 1;
                                    attrAvg = (attrSum / usercnt) * 100 / 100.0f;

                                    attr.put("average", attrAvg);
                                    attr.put("sum", attrSum);
                                    attr.put("usercnt", usercnt);

                                    db.collection("SDattributes").document(dish)
                                            .set(attr, SetOptions.merge());
                                    db.collection("User").document(uid).collection("SDset").document("rating")
                                            .set(rt, SetOptions.merge());
                                    Toast.makeText(getApplicationContext(), "리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show();

                                }
                            } else {
                                Log.d("TAG", "get failed with ", task.getException());
                            }
                            db.collection("SDattributes").document(dish)
                                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                     @Override
                                                                     public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                         if (task.isSuccessful()) {
                                                                             DocumentSnapshot document = task.getResult();
                                                                             rbSD.setRating(Float.parseFloat(document.getData().get("average").toString()));
                                                                         }
                                                                     }
                                                                 });



                        }
                });
            }
        });

        dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        switch (dish){
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

        img.setImageResource(imageId);
        rbSD.setRating(average);
        tvDish.setText(dishName);
        tvPrice.setText(price);


        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialogView.getParent() != null) {
                    ((ViewGroup) dialogView.getParent()).removeView(dialogView);
                }
                dlg.show();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    void readComment(ArrayList cmmt, String dish, ListAdapter adapter){
        db.collection("User")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            cmmt.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try{
                                    cmmt.add(document.getData().get(dish).toString());
                                    Log.d("TAG", document.getId() + " => " + document.getData().get(dish));
                                }
                                catch (NullPointerException e){

                                }

                            }
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }
                        //adapter.notifyDataSetChanged();
                        lv.setAdapter(adapter);
                    }
                });
    }
}