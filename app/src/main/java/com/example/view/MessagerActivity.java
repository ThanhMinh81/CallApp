package com.example.view;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.Adapter.MessageAdapter;
import com.example.Adapter.SuggestAdapter;
import com.example.DAO.MessageDao;
import com.example.Interface.IClickSuggest;
import com.example.Model.ChatMessage;
import com.example.Model.UserWithChat;
import com.example.RoomDatabase.MessageDatabase;
import com.example.myappcall.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MessagerActivity extends AppCompatActivity {

    RecyclerView rcvMess, rcvSuggest;
    MessageAdapter messageAdapter;
    ArrayList<ChatMessage> messageList;
    SuggestAdapter suggestAdapter;
    ArrayList<String> suggestList;

    IClickSuggest iClickSuggest;
    LinearLayout layoutTyping;
    private MediaPlayer mediaSend;
    private MediaPlayer mediaReceive;

    private Handler mHandler = new Handler(Looper.getMainLooper());
    private Handler handler = new Handler();

    Typewriter typewriter;

    TextView tvTyping;

    ImageView nav_back;

    TextView tvNamePerson;

    ShapeableImageView imgAvatar;

    UserWithChat userWithChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_mess);

        // nhan object va hien thi len giao dien

        userWithChat = getIntent().getExtras().getParcelable("Object");
        tvNamePerson = findViewById(R.id.tvName);
        imgAvatar = this.<ShapeableImageView>findViewById(R.id.circleAvatar);
        tvNamePerson.setText(userWithChat.getUser().getPersonName());


        layoutTyping = this.<LinearLayout>findViewById(R.id.layoutTyping);
        tvTyping = this.<TextView>findViewById(R.id.tvCuoi);

        tvTyping.setVisibility(View.GONE);
        nav_back = this.<ImageView>findViewById(R.id.nav_back);

        nav_back.setOnClickListener(view -> {
            saveListMessage();
            userWithChat.setChatMessages(messageList);
            Intent intent = new Intent();

            intent.putExtra("Object", userWithChat);

            setResult(RESULT_OK, intent);
            finish();
        });

        mediaSend = MediaPlayer.create(this, R.raw.sound_send);
        mediaReceive = MediaPlayer.create(this, R.raw.sound_receiver);

        typewriter = findViewById(R.id.typewriter);

        rcvMess = findViewById(R.id.rcvChat);
        rcvSuggest = findViewById(R.id.rcvSugessMess);
        messageList = new ArrayList<>();
        suggestList = new ArrayList<>();

//         Trong Activity hoặc Fragment
//        Observable.fromCallable(() -> messageDao.getMessagesByUserId("MonsterMan"))
//                .subscribeOn(Schedulers.io()) // Thực hiện trên luồng IO (nền)
//                .observeOn(AndroidSchedulers.mainThread()) // Nhận kết quả trên luồng chính (UI Thread)
//                .subscribe(new Observer<List<ChatMessage>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(List<ChatMessage> messages) {
//                        Log.d("fsfasfas33", messages.size() + " ");
//                        messageList.addAll(messages);
//                        messageAdapter.notifyDataSetChanged();
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        // Không cần thiết
//                    }
//                });

        iClickSuggest = new IClickSuggest() {
            @Override
            public void Suggest(String s) {
                ChatMessage chatMessage = new ChatMessage();
                addMessageList(s, chatMessage);
            }
        };

        initValueRcv();
        initValueSuggest();

    }


    private void saveListMessage() {
        // Trong Activity hoặc Fragment
//        Observable.fromCallable(() -> {
//                     messageDao
//                    return true; // Trả về true nếu chèn thành công
//                })
//                .subscribeOn(Schedulers.io()) // Xử lý trên luồng IO (nền)
//                .observeOn(Schedulers.io()) // Kết quả trả về trên luồng chính (UI Thread)
//                .subscribe(result -> {
//                    messageDao.insertList(messageList);
//                }, error -> {
//                    Log.d("fsdfas", "3232");
//                });

    }


    public void addMessageList(String textSend, ChatMessage chatMessage) {
        if (textSend != null && chatMessage != null) {

            boolean check = false;

            mHandler.post(new Runnable() {
                @Override
                public void run() {

                    mediaSend.start();
                    Toast.makeText(MessagerActivity.this, "ollll", Toast.LENGTH_SHORT).show();
                    ChatMessage chatMessage1 = new ChatMessage();
//                    chatMessage1.setUserId("MonsterMan");
                    chatMessage1.setUserId(userWithChat.getUser().getId());
                    chatMessage1.setMessageText(textSend);
                    chatMessage1.setChecked(true);
                    messageList.add(chatMessage1);
                    rcvMess.scrollToPosition(messageList.size() - 1);
                    messageAdapter.notifyDataSetChanged();
                    typewriter.setVisibility(View.VISIBLE);
                    tvTyping.setVisibility(View.VISIBLE);
                    typewriter.animateText("...");

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mediaReceive.start();
                            String receive = referenMessage(textSend);
                            ChatMessage chatMessage1 = new ChatMessage();
                            chatMessage1.setUserId(userWithChat.getUser().getId());
                            chatMessage1.setMessageText(receive);
//                            chatMessage1.setUserId("MonsterMan");
                            chatMessage1.setChecked(false);
                            messageList.add(chatMessage1);
                            rcvMess.scrollToPosition(messageList.size() - 1);
                            messageAdapter.notifyDataSetChanged();
                            typewriter.stopAnimation();
                            typewriter.setVisibility(View.GONE);
                            tvTyping.setVisibility(View.GONE);
                            Log.d("kiemtradulieu", messageList.size() + " ");
                        }
                    }, 1500); // 5000 milliseconds = 5 seconds
                }
            });
        }
    }


    private String referenMessage(String textSend) {
        String result = "";

        String temp = textSend.substring(0, 8);

        switch (temp) {
            case "Hi <img ":
                result = "Hi My friend <img src='cuoi'/> How are you <img src='cuoi1'/> <img src='cuoi1'/>";
                break;

            case "I'm Good":
                result = "Super good , thank you friend! It's great to talk with you today <img src='cuoi_good'/> ";
                break;

            case "Who are ":
                result = "Basically , I'm the coolest monster in the world <img src='cuoi_who'/>   ";
                break;

            case "Are you ":
                result = "Youu can guess haha...";
                break;

            case "What you":
                result = "Famili Guys hehehe <img src='cuoi_hi'/>  <img src='cuoi_hi'/>  <img src='cuoi_hi'/> ";
                break;

            case "Tell me ":
                result = "I'm not a joker but let me think about this joke. <img src='cuoi_joker'/> ";
                break;

            case "What kin":
                result = "I love your lips <img src='cuoi'/> . It's delicious hahaha";
                break;

            case "But i he":
                result = "It's just a koke bro <img src='cuoi_hi'/>  <img src='cuoi_hi'/> , I'm still alive . I'm" + " traveling all over Europe and I post my trip on Instagram every day , Do you know my Insta ? ";
                break;

            case "What is ":
                result = "I like traveling listening to music  , and buying shoes <img src='cuoi1'/> ";
                break;

            case "Where ar":
                result = "I'm from Palermo, Italy. But.. long time I haven't been home <img src='cuoi_joker'/> <img src='cuoi_joker'/> ";
                break;

            case "Thank yo":
                result = "Welcome, it's nice to speak with you today <img src='cuoi'/>   <img src='cuoi_good'/>  <img src='cuoi_good'/>";
                break;
        }

        return result;

    }

    private void initValueSuggest() {
        suggestList.add("Hi <img src='cuoi_hi'/> ");
        suggestList.add("Who are you ?");
        suggestList.add("What is your hobby <img src='cuoi1'/> ");
        suggestList.add("Are you alive ? <img src='cuoi1'/> ");
        suggestList.add("Thank you for this conversation <img src='cuoi1'/> <img src='cuoi1'/>");
        suggestList.add("Tell me a joke <img src='cuoi1'/> <img src='cuoi1'/>");
        suggestList.add("I'm Good , what about you ?");
        suggestList.add("Where are you from ?");
        suggestList.add("But i heard you're been dead for a while , it that true ? <img src='cuoi1'/>");
    }

    private void initValueRcv() {

        messageAdapter = new MessageAdapter(messageList, MessagerActivity.this);
        rcvMess.setAdapter(messageAdapter);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessagerActivity.this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setStackFromEnd(true);
        rcvMess.setLayoutManager(linearLayoutManager);
        suggestAdapter = new SuggestAdapter(suggestList, iClickSuggest, MessagerActivity.this);
        rcvSuggest.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        rcvSuggest.setAdapter(suggestAdapter);
        messageList.addAll(userWithChat.getChatMessages());
        messageAdapter.notifyDataSetChanged();

    }


}
