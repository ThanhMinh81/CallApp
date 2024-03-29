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

            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Monster Man', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/camomonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_camomonster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('G-man Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/giantcamera.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_giantcamera.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('DJ Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/grandpamonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_grandpamonster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Holy Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/holymonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_holymonster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Camo Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/camomonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_mafiamonster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Speaker Man', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/speakerman.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_speakerman.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Titan Camera', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/titancamera.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_titancamera.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Titan Speaker', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/camomonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_titanspeaker.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Rocket Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/giantcamera.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_titanspeaker.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Hydra Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/holymonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_titanspeaker.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Urinal Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/speakerman.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_grandpamonster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Spider Camera', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/camomonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_titanspeaker.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Grandpa Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/grandpamonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_mafiamonster.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Giant Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/titanspeaker.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_titanspeaker.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Giant Camera', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/holymonster.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_giantcamera.mp4')");
            db.execSQL("INSERT INTO personDB (personName, personAvt, urlVideo) VALUES ('Mafia Monster', 'https://storage.lutech.vn/app/PrankCall/AvatarUser/speakerman.png', 'https://storage.lutech.vn/app/PrankCall/VideoCall/video_speakerman.mp4')");

        }
    };
}
