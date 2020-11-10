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
    private Category[] categories;
    private FrameLayout recyclerContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);
        recyclerContainer = findViewById(R.id.recyclerContainer);

        categories = Category.GetByParentId(0);

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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
