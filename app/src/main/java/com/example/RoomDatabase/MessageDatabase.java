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

            // Thêm các giá trị sẵn có vào bảng khi cơ sở dữ liệu được tạo
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Monster Man', 'img_1', 'video_1.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('G-man Monster', 'img_3', 'video_2.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('DJ Monster', 'img_4', 'video_3.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Holy Monster', 'img_3', 'video_4.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Camo Monster', 'img', 'video_5.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('G-man Monster', 'img', 'video_2.mp4')");



        }
    };
}
