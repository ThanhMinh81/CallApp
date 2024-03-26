package com.example.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/// day la bang noi
public class UserWithChat  implements Parcelable {

    @Embedded
    private User user;

    @Relation(parentColumn = "id", entityColumn = "userId")
    private List<ChatMessage> chatMessages;


    public UserWithChat() {
    }


    protected UserWithChat(Parcel in) {
        user = in.readParcelable(User.class.getClassLoader());
        chatMessages = in.createTypedArrayList(ChatMessage.CREATOR);
    }

    public static final Creator<UserWithChat> CREATOR = new Creator<UserWithChat>() {
        @Override
        public UserWithChat createFromParcel(Parcel in) {
            return new UserWithChat(in);
        }

        @Override
        public UserWithChat[] newArray(int size) {
            return new UserWithChat[size];
        }
    };

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void setChatMessages(List<ChatMessage> chatMessages) {
        this.chatMessages = chatMessages;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeParcelable(user, i);
        parcel.writeTypedList(chatMessages);
    }
}
