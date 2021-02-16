package com.interiordesigner;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.interiordesigner.CardsAdapters.CategoryCardAdapter;
import com.interiordesigner.Classes.Category;

public class CategoryPlanFragment extends Fragment {
    private Category[] categories;
    private int parentId;

    public CategoryPlanFragment(int parentId) {
        this.parentId = parentId;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_category,
                container, false);

        RecyclerView categoryRecycler = layout.findViewById(R.id.mainCategoriesRecycler);

        categories = Category.GetByParentId(parentId);

        CategoryCardAdapter adapter = new CategoryCardAdapter(categories);
        categoryRecycler.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        categoryRecycler.setLayoutManager(layoutManager);
        adapter.setListener(new CategoryCardAdapter.Listener() {
            @Override
            public void onClick(int id) {
                categories = Category.GetByParentId(id);
                if (categories.length == 0) {
                    Intent intent = new Intent(getActivity(), FurniturePreviewActivity.class);
                    intent.putExtra(FurniturePreviewActivity.EXTRA_CATEGORY_ID, id);
                    startActivity(intent);
                } else {
                    ((CatalogActivity)getActivity()).SetRecycler(id);
                }
            }
        });

        return layout;
    }
}