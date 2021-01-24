package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.interiordesigner.CardsAdapters.ProjectCardAdapter;
import com.interiordesigner.Classes.Project;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private List<Project> projects;

    private RecyclerView projectsRecycler;
    private ProjectCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //this.deleteDatabase("InteriorDesigner");
        databaseHelper = new DatabaseHelper(this);
        projects = databaseHelper.GetProjects();
        projectsRecycler = findViewById(R.id.recyclerProjectsView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        projects = databaseHelper.GetProjects();
        adapter = new ProjectCardAdapter(projects);
        projectsRecycler.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        projectsRecycler.setLayoutManager(linearLayoutManager);

        adapter.setListener(new ProjectCardAdapter.Listener() {
            @Override
            public void onClick(int id) {
                Intent intent = new Intent(StartActivity.this, MainActivity.class);
                intent.putExtra(MainActivity.EXTRA_PROJECT_ID, id);
                startActivity(intent);
            }
        });
    }


    public void onClickCatalog(View v) {
        Intent intent = new Intent(this, CatalogActivity.class);
        startActivity(intent);
    }

    public void onClickNewProject(View view) {
        Intent intent = new Intent(this, CreateProjectActivity.class);
        startActivity(intent);
    }
}
