package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.interiordesigner.Classes.Project;

public class CreateProjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_project);
    }

    public void onClickCreate(View view) {
        EditText editName = findViewById(R.id.editTextName);
        EditText editDescription = findViewById(R.id.editTextDescription);

        String name = editName.getText().toString();
        String description = editDescription.getText().toString();

        Project project = new Project(name, description);

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        dbHelper.AddProject(db, project);

        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    public void onClickCancel(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
