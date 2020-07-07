package com.example.likedislike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HomeActivity extends AppCompatActivity {
    Spinner subjectSpinner;
    Button listButton, searchButton;
    ListView listView;

    static boolean calledAlready = false;

    private ArrayList<LDLObject> dataSet = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!calledAlready) {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true); // 다른 인스턴스보다 먼저 실행되어야 한다.
            calledAlready = true;
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        subjectSpinner = findViewById(R.id.subjectSpinner);
        subjectSpinner.setSelection(0);
        listButton = findViewById(R.id.likeButton);
        searchButton = findViewById(R.id.searchButton);
        listView = findViewById(R.id.listView);

        final LDLAdapter ldlAdapter = new LDLAdapter(getApplicationContext(), dataSet);
        listView.setAdapter(ldlAdapter);

        dataSet.add(new LDLObject("ha", "실험용", 0, 0));

        myRef.child("likeDislike").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String email = fileSnapshot.child("user").getValue(String.class);
                    String text = fileSnapshot.child("text").getValue(String.class);
                    int like = fileSnapshot.child("like").getValue(Integer.class);
                    int dislike = fileSnapshot.child("dislike").getValue(Integer.class);

                    LDLObject ldl = new LDLObject(email,text,like,dislike);
                    dataSet.add(ldl);
                }
                ldlAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(HomeActivity.this, "실패", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

class LDLObject {
    String email;
    String text;
    int like=0;
    int dislike=0;

    LDLObject(){}

    LDLObject(String email, String text, int like, int dislike) {
        this.email = email;
        this.text = text;
        this.like = like;
        this.dislike = dislike;
    }
}

    class LDLAdapter extends BaseAdapter {
        private ArrayList<LDLObject> dataSet;
        Context context;

        public LDLAdapter(Context context, ArrayList<LDLObject> dataSet) {
            super();
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
            emailView.setText(ldl.email);
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
        
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.listview_item, null);
        TextView emailView = convertView.findViewById(R.id.emailView);
        TextView textView = convertView.findViewById(R.id.textView);
        final Button likeButton = convertView.findViewById(R.id.likeButton);
        final Button disLikeButton = convertView.findViewById(R.id.disLikeButton);

        final LDLObject ldl = dataSet.get(position);
        emailView.setText(ldl.email);
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


