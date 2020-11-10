package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.interiordesigner.CardsAdapters.FurnitureCardAdapter;
import com.interiordesigner.Classes.Category;
import com.interiordesigner.Classes.Furniture;

public class FurniturePreviewActivity extends AppCompatActivity {
    public static final String EXTRA_CATEGORY_ID = "category_id";
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furnitures_list);
        int categoryId = (Integer) getIntent().getExtras().get(EXTRA_CATEGORY_ID);

        category = Category.GetById(categoryId);

        TextView categoryNameTxt = findViewById(R.id.categoryName);
        categoryNameTxt.setText(category.GetName());

        RecyclerView furnituresRecycler = findViewById(R.id.furnituresRecycler);
        FurnitureCardAdapter adapter = new FurnitureCardAdapter(Furniture.GetByCategoryId(categoryId));
        furnituresRecycler.setAdapter(adapter);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        furnituresRecycler.setLayoutManager(layoutManager);
        adapter.setListener(new FurnitureCardAdapter.Listener() {
            @Override
            public void onClick(int id) {
                Intent intent = new Intent(FurniturePreviewActivity.this, FurnitureDetails.class);
                intent.putExtra(FurnitureDetails.EXTRA_FURNITURE_ID, id);
                startActivity(intent);
            }
        });

    }

}
