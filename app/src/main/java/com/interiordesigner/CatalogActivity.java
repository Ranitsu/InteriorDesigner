package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.interiordesigner.Classes.Category;

import java.util.ArrayList;
import java.util.List;

public class CatalogActivity extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase db;
    private Cursor cursor;
    private List<Category> categories;
    private FrameLayout recyclerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        recyclerContainer = findViewById(R.id.recyclerContainer);

        databaseHelper = new DatabaseHelper(this);
        categories = GetBasicCategories();

        InitRecycler();
    }

    public void SetRecycler(int parentCategoryId) {
        CreateRecycler(parentCategoryId, false);
    }

    private void InitRecycler() {
        CreateRecycler(0, true);
    }

    private void CreateRecycler(int parentCategoryId, boolean isInitRecycler) {
        Fragment categoryRecycler = new CategoryFragment(parentCategoryId);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.recyclerContainer, categoryRecycler);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        if (!isInitRecycler)
            ft.addToBackStack(null);
        ft.commit();
    }

    public List<Category> GetBasicCategories() {
        return GetCategoriesByParentId(0);
    }

    public List<Category> GetCategoriesByParentId(int whereParentId) {
        List<Category> categories = new ArrayList<>();

        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("Category", new String[] {"_id", "Name", "ParentId"},
                    "ParentId = ?", new String[] {Integer.toString(whereParentId)},
                    null, null, null);
            categories = GetCategories();
        } catch (SQLiteException ex) {
            Toast toast = Toast.makeText(this, R.string.DB_notAvailable, Toast.LENGTH_SHORT);
            toast.show();
        }

        return categories;
    }

    public List<Category> GetAllCategories() {
        List<Category> categories = new ArrayList<>();

        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("Category", new String[] {"_id", "Name", "ParentId"},
                    null, null,
                    null, null, null);
            categories = GetCategories();
        } catch (SQLiteException ex) {
            Toast toast = Toast.makeText(this, R.string.DB_notAvailable, Toast.LENGTH_SHORT);
            toast.show();
        }

        return categories;
    }

    private List<Category> GetCategories() {
        List<Category> categories = new ArrayList<>();

        try {
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
