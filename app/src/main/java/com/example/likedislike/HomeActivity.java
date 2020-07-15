package com.example.likedislike;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeActivity extends AppCompatActivity {
    ListView listView;
    ImageButton signOutButton,plusButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    TextView seeEmail;

    public final static String TAG = "Yahoo";
    static boolean calledAlready = false;

    private ArrayList<LDLObject> dataSet = new ArrayList<>();
    private LDLAdapter ldlAdapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    Map<String,Object> newInfo = null;
    final Map childUpdate = new HashMap();

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

        signOutButton = findViewById(R.id.signOutButton);
        listView = findViewById(R.id.listView);
        plusButton = findViewById(R.id.plusButton);
        seeEmail=findViewById(R.id.seeEmail);

        ldlAdapter = new LDLAdapter(getApplicationContext(), dataSet);
        listView.setAdapter(ldlAdapter);

        seeEmail.setText(currentUser.getEmail().toString());

        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent writeIntent = new Intent(getApplicationContext(),WriteActivity.class);
                startActivityForResult(writeIntent,1);
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Log.d(TAG,"로그아웃 함");
                Toast.makeText(HomeActivity.this, "로그아웃 됨", Toast.LENGTH_SHORT).show();
                Intent LogOutIntent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(LogOutIntent);
            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSet != null) {
                    dataSet.clear();
                }
                for (DataSnapshot fileSnapshot : dataSnapshot.getChildren()) {
                    String title = fileSnapshot.child("title").getValue(String.class);
                    String text = fileSnapshot.child("text").getValue(String.class);
                    Long like = fileSnapshot.child("like").getValue(Long.class);
                    Long dislike = fileSnapshot.child("dislike").getValue(Long.class);
                    String likeUsers = fileSnapshot.child("likeUsers").getValue(String.class);
                    String dislikeUsers = fileSnapshot.child("dislikeUsers").getValue(String.class);
                    if(like == null){
                        like = 0l;
                    }
                    if(dislike == null){
                        dislike = 0l;
                    }
                    if(likeUsers == null){
                        likeUsers = "LiKE";
                    }
                    if(dislike == null){
                        dislikeUsers = "DiSLiKE";
                    }
                    LDLObject ldl = new LDLObject(title, text, like, dislike,likeUsers,dislikeUsers);
                    newInfo = ldl.toMap();
                    dataSet.add(ldl);
                    Log.d(TAG, "" + dataSet.size());
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
        currentUser = mAuth.getCurrentUser();
        Long like = 0l;
        Long dislike = 0l;
        String text =data.getStringExtra("text");
        String title =data.getStringExtra("title");
        String likeUsers = "LiKE";
        String dislikeUsers = "DiSLiKE";
        LDLObject ldlOb = new LDLObject(title,text,like,dislike,likeUsers,dislikeUsers);
        newInfo = ldlOb.toMap();
        dataSet.add(ldlOb);
        childUpdate.put("post"+(dataSet.size()-1),newInfo);
        myRef.updateChildren(childUpdate);
        childUpdate.clear();
        Log.d(TAG,""+dataSet.size());
    }

    public class LDLObject {
        String title = "title";
        String text = "null";
        long like = 0l;
        long dislike = 0l;
        String likeUsers = "LiKE";
        String dislikeUsers = "DiSLiKE";

        LDLObject(){} // 기본 생성자

        public LDLObject(String title, String text, Long like, Long dislike,String likeUsers,String dislikeUsers) {
            Log.d("Yahoo", "LDLObject 생성");
            this.title = title;
            this.text = text;
            this.like = like;
            this.dislike = dislike;
            this.likeUsers = likeUsers;
            this.dislikeUsers = dislikeUsers;
        }

        public Map<String, Object> toMap() {
            HashMap<String, Object> result = new HashMap<>();
            result.put("title", title);
            result.put("text", text);
            result.put("like", like);
            result.put("dislike", dislike);
            result.put("likeUsers", likeUsers);
            result.put("dislikeUsers", dislikeUsers);
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
            emailView.setText(ldl.title);
            textView.setText(ldl.text);
            likeButton.setText("LIKE : "+ldl.like);
            disLikeButton.setText("DISLIKE : "+ldl.dislike);
            final String ldl_like = ldl.likeUsers;
            final String ldl_dislike = ldl.dislikeUsers;

            likeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("yahoo",ldl_like);
                    Log.d("yahoo",ldl_dislike);
                        if (ldl.likeUsers.contains(currentUser.getEmail()) || ldl.dislikeUsers.contains(currentUser.getEmail())) {
                            Toast.makeText(HomeActivity.this, "이미 투표한 글 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "LIKE 선택", Toast.LENGTH_SHORT).show();
                            ++ldl.like;
                            ldl.likeUsers = ldl.likeUsers + "\n" + currentUser.getEmail();
                            info = ldl.toMap();
                            childUpdate.put("post" + position, info);
                            Log.d("Yahoo", "" + position);
                            myRef.updateChildren(childUpdate);
                            notifyDataSetChanged();
                        }
                    }
            });

            disLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("yahoo",currentUser.getEmail());
                    Log.d("yahoo",ldl_like);
                    Log.d("yahoo",ldl_dislike);
                    if (ldl_like.contains(currentUser.getEmail()) || ldl_dislike.contains(currentUser.getEmail())) {
                            Toast.makeText(HomeActivity.this, "이미 투표한 글 입니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(HomeActivity.this, "DISLIKE 선택", Toast.LENGTH_SHORT).show();
                            ++ldl.dislike;
                            ldl.dislikeUsers = ldl.dislikeUsers + "\n" + currentUser.getEmail();
                            info = ldl.toMap();
                            childUpdate.put("post" + position, info);
                            Log.d("Yahoo", "" + position);
                            myRef.updateChildren(childUpdate);
                            notifyDataSetChanged();
                        }
                    }
            });

                likeButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                    CustomDialog likeDialog = new CustomDialog(HomeActivity.this);
                    likeDialog.callCustomDlg_like(ldl.likeUsers);
                    return false;
                    }
                });

                disLikeButton.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        // 누른 사람 보여주기
                        CustomDialog dislikeDialog = new CustomDialog(HomeActivity.this);
                        dislikeDialog.callCustomDlg_dislike(ldl.dislikeUsers);
                        return false;
                    }
                });
                return convertView;
        }
    }

    public class CustomDialog {
        private Context context;

        public CustomDialog(Context context) {
            this.context = context;
        }
        public void callCustomDlg_like(String text) {
            final Dialog dlg = new Dialog(context);
            dlg.setContentView(R.layout.like_custim_dialog);
            AppCompatTextView like_textView =dlg.findViewById(R.id.like_textView);
            like_textView.setText(text);
            dlg.show();
        }
        public void callCustomDlg_dislike(String text) {
            final Dialog dlg = new Dialog(context);
            dlg.setContentView(R.layout.dislike_custom_dialog);
            AppCompatTextView dislike_textView =dlg.findViewById(R.id.dislike_textView);
            dislike_textView.setText(text);
            dlg.show();
        }
    }
}