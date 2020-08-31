package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.interiordesigner.Classes.Project;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_PROJECT_ID = "projectId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int projectId = (Integer) getIntent().getExtras().get(EXTRA_PROJECT_ID);
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Project project = databaseHelper.GetProject(projectId);

        TextView projectName = findViewById(R.id.projectNameTxt);
        TextView projectDescription = findViewById(R.id.descriptionTxt);

        projectName.setText(project.GetName());
        projectDescription.setText(project.GetDescription());

    }

    public void onClickBackToMenu(View view) {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }
}
