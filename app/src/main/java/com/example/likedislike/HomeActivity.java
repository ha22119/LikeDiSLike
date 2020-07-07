package com.example.likedislike;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    Spinner subjectSpinner;
    Button listButton, searchButton;
    ListView listView;

    public final static String TAG = "Yahoo";
    static boolean calledAlready = false;

    private ArrayList<LDLObject> dataSet = new ArrayList<>();
    private LDLAdapter ldlAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
            calledAlready = true;
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("likedislike"); // db 주소 저장

        subjectSpinner = findViewById(R.id.subjectSpinner);
        subjectSpinner.setSelection(0);
        listButton = findViewById(R.id.likeButton);
        searchButton = findViewById(R.id.searchButton);
        listView = findViewById(R.id.listView);

        ldlAdapter = new LDLAdapter(getApplicationContext(), dataSet);
        listView.setAdapter(ldlAdapter);

        myRef.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    Log.d(TAG,"ldl에 값 넣기");
                    String user = fileSnapshot.child("user").getValue(String.class);
                    String text = fileSnapshot.child("text").getValue(String.class);
                    Long like = fileSnapshot.child("like").getValue(Long.class);
                    Long dislike = fileSnapshot.child("dislike").getValue(Long.class);
                    LDLObject ldl = new LDLObject(user,text,like,dislike);
                    dataSet.add(ldl);
                }
                ldlAdapter.notifyDataSetChanged();
                Log.d(TAG,"값 바뀜요");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        }
    }

    class LDLObject {
        String user="null";
        String text="null";
        long like=0;
        long dislike=0;

        LDLObject(){} // 기본 생성자

        public LDLObject(String user, String text,Long like,Long dislike) {
            Log.d("Yahoo","LDLObject 생성");
            this.user = user;
            this.text = text;
            this.like =like;
            this.dislike = dislike;
        }
    }

    class LDLAdapter extends BaseAdapter {
        private ArrayList<LDLObject> dataSet;
        Context context;

        public LDLAdapter(Context context, ArrayList<LDLObject> dataSet) {
            super();
            Log.d("Yahoo","LDLAdapter 생성");
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

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeButton.setSelected(true);
                    disLikeButton.setSelected(false);
                    ldl.like++;
                }
            });

            disLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    likeButton.setSelected(false);
                    disLikeButton.setSelected(true);
                    ldl.dislike++;
                }
            });

            return convertView;
        }
    }


