package com.interiordesigner;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.interiordesigner.Classes.Project;

import java.util.List;

public class ProjectCardAdapter extends RecyclerView.Adapter<ProjectCardAdapter.ViewHolder> {
    private List<Project> projects;
    private Listener listener;

    public ProjectCardAdapter(List<Project> projects) {
        this.projects = projects;
    }

    @Override
    public int getItemCount() {
        return projects.size();
    }

    public void setListener(Listener listener) { this.listener = listener; }

    @Override
    public ProjectCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.project_selection_card, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView) cardView.findViewById(R.id.project_image);
        TextView textView = (TextView) cardView.findViewById(R.id.project_name);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), projects.get(position).GetThumbnailId());

        imageView.setImageDrawable(drawable);
        textView.setText(projects.get(position).GetName());
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(listener != null)
                    listener.onClick(projects.get(position).GetId());
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
