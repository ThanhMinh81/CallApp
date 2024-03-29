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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.Adapter.CallAdapter;
import com.example.DAO.MessageDao;
import com.example.Interface.IClickCall;
import com.example.Model.User;
import com.example.myappcall.R;
import com.example.view.MainActivity;
import com.example.view.OptionCallActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
    public static ArrayList<User> userArrayList;
    RecyclerView rcvCallFragment;
    IClickCall iClickCall;
    View view;
    Dialog dialogCall;
    private Dialog diagloCall;

    public CallFragment() {
    }

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
                MainActivity.checkSoundAndVibarte();
                showDialogCall(user);
            }
        };


        callAdapter = new CallAdapter(userArrayList, this.getContext(), iClickCall);

        rcvCallFragment = view.findViewById(R.id.rcvFragmentCall);
        rcvCallFragment.setAdapter(callAdapter);
        rcvCallFragment.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return view;
    }

    private void getData() {


        try {

            // load json
            InputStream inputStream = getContext().getAssets().open("data.json");
            int size = inputStream.available();
            byte[] buffter = new byte[size];
            inputStream.read(buffter);
            inputStream.close();


            // fetch json

            String json;
            int max;


            json = new String(buffter, StandardCharsets.UTF_8);
            JSONArray jsonArray = new JSONArray(json);
            max = jsonArray.length();

            for (int i = 0; i < max; i++) {

                User user = new User();

                JSONObject jsonObject = jsonArray.getJSONObject(i);
                user.setId(jsonObject.getInt("id"));
                user.setPersonName(jsonObject.getString("name"));
                user.setPersonAvt(jsonObject.getString("avatar"));
                user.setUrlVideo(jsonObject.getString("videoCall"));
                Log.d("324242", user.getId() + " == " + user.getPersonAvt());

                userArrayList.add(user);
            }
            callAdapter.notifyDataSetChanged();


        } catch (Exception e) {
        }

//        Observable.fromCallable(() -> messageDao.getListPerson())
//                .subscribeOn(Schedulers.io()) // thuc hien tren bakcgorund thread
//                .observeOn(AndroidSchedulers.mainThread())  // get result tren UI thread
//                .subscribe(new Observer<List<User>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull List<User> users) {
//                        userArrayList.addAll(users);
//                        callAdapter.notifyDataSetChanged();
//                        Log.d("faasdfewr", users.size() + " ");
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });


    }

    private void showDialogCall(User user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.WrapContentDialog);
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        final View dialogView = layoutInflater.inflate(R.layout.layout_dialog_call, null);
        ImageView imgAvatar = dialogView.findViewById(R.id.imgAvatar);
        TextView tvName = dialogView.findViewById(R.id.tvName);
        tvName.setText(user.getPersonName());

        Glide.with(getContext()).load(user.getPersonAvt()).into(imgAvatar);

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

            MainActivity.checkSoundAndVibarte();

            Intent intent = new Intent(getContext(), OptionCallActivity.class);
            intent.putExtra("checkCallVideo", true);
            intent.putExtra("Object", user);
            startActivity(intent);
        });


        microCall.setOnClickListener(v -> {

            MainActivity.checkSoundAndVibarte();


            Intent intent = new Intent(getContext(), OptionCallActivity.class);
            intent.putExtra("checkCallVideo", false);
            intent.putExtra("Object", user);

            startActivity(intent);
        });


        closeCall.setOnClickListener(v -> {

            MainActivity.checkSoundAndVibarte();


            dialog.dismiss();
        });


    }


}