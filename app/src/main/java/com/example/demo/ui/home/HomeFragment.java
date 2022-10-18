package com.example.demo.ui.home;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.demo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private String htmlPageUrl = "https://www.hanbat.ac.kr/prog/carteGuidance/kor/sub06_030301/C1/getCalendar.do"; //파싱할 홈페이지의 URL주소
    private TextView tvMenuMon, tvMenuTue, tvMenuWed, tvMenuThu, tvMenuFri, tvDay;
    private ViewFlipper vfMenu;
    private ImageButton btnPrev, btnNext;
    private String htmlContentInStringFormat="";
    private String menu1, menu2, menu3, menu4, menu5;
    String day[] = {"월요일", "화요일", "수요일", "목요일", "금요일"};
    int dayindex=0;

    int cnt=0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        tvMenuMon = root.findViewById(R.id.tvMenuMon);
        tvMenuTue = root.findViewById(R.id.tvMenuTue);
        tvMenuWed = root.findViewById(R.id.tvMenuWed);
        tvMenuThu = root.findViewById(R.id.tvMenuThu);
        tvMenuFri = root.findViewById(R.id.tvMenuFri);
        tvDay = root.findViewById(R.id.tvDay);

        btnPrev = root.findViewById(R.id.btnPrev);
        btnNext = root.findViewById(R.id.btnNext);

        vfMenu = root.findViewById(R.id.vfMenu);
        tvDay.setText(day[dayindex]);

//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        // textviewHtmlDocument.setMovementMethod (new ScrollingMovementMethod()); //스크롤 가능한 텍스트뷰로 만들기
        System.out.println( (cnt+1) +"번째 파싱");
        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();
        cnt++;

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayindex--;
                if(dayindex<0) dayindex=4;
                tvDay.setText(day[dayindex]);

                vfMenu.showPrevious();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dayindex++;
                if(dayindex>4) dayindex=0;
                tvDay.setText(day[dayindex]);

////                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
////                String user_uId = currentUser.getUid();
////                System.out.println("**********"+user_uId);
//
////                ------------------------------------------------------------
//                FirebaseFirestore db = FirebaseFirestore.getInstance();
////                db.collection("User")
////                        //.whereNotEqualTo("num", null)
////                        .get()
////                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////                            @Override
////                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                                if (task.isSuccessful()) {
////                                    for (QueryDocumentSnapshot document : task.getResult()) {
////                                        if(document.getData().get("number") != null)
////                                            Log.d("hwang11", document.getId() + " => " + (Integer.parseInt(document.getData().get("number").toString()) + 1));
////                                        //System.out.println(document.getId() + " => " + (Integer.parseInt(document.getData().get("num").toString()) + 1));
////                                        if(document.getData().get("number") == null)
////                                            Log.d("hwang22", document.getId() + " => " + document.getData().get("number"));
////                                    }
////                                } else {
////                                    Log.d("hwang", "Error getting documents: ", task.getException());
////                                }
////                            }
////                        });
//                db.collection("User")
//                        .get()
//                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                                if (task.isSuccessful()) {
//                                    for (QueryDocumentSnapshot document : task.getResult()) {
//                                        if(document.getData().get("email") != null)
//                                            Log.d("TAG", document.getId() + " => " + document.getData().get("email"));
//                                        else
//                                            Log.d("TAG", document.getId() + " => " + "NULL VALUE");
//
//                                    }
//                                } else {
//                                    Log.d("TAG", "Error getting documents: ", task.getException());
//                                }
//                            }
//                        });
//
//                Map<String, Object> data = new HashMap<>();
//                data.put("capital", true);
//                data.put("test", 55);
//
//                db.collection("User").document("dafadf")
//                        .set(data, SetOptions.merge());


                vfMenu.showNext();
            }
        });

        return root;
    }

    private class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            System.out.println("start");
            try {
                Long l = System.currentTimeMillis();

                Document doc = Jsoup.connect(htmlPageUrl)
                        .header("Content-Type", "application/json")
                        .method(Connection.Method.POST)
                        .data("item", l.toString()).post();

                String jsonString = doc.body().text();
                JSONObject jObject = new JSONObject(jsonString);

                JSONArray jArray = jObject.getJSONArray("item");


                JSONObject obj = jArray.getJSONObject(1);
                menu1 = obj.getString("menu1");
                menu2 = obj.getString("menu2");
                menu3 = obj.getString("menu3");
                menu4 = obj.getString("menu4");
                menu5 = obj.getString("menu5");


                System.out.println();

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

            System.out.println("end");

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            tvMenuMon.setText(menu1);
            tvMenuTue.setText(menu2);
            tvMenuWed.setText(menu3);
            tvMenuThu.setText(menu4);
            tvMenuFri.setText(menu5);
            // textviewHtmlDocument.setText(htmlContentInStringFormat);
        }
    }
}