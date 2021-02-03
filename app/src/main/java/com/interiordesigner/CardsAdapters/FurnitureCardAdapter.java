package com.interiordesigner.CardsAdapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.interiordesigner.Classes.Color;
import com.interiordesigner.Classes.Furniture;
import com.interiordesigner.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FurnitureCardAdapter extends RecyclerView.Adapter<FurnitureCardAdapter.ViewHolder> {
    private Furniture[] furnitures;
    private Listener listener;
    private Context context;


    public FurnitureCardAdapter(Furniture[] furnitures) { this.furnitures = furnitures; }

    public void setListener(Listener listener) { this.listener = listener; }

    @Override
    public int getItemCount() { return furnitures.length; }

    @Override
    public FurnitureCardAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                                               .inflate(R.layout.furniture_card, parent,
                                                    false);
        context = parent.getContext();

        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Furniture furniture = furnitures[position];
        List<Color> avaiableColors = new ArrayList<>();

        CardView cardView = holder.cardView;
        ImageView imageView = cardView.findViewById(R.id.furnitureImage);
        TextView textView = cardView.findViewById(R.id.furnitureName);
        LinearLayout layout = cardView.findViewById(R.id.furnitureColorsLayout);

//        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(),
//                                                      furniture.GetPhotoId());

        String path = furniture.getPhotoPath();

        Drawable drawable = null;
        try {
            drawable = Drawable.createFromStream(context.getAssets().open(path), null);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imageView.setImageDrawable(drawable);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        textView.setText(furniture.getName());

        int[] availableColorsIds = furniture.getColorsIds();
        for (Color color : Color.Colors)
        {
            for (int availableColorId : availableColorsIds)
            {
                if (color.GetId() == availableColorId)
                    avaiableColors.add(color);
            }
        }

        for (Color color : avaiableColors)
        {
            ImageView iv = new ImageView(cardView.getContext());
            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(ContextCompat.getColor(cardView.getContext(),color.GetMiniatureColor()));
            shape.setStroke(10, ContextCompat.getColor(cardView.getContext(), color.GetMiniatureBorderColor()));
            shape.setSize(100, 100);

            iv.setImageDrawable(shape);
            iv.setElevation(8);
            iv.setPadding(10, 0,10,0);
            //iv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            //                                            ViewGroup.LayoutParams.WRAP_CONTENT));

            layout.addView(iv);
        }

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null)
                    listener.onClick(furniture.getId());
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public ViewHolder(CardView cv)
        {
            super(cv);
            cardView = cv;
        }
    }

    public interface Listener {
        void onClick(int id);
    }
}
