package com.interiordesigner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Debug;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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

import java.io.IOException;
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

        CategoryPlanCardAdapter adapter = new CategoryPlanCardAdapter(this ,categories);
        recyclerView.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter.setListener(new CategoryPlanCardAdapter.Listener() {
            @Override
            public void onClick(Card card) {
                if (card.getType() == CardType.Category)
                    adapter.addCards(card);
                else
                    createFurniture((Furniture) card);
            }
        });

        View.OnDragListener dragListener = new View.OnDragListener() {
            @Override
            public boolean onDrag(View v, DragEvent event) {
                switch (event.getAction()) {
                    case DragEvent.ACTION_DRAG_ENTERED:
                        editorView.setBackgroundColor(Color.GREEN);
                        break;
                    case DragEvent.ACTION_DRAG_EXITED:
                        editorView.setBackgroundColor(Color.RED);
                        break;
                    case DragEvent.ACTION_DRAG_ENDED:
                        editorView.setBackgroundColor(Color.WHITE);
                        break;
                    case DragEvent.ACTION_DROP:
                        float dropX = event.getX();
                        float dropY = event.getY();
                        Furniture furniture = (Furniture) event.getLocalState();

//                ImageView shape = (ImageView) LayoutInflater.from(this).inflate(
//                        R.layout.view_shape, dropContainer, false);
//                shape.setImageResource(state.item.getImageDrawable());
//                shape.setX(dropX - (float) state.width / 2);
//                shape.setY(dropY - (float) state.height / 2);
//                shape.getLayoutParams().width = state.width;
//                shape.getLayoutParams().height = state.height;
//                editorView.addView(shape);
                        break;
                    default:
                        break;
                }
                return true;
            }
        };

        editorView.setOnDragListener(dragListener);



    }

    public void createFurniture(Furniture furniture)
    {
    }

//    @Override
//    public boolean onDrag(View view, DragEvent event) {
//        switch (event.getAction()) {
//            case DragEvent.ACTION_DRAG_ENTERED:
//                editorView.setBackgroundColor(Color.GREEN);
//                break;
//            case DragEvent.ACTION_DRAG_EXITED:
//                editorView.setBackgroundColor(Color.RED);
//                break;
//            case DragEvent.ACTION_DRAG_ENDED:
//                editorView.setBackgroundColor(Color.WHITE);
//                break;
//            case DragEvent.ACTION_DROP:
//                float dropX = event.getX();
//                float dropY = event.getY();
//                Furniture furniture = (Furniture) event.getLocalState();
//
////                ImageView shape = (ImageView) LayoutInflater.from(this).inflate(
////                        R.layout.view_shape, dropContainer, false);
////                shape.setImageResource(state.item.getImageDrawable());
////                shape.setX(dropX - (float) state.width / 2);
////                shape.setY(dropY - (float) state.height / 2);
////                shape.getLayoutParams().width = state.width;
////                shape.getLayoutParams().height = state.height;
////                editorView.addView(shape);
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
}



class CategoryPlanCardAdapter extends RecyclerView.Adapter<CategoryPlanCardAdapter.ViewHolder> {
    private Context context;
    private List<Card> cards;
    private CategoryPlanCardAdapter.Listener listener;

    public CategoryPlanCardAdapter(Context context, Card[] cards) {
        this.context = context;
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

    public void addCards(Card card) {
        int index = this.cards.indexOf(card);

        Card[] childCards = Category.GetByParentId(card.getId());
        if (childCards.length == 0) {
            childCards = Furniture.GetByCategoryId(card.getId());
        }

        if (childCards.length == 0) return;

        if (this.cards.containsAll(Arrays.asList(childCards))) {
            int nextIndex = 0;

            for (int i = index + 1; i < this.cards.size(); i++) {
                if (card.getLevel() == this.cards.get(i).getLevel()) {
                    nextIndex = i;
                    break;
                }
            }
            List<Card> sublist = this.cards.subList(index + 1, nextIndex);
            this.cards.removeAll(sublist);
        } else {
            this.cards.addAll(index + 1, Arrays.asList(childCards));
        }

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
        ImageView imageView = cardView.findViewById(R.id.furniturePlanImage);
        imageView.setVisibility(View.INVISIBLE);
        nameView.setText(cards.get(position).getText());
        switch (cards.get(position).getLevel()) {
            case 1:
                nameView.setBackgroundColor(Color.rgb(0, 153, 255));
                break;
            case 2:
                nameView.setBackgroundColor(Color.rgb(77, 184, 255));
                break;
            case 3:
                if (cards.get(position).getType() == CardType.Furniture) {
                    Furniture furniture = (Furniture) cards.get(position);
                    String path = furniture.getPhotoPath();
                    Drawable drawable = null;
                    try {
                        drawable = Drawable.createFromStream(context.getAssets().open(path), null);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imageView.setImageDrawable(drawable);
                    imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    imageView.setVisibility(View.VISIBLE);

                    View.OnLongClickListener longClickListener = new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            View.DragShadowBuilder shadow = new View.DragShadowBuilder(imageView);
                            ViewCompat.startDragAndDrop(imageView, null, shadow, furniture, 0);
                            return true;
                        }
                    };

                    holder.itemView.setOnLongClickListener(longClickListener);


                } else {
                    nameView.setBackgroundColor(Color.rgb(153, 214, 255));
                }

                break;
            default:
                nameView.setBackgroundColor(Color.rgb(255, 255, 255));
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(cards.get(position));
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
        void onClick(Card card);
    }
}