package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.interiordesigner.CardsAdapters.CategoryCardAdapter;
import com.interiordesigner.Classes.CardType;
import com.interiordesigner.Classes.Category;
import com.interiordesigner.Classes.Furniture;
import com.interiordesigner.Classes.Point;
import com.interiordesigner.Classes.RoomPlan;
import com.interiordesigner.Interfaces.Card;
import com.interiordesigner.Views.FurnituresPlanEditorView;
import com.interiordesigner.Views.PlanEditorView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PlanFurnitureActivity extends AppCompatActivity {
    int projectId;
    RoomPlan roomPlan;
    DatabaseHelper databaseHelper;
    SQLiteDatabase db;
    FurnituresPlanEditorView editorView;
    RecyclerView recyclerView;
    Category[] categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_furniture);

        databaseHelper = new DatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();

        projectId = (Integer) getIntent().getExtras().get(MainActivity.EXTRA_PROJECT_ID);
        roomPlan = databaseHelper.GetRoomPlanByProjectId(projectId);

        editorView = findViewById(R.id.viewFurniturePlanEditor);
        recyclerView = findViewById(R.id.recyclerView);

        editorView.roomPlan = roomPlan;
        categories = Category.GetByParentId(0);

        CategoryPlanCardAdapter adapter = new CategoryPlanCardAdapter(categories);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setListener(new CategoryPlanCardAdapter.Listener() {
            @Override
            public void onClick(int id) {
                categories = Category.GetByParentId(id);
                if (categories.length == 0) {
                    Furniture[] furnitures = Furniture.GetByCategoryId(id);
                    adapter.addFurnitures(furnitures);
                } else {
                    adapter.addCategories(categories);
                }
            }
        });
    }

}

class CategoryPlanCardAdapter extends RecyclerView.Adapter<CategoryPlanCardAdapter.ViewHolder> {
    private List<Card> cards;
    private CategoryPlanCardAdapter.Listener listener;

    public CategoryPlanCardAdapter(Card[] cards) {
        this.cards = new ArrayList<>();
        this.cards.addAll(Arrays.asList(cards));
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void setListener(CategoryPlanCardAdapter.Listener listener) {
        this.listener = listener;
    }

    public void addCategories(Category[] categories) {
        if (this.cards.contains(categories[0])) {
            this.cards.removeAll(Arrays.asList(categories));
        } else {
            int parentId = categories[0].GetParentId();
            Card parent = null;

            for (Card card : this.cards) {
                if (card.getId() == parentId) {
                    parent = card;
                    break;
                }
            }

            int parentIndex = this.cards.indexOf(parent);
            this.cards.addAll(parentIndex + 1, Arrays.asList(categories));
        }

        notifyDataSetChanged();
    }

    public void addFurnitures(Furniture[] furnitures) {
        int categoryId = furnitures[0].getCategoryId();
        Card parent = null;

        for (Card card : this.cards) {
            if (card.getType() == CardType.Category && card.getId() == categoryId) {
                parent = card;
                break;
            }
        }

        int parentIndex = this.cards.indexOf(parent);
        this.cards.addAll(parentIndex + 1, Arrays.asList(furnitures));
        notifyDataSetChanged();
    }

    @Override
    public CategoryPlanCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.category_plan_card, parent, false);
        return new CategoryPlanCardAdapter.ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(CategoryPlanCardAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView nameView = cardView.findViewById(R.id.furniturePlanName);
        nameView.setText(cards.get(position).getText());
        switch (cards.get(position).getLevel()) {
            case 1:
                nameView.setBackgroundColor(Color.CYAN);
                break;
            case 2:
                nameView.setBackgroundColor(Color.LTGRAY);
                break;
            default:
                nameView.setBackgroundColor(Color.GREEN);
        }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(cards.get(position).getId());
        }
        });
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }

    public interface Listener {
        void onClick(int id);
    }
}