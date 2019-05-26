package com.example.memorableplaces;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static ListView listView;
    static ArrayList<LatLng> locations = new ArrayList<>();
    static ArrayList<String> places = new ArrayList<>();
    static ArrayList<String> lat;
    static ArrayList<String> lng;
    static ArrayAdapter arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = this.getSharedPreferences("com.example.memorableplaces", Context.MODE_PRIVATE);

        places.clear();
        lat.clear();
        lng.clear();
        locations.clear();

        try {
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places", ObjectSerializer.serialize(new ArrayList<>())));

            lat = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats", ObjectSerializer.serialize(new ArrayList<>())));
            lng = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lngs", ObjectSerializer.serialize(new ArrayList<>())));

        } catch (IOException e) {
            e.printStackTrace();
        }

        if (places.size() > 0 && lat.size() > 0 && lng.size() > 0) {
            if (places.size() == lat.size() && places.size() == lng.size()) {
                for (int i = 0; i < places.size(); i++) {
                    locations.add(new LatLng(Double.parseDouble(lat.get(i)), Double.parseDouble(lng.get(i))));
                }
            } else {
                places.add("Add a new place...");
                locations.add(new LatLng(0, 0));
            }
        }

        listView = findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, places);

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("placeNumber", position);
                startActivity(intent);
            }
        });
    }
}
