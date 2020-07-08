package com.example.likedislike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    Spinner subjectSpinner;
    Button searchButton;
    ListView listView;
    ImageButton listButton,plusButton;

    public final static String TAG = "Yahoo";
    static boolean calledAlready = false;

    private ArrayList<LDLObject> dataSet = new ArrayList<>();
    private LDLAdapter ldlAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Map<String,Object> newInfo = null;
    final Map childUpdate = new HashMap();

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
            calledAlready = true;
        }

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("likedislike"); // db 주소 저장

        subjectSpinner = findViewById(R.id.subjectSpinner);
        subjectSpinner.setSelection(0);
        listButton = findViewById(R.id.likeButton);
        searchButton = findViewById(R.id.searchButton);
        listView = findViewById(R.id.listView);
        plusButton = findViewById(R.id.plusButton);

        ldlAdapter = new LDLAdapter(getApplicationContext(), dataSet);
        listView.setAdapter(ldlAdapter);

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent writeIntent = new Intent(getApplicationContext(),WriteActivity.class);
                startActivityForResult(writeIntent,1);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSet != null) {
                    dataSet.clear();
                }
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String user = fileSnapshot.child("user").getValue(String.class);
                    String text = fileSnapshot.child("text").getValue(String.class);
                    Long like = fileSnapshot.child("like").getValue(Long.class);
                    Long dislike = fileSnapshot.child("dislike").getValue(Long.class);
                    if(like == null){
                        like = 0l;
                    }
                    if(dislike == null){
                        dislike = 0l;
                    }
                    LDLObject ldl = new LDLObject(user, text, like, dislike);
                    newInfo = ldl.toMap();
                    dataSet.add(ldl);
                    Log.d(TAG,""+dataSet.size());
                }
                ldlAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Long like = 0l;
        Long dislike = 0l;
        String text =data.getStringExtra("text");
        String user =data.getStringExtra("user");
        LDLObject ldlOb = new LDLObject(user,text,like,dislike);
        newInfo = ldlOb.toMap();
        dataSet.add(ldlOb);
        childUpdate.put("post"+(dataSet.size()-1),newInfo);
        myRef.updateChildren(childUpdate);
        childUpdate.clear();
        Log.d(TAG,""+dataSet.size());
    }

    public class LDLObject {
        String user = "null";
        String text = "null";
        long like;
        long dislike;

        LDLObject(){} // 기본 생성자

        public LDLObject(String user, String text, Long like, Long dislike) {
            Log.d("Yahoo", "LDLObject 생성");
            this.user = user;
            this.text = text;
            this.like = like;
            this.dislike = dislike;
        }

        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("user", user);
            result.put("text", text);
            result.put("like", like);
            result.put("dislike", dislike);
            return result;
        }
    }

    public class LDLAdapter extends BaseAdapter {
        private ArrayList<LDLObject> dataSet;
        Context context;
        Map<String,Object> info = null;

        public LDLAdapter(Context context, ArrayList<LDLObject> dataSet) {
            super();
            Log.d("Yahoo", "LDLAdapter 생성");
            this.context = context;
            this.dataSet = dataSet;
        }

        @Override
        public int getCount() {
            return dataSet.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, null);
            TextView emailView = convertView.findViewById(R.id.emailView);
            TextView textView = convertView.findViewById(R.id.textView);
            final Button likeButton = convertView.findViewById(R.id.likeButton);
            final Button disLikeButton = convertView.findViewById(R.id.disLikeButton);

            final LDLObject ldl = dataSet.get(position);
            emailView.setText(ldl.user);
            textView.setText(ldl.text);
            likeButton.setText("LIKE : "+ldl.like);
            disLikeButton.setText("DISLIKE : "+ldl.dislike);

                likeButton.setOnClickListener(new View.OnClickListener() { // 좋아요 값 바뀔때
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "LIKE 선택", Toast.LENGTH_SHORT).show();
                        ++ldl.like;
                        info = ldl.toMap();
                        childUpdate.put("post" + position, info);
                        Log.d("Yahoo",""+position);
                        myRef.updateChildren(childUpdate);
                        notifyDataSetChanged();
                        likeButton.setEnabled(false);
                        disLikeButton.setEnabled(false);
                    }
                });

                disLikeButton.setOnClickListener(new View.OnClickListener() { // 싫어요 값 바뀔때
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getApplicationContext(), "DISLIKE 선택", Toast.LENGTH_SHORT).show();
                        ++ldl.dislike;
                        info = ldl.toMap();
                        childUpdate.put("post" + position, info);
                        Log.d("Yahoo",""+position);
                        myRef.updateChildren(childUpdate);
                        notifyDataSetChanged();
                        likeButton.setEnabled(false);
                        disLikeButton.setEnabled(false);
                    }
                });
            return convertView;
        }
    }
}