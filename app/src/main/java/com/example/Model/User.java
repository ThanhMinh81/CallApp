package com.example.Model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "personDB")
public class User implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String personName;
    private String personAvt;

    private String urlVideo;

    public User(String personName, String personAvt, String urlVideo) {
        this.personName = personName;
        this.personAvt = personAvt;
        this.urlVideo = urlVideo;
    }

    protected User(Parcel in) {
        id = in.readInt();
        personName = in.readString();
        personAvt = in.readString();
        urlVideo = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonAvt() {
        return personAvt;
    }

    public String getUrlVideo() {
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public void setPersonAvt(String personAvt) {
        this.personAvt = personAvt;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(personName);
        parcel.writeString(personAvt);
        parcel.writeString(urlVideo);
    }
}
