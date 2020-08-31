package com.interiordesigner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.interiordesigner.Classes.Category;

import org.w3c.dom.Text;

import java.util.List;

public class CategoryCardAdapter extends RecyclerView.Adapter<CategoryCardAdapter.ViewHolder> {
    private List<Category> categories;
    private Listener listener;

    public CategoryCardAdapter(List<Category> categories) { this.categories = categories; }

    @Override
    public int getItemCount() { return categories.size(); }

    public void setListener(Listener listener) { this.listener = listener; }

    @Override
    public CategoryCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.category_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        TextView nameView = cardView.findViewById(R.id.category_name);
        nameView.setText(categories.get(position).GetName());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(categories.get(position).GetId());
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

    interface Listener {
        void onClick(int id);
    }
}
