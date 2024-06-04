package com.example.myjournal;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context context;
    private List<Diary> diaryList;

    public RecyclerViewAdapter(Context context, List<Diary> diaryList) {
        this.context = context;
        this.diaryList = diaryList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.diarylayout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
            Diary diary=diaryList.get(position);
            holder.dTitle.setText(diary.getDiaryTitle());
            holder.dInit.setText(diary.getdInit());
    }



    @Override
    public int getItemCount() {
        return diaryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View

            .OnClickListener {
        public TextView dTitle;
        public TextView dInit;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            dTitle=itemView.findViewById(R.id.textView4);
            dInit=itemView.findViewById(R.id.textInit);

        }

        @Override
        public void onClick(View v) {
            String title=dTitle.getText().toString();
            Intent intent=new Intent(context,InsideDiaryActivity.class);
            intent.putExtra("title", title);

            context.startActivity(intent);


        }
    }
}
