package com.example.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.Adapter.CallAdapter;
import com.example.DAO.MessageDao;
import com.example.Interface.IClickCall;
import com.example.Model.User;
import com.example.myappcall.R;
import com.example.view.OptionCallActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class CallFragment extends Fragment {



    MessageDao messageDao;
    CallAdapter callAdapter;
    ArrayList<User> userArrayList;
    RecyclerView rcvCallFragment;
    IClickCall iClickCall;
    View view;
    Dialog dialogCall;
    private Dialog diagloCall;

    public CallFragment(MessageDao messageDao) {
        this.messageDao = messageDao;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_call, container, false);

        dialogCall = new Dialog(getContext());

        userArrayList = new ArrayList<>();



        getData();



        iClickCall = new IClickCall() {
            @Override
            public void callPerson(User user) {
                showDialogCall2(user);
            }
        };

//        iClickCall = new IClickCall() {
//            @Override
//            public void callPerson(User personCall) {
//                showDialogCall2(personCall);
//            }
//        };


        callAdapter = new CallAdapter(userArrayList, this.getContext(), iClickCall);

        rcvCallFragment = view.findViewById(R.id.rcvFragmentCall);
        rcvCallFragment.setAdapter(callAdapter);
        rcvCallFragment.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return view;
    }

    private void getData() {

//        ArrayList<User> personCalls = new ArrayList<>();
//
//        personCalls.add(new User("A", "img_1.png", "video1"));
//        personCalls.add(new User("B", "img_1.png", "video1"));
//        personCalls.add(new User("C", "img_1.png", "video1"));
//        personCalls.add(new User("D", "img_1.png", "video1"));
//        personCalls.add(new User("E", "img_1.png", "video1"));



        Observable.fromCallable(() -> messageDao.getListPerson())
                .subscribeOn(Schedulers.io()) // Thực hiện trên luồng IO (nền)
                .observeOn(AndroidSchedulers.mainThread()) // Nhận kết quả trên luồng chính (UI Thread)
                .subscribe(new Observer<List<User>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull List<User> users) {
                        userArrayList.addAll(users);
                        callAdapter.notifyDataSetChanged();
                                Log.d("fdff", users.size() + " ");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });


    }

    private void showDialogCall2(User user) {
        Log.d("dfiah",user.getPersonAvt().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.WrapContentDialog);
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        final View dialogView = layoutInflater.inflate(R.layout.layout_dialog_call, null);
        ImageView imgAvatar = dialogView.findViewById(R.id.imgAvatar);
         String s =   user.getPersonAvt();
           int resourceId = getResources().getIdentifier(s, "drawable", getContext().getPackageName());
            imgAvatar.setImageResource(resourceId);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.TOP;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        // mrgintop cho dialog
        params.y = 16;
        window.setAttributes(params);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        // cai nay cho no mau trong suot de co the thay duoc icon close
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        window.setGravity(Gravity.CENTER);
        dialog.show();



        ImageView closeCall = dialog.findViewById(R.id.img_closeCall);
        ImageView videoCall = dialog.findViewById(R.id.imgVideoCall);
        ImageView microCall = dialog.findViewById(R.id.imgCallMicro);

        //videocall
        videoCall.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OptionCallActivity.class);
            intent.putExtra("checkCallVideo",true);
            startActivity(intent);
        });


        microCall.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), OptionCallActivity.class);
            intent.putExtra("checkCallVideo",false);
            startActivity(intent);
        });


        closeCall.setOnClickListener(v -> {
            dialog.dismiss();
        });



    }


}