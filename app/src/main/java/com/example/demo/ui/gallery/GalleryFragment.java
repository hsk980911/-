package com.example.demo.ui.gallery;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo.R;
import com.example.demo.ui.slideshow.MyAdapter;
import com.example.demo.ui.slideshow.SingleDish;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private Button btnRg;
    private RatingBar rbReview;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    ArrayList<Dish> dDataset = new ArrayList<>();

    float attrSum, attrAvg;
    int usercnt;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    String uid = currentUser.getUid();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                new ViewModelProvider(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        btnRg = (Button) root.findViewById(R.id.btnRg);
        rbReview = (RatingBar) root.findViewById(R.id.rbReview);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.dish_recycler_view);

        Date today = new Date();
        final SimpleDateFormat date = new SimpleDateFormat("yyyy.MM.dd");
        System.out.println(date.format(today));

        btnRg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Dattributes").document(date.format(today))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            try {
                                attrSum = Float.parseFloat(document.getData().get("sum").toString());
                                attrAvg = Float.parseFloat(document.getData().get("average").toString());
                                usercnt = Integer.parseInt(document.getData().get("usercnt").toString());
                                Log.d("Dattributes", attrSum+" "+attrAvg+" "+usercnt, task.getException());

                            }catch (NullPointerException e){
                                attrSum=0;
                                attrAvg=0;
                                usercnt=0;
                                Log.d("Dattributes", attrSum+" "+attrAvg+" "+usercnt, task.getException());
                            }

                        }
                    }
                });

                db.collection("Deval").document(date.format(today))
                        .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> attr = new HashMap<>();
                            Map<String, Object> rb = new HashMap<>();

                            rb.put(uid, rbReview.getRating());

                            DocumentSnapshot document = task.getResult();

                            // 이전에 리뷰가 있다면
                            try
                            {
                                Float prev_rt = Float.parseFloat(document.getData().get(uid).toString());
                                attrSum -= prev_rt;
                                attrSum += rbReview.getRating();
                                attrAvg = (attrSum/usercnt)*100/100.0f;

                                attr.put("date", date.format(today));
                                attr.put("average", attrAvg);
                                attr.put("sum", attrSum);
                                attr.put("usercnt", usercnt);

                                db.collection("Dattributes").document(date.format(today))
                                        .set(attr, SetOptions.merge());
                                db.collection("Deval").document(date.format(today))
                                        .set(rb, SetOptions.merge());

                                Toast.makeText(getContext(), "리뷰가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                            catch(NullPointerException e) {
                                attrSum = rbReview.getRating();
                                usercnt += 1;
                                attrAvg = (attrSum / usercnt) * 100 / 100.0f;

                                attr.put("date", date.format(today));
                                attr.put("average", attrAvg);
                                attr.put("sum", attrSum);
                                attr.put("usercnt", usercnt);

                                db.collection("Dattributes").document(date.format(today))
                                        .set(attr, SetOptions.merge());
                                db.collection("Deval").document(date.format(today))
                                        .set(rb, SetOptions.merge());
                                Toast.makeText(getContext(), "리뷰가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d("TAG", "get failed with ", task.getException());
                        }
                        setdAdapter();
                    }
                });
            }
        });


        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


        setdAdapter();



        return root;
    }

    public void setdAdapter(){
        dDataset.clear();
        db.collection("Dattributes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Dish ds = document.toObject(Dish.class);
                                dDataset.add(ds);
                                Log.d("hwnag", ds.average + " " + ds.usercnt);
                            }
                        } else
                            Log.d("Log.d", "Failed" + "\n" + task.getException());


                        mAdapter = new DishAdapter(dDataset);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);
                    }
                });
    }
}