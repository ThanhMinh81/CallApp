package com.example.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Interface.IClickCall;
import com.example.Model.User;
import com.example.myappcall.R;

import java.util.ArrayList;

public class CallAdapter extends RecyclerView.Adapter<CallAdapter.ViewHolder> {

    ArrayList<User> users;

    Context context;

    IClickCall iClickCall;


    public CallAdapter(ArrayList<User> users, Context context, IClickCall iClickCall) {
        this.users = users;
        this.context = context;
        this.iClickCall = iClickCall;
    }

    @NonNull
    @Override
    public CallAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        view = inflater.inflate(R.layout.item_layout_call, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull CallAdapter.ViewHolder holder, int position) {

        User user = users.get(position);

        String s =   user.getPersonAvt();
        int resourceId = context.getResources().getIdentifier(s, "drawable", context.getPackageName());
        holder.imgAvtar.setImageResource(resourceId);

//        Glide.with(context).load(getImage("img_1")).into(holder.imgAvtar);

        holder.imgAvtar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickCall.callPerson(user);
            }
        });

    }


    public int getImage(String imageName) {

        int drawableResourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());

        return drawableResourceId;
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgAvtar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgAvtar = itemView.findViewById(R.id.imgAvatar);


        }
    }
}
