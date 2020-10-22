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

import java.util.List;


public class CategoryFragment extends Fragment {
    private List<Category> categories;
    private int parentId;

    public CategoryFragment(int parentId) {
        this.parentId = parentId;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_category,
                                                                    container, false);

        TextView categoryNameText = layout.findViewById(R.id.categoryName);
        RecyclerView categoryRecycler = layout.findViewById(R.id.mainCategoriesRecycler);

        if (parentId == 0) {
            categoryNameText.setText(R.string.allCategory);
        } else {
            String categoryName = "NO NAME";
            List<Category> allCategories = ((CatalogActivity)getActivity()).GetAllCategories();
            for (Category category : allCategories) {
                if (category.GetId() == parentId)
                {
                    categoryName = category.GetName();
                    break;
                }

            }

            categoryNameText.setText(categoryName);
        }


        categories = ((CatalogActivity)getActivity()).GetCategoriesByParentId(parentId);

        CategoryCardAdapter adapter = new CategoryCardAdapter(categories);
        categoryRecycler.setAdapter(adapter);

        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        categoryRecycler.setLayoutManager(layoutManager);
        adapter.setListener(new CategoryCardAdapter.Listener() {
            @Override
            public void onClick(int id) {
                categories = ((CatalogActivity)getActivity()).GetCategoriesByParentId(id);
                if (categories.isEmpty()) {
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
