package com.example.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.example.IClickCall;
import com.example.Model.PersonCall;
import com.example.myappcall.R;
import com.example.view.OptionCallActivity;

import java.util.ArrayList;


public class CallFragment extends Fragment {

    CallAdapter callAdapter;
    ArrayList<PersonCall> personCallArrayList;
    RecyclerView rcvCallFragment;
    IClickCall iClickCall;
    View view;
    Dialog dialogCall;
    private Dialog diagloCall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_call, container, false);

        dialogCall = new Dialog(getContext());

        personCallArrayList = new ArrayList<>();

        getData();

        iClickCall = new IClickCall() {
            @Override
            public void callPerson(PersonCall personCall) {
                showDialogCall2(personCall);
            }
        };


        callAdapter = new CallAdapter(personCallArrayList, this.getContext(), iClickCall);

        rcvCallFragment = view.findViewById(R.id.rcvFragmentCall);
        rcvCallFragment.setAdapter(callAdapter);
        rcvCallFragment.setLayoutManager(new GridLayoutManager(getContext(), 2));

        return view;
    }


    private void getData() {
        personCallArrayList.add(new PersonCall("A", "img_1.png", "video1"));
        personCallArrayList.add(new PersonCall("B", "img_1.png", "video1"));
        personCallArrayList.add(new PersonCall("C", "img_1.png", "video1"));
        personCallArrayList.add(new PersonCall("D", "img_1.png", "video1"));
        personCallArrayList.add(new PersonCall("E", "img_1.png", "video1"));
    }

    private void showDialogCall2(PersonCall personCall) {



        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext(), R.style.WrapContentDialog);
        LayoutInflater layoutInflater = LayoutInflater.from(this.getContext());
        final View dialogView = layoutInflater.inflate(R.layout.layout_dialog_call, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.TOP;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        // Thiết lập margin để căn chỉnh dialog ở trên cùng
        params.y = 16; // Đổi giá trị này nếu bạn muốn căn chỉnh khoảng cách từ top
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