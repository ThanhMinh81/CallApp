package com.example.RoomDatabase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.DAO.MessageDao;
import com.example.Model.ChatMessage;
import com.example.Model.User;

@Database(entities = {ChatMessage.class, User.class }, version = 3 , exportSchema = false)
public abstract class MessageDatabase extends RoomDatabase {
    public abstract MessageDao messageDao();

    private static volatile MessageDatabase instance;

    public static synchronized MessageDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            MessageDatabase.class, "my_database")
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    public static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Monster Man', 'img_camomonster', 'video1')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('G-man Monster', 'img_holymonster', 'video_2')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('DJ Monster', 'img_4', 'video_3')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Holy Monster', 'img_5', 'video_4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Camo Monster', 'img_6', 'video_5')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('G-man Monster', 'img_8', 'video_2')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Speaker Man', 'img_4', 'video_6')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Urinal Monster', 'img_3', 'video_7')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Grandpa Monster', 'img', 'video_8')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Giant Monster', 'img', 'video_9')");

        }
    };
}
