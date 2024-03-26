package com.example.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Interface.IClickMess;
import com.example.Model.UserWithChat;
import com.example.myappcall.R;

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


        holder.tvName.setText(userWithChat.getUser().getPersonName());
        if (userWithChat.getChatMessages().size() > 0) {
            int totalList = userWithChat.getChatMessages().size() - 1;

            holder.tvMess.setText(userWithChat.getChatMessages().get(totalList).getMessageText());

        } else {
            holder.tvMess.setText("New messager");
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tvName);
            tvMess = itemView.findViewById(R.id.tvMess);
            constraintLayout = itemView.findViewById(R.id.layoutMessager);

        }
    }
}

