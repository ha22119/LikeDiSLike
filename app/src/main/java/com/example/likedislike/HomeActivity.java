package com.example.likedislike;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    Spinner subjectSpinner;
    Button listButton,searchButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        subjectSpinner = findViewById(R.id.subjectSpinner);
        subjectSpinner.setSelection(0);
        listButton = findViewById(R.id.likeButton);
        searchButton = findViewById(R.id.searchButton);
        listView = findViewById(R.id.listView);

        ArrayList<LDLObject> dataSet = new ArrayList<>();
        LDLAdapter ldlAdapter = new LDLAdapter(getApplicationContext(), dataSet);

        LDLObject ldl1 = new LDLObject("s19060@gsm.hs.kr","실험용 아무말이 들어갈 장소로 아무말이나 넣자 야호 오늘 저녁 삼겹살이면 좋겠다 배고프다");
        LDLObject ldl2 = new LDLObject("YaHoo@gsm.hs.kr","내 멘탈 와장창 콰장창 공무원 되서 코딩 안하고 싶다 런이ㅓㅇ나렁나ㅓㅇ너ㅣㅇㄴ린ㅇ린ㅇㅁ");
        dataSet.add(ldl1);
        dataSet.add(ldl2);

        listView.setAdapter(ldlAdapter);
    }
}
class LDLObject{
    String email;
    String text;

    LDLObject(String email,String text){
        this.email= email;
        this.text= text;
    }
}
class LDLAdapter extends BaseAdapter {
    private ArrayList<LDLObject> dataSet;
    Context context;

    public LDLAdapter(Context context, ArrayList<LDLObject> dataSet){
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
    public View getView(final int position,View convertView,ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item,null);
            TextView emailView = convertView.findViewById(R.id.emailView);
            TextView textView = convertView.findViewById(R.id.textView);

            LDLObject ldl = dataSet.get(position);
            emailView.setText(ldl.email);
            textView.setText(ldl.text);
        return convertView;
    }
}


