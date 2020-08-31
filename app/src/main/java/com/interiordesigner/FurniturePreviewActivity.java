package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.interiordesigner.Classes.Category;

import java.util.ArrayList;
import java.util.List;

public class FurniturePreviewActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY_ID = "category_id";
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture_preview);
        databaseHelper = new DatabaseHelper(this);
        int categoryId = (Integer) getIntent().getExtras().get(EXTRA_CATEGORY_ID);

        List<Category> allCategories = GetAllCategories();
        for (Category category : allCategories) {
            if (category.GetId() == categoryId)
            {
                this.category = category;
                break;
            }

        }

        TextView categoryNameTxt = findViewById(R.id.categoryName);
        categoryNameTxt.setText(category.GetName());
    }

    public List<Category> GetAllCategories() {
        List<Category> categories = new ArrayList<>();

        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("Category", new String[] {"_id", "Name", "ParentId"},
                    null, null,
                    null, null, null);
            if (cursor.moveToFirst()) {
                while(!cursor.isAfterLast()) {
                    int id = cursor.getInt(0);
                    String name = cursor.getString(1);
                    int parentId = cursor.getInt(2);

                    Category category = new Category(id, name, parentId);
                    categories.add(category);

                    cursor.moveToNext();
                }
            }
        } catch (SQLiteException ex) {
            Toast toast = Toast.makeText(this, R.string.DB_notAvailable, Toast.LENGTH_SHORT);
            toast.show();
        }

        return categories;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}
