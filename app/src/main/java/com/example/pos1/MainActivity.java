package com.example.pos1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    AppCompatButton mangerButton =  findViewById(R.id.mangerButtone);

        databaseHelper = new DatabaseHelper(this);

        // Get a writable database
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        // The users table will be created in the onCreate() method of DatabaseHelper class
//        listView = findViewById(R.id.listView);
//        databaseHelper = new DatabaseHelper(getApplicationContext());

        // Retrieve the data from the database
//        List<String> dataList = databaseHelper.getData();

        // Create an ArrayAdapter to display the data in the ListView
//        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);

        // Set the adapter to the ListView

//        listView.setAdapter(adapter);

// Inside your activity or fragment
        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        List<String> dataList = databaseHelper.getData(); // Get the data from your database
        dataList.add(0, "Name, Email");
        tableAdapter = new TableAdapter(dataList);
        recyclerView.setAdapter(tableAdapter);

        db.close();
        mangerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Manager.class);
                startActivity(intent);
            }
        });
    }
}



