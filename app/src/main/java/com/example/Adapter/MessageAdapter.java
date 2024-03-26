package com.example.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Model.ChatMessage;
import com.example.myappcall.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    ArrayList<ChatMessage> chatMessages;

    Context context;

    public MessageAdapter(ArrayList<ChatMessage> chatMessages, Context context) {
        this.chatMessages = chatMessages;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view;
        view = inflater.inflate(R.layout.item_layout_message_1, parent, false);

        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        ChatMessage chatMessage = chatMessages.get(position);

        String messReceive = chatMessage.getMessageText();

        if (chatMessage.getMessageText().contains("img")) {

            String[] parts = messReceive.split("<img src='");

            for (String part : parts) {

                if (part.contains("cuoi")) {

                    String cuoi = part.substring(0, part.indexOf("'"));
                    String nameIconDrawable = cuoi;

                    holder.tvReceive.setText(Html.fromHtml(messReceive, new Html.ImageGetter() {
                        @Override
                        public Drawable getDrawable(String source) {
                            int resourceId = context.getResources().getIdentifier(nameIconDrawable, "drawable", context.getPackageName());
                            Log.d("78934579", resourceId + " ");
                            Drawable iconDrawable = context.getResources().getDrawable(resourceId);
                            int iconSize = (int) (holder.tvReceive.getTextSize());
                            iconDrawable.setBounds(0, 0, iconSize, iconSize);
                            return iconDrawable;
                        }
                    }, null));

                }
            }

        } else {
            holder.tvReceive.setText(chatMessage.getMessageText());

        }


        if (chatMessage.isChecked() == true) {

            holder.linearLayout.setGravity(Gravity.RIGHT);
            holder.tvReceive.setTextColor(context.getResources().getColor(R.color.white));
            holder.tvReceive.setBackground(context.getDrawable(R.drawable.bg_item_message_send));

        } else {

            holder.linearLayout.setGravity(Gravity.LEFT);
            holder.tvReceive.setTextColor(context.getResources().getColor(R.color.black));
            holder.tvReceive.setBackground(context.getDrawable(R.drawable.bg_item_message));
        }

        if (position == chatMessages.size() - 1) {

            Animation slideFromBottom = AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.slide_from_bottom);
            holder.linearLayout.setAnimation(slideFromBottom);

        }


    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //        TextView tvSend, tvReceive;
        TextView tvReceive;
        LinearLayout linearLayout;

//        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

//            tvSend = itemView.findViewById(R.id.messageSend);
            tvReceive = itemView.findViewById(R.id.messageRevice);
            linearLayout = itemView.findViewById(R.id.layoutParentMess);

        }
    }
}
