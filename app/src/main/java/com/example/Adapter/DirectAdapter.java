package com.example.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Interface.IClickMess;
import com.example.Model.UserWithChat;
import com.example.myappcall.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

public class DirectAdapter extends RecyclerView.Adapter<DirectAdapter.ViewHolder> {

    ArrayList<UserWithChat> userWithChats;

    IClickMess iClickMess;

    Context context;

    public DirectAdapter(ArrayList<UserWithChat> userWithChats, IClickMess iClickMess, Context context) {
        this.userWithChats = userWithChats;
        this.iClickMess = iClickMess;
        this.context = context;
    }

    @NonNull
    @Override
    public DirectAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_layout_mess, parent, false);
        return new DirectAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectAdapter.ViewHolder holder, int position) {


        UserWithChat userWithChat = userWithChats.get(position);


        // xet resoure voi kieu string cho img
//        String s = userWithChat.getUser().getPersonAvt();
//        int resourceId = context.getResources().getIdentifier(s, "drawable", context.getPackageName());
//        holder.shapeableImageView.setImageResource(resourceId);


        Glide.with(context).load(userWithChat.getUser().getPersonAvt()).into(holder.shapeableImageView);
        Log.d("erw7r0wq523432---3--3-3",userWithChat.getUser().getPersonAvt());




        holder.tvName.setText(userWithChat.getUser().getPersonName());
        if (userWithChat.getChatMessages().size() > 0) {

            int totalList = userWithChat.getChatMessages().size() - 1;

                String message =  userWithChat.getChatMessages().get(totalList).getMessageText();
//                Log.d("erw7r0wq5234",message);

            if (message.contains("img")) {

                String[] parts = message.split("<img src='");
                Log.d("3424242","fsadf");

                for (String part : parts) {

                    if (part.contains("cuoi")) {

                        String cuoi = part.substring(0, part.indexOf("'"));

                        String nameIconDrawable = cuoi;
                        holder.tvMess.setText(Html.fromHtml(message, new Html.ImageGetter() {
                            @Override
                            public Drawable getDrawable(String source) {
                                int resourceId = context.getResources().getIdentifier(nameIconDrawable, "drawable", context.getPackageName());
                                Log.d("78934579", resourceId + " ");
                                Drawable iconDrawable = context.getResources().getDrawable(resourceId);
                                int iconSize = (int) (holder.tvMess.getTextSize());
                                iconDrawable.setBounds(0, 0, iconSize, iconSize);
                                return iconDrawable;
                            }
                        }, null));


                    }
                }

            } else {
                holder.tvMess.setText(userWithChat.getChatMessages().get(totalList).getMessageText());

            }


        }

        holder.constraintLayout.setOnClickListener(view -> {

            iClickMess.clickMess(userWithChat);

        });

    }

    @Override
    public int getItemCount() {
        return userWithChats.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvMess;
        ConstraintLayout constraintLayout;

        ShapeableImageView shapeableImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvMess = itemView.findViewById(R.id.tvMess);
            constraintLayout = itemView.findViewById(R.id.layoutMessager);
            shapeableImageView = itemView.findViewById(R.id.circle);

        }
    }
}

