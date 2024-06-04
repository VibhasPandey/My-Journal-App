package com.example.myjournal;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PopUpClass {

    EditText title;
    String titleJournal;
    Button saveButt;
    private PopupCallback callback;

    public void setCallback(PopupCallback callback) {
        this.callback = callback;
    }



    public void showPopUp(final View view) {


        LayoutInflater inflater=(LayoutInflater) view.getContext().getSystemService(view.getContext().LAYOUT_INFLATER_SERVICE);
        View popupView=inflater.inflate(R.layout.pop_up_layout,null);


        int width= LinearLayout.LayoutParams.MATCH_PARENT;
        int height= LinearLayout.LayoutParams.MATCH_PARENT;


        boolean focusable=true;

        final PopupWindow popupWindow=new PopupWindow(popupView,width,height,focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER,0,0);

        title=popupView.findViewById(R.id.editText);

        saveButt=popupView.findViewById(R.id.setTitle);
        saveButt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                titleJournal = title.getText().toString();
                if (callback != null) {
                    callback.onTitleSet(titleJournal);
                }
                popupWindow.dismiss();


            }
        });

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                popupWindow.dismiss();
                return true;
            }
        });



    }
    public interface PopupCallback {
        void onTitleSet(String title);
    }


}
