package com.example.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.Adapter.DirectAdapter;
import com.example.DAO.MessageDao;
import com.example.Interface.IClickMess;
import com.example.Model.ChatMessage;
import com.example.Model.User;
import com.example.Model.UserWithChat;
import com.example.myappcall.R;
import com.example.view.MessagerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DirectFragment extends Fragment {

    RecyclerView rcvDirect;
    MessageDao messageDao;

    DirectAdapter directAdapter;
    ArrayList<UserWithChat> chatArrayList;
    View view;

    IClickMess iClickMess ;

    public DirectFragment(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_direct, container, false);
        rcvDirect = view.findViewById(R.id.rcvDirect);

         chatArrayList = new ArrayList<>();

        iClickMess = userWithChat -> {
            Intent intent = new Intent(getActivity(), MessagerActivity.class);
            intent.putExtra("Object",userWithChat);
            startActivityForResult(intent,10);


        };

        directAdapter = new DirectAdapter(chatArrayList,iClickMess, getContext());

        rcvDirect = view.findViewById(R.id.rcvDirect);
        rcvDirect.setAdapter(directAdapter);
        rcvDirect.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));

        getData();


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 10)
        {
            if(data !=  null)
            {

                UserWithChat user = (UserWithChat) data.getParcelableExtra("Object");

                fetchDataAndUpdateDatabase(user)
                        .subscribe(() -> {
                            Log.d("fsf3", "Them data thanh cong");
                            getData();
                        }, error -> {
                            Log.e("4523", "Eloioi: " + error.getMessage());
                        });

            }
        }

    }

    private void getData() {

        Observable.fromCallable(() -> messageDao.getJoinChatPersons())
                .subscribeOn(Schedulers.io()) // Thực hiện trên luồng IO (nền)
                .observeOn(AndroidSchedulers.mainThread()) // Nhận kết quả trên luồng chính (UI Thread)
                .subscribe(new Observer<List<UserWithChat>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {}
                    @Override
                    public void onNext(@NonNull List<UserWithChat> userWithChats) {
                        chatArrayList.clear();
                        chatArrayList.addAll(userWithChats);
                        directAdapter.notifyDataSetChanged();
                        Log.d("fsfafa",userWithChats.get(1).getChatMessages().size() + " ");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    public Completable fetchDataAndUpdateDatabase(UserWithChat user) {
        return Completable.fromAction(() -> {
                    messageDao.deleteMessagesByUserId(user.getUser().getId());
                    messageDao.insertList(user.getChatMessages());
                }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io());
    }





}