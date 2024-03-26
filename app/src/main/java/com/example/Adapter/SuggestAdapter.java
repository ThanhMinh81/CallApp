package com.example.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Interface.IClickSuggest;
import com.example.myappcall.R;

import java.util.ArrayList;

public class SuggestAdapter extends RecyclerView.Adapter<SuggestAdapter.ViewHolder> {

    ArrayList<String> suggestMessList;
    IClickSuggest iClickSuggest;

    Context context;

    public SuggestAdapter(ArrayList<String> suggestMessList, IClickSuggest iClickSuggest, Context context) {
        this.suggestMessList = suggestMessList;
        this.iClickSuggest = iClickSuggest;
        this.context = context;
    }

    @NonNull
    @Override
    public SuggestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());


        View view;

        view = inflater.inflate(R.layout.layout_item_sugess_message, parent, false);


        return new SuggestAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestAdapter.ViewHolder holder, int position) {

        String suggestMess = suggestMessList.get(position);

        if (suggestMess.contains("img")) {

            String[] parts = suggestMess.split("<img src='");

            for (String part : parts) {

                if (part.contains("cuoi")) {
                    String cuoi = part.substring(0, part.indexOf("'"));
                    holder.tvSugess.setText(suggestMess);
                    String nameIconDrawable = cuoi;

                    holder.tvSugess.setText(Html.fromHtml(suggestMess, new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {
                            int resourceId = context.getResources().getIdentifier(nameIconDrawable, "drawable", context.getPackageName());
                            Log.d("78934579", resourceId + " ");
                            Drawable iconDrawable = context.getResources().getDrawable(resourceId);
                            int iconSize = (int) (holder.tvSugess.getTextSize());
                            iconDrawable.setBounds(0, 0, iconSize, iconSize);
                            return iconDrawable;
                        }
                    }, null));

                }
            }

        } else {
            holder.tvSugess.setText(suggestMess);

        }

        holder.constraintLayout.setOnClickListener(view -> {
            iClickSuggest.Suggest(suggestMess);

        });


    }

    @Override
    public int getItemCount() {
        return suggestMessList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvSugess;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            constraintLayout = itemView.findViewById(R.id.layoutSuggest);
            tvSugess = itemView.findViewById(R.id.messageSend);
        }
    }
}
