package com.example.readwritexml;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class ReadXMLfile extends AppCompatActivity {

    ListView l_view;
    ArrayList<String> people = new ArrayList<>();
    TextView tv_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_xmlfile);

        l_view = findViewById(R.id.l_view);
        //tv_res = findViewById(R.id.tv_res);


        String size = getIntent().getStringExtra("size");
        //tv_res.setText(size);

        for(int i=0;i<Integer.parseInt(size);i++)
        {
            String str = "people" + String.valueOf(i);
            Parcelable parc = getIntent().getParcelableExtra(str);
            Person p = Parcels.unwrap(parc);
            people.add(p.toString());
        }


        ArrayAdapter adapter = new ArrayAdapter(ReadXMLfile.this, android.R.layout.simple_list_item_1,people);
        l_view.setAdapter(adapter);
        //Toast.makeText(this, i.getStringExtra("my_obj"), Toast.LENGTH_SHORT).show();




    }
}